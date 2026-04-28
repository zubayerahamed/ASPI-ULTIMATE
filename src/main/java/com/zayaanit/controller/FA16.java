package com.zayaanit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Acdetail;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcdetailPK;
import com.zayaanit.entity.pk.AcheaderPK;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.TempvoucherPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.ExcelCellType;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.AsyncCSVResult;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.model.YearPeriodResult;
import com.zayaanit.repo.AcdetailRepo;
import com.zayaanit.repo.AcheaderRepo;
import com.zayaanit.repo.AcmstRepo;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.CadocRepo;
import com.zayaanit.repo.TempvoucherRepo;
import com.zayaanit.service.AcheaderService;
import com.zayaanit.service.ImportExportService;
import com.zayaanit.service.impl.AsyncCSVProcessor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 11, 2025
 */
@Slf4j
@Controller
@RequestMapping("/FA16")
public class FA16 extends KitController {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcheaderService acheaderService;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private TempvoucherRepo tempVoucherRepo;
	@Autowired private AsyncCSVProcessor asyncCSVProcessor;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA16";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(
			@RequestParam (required = false) String xvoucher, 
			@RequestParam(required = false) String frommenu,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request, Model model) {

		model.addAttribute("fa16ImportToken", sessionManager.getFromMap("FA16_IMPORT_TOKEN"));
		model.addAttribute("deftab", "tab1");
		model.addAttribute("voucherTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId()));

		Sort.Direction direction = Sort.Direction.ASC; 
		Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(direction, "xrow")));
		List<Tempvoucher> tempvouchers = tempVoucherRepo.findAllByZid(sessionManager.getBusinessId(), pageable).toList();
		for(Tempvoucher t : tempvouchers) {
			if(t.getBusinessUnit() != null) {
				Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), t.getBusinessUnit()));
				if(businessUnitOp.isPresent()) t.setBusinessUnitName(businessUnitOp.get().getXname());
			}

			if(t.getDebitAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getDebitAcc()));
				if(accountOp.isPresent()) t.setDebitAccountName(accountOp.get().getXdesc());
			}

			if(t.getDebitSubAcc() != null) {
				Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getDebitSubAcc()));
				if(acsubOp.isPresent()) t.setDebitSubAccountName(acsubOp.get().getXname());
			}

			if(t.getCreditAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getCreditAcc()));
				if(accountOp.isPresent()) t.setCreditAccountName(accountOp.get().getXdesc());
			}

			if(t.getCreditSubAcc() != null) {
				Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getCreditSubAcc()));
				if(acsubOp.isPresent()) t.setCreditSubAccountName(acsubOp.get().getXname());
			}
		}
		model.addAttribute("tempvouchers", tempvouchers);
		long totalData = tempVoucherRepo.countByZid(sessionManager.getBusinessId());
		model.addAttribute("totalData", totalData);
		model.addAttribute("totalPages", (int) Math.ceil((double) totalData / size));
		model.addAttribute("currentPage", page);
		model.addAttribute("errorRecords", tempVoucherRepo.countErrorRecords(sessionManager.getBusinessId()));

		int startRecord = 0;
		int endRecord = 0;

		if (totalData > 0) {
			startRecord = (page * size) + 1;
			endRecord = Math.min(startRecord + tempvouchers.size() - 1, (int) totalData);
		}

		model.addAttribute("startRecord", startRecord);
		model.addAttribute("endRecord", endRecord);

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xvoucher)) {
				model.addAttribute("acheader", Acheader.getDefaultInstance());
				return "pages/FA16/FA16-fragments::main-form";
			}

			Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
			Acheader acheader = null;
			if(op.isPresent()) {
				acheader = op.get();

				if(acheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), acheader.getXbuid()));
					if(cabunitOp.isPresent()) acheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(acheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acheader.getXstaff()));
					if(acsubOp.isPresent()) acheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("acheader", acheader != null ? acheader : Acheader.getDefaultInstance());

			if(acheader != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("FA16")
							.xfunc("View Data")
							.xsource("FA16")
							.xtable(null)
							.xdata(acheader.getXvoucher().toString())
							.xstatement("View data " + acheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}

			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA16", Integer.valueOf(xvoucher));
			model.addAttribute("documents", cdocList);

			return "pages/FA16/FA16-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("acheader", Acheader.getDefaultInstance());
		return "pages/FA16/FA16";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xvoucher, @RequestParam String xrow, @RequestParam(required = false) Integer xacc, Model model) {
		if("RESET".equalsIgnoreCase(xvoucher) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA16/FA16-fragments::detail-table";
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
		if(!oph.isPresent()) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA16/FA16-fragments::detail-table";
		}
		model.addAttribute("acheader", oph.get());

		List<Acdetail> detailList = acdetailRepo.findAllByZidAndXvoucher(sessionManager.getBusinessId(), Integer.parseInt(xvoucher));
		for(Acdetail detail : detailList) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), detail.getXacc()));
			if(accountOp.isPresent()) {
				detail.setAccountName(accountOp.get().getXdesc());
				detail.setAccountUsage(accountOp.get().getXaccusage());
			}

			Optional<Acsub> subAccountOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), detail.getXsub()));
			if(subAccountOp.isPresent()) detail.setSubAccountName(subAccountOp.get().getXname());
		}
		model.addAttribute("detailList", detailList);

		Acmst account = null;
		if(xacc != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), xacc));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Acdetail acdetail = Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
			if(account != null) {
				acdetail.setXacc(account.getXacc());
				acdetail.setAccountName(account.getXdesc());
				acdetail.setAccountUsage(account.getXaccusage());
			}

			model.addAttribute("acdetail", acdetail);
			return "pages/FA16/FA16-fragments::detail-table";
		}

		Optional<Acdetail> acdetailOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher), Integer.parseInt(xrow)));
		Acdetail acdetail = acdetailOp.isPresent() ? acdetailOp.get() : Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
		if(acdetail != null && acdetail.getXacc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}
		if(acdetail != null && account != null) {
			acdetail.setXacc(account.getXacc());
			acdetail.setAccountName(account.getXdesc());
			acdetail.setAccountUsage(account.getXaccusage());
		}

		if(acdetail != null && acdetail.getXrow() != 0) {
			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA16")
						.xfunc("View Detail")
						.xsource("FA16")
						.xtable(null)
						.xdata(acdetail.getXvoucher().toString() + "/" + acdetail.getXrow())
						.xstatement("View detail data " + acdetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		}

		model.addAttribute("acdetail", acdetail);
		return "pages/FA16/FA16-fragments::detail-table";
	}

	@GetMapping("/import-table")
	public String loadImportTableFragment(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request, Model model) {

		model.addAttribute("fa16ImportToken", sessionManager.getFromMap("FA16_IMPORT_TOKEN"));
		model.addAttribute("deftab", "tab1");
		model.addAttribute("voucherTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId()));

		Sort.Direction direction = Sort.Direction.ASC; 
		Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(direction, "xrow")));
		List<Tempvoucher> tempvouchers = tempVoucherRepo.findAllByZid(sessionManager.getBusinessId(), pageable).toList();
		for(Tempvoucher t : tempvouchers) {
			if(t.getBusinessUnit() != null) {
				Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), t.getBusinessUnit()));
				if(businessUnitOp.isPresent()) t.setBusinessUnitName(businessUnitOp.get().getXname());
			}

			if(t.getDebitAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getDebitAcc()));
				if(accountOp.isPresent()) t.setDebitAccountName(accountOp.get().getXdesc());
			}

			if(t.getDebitSubAcc() != null) {
				Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getDebitSubAcc()));
				if(acsubOp.isPresent()) t.setDebitSubAccountName(acsubOp.get().getXname());
			}

			if(t.getCreditAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getCreditAcc()));
				if(accountOp.isPresent()) t.setCreditAccountName(accountOp.get().getXdesc());
			}

			if(t.getCreditSubAcc() != null) {
				Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getCreditSubAcc()));
				if(acsubOp.isPresent()) t.setCreditSubAccountName(acsubOp.get().getXname());
			}
		}
		model.addAttribute("tempvouchers", tempvouchers);
		long totalData = tempVoucherRepo.countByZid(sessionManager.getBusinessId());
		model.addAttribute("totalData", totalData);
		model.addAttribute("totalPages", (int) Math.ceil((double) totalData / size));
		model.addAttribute("currentPage", page);
		model.addAttribute("errorRecords", tempVoucherRepo.countErrorRecords(sessionManager.getBusinessId()));

		int startRecord = 0;
		int endRecord = 0;

		if (totalData > 0) {
			startRecord = (page * size) + 1;
			endRecord = Math.min(startRecord + tempvouchers.size() - 1, (int) totalData);
		}

		model.addAttribute("startRecord", startRecord);
		model.addAttribute("endRecord", endRecord);

		return "pages/FA16/FA16-fragments::import-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acheader acheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateAcheader(acheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(acheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher date required");
			return responseHelper.getResponse();
		}

		if(acheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acheader.getXvtype())) {
			responseHelper.setErrorStatusAndMessage("Voucher Type required");
			return responseHelper.getResponse();
		}

		YearPeriodResult yp = acheaderService.getYearPeriod(acheader.getXdate());
		if(yp == null) {
			responseHelper.setErrorStatusAndMessage("Error with voucher year period.");
			return responseHelper.getResponse();
		}
		acheader.setXyear(yp.getYear());
		acheader.setXper(yp.getPeriod());

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}
		acheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(acheader.getSubmitFor())) {
			acheader.setXtype("Imported");
			acheader.setXstatusjv("Balanced");
			acheader.setXvoucher(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "FA16"));
			acheader.setZid(sessionManager.getBusinessId());
			try {
				acheader = acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA16")
						.xfunc("Add Data")
						.xsource("FA16")
						.xtable(null)
						.xdata(acheader.getXvoucher().toString())
						.xstatement("Add data " + acheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acheader.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Voucher created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), acheader.getXvoucher()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if("Posted".equalsIgnoreCase(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already Posted");
			return responseHelper.getResponse();
		}

		Acheader existObj = op.get();
		BeanUtils.copyProperties(acheader, existObj, "zid", "zuserid", "ztime", "xvoucher", "xtype", "xstatusjv");

		try {
			existObj = acheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Update Data")
					.xsource("FA16")
					.xtable(null)
					.xdata(existObj.getXvoucher().toString())
					.xstatement("Update data " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + existObj.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Acdetail acdetail, BindingResult bindingResult){
		if(acdetail.getXvoucher() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), acdetail.getXvoucher()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = oph.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		if(acdetail.getXacc() == null) {
			responseHelper.setErrorStatusAndMessage("Account requried");
			return responseHelper.getResponse();
		}

		Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
		if(!accountOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Account not found");
			return responseHelper.getResponse();
		}
		Acmst account = accountOp.get();
		if(!"Default".equalsIgnoreCase(account.getXaccusage())) {
			if(acdetail.getXsub() == null) {
				responseHelper.setErrorStatusAndMessage("Sub account required");
				return responseHelper.getResponse();
			}
		}

		if(acdetail.getXdebit() == null) acdetail.setXdebit(BigDecimal.ZERO);
		if(acdetail.getXcredit() == null) acdetail.setXcredit(BigDecimal.ZERO);

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Enter only debit or credit amount!");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 0 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Enter valid amount!");
			return responseHelper.getResponse();
		}

		acdetail.setXprime(acdetail.getXdebit().subtract(acdetail.getXcredit()));

		// Create new
		if(SubmitFor.INSERT.equals(acdetail.getSubmitFor())) {
			acdetail.setXrow(acdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), acdetail.getXvoucher()));
			acdetail.setZid(sessionManager.getBusinessId());
			try {
				acdetail = acdetailRepo.save(acdetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA16")
						.xfunc("Update Detail")
						.xsource("FA16")
						.xtable(null)
						.xdata(acdetail.getXvoucher().toString() + "/" + acdetail.getXrow())
						.xstatement("Add detail data " + acdetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), acdetail.getXvoucher());
			acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
			try {
				acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA16")
						.xfunc("Update Data")
						.xsource("FA16")
						.xtable(null)
						.xdata(acheader.getXvoucher().toString())
						.xstatement("Update data " + acheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acdetail.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Voucher detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Acdetail> existOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), acdetail.getXvoucher(), acdetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found in this system");
			return responseHelper.getResponse();
		}

		Acdetail exist = existOp.get();
		BeanUtils.copyProperties(acdetail, exist, "zid", "zuserid", "ztime", "xvoucher", "xrow", "xacc");
		try {
			acdetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Update Detail")
					.xsource("FA16")
					.xtable(null)
					.xdata(exist.getXvoucher().toString() + "/" + exist.getXrow())
					.xstatement("Update detail data " + exist.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), exist.getXvoucher());
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		try {
			acheaderRepo.save(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Update Data")
					.xsource("FA16")
					.xtable(null)
					.xdata(acheader.getXvoucher().toString())
					.xstatement("Update data " + acheader.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acdetail.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xvoucher){
		Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(op.get().getXstatusjv().equals("Posted")) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		try {
			acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), xvoucher);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Delete All Detail")
					.xsource("FA16")
					.xtable(null)
					.xdata(op.get().getXvoucher().toString())
					.xstatement("Delete all detail data : " + op.get().toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		Acheader obj = op.get();
		try {
			acheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Delete Data")
					.xsource("FA16")
					.xtable(null)
					.xdata(obj.getXvoucher().toString())
					.xstatement("Delete data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xvoucher, @RequestParam Integer xrow){
		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = oph.get();

		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		Optional<Acdetail> op = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), xvoucher, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Acdetail obj = op.get();
		try {
			acdetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Delete Detail")
					.xsource("FA16")
					.xtable(null)
					.xdata(obj.getXvoucher().toString() + "/" + obj.getXrow())
					.xstatement("Delete detail data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		// Update line amount and total amount of header
		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), xvoucher);
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		try {
			acheaderRepo.save(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA16")
					.xfunc("Update Data")
					.xsource("FA16")
					.xtable(null)
					.xdata(acheader.getXvoucher().toString())
					.xstatement("Update data : " + acheader.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + xvoucher));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+xvoucher+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/download/template")
	public void downloadTemplate(@RequestParam String type, HttpServletResponse response) throws IOException {
		String fileName;
		String contentType;

		if ("XLSX".equalsIgnoreCase(type)) {
			fileName = "Vouchers.xlsx";
			contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if ("XLS".equalsIgnoreCase(type)) {
			fileName = "Vouchers.xls";
			contentType = "application/vnd.ms-excel";
		} else {
			fileName = "Vouchers.csv";
			contentType = "text/csv";
		}

		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		ServletOutputStream out = response.getOutputStream();

		if ("CSV".equalsIgnoreCase(type)) {
			writeCsv(out);
		} else {
			writeExcel(type, out);
		}

		out.flush();
		out.close();
		response.flushBuffer();
	}

	private void writeExcel(String type, ServletOutputStream out) throws IOException {
		Workbook workbook;

		if ("XLSX".equalsIgnoreCase(type)) {
			workbook = new SXSSFWorkbook(100); // Efficient for large files
		} else {
			workbook = new HSSFWorkbook(); // For older XLS format
		}

		Sheet sheet = workbook.createSheet("Voucher");
		Row row = sheet.createRow(0);

		CellStyle textStyle = workbook.createCellStyle();
		CellStyle integerStyle = workbook.createCellStyle();
		integerStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

		CellStyle dateStyle = workbook.createCellStyle();
		short dateFormat = workbook.createDataFormat().getFormat("MM/dd/yyyy");
		dateStyle.setDataFormat(dateFormat);

		CellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

		createCell(workbook, sheet, row, 0, "Voucher Date", ExcelCellType.DATE, dateStyle);
		createCell(workbook, sheet, row, 1, "Business Unit", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 2, "Debit Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 3, "Debit Sub Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 4, "Credit Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 5, "Credit Sub Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 6, "Amount", ExcelCellType.DOUBLE, doubleStyle);
		createCell(workbook, sheet, row, 7, "Narration", ExcelCellType.RICHTEXT, textStyle);

		workbook.write(out);
		workbook.close();
	}

	private void writeCsv(ServletOutputStream out) throws IOException {
		StringBuilder csvData = new StringBuilder();
		csvData.append("Voucher Date,Business Unit,Debit Account,Debit Sub Account,Credit Account,Credit Sub Account,Amount,Narration\n");

		out.write(csvData.toString().getBytes(StandardCharsets.UTF_8));
	}

	private void createCell(Workbook workbook, Sheet sheet, Row row, int columnCount, Object valueOfCell, ExcelCellType type, CellStyle style) {

		Cell cell = row.createCell(columnCount);
		if(valueOfCell == null) {
			cell.setCellValue("");
		} else {
			if (ExcelCellType.INTEGER.equals(type)) {
				if(valueOfCell instanceof Integer) {
					cell.setCellValue((Integer) valueOfCell);
				} else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.DATE.equals(type)) {
				if(valueOfCell instanceof Date) {
					cell.setCellValue((Date) valueOfCell);
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.TEXT.equals(type) || ExcelCellType.RICHTEXT.equals(type)) {
				cell.setCellValue((String) valueOfCell);
			} else if (ExcelCellType.BOOLEAN.equals(type)) {
				cell.setCellValue((Boolean) valueOfCell);
			} else if (ExcelCellType.DOUBLE.equals(type)) {
				if(valueOfCell instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) valueOfCell).doubleValue());
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			}
		}

		cell.setCellStyle(style);
	}

	@PostMapping("/process/excel")
	public @ResponseBody Map<String, Object> processExcel(
			@RequestParam("fileName") String fileName,
			@RequestParam("sheetName") String sheetName
			){

		String fileLocation = appConfig.getImportExportPath() + File.separator + fileName;

		File uploadedFile = null;
		try {
			uploadedFile = new File(fileLocation);
			if (!uploadedFile.exists()) {
				responseHelper.setErrorStatusAndMessage("File upload failed");
				return responseHelper.getResponse();
			}
		} catch (Exception e) {
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}

		String token = UUID.randomUUID().toString();

		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
				.setUpdateExisting(false)
				.setIgnoreHeading(true)
				.setDelimeterType(',')
				.setImportDate(new Date())
				.setLatch(new CountDownLatch(1))
				.setToken(token)
				.setProgress(0.0)
				.setIsWorkInProgress(true)
				.setAllOk(true)
				.setFileName(fileName)
				.setSelectedSheetName(sheetName)
				.setUploadedFileLocation(fileLocation)
				.setModuleName("FA16")
				.setBusinessId(sessionManager.getBusinessId())
				.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

		ImportExportService importExportService = getImportExportService("FA16");

		asyncCSVProcessor.processDataFromExcel(asyncCSVResult, importExportService);

		sessionManager.addToMap(token, asyncCSVResult);
		sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
		sessionManager.addToMap("FA16_IMPORT_TOKEN", token);

		responseHelper.addDataToResponse("asyncCSVResult", asyncCSVResult);

		responseHelper.setSuccessStatusAndMessage("Excel processing started");
		return responseHelper.getResponse();
	}

	@PostMapping("/upload/chunk")
	public @ResponseBody Map<String, Object> uploadChunk(
			@RequestParam("file") MultipartFile file,
			@RequestParam("currentChunk") int currentChunk, 
			@RequestParam("totalChunks") int totalChunks,
			@RequestParam("fileName") String fileName,
			@RequestParam("initialStart") String initialStart) throws IOException {

		// Ensure the upload directory exists
		File uploadDir = new File(appConfig.getImportExportPath());
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// Remove file if the upload is initially started
		if("Y".equalsIgnoreCase(initialStart) && Files.exists(Paths.get(appConfig.getImportExportPath(), fileName))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Files.copy(Paths.get(appConfig.getImportExportPath(), fileName), Paths.get(appConfig.getImportExportPath(), "b_"+sdf.format(new Date()) +"_" + fileName));
			Files.delete(Paths.get(appConfig.getImportExportPath(), fileName));
		}

		// Create or append to the file
		Path filePath = Paths.get(appConfig.getImportExportPath(), fileName);
		Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		if (currentChunk == totalChunks - 1) {

			String fileLocation = appConfig.getImportExportPath() + File.separator + fileName;

			File uploadedFile = null;
			try {
				uploadedFile = new File(fileLocation);
				if (!uploadedFile.exists()) {
					responseHelper.setErrorStatusAndMessage("File upload failed");
					return responseHelper.getResponse();
				}
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
				return responseHelper.getResponse();
			}

			String extention = getFileExtension(uploadedFile);
			responseHelper.addDataToResponse("fileType", extention);
			responseHelper.addDataToResponse("fileName", fileName);
			if("xlsx".equalsIgnoreCase(extention) || "xls".equalsIgnoreCase(extention)) {
				try (
						InputStream inputStream = new FileInputStream(uploadedFile); 
						Workbook workbook = "xlsx".equalsIgnoreCase(extention) ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);
					) {

					List<String> sheetNames = new ArrayList<>();
					for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
						sheetNames.add(workbook.getSheetName(i));
					}

					responseHelper.addDataToResponse("sheetNames", sheetNames);
				} catch (Exception e) {
					responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
					return responseHelper.getResponse();
				}
			} else {
				String token = UUID.randomUUID().toString();

				AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
						.setUpdateExisting(false)
						.setIgnoreHeading(true)
						.setDelimeterType(',')
						.setImportDate(new Date())
						.setLatch(new CountDownLatch(1))
						.setToken(token)
						.setProgress(0.0)
						.setIsWorkInProgress(true)
						.setAllOk(true)
						.setFileName(fileName)
						.setUploadedFileLocation(fileLocation)
						.setModuleName("FA16")
						.setBusinessId(sessionManager.getBusinessId())
						.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

				ImportExportService importExportService = getImportExportService("FA16");

				asyncCSVProcessor.processDataFromCSV(asyncCSVResult, importExportService);

				sessionManager.addToMap(token, asyncCSVResult);
				sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
				sessionManager.addToMap("FA16_IMPORT_TOKEN", token);

				responseHelper.addDataToResponse("asyncCSVResult", asyncCSVResult);
			}

		}

		responseHelper.setSuccessStatusAndMessage("File uploaded successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/import/data/confirm/{post}")
	public @ResponseBody Map<String, Object> confirmDataImport(@PathVariable Integer post) throws IOException {

		try {
			tempVoucherRepo.FA_ImportVoucher(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), post.equals(1));
		} catch (Exception e) {
			log.error(e.getCause().getMessage());
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("import-table-container", "/FA16/import-table?page=0&size=10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Vouchers confirmed " + (post.equals(1) ? "with post" : "without post"));
		return responseHelper.getResponse();
	}

	@PostMapping("/import/validation/confirm")
	public @ResponseBody Map<String, Object> validateAndConfirmImport() throws IOException {

		String token = UUID.randomUUID().toString();
		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
				.setUpdateExisting(false)
				.setIgnoreHeading(true)
				.setDelimeterType(',')
				.setImportDate(new Date())
				.setLatch(new CountDownLatch(1))
				.setToken(token)
				.setProgress(0.0)
				.setIsWorkInProgress(true)
				.setAllOk(true)
				.setFileName(null)
				.setUploadedFileLocation(null)
				.setModuleName("FA16")
				.setBusinessId(sessionManager.getBusinessId())
				.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

		ImportExportService importExportService = getImportExportService("FA16");

		asyncCSVProcessor.confirmImportData(asyncCSVResult, importExportService);

		sessionManager.addToMap(token, asyncCSVResult);
		sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
		sessionManager.addToMap("FA16_IMPORT_TOKEN", token);

		responseHelper.addDataToResponse("asyncCSVResult", asyncCSVResult);
		responseHelper.setSuccessStatusAndMessage("Validation & Confirm Import process started");
		return responseHelper.getResponse();
	}

	@GetMapping("/progress/status/{token}")
	public @ResponseBody AsyncCSVResult checkProgressStatus(@PathVariable String token){
		AsyncCSVResult asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap(token);

		if(asyncCSVResult.isTerminated()) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setAllOk(false);
			asyncCSVResult.setError("Process Terminated");
			sessionManager.removeFromMap(token);
			sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
			return asyncCSVResult;
		}

		if(asyncCSVResult.getIsWorkInProgress()) {
			if(!asyncCSVResult.isAllOk()) {
				asyncCSVResult.setIsWorkInProgress(false);
				sessionManager.removeFromMap(token);
				sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
				return asyncCSVResult;
			}
		}

		if(asyncCSVResult.getLatch().getCount() == 0) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setProgress(100.00);
			sessionManager.removeFromMap(token);
			sessionManager.removeFromMap("FA16_IMPORT_TOKEN");
		}

		return asyncCSVResult;
	}

	@GetMapping("/process/terminate")
	public @ResponseBody AsyncCSVResult terminateProcess(){
		if(sessionManager.getFromMap("FA16_IMPORT_TOKEN") == null) return new AsyncCSVResult();
		if(sessionManager.getFromMap((String) sessionManager.getFromMap("FA16_IMPORT_TOKEN")) == null) return new AsyncCSVResult();

		AsyncCSVResult asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap((String) sessionManager.getFromMap("FA16_IMPORT_TOKEN"));

		if(asyncCSVResult.getIsWorkInProgress()) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setAllOk(false);
			asyncCSVResult.setTerminated(true);
			asyncCSVResult.setError("Process Terminated");
			return asyncCSVResult;
		}

		return asyncCSVResult;
	}

	@GetMapping("/import/edit")
	public String loadRowEditForm(@RequestParam Integer xrow, Model model) {
		Optional<Tempvoucher> vOp = tempVoucherRepo.findById(new TempvoucherPK(sessionManager.getBusinessId(), xrow));
		Tempvoucher t = vOp.isPresent() ? vOp.get() : new Tempvoucher();
		if(t.getBusinessUnit() != null) {
			Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), t.getBusinessUnit()));
			if(businessUnitOp.isPresent()) t.setBusinessUnitName(businessUnitOp.get().getXname());
		}

		if(t.getDebitAcc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getDebitAcc()));
			if(accountOp.isPresent()) t.setDebitAccountName(accountOp.get().getXdesc());
		}

		if(t.getDebitSubAcc() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getDebitSubAcc()));
			if(acsubOp.isPresent()) t.setDebitSubAccountName(acsubOp.get().getXname());
		}

		if(t.getCreditAcc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getCreditAcc()));
			if(accountOp.isPresent()) t.setCreditAccountName(accountOp.get().getXdesc());
		}

		if(t.getCreditSubAcc() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getCreditSubAcc()));
			if(acsubOp.isPresent()) t.setCreditSubAccountName(acsubOp.get().getXname());
		}
		model.addAttribute("t", t);
		return "pages/FA16/FA16-fragments::row-edit-form";
	}

	@GetMapping("/import/row-actual")
	public String loadSingleRow(@RequestParam Integer xrow, Model model) {
		Optional<Tempvoucher> vOp = tempVoucherRepo.findById(new TempvoucherPK(sessionManager.getBusinessId(), xrow));
		Tempvoucher t = vOp.isPresent() ? vOp.get() : new Tempvoucher();
		if(t.getBusinessUnit() != null) {
			Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), t.getBusinessUnit()));
			if(businessUnitOp.isPresent()) t.setBusinessUnitName(businessUnitOp.get().getXname());
		}

		if(t.getDebitAcc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getDebitAcc()));
			if(accountOp.isPresent()) t.setDebitAccountName(accountOp.get().getXdesc());
		}

		if(t.getDebitSubAcc() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getDebitSubAcc()));
			if(acsubOp.isPresent()) t.setDebitSubAccountName(acsubOp.get().getXname());
		}

		if(t.getCreditAcc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getCreditAcc()));
			if(accountOp.isPresent()) t.setCreditAccountName(accountOp.get().getXdesc());
		}

		if(t.getCreditSubAcc() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), t.getCreditSubAcc()));
			if(acsubOp.isPresent()) t.setCreditSubAccountName(acsubOp.get().getXname());
		}
		model.addAttribute("t", t);
		return "pages/FA16/FA16-fragments::row-actual";
	}

	@Transactional
	@PostMapping("/import/update")
	public @ResponseBody Map<String, Object> updateRow(Tempvoucher tempvoucher, BindingResult bindingResult){

		// VALIDATE
		if(tempvoucher.getXrow() == null) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Optional<Tempvoucher> existOp = tempVoucherRepo.findById(new TempvoucherPK(sessionManager.getBusinessId(), tempvoucher.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Tempvoucher existObj = existOp.get();

		if(tempvoucher.getVoucherDate() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher date required");
			return responseHelper.getResponse();
		}

		if(tempvoucher.getBusinessUnit() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), tempvoucher.getBusinessUnit()));
		if(!businessUnitOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Business unit not exist in this system");
			return responseHelper.getResponse();
		}

		if(tempvoucher.getDebitAcc() == null) {
			responseHelper.setErrorStatusAndMessage("Debit account required");
			return responseHelper.getResponse();
		}

		Optional<Acmst> debitAccountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), tempvoucher.getDebitAcc()));
		if(!debitAccountOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Debit account not exist in this system");
			return responseHelper.getResponse();
		}

		Acmst debitAccount = debitAccountOp.get();
		String dAccUsage = debitAccount.getXaccusage();

		if("Default".equals(dAccUsage)) {
			if(tempvoucher.getDebitSubAcc() != null) {
				responseHelper.setErrorStatusAndMessage("You can't set any debit sub account for this Default debit account type");
				return responseHelper.getResponse();
			}
		} else {
			if(tempvoucher.getDebitSubAcc() == null) {
				responseHelper.setErrorStatusAndMessage("Debit sub account required");
				return responseHelper.getResponse();
			} else {
				Optional<Acsub> debitSubAccOp = acsubRepo.findByZidAndXsubAndXtype(sessionManager.getBusinessId(), tempvoucher.getDebitSubAcc(), dAccUsage);
				if(!debitSubAccOp.isPresent()) {
					responseHelper.setErrorStatusAndMessage("Debit sub account not exist in this system");
					return responseHelper.getResponse();
				}
			}
		}

		if(tempvoucher.getCreditAcc() == null) {
			responseHelper.setErrorStatusAndMessage("Credit account required");
			return responseHelper.getResponse();
		}

		Optional<Acmst> creditAccountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), tempvoucher.getCreditAcc()));
		if(!creditAccountOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Credit account not exist in this system");
			return responseHelper.getResponse();
		}

		Acmst creditAccount = creditAccountOp.get();
		String cAccUsage = creditAccount.getXaccusage();

		if("Default".equals(cAccUsage)) {
			if(tempvoucher.getCreditSubAcc() != null) {
				responseHelper.setErrorStatusAndMessage("You can't set any credit sub account for this Default credit account type");
				return responseHelper.getResponse();
			}
		} else {
			if(tempvoucher.getCreditSubAcc() == null) {
				responseHelper.setErrorStatusAndMessage("Credit sub account required");
				return responseHelper.getResponse();
			} else {
				Optional<Acsub> creditSubAccOp = acsubRepo.findByZidAndXsubAndXtype(sessionManager.getBusinessId(), tempvoucher.getCreditSubAcc(), cAccUsage);
				if(!creditSubAccOp.isPresent()) {
					responseHelper.setErrorStatusAndMessage("Credit sub account not exist in this system");
					return responseHelper.getResponse();
				}
			}
		}

		if(tempvoucher.getAmount() == null) {
			responseHelper.setErrorStatusAndMessage("Amount required");
			return responseHelper.getResponse();
		}

		if(tempvoucher.getAmount().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		if(StringUtils.isNotBlank(tempvoucher.getNarration()) && tempvoucher.getNarration().length() > 200) {
			responseHelper.setErrorStatusAndMessage("Narration is too long. Write narration up to 200 character");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(tempvoucher, existObj, "zid", "xrow");

		try {
			existObj.setAllOk(true);
			existObj.setErrorDetails(null);
			existObj = tempVoucherRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("row-actual-container-" + tempvoucher.getXrow(), "/FA16/import/row-actual?xrow=" + tempvoucher.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Data updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/import/delete-selected-rows")
	public @ResponseBody Map<String, Object> deleteAllSelectedRows(@RequestParam String selectedVouchers) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<Integer> xrows = new ArrayList<>();
		vouchers.stream().forEach(f -> {
			xrows.add(Integer.valueOf(f));
		});

		try {
			tempVoucherRepo.deleteByZidAndXrowIn(sessionManager.getBusinessId(), xrows);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("import-table-container", "/FA16/import-table?page=0&size=10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Slected data deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/import/delete-all-rows")
	public @ResponseBody Map<String, Object> deleteAllRows() {

		try {
			tempVoucherRepo.deleteAllByZid(sessionManager.getBusinessId());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("import-table-container", "/FA16/import-table?page=0&size=10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All data deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/import/delete")
	public @ResponseBody Map<String, Object> deleteRow(@RequestParam Integer xrow){

		Optional<Tempvoucher> existOp = tempVoucherRepo.findById(new TempvoucherPK(sessionManager.getBusinessId(), xrow));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Tempvoucher existObj = existOp.get();

		try {
			tempVoucherRepo.delete(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("import-table-container", "/FA16/import-table?page=0&size=10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Data deleted successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/subaccount/{type}")
	public String loadSubAccountForm(@PathVariable String type, @RequestParam Integer xrow, @RequestParam Integer xacc, Model model) {
		Optional<Tempvoucher> vOp = tempVoucherRepo.findById(new TempvoucherPK(sessionManager.getBusinessId(), xrow));
		Tempvoucher t = vOp.isPresent() ? vOp.get() : new Tempvoucher();
		if("credit".equals(type)) {
			t.setCreditAcc(xacc);
			t.setCreditSubAcc(null);

			if(t.getCreditAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getCreditAcc()));
				if(accountOp.isPresent()) {
					t.setCreditAccountName(accountOp.get().getXdesc());
					model.addAttribute("accountUsage", accountOp.get().getXaccusage());
				}
			}
		} else {
			t.setDebitAcc(xacc);
			t.setDebitSubAcc(null);

			if(t.getDebitAcc() != null) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), t.getDebitAcc()));
				if(accountOp.isPresent()) {
					t.setDebitAccountName(accountOp.get().getXdesc());
					model.addAttribute("accountUsage", accountOp.get().getXaccusage());
				}
			}
		}
		model.addAttribute("t", t);
		return "pages/FA16/FA16-fragments::"+type+"subacc-field";
	}
}
