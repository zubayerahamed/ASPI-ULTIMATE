package com.zayaanit.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WI01Dto;
import com.zayaanit.repo.ImtorheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WI01Service extends AbstractGenericService {
	@Autowired private ImtorheaderRepo imtorheaderRepo;

	public WI01Dto inventoryTransfer() {
		String today = imtorheaderRepo.todaysWG01(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = imtorheaderRepo.monthWG01(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = imtorheaderRepo.yearWG01(sessionManager.getBusinessId(), LocalDate.now());

		return WI01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}

	public WI01Dto batchProcess() {
		String today = imtorheaderRepo.todaysWG02(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = imtorheaderRepo.monthWG02(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = imtorheaderRepo.yearWG02(sessionManager.getBusinessId(), LocalDate.now());

		return WI01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}

	public WI01Dto inventoryIssue() {
		String today = imtorheaderRepo.todaysWG03(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = imtorheaderRepo.monthWG03(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = imtorheaderRepo.yearWG03(sessionManager.getBusinessId(), LocalDate.now());

		return WI01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}

	public WI01Dto inventoryAdjustment() {
		String today = imtorheaderRepo.todaysWG04(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = imtorheaderRepo.monthWG04(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = imtorheaderRepo.yearWG04(sessionManager.getBusinessId(), LocalDate.now());

		return WI01Dto.builder()
				.today(formatValueOfDashboard(today))
				.thisMonth(formatValueOfDashboard(thisMonth))
				.thisYear(formatValueOfDashboard(thisYear))
				.build();
	}
}
