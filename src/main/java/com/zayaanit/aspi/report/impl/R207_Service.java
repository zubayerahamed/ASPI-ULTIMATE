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
@Service(value = "R207_Service")
public class R207_Service extends AbstractReportService {

	@Autowired private AcheaderService acheaderService;

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		return fieldsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map reportParams) {
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

		Date xfdate = null;
		Date xtdate = null;

		Map<String, Object> map = (Map<String, Object>) reportParams;
		for(Map.Entry<String, Object> m : map.entrySet()) {
			if("xfdate".equalsIgnoreCase(m.getKey())) {
				try {
					xfdate = format.parse(m.getValue().toString());
				} catch (ParseException e) {
					log.error(e.getCause().getMessage());
				}
			}
			if("xtdate".equalsIgnoreCase(m.getKey())) {
				try {
					xtdate = format.parse(m.getValue().toString());
				} catch (ParseException e) {
					log.error(e.getCause().getMessage());
				}
			}
		}

		if(xfdate == null || xtdate == null) {
			responseHelper.setErrorStatusAndMessage("Dates required");
			return responseHelper.getResponse();
		}

		if(xtdate.before(xfdate)) {
			responseHelper.setErrorStatusAndMessage("From date must be before or same as after date");
			return responseHelper.getResponse();
		}

		BigDecimal fyear = BigDecimal.valueOf(acheaderService.getYearPeriod(xfdate).getYear());
		BigDecimal tyear = BigDecimal.valueOf(acheaderService.getYearPeriod(xtdate).getYear());

		if(fyear.compareTo(tyear) != 0) {
			responseHelper.setErrorStatusAndMessage("Dates should be within the same fiscal year!");
			return responseHelper.getResponse();
		}

		// Replacable Param
		Map<String, Object> rParamMap = new HashMap<>();
		rParamMap.put("param5", fyear);

		// TODO Auto-generated method stub
		responseHelper.setSuccessStatusAndMessage("Validation success");
		responseHelper.setDisplayMessage(false);
		responseHelper.addDataToResponse("rparam", rParamMap);
		return responseHelper.getResponse();
	}
}
