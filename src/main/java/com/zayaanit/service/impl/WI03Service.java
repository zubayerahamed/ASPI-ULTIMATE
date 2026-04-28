package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WI03Dto;
import com.zayaanit.repo.ImtrnRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WI03Service extends AbstractGenericService {

	@Autowired private ImtrnRepo imtrnRepo;

	public List<WI03Dto> inwardPieChartWG01_WG02(Integer xbuid, Integer xitem, Integer xwh, String xfdate, String xtdate) {
		List<WI03Dto> result = imtrnRepo.inwardPieChartWG01_WG02(sessionManager.getBusinessId(), xbuid, xitem, xwh, xfdate, xtdate)
				.stream()
				.map(row -> new WI03Dto(((String) row[0]).toString(), (BigDecimal) row[1],  (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}

	public List<WI03Dto> outwardPieChartWG03_WG04(Integer xbuid, Integer xitem, Integer xwh, String xfdate, String xtdate) {
		List<WI03Dto> result = imtrnRepo.outwardPieChartWG03_WG04(sessionManager.getBusinessId(), xbuid, xitem, xwh, xfdate, xtdate)
				.stream()
				.map(row -> new WI03Dto(((String) row[0]).toString(), (BigDecimal) row[1],  (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}

	public List<WI03Dto> barChartWG05(Integer xbuid, Integer xitem, Integer xwh, String xfdate, String xtdate) {
		List<WI03Dto> result = imtrnRepo.barChartWG05(sessionManager.getBusinessId(), xbuid, xitem, xwh, xfdate, xtdate)
				.stream()
				.map(row -> new WI03Dto(((String) row[0]).toString(), (BigDecimal) row[1],  (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}

	public List<WI03Dto> barChartWG06(Integer xbuid, Integer xitem, Integer xwh, String xfdate, String xtdate) {
		List<WI03Dto> result = imtrnRepo.barChartWG06(sessionManager.getBusinessId(), xbuid, xitem, xwh, xfdate, xtdate)
				.stream()
				.map(row -> new WI03Dto(((String) row[0]).toString(), (BigDecimal) row[1],  (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
