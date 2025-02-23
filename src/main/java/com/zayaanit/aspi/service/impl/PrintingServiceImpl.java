package com.zayaanit.aspi.service.impl;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.crystaldecisions.sdk.occa.report.application.DataDefController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.data.FieldDisplayNameType;
import com.crystaldecisions.sdk.occa.report.data.IConnectionInfo;
import com.crystaldecisions.sdk.occa.report.data.ITable;
import com.crystaldecisions.sdk.occa.report.data.IValue;
import com.crystaldecisions.sdk.occa.report.data.ParameterField;
import com.crystaldecisions.sdk.occa.report.data.ParameterFieldDiscreteValue;
import com.crystaldecisions.sdk.occa.report.data.Tables;
import com.crystaldecisions.sdk.occa.report.data.Values;
import com.crystaldecisions.sdk.occa.report.document.SummaryInfo;
import com.crystaldecisions.sdk.occa.report.exportoptions.DataOnlyExcelExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExcelExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.IExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.IExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.PDFExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.IStrings;
import com.crystaldecisions.sdk.occa.report.lib.PropertyBag;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.zayaanit.aspi.enums.ReportType;
import com.zayaanit.aspi.model.DBConfig;
import com.zayaanit.aspi.service.PrintingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
@Service
public class PrintingServiceImpl extends AbstractGenericService implements PrintingService {

	private static final String TRUSTED_CONNECTION = "false";
	private static final String SERVER_TYPE = "JDBC (JNDI)";
	private static final String USE_JDBC = "true";
	private static final String DATABASE_DLL = "crdb_jdbc.dll";

