package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Acdetail;
import com.zayaanit.aspi.entity.Acheader;
import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Cadoc;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.AcdetailPK;
import com.zayaanit.aspi.entity.pk.AcheaderPK;
import com.zayaanit.aspi.entity.pk.AcmstPK;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.YearPeriodResult;
import com.zayaanit.aspi.repo.AcdetailRepo;
import com.zayaanit.aspi.repo.AcheaderRepo;
import com.zayaanit.aspi.repo.AcmstRepo;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CadocRepo;
import com.zayaanit.aspi.service.AcheaderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA15")
public class FA15 extends KitController {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcheaderService acheaderService;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private CadocRepo cadocRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xvoucher, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("voucherTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xvoucher)) {
				model.addAttribute("acheader", Acheader.getDefaultInstance());
				return "pages/FA15/FA15-fragments::main-form";
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

			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA15", Integer.valueOf(xvoucher));
			model.addAttribute("documents", cdocList);

			return "pages/FA15/FA15-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("acheader", Acheader.getDefaultInstance());
		return "pages/FA15/FA15";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xvoucher, @RequestParam String xrow, @RequestParam(required = false) Integer xacc, Model model) {
		if("RESET".equalsIgnoreCase(xvoucher) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA15/FA15-fragments::detail-table";
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
		if(!oph.isPresent()) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA15/FA15-fragments::detail-table";
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
			return "pages/FA15/FA15-fragments::detail-table";
		}

		Optional<Acdetail> acdetailOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher), Integer.parseInt(xrow)));
		Acdetail acdetail = acdetailOp.isPresent() ? acdetailOp.get() : Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
		if(acdetail != null && acdetail.getXacc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}
		if(account != null) {
			acdetail.setXacc(account.getXacc());
			acdetail.setAccountName(account.getXdesc());
			acdetail.setAccountUsage(account.getXaccusage());
		}

		model.addAttribute("acdetail", acdetail);
		return "pages/FA15/FA15-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/FA15/FA15-fragments::list-table";
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
			acheader.setXtype("General");
			acheader.setXstatusjv("Balanced");
			acheader.setXvoucher(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "FA15"));
			acheader.setZid(sessionManager.getBusinessId());
			try {
				acheader = acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=" + acheader.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
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

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=" + existObj.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
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

			BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), acdetail.getXvoucher());
			acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
			try {
				acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=" + acdetail.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
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
			exist = acdetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), exist.getXvoucher());
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		try {
			acheaderRepo.save(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=" + acdetail.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
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

		Acheader obj = op.get();
		try {
			acheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
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

		// Update line amount and total amount of header
		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), xvoucher);
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		try {
			acheaderRepo.save(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA15?xvoucher=" + xvoucher));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA15/detail-table?xvoucher="+xvoucher+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA15/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
