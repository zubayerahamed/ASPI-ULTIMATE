package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WS04Dto;
import com.zayaanit.repo.OpdoheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WS04Service extends AbstractGenericService {

	@Autowired private OpdoheaderRepo opdoheaderRepo;

	public List<WS04Dto> topCustomerTransaction(Integer xbuid, Integer topxcus, Integer last, String type, String xfdate, String xtdate) {
		if(topxcus == null) return Collections.emptyList();
		if("DAYS".equalsIgnoreCase(type)) {
			if(last < 1) last = 1; 
			if(last > 100) last = 100;
			List<WS04Dto> result = opdoheaderRepo.getTopCustomerTransactionForDays(sessionManager.getBusinessId(), xbuid, topxcus, last)
					.stream()
					.map(row -> new WS04Dto((String) row[0], (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WS04Dto> result = opdoheaderRepo.getTopCustomerTransactionForDateBetween(sessionManager.getBusinessId(), xbuid, topxcus, xfdate, xtdate)
				.stream()
				.map(row -> new WS04Dto((String) row[0], (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
