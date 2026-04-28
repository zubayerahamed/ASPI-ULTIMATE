package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WS02Dto;
import com.zayaanit.repo.OpdoheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WS02Service extends AbstractGenericService {

	@Autowired private OpdoheaderRepo opdoheaderRepo;

	public List<WS02Dto> customerSalesStatement(Integer xbuid, Integer xcus, Integer last, String type, String xfdate, String xtdate) {

		if("DAYS".equalsIgnoreCase(type)) {
			if(last > 100) last = 100;
			List<WS02Dto> result = opdoheaderRepo.getCustomerSalesStatementForDays(sessionManager.getBusinessId(), xbuid, xcus, last)
					.stream()
					.map(row -> new WS02Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WS02Dto> result = opdoheaderRepo.getCustomerSalesStatementForDateBetween(sessionManager.getBusinessId(), xbuid, xcus, xfdate, xtdate)
				.stream()
				.map(row -> new WS02Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
