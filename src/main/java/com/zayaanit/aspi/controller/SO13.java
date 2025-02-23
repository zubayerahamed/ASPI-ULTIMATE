package com.zayaanit.aspi.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.OpordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.ReloadSectionParams;
import com.zayaanit.aspi.model.SO13SearchParam;
import com.zayaanit.aspi.repo.OpordheaderRepo;
import com.zayaanit.aspi.service.OpordheaderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Slf4j
@Controller
@RequestMapping("/SO13")
public class SO13 extends KitController {

	@Autowired private OpordheaderRepo opordheaderRepo;
	@Autowired private OpordheaderService opordheaderService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO13";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		model.addAttribute("searchParam", SO13SearchParam.getDefaultInstance());

		if(isAjaxRequest(request) && frommenu == null) {
			xlogsdtService.save(new Xlogsdt("SO13", "Clear", this.pageTitle, null, null, false, 0));
			return "pages/SO13/SO13-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/SO13/SO13";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SO13SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SO13/SO13-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<Opordheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) Integer xcus,
		@RequestParam(required = false) String xstatusord
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO13SearchParam param = new SO13SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXcus(xcus);
		param.setXstatusord(xstatusord);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opordheader> list = opordheaderService.LSO13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		int	totalRows = opordheaderService.LSO13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

		xlogsdtService.save(new Xlogsdt("SO13", "Search", this.pageTitle, param.toString(), null, false, 0));

		DatatableResponseHelper<Opordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/create-invoice")
	public @ResponseBody Map<String, Object> createInvoice(
		@RequestParam Integer xordernum,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) Integer xcus,
		@RequestParam(required = false) String xstatusord
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO13SearchParam param = new SO13SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXcus(xcus);
		param.setXstatusord(xstatusord);

		Optional<Opordheader> opordheaderOp = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), xordernum));
		if(!opordheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Opordheader opordheader = opordheaderOp.get();
		if(!"Confirmed".equals(opordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not confirmed");
			return responseHelper.getResponse();
		}

		if(!("Open".equals(opordheader.getXstatusord()) || "Invoice Created".equals(opordheader.getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Order status not in Open or Invoice Created");
			return responseHelper.getResponse();
		}

		if(opordheaderRepo.getOpenInvoiceCount(sessionManager.getBusinessId(), xordernum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending invoice found. Confirm/delete pending invoice first");
			return responseHelper.getResponse();
		}

		try {
			opordheaderRepo.SO_CreateDOfromOrder(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xordernum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO13", "Create Invoice", this.pageTitle, param.toString(), "SO_CreateDOfromOrder(" + sessionManager.getBusinessId() + "," + sessionManager.getLoggedInUserDetails().getUsername() + "," + xordernum + ")", false, 0));

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xcus", xcus != null ? xcus.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusord", xstatusord));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/dismiss-order")
	public @ResponseBody Map<String, Object> dismissOrder(
		@RequestParam Integer xordernum,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) Integer xcus,
		@RequestParam(required = false) String xstatusord
		) {

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("You are not staff and you don't have any access to do confirm action.");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO13SearchParam param = new SO13SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXcus(xcus);
		param.setXstatusord(xstatusord);

		Optional<Opordheader> opordheaderOp = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), xordernum));
		if(!opordheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Opordheader opordheader = opordheaderOp.get();
		if(!"Confirmed".equals(opordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not confirmed");
			return responseHelper.getResponse();
		}

		if(!("Open".equals(opordheader.getXstatusord()) || "Invoice Created".equals(opordheader.getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Order status not in Open or Invoice Created");
			return responseHelper.getResponse();
		}

		if(opordheaderRepo.getOpenInvoiceCount(sessionManager.getBusinessId(), xordernum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending Invoice found. Confirm/delete pending GRN first");
			return responseHelper.getResponse();
		}

		Long confirmedInvoiceCount = opordheaderRepo.getConfirmedInvoiceCount(sessionManager.getBusinessId(), xordernum);

		if("Open".equals(opordheader.getXstatusord())) {
			opordheader.setXstatusord("Dismissed");
			opordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			opordheader.setXapprovertime(new Date());
		} else if ("Invoice Created".equals(opordheader.getXstatusord()) && confirmedInvoiceCount == 0) {
			opordheader.setXstatusord("Dismissed");
			opordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			opordheader.setXapprovertime(new Date());
		} else if ("Invoice Created".equals(opordheader.getXstatusord()) && confirmedInvoiceCount > 0) {
			opordheader.setXstatusord("Invoice Created & Dismissed");
			opordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			opordheader.setXapprovertime(new Date());
		}

		try {
			opordheaderRepo.save(opordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO13", "Dismiss Order", this.pageTitle, param.toString(), opordheader.toString(), false, 0));

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xcus", xcus != null ? xcus.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusord", xstatusord));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Order dismissed successfully");
		return responseHelper.getResponse();
	}
}
