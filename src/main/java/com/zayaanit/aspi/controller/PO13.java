package com.zayaanit.aspi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.zayaanit.aspi.entity.Poordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.PoordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.PO13SearchParam;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.ReloadSectionParams;
import com.zayaanit.aspi.repo.PoordheaderRepo;
import com.zayaanit.aspi.service.PoordheaderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/PO13")
public class PO13 extends KitController {

	@Autowired private PoordheaderRepo poordheaderRepo;
	@Autowired private PoordheaderService poordheaderService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "PO13";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "PO13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		model.addAttribute("searchParam", PO13SearchParam.getDefaultInstance());

		if(isAjaxRequest(request) && frommenu == null) {
			xlogsdtService.save(new Xlogsdt("PO13", "Clear", this.pageTitle, null, null, false, 0));
			return "pages/PO13/PO13-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/PO13/PO13";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(PO13SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/PO13/PO13-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<Poordheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) Integer xcus,
		@RequestParam(required = false) String xstatusord
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		PO13SearchParam param = new PO13SearchParam();
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

		List<Poordheader> list = poordheaderService.LPO13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		int	totalRows = poordheaderService.LPO13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

		xlogsdtService.save(new Xlogsdt("PO13", "Search", this.pageTitle, param.toString(), null , false, 0));

		DatatableResponseHelper<Poordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/create-grn")
	public @ResponseBody Map<String, Object> createGrn(
		@RequestParam Integer xpornum,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) Integer xcus,
		@RequestParam(required = false) String xstatusord
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		PO13SearchParam param = new PO13SearchParam();
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

		Optional<Poordheader> poordheaderOp = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), xpornum));
		if(!poordheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Poordheader poordheader = poordheaderOp.get();
		if(!"Confirmed".equals(poordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Order not confirmed");
			return responseHelper.getResponse();
		}

		if(!("Open".equals(poordheader.getXstatusord()) || "GRN Created".equals(poordheader.getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Order status not Open or GRN Created");
			return responseHelper.getResponse();
		}

		if(poordheaderRepo.getOpenGRNCount(sessionManager.getBusinessId(), xpornum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending GRN found. Confirm/delete pending GRN first");
			return responseHelper.getResponse();
		}

		try {
			poordheaderRepo.PO_CreateGRNfromOrder(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xpornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO13", "Create GRN", this.pageTitle, param.toString(), "PO_CreateGRNfromOrder(" + sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xpornum + ")" , false, 0));

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xcus", xcus != null ? xcus.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusord", xstatusord));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/PO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/dismiss-order")
	public @ResponseBody Map<String, Object> voucherDelete(
		@RequestParam Integer xpornum,
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
		PO13SearchParam param = new PO13SearchParam();
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

		Optional<Poordheader> poordheaderOp = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), xpornum));
		if(!poordheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Poordheader poordheader = poordheaderOp.get();
		if(!"Confirmed".equals(poordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Order not confirmed");
			return responseHelper.getResponse();
		}

		if(!("Open".equals(poordheader.getXstatusord()) || "GRN Created".equals(poordheader.getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Order status not Open or GRN Created");
			return responseHelper.getResponse();
		}

		if(poordheaderRepo.getOpenGRNCount(sessionManager.getBusinessId(), xpornum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending GRN found. Confirm/delete pending GRN first");
			return responseHelper.getResponse();
		}


		Long confirmedGrnCount = poordheaderRepo.getConfirmedGRNCount(sessionManager.getBusinessId(), xpornum);

		if("Open".equals(poordheader.getXstatusord())) {
			poordheader.setXstatusord("Dismissed");
			poordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			poordheader.setXapprovertime(new Date());
		} else if ("GRN Created".equals(poordheader.getXstatusord()) && confirmedGrnCount == 0) {
			poordheader.setXstatusord("Dismissed");
			poordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			poordheader.setXapprovertime(new Date());
		} else if ("GRN Created".equals(poordheader.getXstatusord()) && confirmedGrnCount > 0) {
			poordheader.setXstatusord("GRN Created & Dismissed");
			poordheader.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
			poordheader.setXapprovertime(new Date());
		}

		try {
			poordheaderRepo.save(poordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO13", "Dismiss Order", this.pageTitle, param.toString(), poordheader.toString(), false, 0));

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xcus", xcus != null ? xcus.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusord", xstatusord));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/PO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Order dismissed successfully");
		return responseHelper.getResponse();
	}
}
