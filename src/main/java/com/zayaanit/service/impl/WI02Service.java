package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WI02Dto;
import com.zayaanit.repo.ImcurstockviewRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WI02Service extends AbstractGenericService {

	@Autowired private ImcurstockviewRepo imcurstockviewRepo;

	public List<WI02Dto> currentStockStatus(Integer xbuid, String xgroup, Integer xwh, String type) {

		if("Value".equalsIgnoreCase(type)) {
			List<WI02Dto> result = imcurstockviewRepo.currentStockStatusQAV(sessionManager.getBusinessId(), xbuid, xgroup, xwh)
					.stream()
					.map(row -> new WI02Dto(((String) row[0]).toString(), BigDecimal.ZERO,  (BigDecimal) row[2]))
					.collect(Collectors.toList());
			return result;
		} else if ("Quantity".equalsIgnoreCase(type)) {
			List<WI02Dto> result = imcurstockviewRepo.currentStockStatusQAV(sessionManager.getBusinessId(), xbuid, xgroup, xwh)
					.stream()
					.map(row -> new WI02Dto(((String) row[0]).toString(), (BigDecimal) row[1], BigDecimal.ZERO))
					.collect(Collectors.toList());
			return result;
		} 

		List<WI02Dto> result = imcurstockviewRepo.currentStockStatusQAV(sessionManager.getBusinessId(), xbuid, xgroup, xwh)
				.stream()
				.map(row -> new WI02Dto(((String) row[0]).toString(), (BigDecimal) row[1],  (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
