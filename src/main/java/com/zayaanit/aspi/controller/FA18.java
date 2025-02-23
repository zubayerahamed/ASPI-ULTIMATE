package com.zayaanit.aspi.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.aspi.entity.Acheader;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.AcheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.FA18SearchParam;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.ReloadSectionParams;
import com.zayaanit.aspi.repo.AcdetailRepo;
import com.zayaanit.aspi.repo.AcheaderRepo;
import com.zayaanit.aspi.service.AcheaderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/FA18")
public class FA18 extends KitController {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private AcheaderService acheaderService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA18";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA18"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		model.addAttribute("searchParam", FA18SearchParam.getDefaultInstance());

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/FA18/FA18-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/FA18/FA18";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(FA18SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/FA18/FA18-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Acheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			throw new IllegalStateException(e.getCause().getMessage());
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acheader> list = acheaderService.LFA18(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		int	totalRows = acheaderService.LFA18(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

		DatatableResponseHelper<Acheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/voucher-post")
	public @ResponseBody Map<String, Object> voucherPost(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		try {
			acheaderRepo.FA_VoucherPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher posted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/post-all")
	public @ResponseBody Map<String, Object> postAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allBalancedAcheader = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if("Balanced".equals(acheader.getXstatusjv())) {
					allBalancedAcheader.add(acheader);
				}
			}
		}

		if(allBalancedAcheader.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Balanced Voucher");
			return responseHelper.getResponse();
		}

		try {
			for(Acheader acheader : allBalancedAcheader) {
				acheaderRepo.FA_VoucherPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All Balanced voucer posted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/voucher-unpost")
	public @ResponseBody Map<String, Object> voucherUnpost(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if(!"Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher not posted");
			return responseHelper.getResponse();
		}

		try {
			acheaderRepo.FA_VoucherUnPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher unposted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/unpost-all")
	public @ResponseBody Map<String, Object> unpostAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allPostedAcheader = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if("Posted".equals(acheader.getXstatusjv())) {
					allPostedAcheader.add(acheader);
				}
			}
		}

		if(allPostedAcheader.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Posted voucher");
			return responseHelper.getResponse();
		}

		try {
			for(Acheader acheader : allPostedAcheader) {
				acheaderRepo.FA_VoucherUnPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All posted voucer unposted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/voucher-delete")
	public @ResponseBody Map<String, Object> voucherDelete(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		try {
			acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), xvoucher);
			acheaderRepo.delete(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/delete-all")
	public @ResponseBody Map<String, Object> deleteAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA18SearchParam param = new FA18SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allUnpostedVouchers = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if(!"Posted".equals(acheader.getXstatusjv())) {
					allUnpostedVouchers.add(acheader);
				}
			}
		}

		if(allUnpostedVouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Balanced or Suspended voucher");
			return responseHelper.getResponse();
		}

		try {
			for(Acheader v : allUnpostedVouchers) {
				acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), v.getXvoucher());
				acheaderRepo.delete(v);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA18/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All Balanced & Suspended voucer deleted successfully");
		return responseHelper.getResponse();
	}
}