	@Override
	public InputStream getDataBytes(String reportName, String reportTitle, boolean attachment, Map<String, Object> reportParams, ReportType reportType) {
		ReportClientDocument rcp = new ReportClientDocument();
		try {
//			rcp.setReportAppServer(ReportClientDocument.inprocConnectionString);
			rcp.open(reportName, 0);
			SummaryInfo si = new SummaryInfo();
			si.setTitle(reportTitle);
			si.setAuthor("Zubayer Ahamed");
			si.setSubject(reportTitle);
			si.setKeywords("This System is developed by Zubayer Ahamed. Contact: +8801515634889, +8801748562164");
			si.setComments("This System is developed by Zubayer Ahamed. Contact: +8801515634889, +8801748562164");
			rcp.setSummaryInfo(si);

			// Database Config
			DBConfig dbConfig = new DBConfig();
			dbConfig.setConnectionURL(env.getProperty("spring.datasource.url"));
			dbConfig.setDriverName(env.getProperty("spring.datasource.driver-class-name"));
			dbConfig.setJndiName(env.getProperty("JNDIName"));
			dbConfig.setUsername(env.getProperty("spring.datasource.username"));
			dbConfig.setPassword(env.getProperty("spring.datasource.password"));
			changeDataSource(rcp, null, null, dbConfig);
			logonDataSource(rcp, dbConfig);

			// Add report parameters
			for(Map.Entry<String, Object> param : reportParams.entrySet()) {
				addDiscreteParameterValue(rcp, "", param.getKey(), param.getValue());
			}

			final ExportOptions exportOptions = new ExportOptions();
			if(ReportType.EXCEL.equals(reportType)) {
				final ExcelExportFormatOptions excelOptions = new ExcelExportFormatOptions();
				exportOptions.setExportFormatType(ReportExportFormat.MSExcel);
				exportOptions.setFormatOptions((IExportFormatOptions) excelOptions);
			} else if (ReportType.EXCEL_DATA.equals(reportType)) {
				final DataOnlyExcelExportFormatOptions excelOptions = new DataOnlyExcelExportFormatOptions();
				exportOptions.setExportFormatType(ReportExportFormat.recordToMSExcel);
				exportOptions.setFormatOptions((IExportFormatOptions)excelOptions);
			} else {
				PDFExportFormatOptions pdfOptions = new PDFExportFormatOptions();
				exportOptions.setExportFormatType(ReportExportFormat.PDF);
				exportOptions.setFormatOptions((IExportFormatOptions) pdfOptions);
			}
//			return new BufferedInputStream(clientDoc.getPrintOutputController().export((IExportOptions) exportOptions));
			//ByteArrayInputStream byteArrayInputStream = convertToByteArrayInputStream(rcp.getPrintOutputController().export((IExportOptions) exportOptions));
			InputStream in = rcp.getPrintOutputController().export((IExportOptions) exportOptions);

//			File file = new File("D:/test.pdf");
//
//			FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//			byte[] byteArray = new byte[byteArrayInputStream.available()];
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayInputStream.available());
//			int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());
//
//			byteArrayOutputStream.write(byteArray, 0, x);
//			byteArrayOutputStream.writeTo(fileOutputStream);
//
//			//Close streams.
//			byteArrayInputStream.close();
//			byteArrayOutputStream.close();
//			fileOutputStream.close();

			rcp.close();
			return in;
		} catch (ReportSDKException e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

//	private static ByteArrayInputStream convertToByteArrayInputStream(InputStream inputStream) throws IOException {
//		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//		byte[] temp = new byte[1024];
//		int bytesRead;
//
//		// Read the InputStream into a ByteArrayOutputStream
//		while ((bytesRead = inputStream.read(temp)) != -1) {
//			buffer.write(temp, 0, bytesRead);
//		}
//
//		// Convert the ByteArrayOutputStream to a ByteArrayInputStream
//		return new ByteArrayInputStream(buffer.toByteArray());
//	}

	@Override
	public void changeDataSource(ReportClientDocument clientDoc, String reportName, String tableName, DBConfig dbConfig) throws ReportSDKException {
		PropertyBag propertyBag = null;
		IConnectionInfo connectionInfo = null;
		ITable origTable = null;
		ITable newTable = null;

		if (reportName == null || reportName.equals("")) {
			final Tables tables = clientDoc.getDatabaseController().getDatabase().getTables();
			for (int i = 0; i < tables.size(); ++i) {
				origTable = tables.getTable(i);
				if (tableName == null || origTable.getName().equals(tableName)) {
					newTable = (ITable) origTable.clone(true);
					newTable.setQualifiedName(origTable.getAlias());
					connectionInfo = newTable.getConnectionInfo();
					propertyBag = new PropertyBag();
					propertyBag.put((Object) "Trusted_Connection", (Object) TRUSTED_CONNECTION);
					propertyBag.put((Object) "Server Type", (Object) SERVER_TYPE);
					propertyBag.put((Object) "Use JDBC", (Object) USE_JDBC);
					propertyBag.put((Object) "Database DLL", (Object) DATABASE_DLL);
					propertyBag.put((Object) "JNDI Datasource Name", (Object) dbConfig.getJndiName());
					propertyBag.put((Object) "Connection URL", (Object) dbConfig.getConnectionURL());
					propertyBag.put((Object) "Database Class Name", (Object) dbConfig.getDriverName());
					connectionInfo.setAttributes(propertyBag);
					connectionInfo.setUserName(dbConfig.getUsername());
					connectionInfo.setPassword(dbConfig.getPassword());
					clientDoc.getDatabaseController().setTableLocation(origTable, newTable);
				}
			}
		}
		if (reportName == null || !reportName.equals("")) {
			final IStrings subNames = clientDoc.getSubreportController().getSubreportNames();
			for (int subNum = 0; subNum < subNames.size(); ++subNum) {
				final Tables tables2 = clientDoc.getSubreportController().getSubreport(subNames.getString(subNum)).getDatabaseController().getDatabase().getTables();
				for (int j = 0; j < tables2.size(); ++j) {
					origTable = tables2.getTable(j);
					if (tableName == null || origTable.getName().equals(tableName)) {
						newTable = (ITable) origTable.clone(true);
						newTable.setQualifiedName(origTable.getAlias());
						connectionInfo = newTable.getConnectionInfo();
						propertyBag = new PropertyBag();
						propertyBag.put((Object) "Trusted_Connection", (Object) TRUSTED_CONNECTION);
						propertyBag.put((Object) "Server Type", (Object) SERVER_TYPE);
						propertyBag.put((Object) "Use JDBC", (Object) USE_JDBC);
						propertyBag.put((Object) "Database DLL", (Object) DATABASE_DLL);
						propertyBag.put((Object) "JNDI Datasource Name", (Object) dbConfig.getJndiName());
						propertyBag.put((Object) "Connection URL", (Object) dbConfig.getConnectionURL());
						propertyBag.put((Object) "Database Class Name", (Object) dbConfig.getDriverName());
						connectionInfo.setAttributes(propertyBag);
						connectionInfo.setUserName(dbConfig.getUsername());
						connectionInfo.setPassword(dbConfig.getPassword());
						clientDoc.getSubreportController().getSubreport(subNames.getString(subNum))
								.getDatabaseController().setTableLocation(origTable, newTable);
					}
				}
			}
		}

	}

	@Override
	public void logonDataSource(ReportClientDocument clientDoc, DBConfig dbConfig) throws ReportSDKException {
		clientDoc.getDatabaseController().logon(dbConfig.getUsername(), dbConfig.getPassword());
	}

	@Override
	public void addDiscreteParameterValue(ReportClientDocument clientDoc, String reportName, String parameterName, Object newValue) throws ReportSDKException {
		DataDefController dataDefController = clientDoc.getDataDefController();
		if (StringUtils.isNotBlank(reportName)) {
			dataDefController = clientDoc.getSubreportController().getSubreport(reportName).getDataDefController();
		}

		final ParameterFieldDiscreteValue newDiscValue = new ParameterFieldDiscreteValue();
		newDiscValue.setValue(newValue);
		final ParameterField paramField = (ParameterField) dataDefController.getDataDefinition().getParameterFields().findField(parameterName, FieldDisplayNameType.fieldName, Locale.getDefault());
		final boolean multiValue = paramField.getAllowMultiValue();
		if (multiValue) {
			final Values newVals = (Values) paramField.getCurrentValues().clone(true);
			newVals.add((IValue) newDiscValue);
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName, (Object) newVals);
		} else {
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName, newValue);
		}
	}
}
