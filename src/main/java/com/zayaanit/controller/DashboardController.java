package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserwidgetsPK;
import com.zayaanit.model.WF01Dto;
import com.zayaanit.model.WF02Dto;
import com.zayaanit.model.WF02ReqParam;
import com.zayaanit.model.WF03Dto;
import com.zayaanit.model.WF03ReqParam;
import com.zayaanit.model.WF04Dto;
import com.zayaanit.model.WF04ReqParam;
import com.zayaanit.model.WF05Dto;
import com.zayaanit.model.WF05ReqParam;
import com.zayaanit.model.WI01Dto;
import com.zayaanit.model.WI02Dto;
import com.zayaanit.model.WI02ReqParam;
import com.zayaanit.model.WI03Dto;
import com.zayaanit.model.WI03ReqParam;
import com.zayaanit.model.WI04Dto;
import com.zayaanit.model.WI04ReqParam;
import com.zayaanit.model.WI05Dto;
import com.zayaanit.model.WI05ReqParam;
import com.zayaanit.model.WP01Dto;
import com.zayaanit.model.WP02Dto;
import com.zayaanit.model.WP02ReqParam;
import com.zayaanit.model.WP03Dto;
import com.zayaanit.model.WP03ReqParam;
import com.zayaanit.model.WP04Dto;
import com.zayaanit.model.WP04ReqParam;
import com.zayaanit.model.WP05Dto;
import com.zayaanit.model.WP05ReqParam;
import com.zayaanit.model.WS01Dto;
import com.zayaanit.model.WS02Dto;
import com.zayaanit.model.WS02ReqParam;
import com.zayaanit.model.WS03Dto;
import com.zayaanit.model.WS03ReqParam;
import com.zayaanit.model.WS04Dto;
import com.zayaanit.model.WS04ReqParam;
import com.zayaanit.model.WS05Dto;
import com.zayaanit.model.WS05ReqParam;
import com.zayaanit.model.WS06Dto;
import com.zayaanit.model.WS06ReqParam;
import com.zayaanit.repo.AcmstRepo;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.XuserwidgetsRepo;
import com.zayaanit.repo.XwidgetsRepo;
import com.zayaanit.service.impl.WA01Service;
import com.zayaanit.service.impl.WF01Service;
import com.zayaanit.service.impl.WF02Service;
import com.zayaanit.service.impl.WF03Service;
import com.zayaanit.service.impl.WF04Service;
import com.zayaanit.service.impl.WF05Service;
import com.zayaanit.service.impl.WI01Service;
import com.zayaanit.service.impl.WI02Service;
import com.zayaanit.service.impl.WI03Service;
import com.zayaanit.service.impl.WI04Service;
import com.zayaanit.service.impl.WI05Service;
import com.zayaanit.service.impl.WP01Service;
import com.zayaanit.service.impl.WP02Service;
import com.zayaanit.service.impl.WP03Service;
import com.zayaanit.service.impl.WP04Service;
import com.zayaanit.service.impl.WP05Service;
import com.zayaanit.service.impl.WS01Service;
import com.zayaanit.service.impl.WS02Service;
import com.zayaanit.service.impl.WS03Service;
import com.zayaanit.service.impl.WS04Service;
import com.zayaanit.service.impl.WS05Service;
import com.zayaanit.service.impl.WS06Service;

