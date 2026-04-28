package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WF02Dto;
import com.zayaanit.repo.AcbalRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WF02Service extends AbstractGenericService {

	@Autowired private AcbalRepo acbalRepo;

	public List<WF02Dto> accountTransactions(Integer xbuid, Integer xacc, Integer last, String type) {
		if(xacc == null) return Collections.emptyList();
		if("DAYS".equalsIgnoreCase(type)) {
			if(last > 100) last = 100;
			List<WF02Dto> result = acbalRepo.getAccountTransactionSummaryForDays(sessionManager.getBusinessId(), xbuid, xacc, last)
					.stream()
					.map(row -> new WF02Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		if(last > 30) last = 30;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<WF02Dto> result = acbalRepo.getAccountTransactionSummaryForMonths(sessionManager.getBusinessId(), xbuid, xacc, last, sdf.format(new Date()))
				.stream()
				.map(row -> new WF02Dto("Year : " + (Integer) row[0] + " | Period : " + (Integer) row[1], (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
