package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WP02Dto;
import com.zayaanit.repo.PogrnheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WP02Service extends AbstractGenericService {

	@Autowired private PogrnheaderRepo pogrnheaderRepo;

	public List<WP02Dto> supplierPurchaseView(Integer xbuid, Integer xacc, Integer last, String type, String xfdate, String xtdate) {
		if(xacc == null) return Collections.emptyList();
		if("DAYS".equalsIgnoreCase(type)) {
			if(last < 1) last = 1;
			if(last > 100) last = 100;
			List<WP02Dto> result = pogrnheaderRepo.getAccountTransactionSummaryForDays(sessionManager.getBusinessId(), xbuid, xacc, last)
					.stream()
					.map(row -> new WP02Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WP02Dto> result = pogrnheaderRepo.getAccountTransactionSummaryForDateBetween(sessionManager.getBusinessId(), xbuid, xacc, xfdate, xtdate)
				.stream()
				.map(row -> new WP02Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
