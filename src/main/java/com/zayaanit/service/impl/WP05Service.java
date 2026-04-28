package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WP05Dto;
import com.zayaanit.repo.PogrnheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WP05Service extends AbstractGenericService {

	@Autowired private PogrnheaderRepo pogrnheaderRepo;

	public List<WP05Dto> topItemPurchase(Integer xbuid, Integer topxitem, Integer last, String type, String xfdate, String xtdate) {
		if(topxitem == null) return Collections.emptyList();
		if("DAYS".equalsIgnoreCase(type)) {
			if(last < 1) last = 1; 
			if(last > 100) last = 100;
			List<WP05Dto> result = pogrnheaderRepo.getTopItemPurchaseForDays(sessionManager.getBusinessId(), xbuid, topxitem, last)
					.stream()
					.map(row -> new WP05Dto((String) row[0], (BigDecimal) row[1], (BigDecimal) row[2]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WP05Dto> result = pogrnheaderRepo.getTopItemPurchaseForDateBetween(sessionManager.getBusinessId(), xbuid, topxitem, xfdate, xtdate)
				.stream()
				.map(row -> new WP05Dto((String) row[0], (BigDecimal) row[1], (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
