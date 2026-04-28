package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WI04Dto;
import com.zayaanit.repo.ImtrnRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WI04Service extends AbstractGenericService {

	@Autowired private ImtrnRepo imtrnRepo;

	public List<WI04Dto> inventoryAgeView(Integer xbuid, Integer xwh, String xcategory, Integer xitem, Integer xfdays, Integer xtdays, String type) {
		List<WI04Dto> result = new ArrayList<>();

		List<WI04Dto> from0to30 = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, 1, 30)
				.stream()
				.map(row -> new WI04Dto("0-30 Days", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(from0to30);

		List<WI04Dto> from31to60 = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, 31, 60)
				.stream()
				.map(row -> new WI04Dto("31-60 Days", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(from31to60);

		List<WI04Dto> from61to180 = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, 61, 180)
				.stream()
				.map(row -> new WI04Dto("61-180 Days", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(from61to180);

		List<WI04Dto> from181to365 = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, 181, 365)
				.stream()
				.map(row -> new WI04Dto("181-365 Days", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(from181to365);

		List<WI04Dto> from366to730 = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, 366, 730)
				.stream()
				.map(row -> new WI04Dto("1-2 Years", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(from366to730);

		List<WI04Dto> custom = imtrnRepo.inventoryAgeView(sessionManager.getBusinessId(), xbuid, xwh, xcategory, xitem, xfdays, xtdays)
				.stream()
				.map(row -> new WI04Dto(xfdays + "-"+ xtdays +" Days", "Value".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[0], "Quantity".equalsIgnoreCase(type) ? BigDecimal.ZERO : (BigDecimal) row[1]))
				.collect(Collectors.toList());
		result.addAll(custom);

		return result;
	}

}
