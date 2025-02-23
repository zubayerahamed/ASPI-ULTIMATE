package com.zayaanit.aspi.report.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.model.FormFieldBuilder;
import com.zayaanit.aspi.model.ResponseHelper;
import com.zayaanit.aspi.service.AcheaderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Aug 29, 2023
 */
@Slf4j
@SuppressWarnings("rawtypes")
@Service(value = "R218_Service")
public class R218_Service extends AbstractReportService {

	@Autowired private AcheaderService acheaderService;

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "As On", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		return fieldsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map reportParams) {
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

		Date xdate = null;

		Map<String, Object> map = (Map<String, Object>) reportParams;
		for(Map.Entry<String, Object> m : map.entrySet()) {
			if("xdate".equalsIgnoreCase(m.getKey())) {
				try {
					xdate = format.parse(m.getValue().toString());
				} catch (ParseException e) {
					log.error(e.getCause().getMessage());
				}
			}
		}

		if(xdate == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		BigDecimal pyear = BigDecimal.valueOf(acheaderService.getYearPeriod(xdate).getYear());

		// Replacable Param
		Map<String, Object> rParamMap = new HashMap<>();
		rParamMap.put("param4", pyear);

		// TODO Auto-generated method stub
		responseHelper.setSuccessStatusAndMessage("Validation success");
		responseHelper.setDisplayMessage(false);
		responseHelper.addDataToResponse("rparam", rParamMap);
		return responseHelper.getResponse();
	}
}
