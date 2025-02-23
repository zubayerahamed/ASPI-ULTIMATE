package com.zayaanit.aspi.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.enums.ReportMenu;
import com.zayaanit.aspi.enums.ReportParamDataType;
import com.zayaanit.aspi.enums.ReportType;
import com.zayaanit.aspi.exceptions.ResourceNotFoundException;
import com.zayaanit.aspi.model.Report;
import com.zayaanit.aspi.model.RequestParameters;
import com.zayaanit.aspi.report.ReportFieldService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
public abstract class AbstractReportController extends KitController {

	protected List<Report> getReports(String code) {

		Map<String, List<Report>> menuCash = new HashMap<>();

		for(ReportMenu rm : ReportMenu.values()) {
			if(menuCash.get(rm.getGroup()) != null) {
				Report report = new Report();
				report.setPrefix(rm.getGroup());
				report.setCode(rm.name());
				report.setTitle(rm.getDescription());
				menuCash.get(rm.getGroup()).add(report);
			} else {
				List<Report> list = new ArrayList<>();
				Report report = new Report();
				report.setPrefix(rm.getGroup());
				report.setCode(rm.name());
				report.setTitle(rm.getDescription());
				list.add(report);
				menuCash.put(rm.getGroup(), list);
			}
		}

		return menuCash.get(code);
	}

	@GetMapping("/{reportCode}")
	public String loadReportForm(@PathVariable String reportCode, Model model) throws ResourceNotFoundException {
		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf(reportCode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			model.addAttribute("reportFound", false);
			model.addAttribute("message", "Report is in under maintenance.");
			return "pages/RP/RP-form::dynamicreport";
		}

		model.addAttribute("reportFound", true);
		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", rm.getDescription());
		model.addAttribute("reportCode", rm.name());

		return "pages/RP/RP-form::dynamicreport";
	}

	@SuppressWarnings("rawtypes")
	protected ReportFieldService getReportFieldService(ReportMenu reportMenu) {
		if(reportMenu == null) return null;
		try {
			return (ReportFieldService) appContext.getBean(reportMenu.name() + "_Service");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/validate")
	public @ResponseBody Map<String, Object> validate(RequestParameters params){
		ReportMenu rm = ReportMenu.valueOf(params.getReportCode());

		ReportType reportType = ReportType.PDF;
		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			if("reportViewType".equalsIgnoreCase(cristalReportParamName)) {
				reportType = (ReportType) method;
				continue;
			}
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		return getReportFieldService(rm).validateParams(responseHelper, reportParams);
	}


	@SuppressWarnings("unchecked")
	@PostMapping("/print")
	public ResponseEntity<Object> print(RequestParameters params) throws IOException {
		ReportMenu rm = ReportMenu.valueOf(params.getReportCode());

		String message = "";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Optional<Zbusiness> z = zbusinessRepo.findById(sessionManager.getBusinessId());
		String reportName = filePath(z.get().getXrptpath()).concat("/").concat(rm.getFileName());
		if(StringUtils.isBlank(reportName) || !fileExist(reportName)) reportName = filePath(z.get().getXrptdefautl()).concat("/").concat(rm.getFileName());
		if(StringUtils.isBlank(reportName) || !fileExist(reportName)) {
			try {
				reportName = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
								.append(File.separator).append("cr").append(File.separator).append("v2").append(File.separator)
								.append(rm.getFileName()).toString();
			} catch (URISyntaxException e) {
				log.error(ERROR, e.getMessage(), e);
			}
		}

		String reportTitle = StringUtils.isBlank(params.getXtitle()) ? rm.getDescription() : params.getXtitle();
		boolean attachment = true;

		ReportType reportType = ReportType.PDF;
		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			if("reportViewType".equalsIgnoreCase(cristalReportParamName)) {
				reportType = (ReportType) method;
				continue;
			}
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		// FOP
//		if(rm.isEnabledFop()) {
//			try {
//				reportName = new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
//								.append(File.separator).append("xsl").append(File.separator)
//								.append(rm.name() + ".xsl").toString();
//			} catch (URISyntaxException e) {
//				log.error(ERROR, e.getMessage(), e);
//			}
//
//			byte[] byt = null;
//			try {
//				byt = getReportFieldService(rm).getPDFReportByte(reportName, reportParams);
//			} catch (JAXBException | ParserConfigurationException | SAXException | TransformerFactoryConfigurationError
//					| TransformerException e) {
//				log.error(ERROR, e.getMessage(), e);
//			} catch (ParseException e) {
//				log.error(ERROR, e.getMessage(), e);
//			}
//			if(byt == null) {
//				String encodedString = Base64.getEncoder().encodeToString(message.getBytes());
//				return new ResponseEntity<>(encodedString, headers, HttpStatus.OK);
//			}
//
//			String encodedString = Base64.getEncoder().encodeToString(byt);
//			return new ResponseEntity<>(encodedString, headers, HttpStatus.OK);
//		}

		// CRISTAL
		byte[] byt = null;
		InputStream in = printingService.getDataBytes(reportName, reportTitle, attachment, reportParams, reportType);
		if (in == null) {
			message = "Can't generate PDF to print";
			byt = message.getBytes();
		} else {
			final byte[] data = new byte[1024];
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			while (in.read(data) > -1) {
				os.write(data);
			}

			byt = os.toByteArray();

			if(ReportType.EXCEL.equals(reportType) || ReportType.EXCEL_DATA.equals(reportType)) {
				headers.add("Content-Disposition", "attachment; filename=report.xls");
				headers.setContentType(new MediaType("application", "excel"));
			} else {
				headers.add("URL Filter", ".*");
				headers.add("Original Type", "application/.*pdf");
				headers.add("Replacement Type", "application/pdf");
				headers.add("Disposition", "inline");
			}
		}

		String encodedString = Base64.getEncoder().encodeToString(byt);
		return new ResponseEntity<>(encodedString, headers, HttpStatus.OK);
	}

	private void convertObjectAndPutIntoMap(String paramName, ReportParamDataType paramType, Object inputValue, Map<String, Object> reportParams) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		switch (paramType) {
			case INTEGER:
				reportParams.put(paramName, StringUtils.isBlank((String) inputValue) ? -1 : (String) inputValue);
				break;
			case BOOLEAN:
				reportParams.put(paramName, (String) inputValue == null ? 0 : 1);
				break;
			case DATE:
				try {
					reportParams.put(paramName, sdf.parseObject((String) inputValue));
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			case DATESTRING:
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = (Date) sdf.parseObject((String) inputValue);
					reportParams.put(paramName, sdf2.format(date));
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			default:
				reportParams.put(paramName, inputValue);
				break;
		}
	}
}