/**
 * @author Zubayer Ahaned
 * @since Jul 20, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/DASH")
public class DashboardController extends KitController {

	private String pageTitle = null;

	@Autowired private WA01Service wa01Service;
	@Autowired private WF01Service wf01Service;
	@Autowired private WF02Service wf02Service;
	@Autowired private WF03Service wf03Service;
	@Autowired private WF04Service wf04Service;
	@Autowired private WF05Service wf05Service;
	@Autowired private WP01Service wp01Service;
	@Autowired private WP02Service wp02Service;
	@Autowired private WP03Service wp03Service;
	@Autowired private WP04Service wp04Service;
	@Autowired private WP05Service wp05Service;
	@Autowired private WS01Service ws01Service;
	@Autowired private WS02Service ws02Service;
	@Autowired private WS03Service ws03Service;
	@Autowired private WS04Service ws04Service;
	@Autowired private WS05Service ws05Service;
	@Autowired private WS06Service ws06Service;
	@Autowired private WI01Service wi01Service;
	@Autowired private WI02Service wi02Service;
	@Autowired private WI03Service wi03Service;
	@Autowired private WI04Service wi04Service;
	@Autowired private WI05Service wi05Service;
	@Autowired private XuserwidgetsRepo xuserwidgetsRepo;
	@Autowired private XwidgetsRepo xwidgetsRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "DASH"));
		if(!op.isPresent()) return "Dashboard";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@Override
	protected String screenCode() {
		return "DASH";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@GetMapping
	public String index(@RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		//xlogsdtService.save(new Xlogsdt(getXscreen(request.getServletPath()), null, xsource, "View Screen", null, null, null, "Success"));
		//xlogsdtService.save(new Xlogsdt("DASH", null, this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));
		
		
		
		List<Xwidgets> displayableWidgets = new ArrayList<>();

		// First get all the widgets from the system
		List<Xwidgets> allWidgets = xwidgetsRepo.findAllByZid(sessionManager.getBusinessId());

		for(Xwidgets widget : allWidgets) {
			widget.setXseqn(0);
			widget.setLoaddefault(true);

			// if user is not system admin then check widget exist from xuserwidget.
			if(Boolean.FALSE.equals(sessionManager.getLoggedInUserDetails().isAdmin())) {
				Optional<Xuserwidgets> userWidget = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), widget.getXwidget()));
				if(!userWidget.isPresent()) {
					model.addAttribute(widget.getXwidget(), "N");
					continue;
				}

				// Sequence set from xuserwidget, it will not work for admin
				widget.setXseqn(userWidget.get().getXsequence());
				widget.setLoaddefault(Boolean.TRUE.equals(userWidget.get().getXisdefault()));
			}

			displayableWidgets.add(widget);
			model.addAttribute(widget.getXwidget(), "Y");

			int last = widget.getXdefault() != null ? widget.getXdefault() : 10;

			if("WF02".equalsIgnoreCase(widget.getXwidget())) {
				Optional<Acmst> acmstOp = acmstRepo.findTopByZidOrderByXaccAsc(sessionManager.getBusinessId());
				Integer xacc = acmstOp.isPresent() ? acmstOp.get().getXacc() : null;
				String accountName = acmstOp.isPresent() ? acmstOp.get().getXdesc() : null;
				model.addAttribute("WF02REQPARAM", WF02ReqParam.builder().xacc(xacc).accountName(accountName).last(last).type("DAYS").build());
			} else if ("WF03".equalsIgnoreCase(widget.getXwidget())) {
				Optional<Acsub> acsubOp = acsubRepo.findTopByZidOrderByXsubAsc(sessionManager.getBusinessId());
				Integer xsub = acsubOp.isPresent() ? acsubOp.get().getXsub() : null;
				String subAccountName = acsubOp.isPresent() ? acsubOp.get().getXname() : null;
				model.addAttribute("WF03REQPARAM", WF03ReqParam.builder().xsub(xsub).subAccountName(subAccountName).last(last).type("DAYS").build());
			} else if ("WF04".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WF04REQPARAM", WF04ReqParam.builder().type("Asset").build());
			} else if ("WF05".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WF05REQPARAM", WF05ReqParam.builder().last(last).type("DAYS").build());
			} else if ("WP02".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WP02REQPARAM", WP02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WP03".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WP03REQPARAM", WP03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WP04".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WP04REQPARAM", WP04ReqParam.builder().topxcus(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WP05".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WP05REQPARAM", WP05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WS02".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WS02REQPARAM", WS02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WS03".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WS03REQPARAM", WS03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WS04".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WS04REQPARAM", WS04ReqParam.builder().topxcus(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WS05".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WS05REQPARAM", WS05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WS06".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WS06REQPARAM", WS06ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			} else if ("WI02".equalsIgnoreCase(widget.getXwidget())) {
				List<Xcodes> xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
				List<String> itemGroups = xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());
				itemGroups = itemGroups.stream().filter(f -> !"Services".equalsIgnoreCase(f)).collect(Collectors.toList());
				model.addAttribute("WI02REQPARAM", WI02ReqParam.builder().xcodes(itemGroups).type("Quantity_n_Value").build());
			} else if ("WI03".equalsIgnoreCase(widget.getXwidget())) {
				Calendar wi03today = Calendar.getInstance();
				wi03today.add(Calendar.DAY_OF_MONTH, -last);
				model.addAttribute("WI03REQPARAM", WI03ReqParam.builder().xfdate(wi03today.getTime()).xtdate(new Date()).type("Pie Chart").build());
			} else if ("WI04".equalsIgnoreCase(widget.getXwidget())) {
				List<Xcodes> wi04xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId());
				List<String> itemCategories = wi04xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());
				model.addAttribute("WI04REQPARAM", WI04ReqParam.builder().xcodes(itemCategories).xfdays(1).xtdays(last).type("Quantity_n_Value").build());
			} else if ("WI05".equalsIgnoreCase(widget.getXwidget())) {
				model.addAttribute("WI05REQPARAM", WI05ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("BATCHES").build());
			}
		}

		displayableWidgets.sort(Comparator.comparing(Xwidgets::getXseqn));;
		model.addAttribute("displayableWidgets", displayableWidgets);

		if(frommenu == null) return "redirect:/";

		return "pages/DASH/DASH";
	}

	@GetMapping("/WA01/WG01")
	public String getTotalUsers(Model model) {
		model.addAttribute("WA01WG01", wa01Service.totalUsers());
		return "pages/DASH/DASH-fragments::WA01WG01";
	}

	@GetMapping("/WA01/WG02")
	public String getActiveUsers(Model model) {
		model.addAttribute("WA01WG02", wa01Service.activeUsers());
		return "pages/DASH/DASH-fragments::WA01WG02";
	}

	@GetMapping("/WA01/WG03")
	public String getTodaysLoggedinUsers(Model model) {
		model.addAttribute("WA01WG03", wa01Service.todaysLoggedInUsers());
		return "pages/DASH/DASH-fragments::WA01WG03";
	}

	@GetMapping("/WA01/WG04")
	public String getCurrentLoggedinUsers(Model model) {
		model.addAttribute("WA01WG04", wa01Service.currentLoggedInUsers());
		return "pages/DASH/DASH-fragments::WA01WG04";
	}

	@GetMapping("/WA01/WG05")
	public String  getProfilewiseUsers(Model model) {
		model.addAttribute("WA01WG05", wa01Service.profileWiseUsers());
		return "pages/DASH/DASH-fragments::WA01WG05";
	}

	@GetMapping("/WF01/WG01")
	public String WF01WG01(Model model) {
		WF01Dto dto = wf01Service.totalVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG01";
	}

	@GetMapping("/WF01/WG02")
	public String WF01WG02(Model model) {
		WF01Dto dto = wf01Service.openVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG02";
	}

	@GetMapping("/WF01/WG03")
	public String WF01WG03(Model model) {
		WF01Dto dto = wf01Service.suspendedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG03";
	}

	@GetMapping("/WF01/WG04")
	public String WF01WG04(Model model) {
		WF01Dto dto = wf01Service.waitingForPosting();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG04";
	}

	@GetMapping("/WF01/WG05")
	public String WF01WG05(Model model) {
		WF01Dto dto = wf01Service.postedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG05";
	}

	@GetMapping("/WF02/WG01")
	public @ResponseBody List<WF02Dto> WF02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xacc, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF02Dto> datas = wf02Service.accountTransactions(xbuid, xacc, last, type);
		return datas;
	}

	@GetMapping("/WF03/WG01")
	public @ResponseBody List<WF03Dto> WF03WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xsub, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF03Dto> datas = wf03Service.subAccountTransactions(xbuid, xsub, last, type);
		return datas;
	}

	@GetMapping("/WF04/WG01")
	public @ResponseBody List<WF04Dto> WF04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF04Dto> datas = wf04Service.accountCurrentBalance(xbuid, type);
		return datas;
	}

	@GetMapping("/WF05/WG01")
	public @ResponseBody List<WF05Dto> WF05WG01(
			@RequestParam(required = true) Integer xbuid, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF05Dto> datas = wf05Service.profitAndLossView(xbuid, last, type);
		return datas;
	}

	@GetMapping("/WP01/WG01")
	public String WP01WG01(Model model) {
		WP01Dto dto = wp01Service.purchaseOrder();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG01";
	}

	@GetMapping("/WP01/WG02")
	public String WP01WG02(Model model) {
		WP01Dto dto = wp01Service.grnAndDirectPurchase();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG02";
	}

	@GetMapping("/WP01/WG03")
	public String WP01WG03(Model model) {
		WP01Dto dto = wp01Service.purchaseReturn();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG03";
	}

	@GetMapping("/WP02/WG01")
	public @ResponseBody List<WP02Dto> WP02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP02Dto> datas = wp02Service.supplierPurchaseView(xbuid, xcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP03/WG01")
	public @ResponseBody List<WP03Dto> WP03WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP03Dto> datas = wp03Service.itemPurchaseStatement(xbuid, xitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP04/WG01")
	public @ResponseBody List<WP04Dto> WP04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP04Dto> datas = wp04Service.topSupplierTransaction(xbuid, topxcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP05/WG01")
	public @ResponseBody List<WP05Dto> WP05WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP05Dto> datas = wp05Service.topItemPurchase(xbuid, topxitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WS01/WG01")
	public String WS01WG01(Model model) {
		WS01Dto dto = ws01Service.salesOrder();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WS01WG01";
	}

	@GetMapping("/WS01/WG02")
	public String WS01WG02(Model model) {
		WS01Dto dto = ws01Service.salesInvocie();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WS01WG02";
	}

	@GetMapping("/WS01/WG03")
	public String WS01WG03(Model model) {
		WS01Dto dto = ws01Service.salesReturn();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WS01WG03";
	}

	@GetMapping("/WS02/WG01")
	public @ResponseBody List<WS02Dto> WS02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WS02Dto> datas = ws02Service.customerSalesStatement(xbuid, xcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WS03/WG01")
	public @ResponseBody List<WS03Dto> WS03WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WS03Dto> datas = ws03Service.itemSalesStatement(xbuid, xitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WS04/WG01")
	public @ResponseBody List<WS04Dto> WS04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WS04Dto> datas = ws04Service.topCustomerTransaction(xbuid, topxcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WS05/WG01")
	public @ResponseBody List<WS05Dto> WS05WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WS05Dto> datas = ws05Service.topItemSales(xbuid, topxitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WS06/WG01")
	public @ResponseBody List<WS06Dto> WS06WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WS06Dto> datas = ws06Service.topProfitableItems(xbuid, topxitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WI01/WG01")
	public String WI01WG01(Model model) {
		WI01Dto dto = wi01Service.inventoryTransfer();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WI01WG01";
	}

	@GetMapping("/WI01/WG02")
	public String WI01WG02(Model model) {
		WI01Dto dto = wi01Service.batchProcess();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WI01WG02";
	}

	@GetMapping("/WI01/WG03")
	public String WI01WG03(Model model) {
		WI01Dto dto = wi01Service.inventoryIssue();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WI01WG03";
	}

	@GetMapping("/WI01/WG04")
	public String WI01WG04(Model model) {
		WI01Dto dto = wi01Service.inventoryAdjustment();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WI01WG04";
	}

	@GetMapping("/WI02/WG01")
	public @ResponseBody List<WI02Dto> WI02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) String xgroup, 
			@RequestParam(required = true) Integer xwh,
			@RequestParam(required = true) String type, 
			Model model
		) {

		List<WI02Dto> datas = wi02Service.currentStockStatus(xbuid, xgroup, xwh, type);
		return datas;
	}

	@GetMapping("/WI03/WG01_WG02")
	public @ResponseBody List<WI03Dto> WG01_WG02(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xwh, 
			@RequestParam(required = true) Integer xitem,
			@RequestParam(required = true) String xfdate,
			@RequestParam(required = true) String xtdate,
			@RequestParam(required = true) String type,
			Model model
		) {

		List<WI03Dto> datas = wi03Service.inwardPieChartWG01_WG02(xbuid, xitem, xwh, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WI03/WG03_WG04")
	public @ResponseBody List<WI03Dto> WG03_WG04(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xwh, 
			@RequestParam(required = true) Integer xitem,
			@RequestParam(required = true) String xfdate,
			@RequestParam(required = true) String xtdate,
			@RequestParam(required = true) String type,
			Model model
		) {

		List<WI03Dto> datas = wi03Service.outwardPieChartWG03_WG04(xbuid, xitem, xwh, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WI03/WG05")
	public @ResponseBody List<WI03Dto> WI03WG05(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xwh, 
			@RequestParam(required = true) Integer xitem,
			@RequestParam(required = true) String xfdate,
			@RequestParam(required = true) String xtdate,
			@RequestParam(required = true) String type,
			Model model
		) {

		List<WI03Dto> datas = wi03Service.barChartWG05(xbuid, xitem, xwh, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WI03/WG06")
	public @ResponseBody List<WI03Dto> WI03WG06(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xwh, 
			@RequestParam(required = true) Integer xitem,
			@RequestParam(required = true) String xfdate,
			@RequestParam(required = true) String xtdate,
			@RequestParam(required = true) String type,
			Model model
		) {

		List<WI03Dto> datas = wi03Service.barChartWG06(xbuid, xitem, xwh, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WI04/WG01")
	public @ResponseBody List<WI04Dto> WI04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xwh,
			@RequestParam(required = true) String xcategory,
			@RequestParam(required = true) Integer xitem,
			@RequestParam(required = true) Integer xfdays,
			@RequestParam(required = true) Integer xtdays,
			@RequestParam(required = true) String type,
			Model model
		) {

		List<WI04Dto> datas = wi04Service.inventoryAgeView(xbuid, xwh, xcategory, xitem, xfdays, xtdays, type);
		return datas;
	}

	@GetMapping("/WI05/WG01")
	public @ResponseBody List<WI05Dto> WI05WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WI05Dto> datas = wi05Service.productionCostingView(xbuid, xitem, last, type, xfdate, xtdate);
		return datas;
	}
}
