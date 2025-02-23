package com.zayaanit.aspi.service;

import java.io.InputStream;
import java.util.Map;

import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.zayaanit.aspi.enums.ReportType;
import com.zayaanit.aspi.model.DBConfig;

/**
 * @author Zubayer Ahamed
 *
 */
public interface PrintingService {

	public InputStream getDataBytes(String reportName, String reportTitle, boolean attachment, Map<String, Object> reportParams, ReportType reportType);
	public void changeDataSource(ReportClientDocument clientDoc, String reportName, String tableName, DBConfig dbConfig) throws ReportSDKException;
	public void logonDataSource(ReportClientDocument clientDoc, DBConfig dbConfig) throws ReportSDKException;
	public void addDiscreteParameterValue(ReportClientDocument clientDoc, String reportName, String parameterName, Object newValue) throws ReportSDKException;
}
