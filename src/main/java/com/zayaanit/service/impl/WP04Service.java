package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WP04Dto;
import com.zayaanit.repo.PogrnheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WP04Service extends AbstractGenericService {

	@Autowired private PogrnheaderRepo pogrnheaderRepo;

	public List<WP04Dto> topSupplierTransaction(Integer xbuid, Integer topxcus, Integer last, String type, String xfdate, String xtdate) {
		if(topxcus == null) return Collections.emptyList();
		if("DAYS".equalsIgnoreCase(type)) {
			if(last < 1) last = 1; 
			if(last > 100) last = 100;
			List<WP04Dto> result = pogrnheaderRepo.getTopSupplierTransactionForDays(sessionManager.getBusinessId(), xbuid, topxcus, last)
					.stream()
					.map(row -> new WP04Dto((String) row[0], (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WP04Dto> result = pogrnheaderRepo.getTopSupplierTransactionForDateBetween(sessionManager.getBusinessId(), xbuid, topxcus, xfdate, xtdate)
				.stream()
				.map(row -> new WP04Dto((String) row[0], (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
