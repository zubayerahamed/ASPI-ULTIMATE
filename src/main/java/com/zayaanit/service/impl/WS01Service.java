package com.zayaanit.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WS01Dto;
import com.zayaanit.repo.OpordheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WS01Service extends AbstractGenericService {
	@Autowired private OpordheaderRepo opordheaderRepo;

	public WS01Dto salesOrder() {
		String today = opordheaderRepo.todaysSalesOrder(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = opordheaderRepo.monthsSalesOrder(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = opordheaderRepo.yearSalesOrder(sessionManager.getBusinessId(), LocalDate.now());

		return WS01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}

	public WS01Dto salesInvocie() {
		String today = opordheaderRepo.todaysSalesInvoice(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = opordheaderRepo.monthsSalesInvoice(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = opordheaderRepo.yearSalesInvoice(sessionManager.getBusinessId(), LocalDate.now());

		return WS01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}

	public WS01Dto salesReturn() {
		String today = opordheaderRepo.todaysSalesReturn(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = opordheaderRepo.monthsSalesReturn(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = opordheaderRepo.yearSalesReturn(sessionManager.getBusinessId(), LocalDate.now());

		return WS01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}
}
