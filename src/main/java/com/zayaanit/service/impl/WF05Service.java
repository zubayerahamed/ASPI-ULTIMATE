package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WF05Dto;
import com.zayaanit.repo.AcbalRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WF05Service extends AbstractGenericService {

	@Autowired private AcbalRepo acbalRepo;

	public List<WF05Dto> profitAndLossView(Integer xbuid, Integer last, String type) {

		if("DAYS".equalsIgnoreCase(type)) {
			if(last > 100) last = 100;
			List<WF05Dto> result = acbalRepo.profitAndLossViewDays(sessionManager.getBusinessId(), xbuid, last)
					.stream()
					.map(row -> new WF05Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		if(last > 30) last = 30;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<WF05Dto> result = acbalRepo.profitAndLossViewMonths(sessionManager.getBusinessId(), xbuid, last, sdf.format(new Date()))
				.stream()
				.map(row -> new WF05Dto("Year : " + (Integer) row[0] + " | Period : " + (Integer) row[1], (BigDecimal) row[2]))
				.collect(Collectors.toList());
		return result;
	}
}
