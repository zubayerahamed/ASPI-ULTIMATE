package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.Modetail;
import com.zayaanit.aspi.entity.Moheader;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.ModetailPK;
import com.zayaanit.aspi.entity.pk.MoheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.ModetailRepo;
import com.zayaanit.aspi.repo.MoheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM14")
public class IM14 extends KitController {

	@Autowired private MoheaderRepo moheaderRepo;
	@Autowired private ModetailRepo modetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM14";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xbatch, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("issueTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Issue Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xbatch)) {
				model.addAttribute("moheader", Moheader.getDefaultInstance());
				return "pages/IM14/IM14-fragments::main-form";
			}

			Optional<Moheader> op = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xbatch)));
			Moheader moheader = null;
			if(op.isPresent()) {
				moheader = op.get();

				if(moheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), moheader.getXbuid()));
					if(cabunitOp.isPresent()) moheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(moheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), moheader.getXwh()));
					if(xwhsOp.isPresent()) moheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(moheader.getXitem() != null) {
					Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), moheader.getXitem()));
					if(caitemOp.isPresent()) {
						moheader.setItemName(caitemOp.get().getXdesc());
						moheader.setXunit(caitemOp.get().getXunit());
					}
				}

				if(moheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), moheader.getXstaff()));
					if(acsubOp.isPresent()) moheader.setStaffName(acsubOp.get().getXname());
				}

				if(moheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), moheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) moheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("moheader", moheader != null ? moheader : Moheader.getDefaultInstance());

			return "pages/IM14/IM14-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("moheader", Moheader.getDefaultInstance());
		return "pages/IM14/IM14";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xbatch, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xbatch) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("moheader", Moheader.getDefaultInstance());
			return "pages/IM14/IM14-fragments::detail-table";
		}

		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xbatch)));
		if(!oph.isPresent()) {
			model.addAttribute("moheader", Moheader.getDefaultInstance());
			return "pages/IM14/IM14-fragments::detail-table";
		}
		model.addAttribute("moheader", oph.get());

		List<Modetail> detailList = modetailRepo.findAllByZidAndXbatchAndXsign(sessionManager.getBusinessId(), Integer.parseInt(xbatch), -1);
		for(Modetail detail : detailList) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), detail.getXitem()));
			if(caitemOp.isPresent()) {
				detail.setItemName(caitemOp.get().getXdesc());
				detail.setXunit(caitemOp.get().getXunit());
			}
		}
		model.addAttribute("detailList", detailList);

		Caitem caitem = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Modetail imtrodetail = Modetail.getRAWDefaultInstance(Integer.parseInt(xbatch));
			if(caitem != null) {
				imtrodetail.setXitem(xitem);
				imtrodetail.setItemName(caitem.getXdesc());
				imtrodetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("modetail", imtrodetail);
			return "pages/IM14/IM14-fragments::detail-table";
		}

		Optional<Modetail> modetailOp = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), Integer.parseInt(xbatch), Integer.parseInt(xrow)));
		Modetail modetail = modetailOp.isPresent() ? modetailOp.get() : Modetail.getRAWDefaultInstance(Integer.parseInt(xbatch));
		if(modetail != null && modetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), modetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && modetail != null) {
			modetail.setXitem(caitem.getXitem());
			modetail.setItemName(caitem.getXdesc());
			modetail.setXunit(caitem.getXunit());
		}

		model.addAttribute("modetail", modetail);
		return "pages/IM14/IM14-fragments::detail-table";
	}

	@GetMapping("/additional-table")
	public String additionalFormFragment(@RequestParam String xbatch, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		model.addAttribute("byTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("By Products Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if("RESET".equalsIgnoreCase(xbatch) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("moheader", Moheader.getDefaultInstance());
			return "pages/IM14/IM14-fragments::additional-table";
		}

		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xbatch)));
		if(!oph.isPresent()) {
			model.addAttribute("moheader", Moheader.getDefaultInstance());
			return "pages/IM14/IM14-fragments::additional-table";
		}
		model.addAttribute("moheader", oph.get());

		List<Modetail> detailList = modetailRepo.findAllByZidAndXbatchAndXsign(sessionManager.getBusinessId(), Integer.parseInt(xbatch), 1);
		for(Modetail detail : detailList) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), detail.getXitem()));
			if(caitemOp.isPresent()) {
				detail.setItemName(caitemOp.get().getXdesc());
				detail.setXunit(caitemOp.get().getXunit());
			}
		}
		model.addAttribute("detailList", detailList);

		Caitem caitem = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Modetail modetail = Modetail.getBYPDefaultInstance(Integer.parseInt(xbatch));
			if(caitem != null) {
				modetail.setXitem(xitem);
				modetail.setItemName(caitem.getXdesc());
				modetail.setXunit(caitem.getXunit());
				modetail.setXrate(caitem.getXcost());
			}

			model.addAttribute("modetail", modetail);
			return "pages/IM14/IM14-fragments::additional-table";
		}

		Optional<Modetail> modetailOp = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), Integer.parseInt(xbatch), Integer.parseInt(xrow)));
		Modetail modetail = modetailOp.isPresent() ? modetailOp.get() : Modetail.getBYPDefaultInstance(Integer.parseInt(xbatch));
		if(modetail != null && modetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), modetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && modetail != null) {
			modetail.setXitem(caitem.getXitem());
			modetail.setItemName(caitem.getXdesc());
			modetail.setXunit(caitem.getXunit());
			if(modetail.getXrow() == 0) {
				modetail.setXrate(caitem.getXcost());
			}
		}

		model.addAttribute("modetail", modetail);
		return "pages/IM14/IM14-fragments::additional-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM14/IM14-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Moheader moheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateMoheader(moheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(moheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(moheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(moheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(moheader.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		if(moheader.getXqty() == null || moheader.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(moheader.getXexptype())) {
			responseHelper.setErrorStatusAndMessage("Add. Exp Type required");
			return responseHelper.getResponse();
		}

		if(moheader.getXexpval() == null || moheader.getXexpval().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Add. Exp (Percent/Amount)");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		moheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(moheader.getSubmitFor())) {
			moheader.setXrate(BigDecimal.ZERO);
			moheader.setXtotamt(BigDecimal.ZERO);
			moheader.setXstatus("Open");
			moheader.setXstatusjv("Open");
			moheader.setXstatusim("Open");
			moheader.setXbatch(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM14"));
			moheader.setZid(sessionManager.getBusinessId());
			try {
				moheader = moheaderRepo.save(moheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + moheader.getXbatch()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch="+ moheader.getXbatch() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch="+ moheader.getXbatch() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Batch process created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Moheader> op = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), moheader.getXbatch()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Moheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xbatch", 
			"xrate",
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
		};
		BeanUtils.copyProperties(moheader, existObj, ignoreProperties);
		try {
			existObj = moheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + existObj.getXbatch()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch="+ moheader.getXbatch() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch="+ moheader.getXbatch() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Batch process updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Modetail modetail, BindingResult bindingResult){
		if(modetail.getXbatch() == null) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), modetail.getXbatch()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Moheader moheader = oph.get();
		if(!"Open".equals(moheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(modetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), modetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(modetail.getXqty() == null || modetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(modetail.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Consumption Type required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(modetail.getSubmitFor())) {
			modetail.setXrow(modetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), modetail.getXbatch()));
			modetail.setZid(sessionManager.getBusinessId());
			modetail.setXrate(BigDecimal.ZERO);
			modetail.setXlineamt(BigDecimal.ZERO);
			modetail.setXsign(-1);
			try {
				modetail = modetailRepo.save(modetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + modetail.getXbatch()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch=" + modetail.getXbatch() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Modetail> existOp = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), modetail.getXbatch(), modetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Modetail exist = existOp.get();
		String[] ignoreProperties = new String[] {
				"zid", "zuserid", "ztime",
				"xbatch", 
				"xrow",
				"xitem",
				"xrate",
				"xlineamt",
				"xsign",
			};
		BeanUtils.copyProperties(modetail, exist, ignoreProperties);
		try {
			exist = modetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + exist.getXbatch()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch=" + exist.getXbatch() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/additional/store")
	public @ResponseBody Map<String, Object> storeAdditionalDetail(Modetail modetail, BindingResult bindingResult){
		if(modetail.getXbatch() == null) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), modetail.getXbatch()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Moheader moheader = oph.get();
		if(!"Open".equals(moheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(modetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), modetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(modetail.getXqty() == null || modetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(modetail.getXrate() == null || modetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(modetail.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Consumption Type required");
			return responseHelper.getResponse();
		}

		modetail.setXlineamt(modetail.getXqty().multiply(modetail.getXrate()));

		// Create new
		if(SubmitFor.INSERT.equals(modetail.getSubmitFor())) {
			modetail.setXrow(modetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), modetail.getXbatch()));
			modetail.setZid(sessionManager.getBusinessId());
			modetail.setXsign(1);
			try {
				modetail = modetailRepo.save(modetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + modetail.getXbatch()));
			reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch=" + modetail.getXbatch() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Modetail> existOp = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), modetail.getXbatch(), modetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Modetail exist = existOp.get();
		String[] ignoreProperties = new String[] {
				"zid", "zuserid", "ztime",
				"xbatch", 
				"xrow",
				"xitem",
				"xsign",
			};
		BeanUtils.copyProperties(modetail, exist, ignoreProperties);
		try {
			exist = modetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + exist.getXbatch()));
		reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch=" + exist.getXbatch() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xbatch){
		Optional<Moheader> op = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), xbatch));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			modetailRepo.deleteAllByZidAndXbatchAndXsign(sessionManager.getBusinessId(), xbatch, -1);
			modetailRepo.deleteAllByZidAndXbatchAndXsign(sessionManager.getBusinessId(), xbatch, 1);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Moheader obj = op.get();
		try {
			moheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xbatch, @RequestParam Integer xrow) throws Exception{
		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), xbatch));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Moheader moheader = oph.get();

		if(!"Open".equals(moheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Modetail> op = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), xbatch, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Modetail obj = op.get();
		try {
			modetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + xbatch));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch="+xbatch+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/additional-table")
	public @ResponseBody Map<String, Object> deleteAdditionalDetail(@RequestParam Integer xbatch, @RequestParam Integer xrow) throws Exception{
		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), xbatch));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Moheader moheader = oph.get();

		if(!"Open".equals(moheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Modetail> op = modetailRepo.findById(new ModetailPK(sessionManager.getBusinessId(), xbatch, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Modetail obj = op.get();
		try {
			modetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + xbatch));
		reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch="+xbatch+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xbatch) {
		Optional<Moheader> oph = moheaderRepo.findById(new MoheaderPK(sessionManager.getBusinessId(), xbatch));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Moheader moheader = oph.get();

		if(!"Open".equals(moheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(moheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Modetail> rawDetails = modetailRepo.findAllByZidAndXbatchAndXsign(sessionManager.getBusinessId(), xbatch, -1);
		if(rawDetails == null || rawDetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No consumption data found");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(moheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Modetail detail : rawDetails) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No consumption data found");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Modetail item : rawDetails) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, moheader.getXbuid(), moheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM14/error-details");
			return responseHelper.getResponse();
		}

		try {
			moheaderRepo.IM_ConfirmBatch(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xbatch);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xbatch=" + xbatch));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xbatch="+xbatch+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("additional-table-container", "/IM14/additional-table?xbatch="+xbatch+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/fg-item")
	public String loadFgItemFragment(@RequestParam Integer xitem, Model model) {
		Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));

		Moheader moheader = Moheader.getDefaultInstance();
		if(caitemOp.isPresent()) {
			moheader.setXitem(xitem);
			moheader.setItemName(caitemOp.get().getXdesc());
			moheader.setXunit(caitemOp.get().getXunit());
		}

		model.addAttribute("moheader", moheader);
		return "pages/IM14/IM14-fragments::fg-item";
	}
}
