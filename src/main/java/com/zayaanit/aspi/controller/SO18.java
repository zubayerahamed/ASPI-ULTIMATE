package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.Opdodetail;
import com.zayaanit.aspi.entity.Opdoheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.OpdoheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.ReportType;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.RequestParameters;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.OpdodetailRepo;
import com.zayaanit.aspi.repo.OpdoheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/SO18")
public class SO18 extends KitController {

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO18";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO18"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public String index(@RequestParam (required = false) String xdornum, @RequestParam(required = false) String frommenu, @RequestParam (required = false) Integer xbuid, @RequestParam (required = false) Integer xwh, HttpServletRequest request, Model model) {
		LocalDateTime serverTime = LocalDateTime.now(); // Get server time
		String formattedTime = serverTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a"));
		model.addAttribute("serverTime", formattedTime);

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xdornum)) {  // clicked on clear button
				if(sessionManager.getFromMap("SO18-DETAILS") != null) {
					sessionManager.removeFromMap("SO18-DETAILS");
				}

				Opdoheader header = Opdoheader.getPOSInstance(sessionManager);
				if(xbuid != null) header.setXbuid(xbuid);
				if(xwh != null) header.setXwh(xwh);
				header = header.build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo);

				model.addAttribute("opdoheader", header);
				xlogsdtService.save(new Xlogsdt("SO18", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/SO18/SO18-fragments::main-form";
			}

			if("RELOAD".equalsIgnoreCase(xdornum)) {   // trigger by adding or deleting product
				Opdoheader header = Opdoheader.getPOSInstance(sessionManager);
				if(xbuid != null) header.setXbuid(xbuid);
				if(xwh != null) header.setXwh(xwh);
				header = header.build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo);

				List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
				BigDecimal totalXlineamt = details == null || details.isEmpty() ? BigDecimal.ZERO : details.stream().map(Opdodetail::getXlineamt).filter(xlineamt -> xlineamt != null).reduce(BigDecimal.ZERO, BigDecimal::add);
				header.setXtotamt(totalXlineamt);

				model.addAttribute("opdoheader", header);
				xlogsdtService.save(new Xlogsdt("SO18", "Reload", this.pageTitle, null, null, false, 0));
				return "pages/SO18/SO18-fragments::main-form";
			}

			Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
			Opdoheader header = opdoheaderOp.isPresent() ? opdoheaderOp.get().build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo) : Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo);
			model.addAttribute("opdoheader", header);

			if(header.getXdornum() != null) {
				if(header.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), header.getXbuid()));
					if(cabunitOp.isPresent()) header.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(header.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXcus()));
					if(acsubOp.isPresent()) header.setCustomerName(acsubOp.get().getXname());
				}

				if(header.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), header.getXwh()));
					if(xwhsOp.isPresent()) header.setWarehouseName(xwhsOp.get().getXname());
				}

				if(header.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXstaff()));
					if(acsubOp.isPresent()) header.setStaffName(acsubOp.get().getXname());
				}

				if(header.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXstaffsubmit()));
					if(acsubOp.isPresent()) header.setSubmitStaffName(acsubOp.get().getXname());
				}
			}

			xlogsdtService.save(new Xlogsdt("SO18", "View", this.pageTitle, header.getXdornum().toString(), header.toString(), false, 0));
			return "pages/SO18/SO18-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		if(sessionManager.getFromMap("SO18-DETAILS") != null) {
			sessionManager.removeFromMap("SO18-DETAILS");
		}

		model.addAttribute("opdoheader", Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo));
		model.addAttribute("opdodetail", Opdodetail.getPOSInstance(null));
		model.addAttribute("detailList", Collections.emptyList());
		return "pages/SO18/SO18";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {

		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo));
			model.addAttribute("opdodetail", Opdodetail.getPOSInstance(null));

			if(xitem != null) {
				Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
				if(caitemOp.isPresent()) {

					if(sessionManager.getFromMap("SO18-DETAILS") == null) {
						Opdodetail detail = Opdodetail.getPOSInstance(null);
						detail.setXitem(xitem);
						detail.setItemName(caitemOp.get().getXdesc());
						detail.setXunit(caitemOp.get().getXunit());
						detail.setXrate(caitemOp.get().getXrate());
						detail.setXqty(BigDecimal.ONE);
						detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));
						detail.setXrow(0);

						List<Opdodetail> details = new ArrayList<>();
						details.add(detail);

						sessionManager.addToMap("SO18-DETAILS", details);
					} else {
						List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");

						Opdodetail existingItem = details.stream().filter(f -> f.getXitem().equals(xitem)).findFirst().orElse(null);

						if(existingItem != null) {
							existingItem.setXqty(existingItem.getXqty().add(BigDecimal.ONE));
							existingItem.setXlineamt(existingItem.getXrate().multiply(existingItem.getXqty()));
						} else {
							Opdodetail detail = Opdodetail.getPOSInstance(null);
							detail.setXitem(xitem);
							detail.setItemName(caitemOp.get().getXdesc());
							detail.setXunit(caitemOp.get().getXunit());
							detail.setXrate(caitemOp.get().getXrate());
							detail.setXqty(BigDecimal.ONE);
							detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));

							Optional<Integer> maxXrow = details.stream().map(Opdodetail::getXrow).max(Integer::compareTo);
							detail.setXrow(maxXrow.isPresent() ? maxXrow.get() + 1 : 0);

							details.add(detail);
						}

					}

				}

			} else {
				if(sessionManager.getFromMap("SO18-DETAILS") != null) {
					sessionManager.removeFromMap("SO18-DETAILS");
				}
			}

			List<Opdodetail> itemList = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
			model.addAttribute("detailList", itemList == null ? Collections.emptyList() : itemList);

			xlogsdtService.save(new Xlogsdt("SO18", "Clear", this.pageTitle, null, null, false, 0));
			return "pages/SO18/SO18-fragments::detail-table";
		}

		if("RELOAD".equalsIgnoreCase(xdornum) && "RELOAD".equalsIgnoreCase(xrow)) {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo));

			List<Opdodetail> itemList = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
			model.addAttribute("detailList", itemList == null ? Collections.emptyList() : itemList);
			xlogsdtService.save(new Xlogsdt("SO18", "Reload", this.pageTitle, null, null, true, 0));
			return "pages/SO18/SO18-fragments::detail-table";
		}

		if(StringUtils.isNotBlank(xdornum)) {
			Optional<Opdoheader> headerOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
			if(!headerOp.isPresent()) {
				model.addAttribute("opdoheader", Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo));
				model.addAttribute("detailList", Collections.emptyList());
			}

			Opdoheader header = headerOp.get().build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo);
			model.addAttribute("opdoheader", header);

			if(xitem == null) {
				List<Opdodetail> details = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), Integer.valueOf(xdornum));
				if(details != null && !details.isEmpty()) {
					for(Opdodetail d : details) {
						Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), d.getXitem()));
						if(caitemOp.isPresent()) {
							d.setItemName(caitemOp.get().getXdesc());
							d.setXunit(caitemOp.get().getXunit());
						}
					}
				}

				if("Open".equals(header.getXstatus())) {
					if(sessionManager.getFromMap("SO18-DETAILS") != null) {
						sessionManager.removeFromMap("SO18-DETAILS");
					}
					sessionManager.addToMap("SO18-DETAILS", details == null ? Collections.emptyList() : details);
				}

				model.addAttribute("detailList", details == null ? Collections.emptyList() : details);
			} else {
				Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
				if(caitemOp.isPresent()) {

					if(sessionManager.getFromMap("SO18-DETAILS") == null) {
						Opdodetail detail = Opdodetail.getPOSInstance(null);
						detail.setXitem(xitem);
						detail.setItemName(caitemOp.get().getXdesc());
						detail.setXunit(caitemOp.get().getXunit());
						detail.setXrate(caitemOp.get().getXrate());
						detail.setXqty(BigDecimal.ONE);
						detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));
						detail.setXrow(0);

						List<Opdodetail> details = new ArrayList<>();
						details.add(detail);

						sessionManager.addToMap("SO18-DETAILS", details);
					} else {
						List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");

						Opdodetail existingItem = details.stream().filter(f -> f.getXitem().equals(xitem)).findFirst().orElse(null);

						if(existingItem != null) {
							existingItem.setXqty(existingItem.getXqty().add(BigDecimal.ONE));
							existingItem.setXlineamt(existingItem.getXrate().multiply(existingItem.getXqty()));
						} else {
							Opdodetail detail = Opdodetail.getPOSInstance(null);
							detail.setXitem(xitem);
							detail.setItemName(caitemOp.get().getXdesc());
							detail.setXunit(caitemOp.get().getXunit());
							detail.setXrate(caitemOp.get().getXrate());
							detail.setXqty(BigDecimal.ONE);
							detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));

							Optional<Integer> maxXrow = details.stream().map(Opdodetail::getXrow).max(Integer::compareTo);
							detail.setXrow(maxXrow.isPresent() ? maxXrow.get() + 1 : 0);

							details.add(detail);
						}

					}
				}

				List<Opdodetail> itemList = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
				model.addAttribute("detailList", itemList == null ? Collections.emptyList() : itemList);

				xlogsdtService.save(new Xlogsdt("SO18", "View", this.pageTitle, null, null, true, 0));
			}
		} else {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(sessionManager).build(sessionManager, acsubRepo, cabunitRepo, xwhsRepo));
			model.addAttribute("detailList", Collections.emptyList());
		}

		return "pages/SO18/SO18-fragments::detail-table";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult) {

		// VALIDATE XSCREENS
		modelValidator.validateOpdoheader(opdoheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(sessionManager.getLoggedInUserDetails().isXislock()) {
			opdoheader.setXbuid(sessionManager.getLoggedInUserDetails().getPosunit());
			opdoheader.setXwh(sessionManager.getLoggedInUserDetails().getPosoutlet());
		}

		if(sessionManager.getLoggedInUserDetails().getZbusiness().getXposcus() == null) {
			responseHelper.setErrorStatusAndMessage("Default POS customer not set");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Outlet required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		opdoheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add items.");
			return responseHelper.getResponse();
		}

		BigDecimal totalXlineamt = details.stream().map(Opdodetail::getXlineamt).filter(xlineamt -> xlineamt != null).reduce(BigDecimal.ZERO, BigDecimal::add);
		if(totalXlineamt.compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Please add items.");
			return responseHelper.getResponse();
		}
		opdoheader.setXlineamt(totalXlineamt);
		opdoheader.setXdiscamt(BigDecimal.ZERO);
		opdoheader.setXtotamt(opdoheader.getXlineamt().subtract(opdoheader.getXdiscamt()));


		// Create new
		if(SubmitFor.INSERT.equals(opdoheader.getSubmitFor())) {
			opdoheader.setZid(sessionManager.getBusinessId());
			opdoheader.setXdornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO14"));
			opdoheader.setXdate(new Date());
			opdoheader.setXcus(sessionManager.getLoggedInUserDetails().getZbusiness().getXposcus());
			if(StringUtils.isBlank(opdoheader.getXstatus())) opdoheader.setXstatus("Confirmed");
			opdoheader.setXstatusim("Open");
			opdoheader.setXstatusjv("Open");
			opdoheader.setXtotcost(BigDecimal.ZERO);
			opdoheader.setXtype("POS Invoice");

			try {
				opdoheader = opdoheaderRepo.save(opdoheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO18", "Save", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0));

			// Salve all details
			for(Opdodetail d : details) {
				d.setZid(sessionManager.getBusinessId());
				d.setXdornum(opdoheader.getXdornum());
				d.setXdocrow(0);
				d.setXqtyord(BigDecimal.ZERO);
				d.setXqtycrn(BigDecimal.ZERO);
				d.setXrategrn(BigDecimal.ZERO);

				if(d.getXrate().compareTo(BigDecimal.ZERO) == -1) {
					throw new IllegalStateException("Invalid rate for item : " + d.getXitem() + " - " + d.getItemName());
				}

				if(d.getXqty().compareTo(BigDecimal.ZERO) != 1) {
					throw new IllegalStateException("Invalid quantity for item : " + d.getXitem() + " - " + d.getItemName());
				}

				try {
					opdodetailRepo.save(d);
				} catch (Exception e) {
					throw new IllegalStateException(e.getCause().getMessage());
				}

				xlogsdtService.save(new Xlogsdt("SO18", "Add", this.pageTitle, d.getXrow().toString(), d.toString(), true, 0));
			}

			if("Confirmed".equals(opdoheader.getXstatus())) {
				RequestParameters printParams = new RequestParameters();
				printParams.setReportCode("oppos");
				printParams.setReportType(ReportType.PDF);
				printParams.setParam1(sessionManager.getBusinessId());
				printParams.setParam2(opdoheader.getXdornum());
				responseHelper.setPrintParam(printParams, "/report/print");
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RESET&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage(opdoheader.getXstatus().equals("Confirmed") ? "Invoice created" : "Invoice on hold");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("Invoice number required to do update");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> existOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opdoheader.getXdornum()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found to do update");
			return responseHelper.getResponse();
		}

		Opdoheader existObj = existOp.get();

		if(!"Open".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Invoice already confirmed");
			return responseHelper.getResponse();
		}

		if(!existObj.getXstaff().equals(sessionManager.getLoggedInUserDetails().getXstaff())) {
			responseHelper.setErrorStatusAndMessage("You are not eligible to confirm this invoice.");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().isXislock()) {
			if(!existObj.getXbuid().equals(sessionManager.getLoggedInUserDetails().getPosunit())) {
				responseHelper.setErrorStatusAndMessage("You unit is locked and not matched with the invoice unit.");
				return responseHelper.getResponse();
			}

			if(!existObj.getXwh().equals(sessionManager.getLoggedInUserDetails().getPosoutlet())) {
				responseHelper.setErrorStatusAndMessage("You outlet is locked and not matched with the invoice unit.");
				return responseHelper.getResponse();
			}
		}

		existObj.setXdate(new Date());
		existObj.setXbuid(opdoheader.getXbuid());
		existObj.setXwh(opdoheader.getXwh());
		existObj.setXcus(sessionManager.getZbusiness().getXposcus());
		existObj.setXstatus(opdoheader.getXstatus());
		existObj.setXlineamt(opdoheader.getXlineamt());
		existObj.setXdiscamt(opdoheader.getXdiscamt());
		existObj.setXtotamt(opdoheader.getXtotamt());
		try {
			existObj = opdoheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO18", "Update", this.pageTitle, existObj.getXdornum().toString(), existObj.toString(), false, 0));

		// Remove all db details first
		try {
			opdodetailRepo.deleteAllByZidAndXdornum(sessionManager.getBusinessId(), existObj.getXdornum());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO18", "Delete", this.pageTitle, existObj.getXdornum().toString(), null, true, 0).setMessage("Delete all details"));

		// Salve all details
		for(Opdodetail d : details) {
			d.setZid(sessionManager.getBusinessId());
			d.setXdornum(existObj.getXdornum());
			d.setXdocrow(0);
			d.setXqtyord(BigDecimal.ZERO);
			d.setXqtycrn(BigDecimal.ZERO);
			d.setXrategrn(BigDecimal.ZERO);

			if(d.getXrate().compareTo(BigDecimal.ZERO) == -1) {
				throw new IllegalStateException("Invalid rate for item : " + d.getXitem() + " - " + d.getItemName());
			}

			if(d.getXqty().compareTo(BigDecimal.ZERO) != 1) {
				throw new IllegalStateException("Invalid quantity for item : " + d.getXitem() + " - " + d.getItemName());
			}

			try {
				opdodetailRepo.save(d);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO18", "Add", this.pageTitle, d.getXrow().toString(), d.toString(), true, 0));
		}

		if("Confirmed".equals(existObj.getXstatus())) {
			RequestParameters printParams = new RequestParameters();
			printParams.setReportCode("oppos");
			printParams.setReportType(ReportType.PDF);
			printParams.setParam1(sessionManager.getBusinessId());
			printParams.setParam2(opdoheader.getXdornum());
			responseHelper.setPrintParam(printParams, "/report/print");
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO18?xdornum=RESET&xbuid=" + existObj.getXbuid() + "&xwh=" + existObj.getXwh()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage(opdoheader.getXstatus().equals("Confirmed") ? "Invoice created" : "Invoice on hold");
		return responseHelper.getResponse();
	}

	@Cacheable(value = "applicationCache", key = "#searchText")
	@GetMapping("/search-item")
	public @ResponseBody long searchItem(@RequestParam(required = false) String searchText) {
		if(StringUtils.isBlank(searchText)) return 0;
		return caitemRepo.searchItemCount(sessionManager.getBusinessId(), searchText.trim());
	}

	@SuppressWarnings("unchecked")
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xrow) throws Exception{

		if(sessionManager.getFromMap("SO18-DETAILS") == null) {
			responseHelper.setErrorStatusAndMessage("Cart is empty");
			return responseHelper.getResponse();
		} 

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		details = details.stream().filter(f -> !f.getXrow().equals(xrow)).collect(Collectors.toList());

		sessionManager.removeFromMap("SO18-DETAILS");
		sessionManager.addToMap("SO18-DETAILS", details);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RELOAD&xrow=RELOAD"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Item removed from cart");
		return responseHelper.getResponse();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/update-qty")
	public @ResponseBody boolean updateItemQty(@RequestParam Integer xrow, @RequestParam BigDecimal qty){
		if(sessionManager.getFromMap("SO18-DETAILS") == null) {
			return false;
		}

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		Opdodetail detail = details.stream().filter(f -> f.getXrow().equals(xrow)).findFirst().orElse(null);
		if(detail == null) return false;

		detail.setXqty(qty);
		detail.setXlineamt(detail.getXqty().multiply(detail.getXrate()));

		return true;
	}

	@Cacheable
	@GetMapping("/all-pos-items")
	public @ResponseBody List<Caitem> getAllItems(Model model){
		return caitemRepo.findByZidAndXisopAndNotXgitem(sessionManager.getBusinessId());
	}
}
