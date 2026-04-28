package com.zayaanit.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WP01Dto;
import com.zayaanit.repo.PoordheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WP01Service extends AbstractGenericService {

	@Autowired private PoordheaderRepo poordheaderRepo;

	public WP01Dto purchaseOrder() {
		String today = poordheaderRepo.todaysPurchaseOrder(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = poordheaderRepo.monthsPurchaseOrder(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = poordheaderRepo.yearPurchaseOrder(sessionManager.getBusinessId(), LocalDate.now());

		return WP01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WP01Dto grnAndDirectPurchase() {
		String today = poordheaderRepo.todaysGRNAndDirectPurchase(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = poordheaderRepo.monthsGRNAndDirectPurchase(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = poordheaderRepo.yearGRNAndDirectPurchase(sessionManager.getBusinessId(), LocalDate.now());

		return WP01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WP01Dto purchaseReturn() {
		String today = poordheaderRepo.todaysPurchaseReturn(sessionManager.getBusinessId(), LocalDate.now());
		String thisMonth = poordheaderRepo.monthsPurchaseReturn(sessionManager.getBusinessId(), LocalDate.now());
		String thisYear = poordheaderRepo.yearPurchaseReturn(sessionManager.getBusinessId(), LocalDate.now());

		return WP01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}
}
