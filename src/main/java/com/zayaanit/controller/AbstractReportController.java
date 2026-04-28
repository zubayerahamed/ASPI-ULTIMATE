package com.zayaanit.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.enums.ReportMenu;
import com.zayaanit.enums.ReportParamDataType;
import com.zayaanit.exceptions.ResourceNotFoundException;
import com.zayaanit.model.Report;
import com.zayaanit.model.RequestParameters;
import com.zayaanit.repo.XscreenrpdtRepo;
import com.zayaanit.service.rp.ReportFieldService;
import com.zayaanit.service.rp.ReportMenuBase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
public abstract class AbstractReportController extends KitController {


	@Autowired protected XscreenrpdtRepo xscreendetailRepo;
	@Autowired protected JdbcTemplate jdbcTemplate;

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
	protected ReportFieldService getReportFieldService(ReportMenuBase reportMenu) {
		if(reportMenu == null) return null;
		try {
			return (ReportFieldService) appContext.getBean(reportMenu.getGroup() + "_Service");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/validate")
	public @ResponseBody Map<String, Object> validate(RequestParameters params){
		ReportMenuBase rm = null;
		try {
			rm = ReportMenu.valueOf(params.getReportCode());
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			log.error("====** This is virtual report, so don't need any validation **====");
			// For virtual report, there is not validation process
			responseHelper.setSuccessStatusAndMessage("No validation.");
			responseHelper.setDisplayMessage(false);
			return responseHelper.getResponse();
		}

		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			if("reportViewType".equalsIgnoreCase(cristalReportParamName)) {
				continue;
			}
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		return getReportFieldService(rm).validateParams(responseHelper, reportParams);
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
