package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WI05Dto;
import com.zayaanit.repo.MoheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WI05Service extends AbstractGenericService {

	@Autowired private MoheaderRepo moheaderRepo;

	public List<WI05Dto> productionCostingView(Integer xbuid, Integer xitem, Integer last, String type, String xfdate, String xtdate) {

		if("BATCHES".equalsIgnoreCase(type)) {
			if(last < 1) last = 1; 
			if(last > 100) last = 100;
			List<WI05Dto> result = moheaderRepo.batchWI05WG01(sessionManager.getBusinessId(), xbuid, xitem, last)
					.stream()
					.map(row -> new WI05Dto(((Integer) row[0]).toString(), (BigDecimal) row[1], (BigDecimal) row[2]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WI05Dto> result = moheaderRepo.dateBetweenWI05WG01(sessionManager.getBusinessId(), xbuid, xitem, xfdate, xtdate)
				.stream()
				.map(row -> new WI05Dto(((Integer) row[0]).toString(), (BigDecimal) row[1], (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
