package com.zayaanit.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WF01Dto;
import com.zayaanit.model.YearPeriodResult;
import com.zayaanit.repo.AcheaderRepo;
import com.zayaanit.service.AcheaderService;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WF01Service extends AbstractGenericService {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private AcheaderService acheaderService;

	public WF01Dto totalVouchers() {
		long today = acheaderRepo.countByZidAndXdate(sessionManager.getBusinessId(), new Date());

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXper(sessionManager.getBusinessId(), yp.getPeriod());
		long thisYear = acheaderRepo.countByZidAndXyear(sessionManager.getBusinessId(), yp.getYear());

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto openVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Open");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Open");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Open");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto suspendedVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Suspended");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Suspended");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Suspended");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto waitingForPosting() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Balanced");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Balanced");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Balanced");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto postedVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Posted");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Posted");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Posted");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}
}
