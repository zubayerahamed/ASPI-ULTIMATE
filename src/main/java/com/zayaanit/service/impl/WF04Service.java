package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WF04Dto;
import com.zayaanit.model.YearPeriodResult;
import com.zayaanit.repo.AcbalRepo;
import com.zayaanit.service.AcheaderService;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WF04Service extends AbstractGenericService {

	@Autowired private AcbalRepo acbalRepo;
	@Autowired private AcheaderService acheaderService;

	public List<WF04Dto> accountCurrentBalance(Integer xbuid, String xtype) {

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		List<WF04Dto> result = acbalRepo.getAccountsCurrentBalance(sessionManager.getBusinessId(), xbuid, yp.getYear(), xtype)
				.stream()
				.map(row -> new WF04Dto((String) row[0], (BigDecimal) row[1]))
				.collect(Collectors.toList());

		return result;
	}
}
