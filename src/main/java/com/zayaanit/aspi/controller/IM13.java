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
import com.zayaanit.aspi.entity.Imissuedetail;
import com.zayaanit.aspi.entity.Imissueheader;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.ImissuedetailPK;
import com.zayaanit.aspi.entity.pk.ImissueheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.ImissuedetailRepo;
import com.zayaanit.aspi.repo.ImissueheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM13")
public class IM13 extends KitController {

	@Autowired private ImissueheaderRepo imissueheaderRepo;
	@Autowired private ImissuedetailRepo imissuedetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM13";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xissuenum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("issueTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Issue Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xissuenum)) {
				model.addAttribute("imissueheader", Imissueheader.getDefaultInstance());
				return "pages/IM13/IM13-fragments::main-form";
			}

			Optional<Imissueheader> op = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xissuenum)));
			Imissueheader imissueheader = null;
			if(op.isPresent()) {
				imissueheader = op.get();

				if(imissueheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imissueheader.getXbuid()));
					if(cabunitOp.isPresent()) imissueheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imissueheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imissueheader.getXwh()));
					if(xwhsOp.isPresent()) imissueheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(imissueheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imissueheader.getXstaff()));
					if(acsubOp.isPresent()) imissueheader.setStaffName(acsubOp.get().getXname());
				}

				if(imissueheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imissueheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imissueheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imissueheader", imissueheader != null ? imissueheader : Imissueheader.getDefaultInstance());

			return "pages/IM13/IM13-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imissueheader", Imissueheader.getDefaultInstance());
		return "pages/IM13/IM13";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xissuenum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xissuenum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imissueheader", Imissueheader.getDefaultInstance());
			return "pages/IM13/IM13-fragments::detail-table";
		}

		Optional<Imissueheader> oph = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xissuenum)));
		if(!oph.isPresent()) {
			model.addAttribute("imissueheader", Imissueheader.getDefaultInstance());
			return "pages/IM13/IM13-fragments::detail-table";
		}
		model.addAttribute("imissueheader", oph.get());

		List<Imissuedetail> detailList = imissuedetailRepo.findAllByZidAndXissuenum(sessionManager.getBusinessId(), Integer.parseInt(xissuenum));
		for(Imissuedetail detail : detailList) {
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
			Imissuedetail imtrodetail = Imissuedetail.getDefaultInstance(Integer.parseInt(xissuenum));
			if(caitem != null) {
				imtrodetail.setXitem(xitem);
				imtrodetail.setItemName(caitem.getXdesc());
				imtrodetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imissuedetail", imtrodetail);
			return "pages/IM13/IM13-fragments::detail-table";
		}

		Optional<Imissuedetail> imtrodetailOp = imissuedetailRepo.findById(new ImissuedetailPK(sessionManager.getBusinessId(), Integer.parseInt(xissuenum), Integer.parseInt(xrow)));
		Imissuedetail imtrodetail = imtrodetailOp.isPresent() ? imtrodetailOp.get() : Imissuedetail.getDefaultInstance(Integer.parseInt(xissuenum));
		if(imtrodetail != null && imtrodetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtrodetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imtrodetail != null) {
			imtrodetail.setXitem(caitem.getXitem());
			imtrodetail.setItemName(caitem.getXdesc());
			imtrodetail.setXunit(caitem.getXunit());
		}

		model.addAttribute("imissuedetail", imtrodetail);
		return "pages/IM13/IM13-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM13/IM13-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imissueheader imissueheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImissueheader(imissueheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imissueheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imissueheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("From business unit required");
			return responseHelper.getResponse();
		}

		if(imissueheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("From store required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(imissueheader.getXisstype())) {
			responseHelper.setErrorStatusAndMessage("Issue type required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imissueheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(imissueheader.getSubmitFor())) {
			imissueheader.setXtotamt(BigDecimal.ZERO);
			imissueheader.setXstatus("Open");
			imissueheader.setXstatusim("Open");
			imissueheader.setXstatusjv("Open");
			imissueheader.setXissuenum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM13"));
			imissueheader.setZid(sessionManager.getBusinessId());
			try {
				imissueheader = imissueheaderRepo.save(imissueheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + imissueheader.getXissuenum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+ imissueheader.getXissuenum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventory issue created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imissueheader> op = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), imissueheader.getXissuenum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Imissueheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xissuenum", 
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
		};
		BeanUtils.copyProperties(imissueheader, existObj, ignoreProperties);
		try {
			existObj = imissueheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + existObj.getXissuenum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+ imissueheader.getXissuenum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory issue updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imissuedetail imtrodetail, BindingResult bindingResult){
		if(imtrodetail.getXissuenum() == null) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Optional<Imissueheader> oph = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), imtrodetail.getXissuenum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imissueheader imissueheader = oph.get();
		if(!"Open".equals(imissueheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Issue not open");
			return responseHelper.getResponse();
		}

		if(imtrodetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtrodetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imtrodetail.getXqty() == null || imtrodetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imtrodetail.getSubmitFor())) {
			imtrodetail.setXrow(imissuedetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imtrodetail.getXissuenum()));
			imtrodetail.setZid(sessionManager.getBusinessId());
			imtrodetail.setXrate(BigDecimal.ZERO);
			imtrodetail.setXlineamt(BigDecimal.ZERO);
			try {
				imtrodetail = imissuedetailRepo.save(imtrodetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + imtrodetail.getXissuenum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=" + imtrodetail.getXissuenum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Transfer detail added successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not applicatble here");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xissuenum){
		Optional<Imissueheader> op = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), xissuenum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			imissuedetailRepo.deleteAllByZidAndXissuenum(sessionManager.getBusinessId(), xissuenum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Imissueheader obj = op.get();
		try {
			imissueheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xissuenum, @RequestParam Integer xrow) throws Exception{
		Optional<Imissueheader> oph = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), xissuenum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imissueheader imissueheader = oph.get();

		if(!"Open".equals(imissueheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imissuedetail> op = imissuedetailRepo.findById(new ImissuedetailPK(sessionManager.getBusinessId(), xissuenum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imissuedetail obj = op.get();
		try {
			imissuedetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + xissuenum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+xissuenum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xissuenum) {
		Optional<Imissueheader> oph = imissueheaderRepo.findById(new ImissueheaderPK(sessionManager.getBusinessId(), xissuenum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imissueheader imissueheader = oph.get();

		if(!"Open".equals(imissueheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(imissueheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Imissuedetail> details = imissuedetailRepo.findAllByZidAndXissuenum(sessionManager.getBusinessId(), xissuenum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(imissueheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imissuedetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imissuedetail item : details) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, imissueheader.getXbuid(), imissueheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM13/error-details");
			return responseHelper.getResponse();
		}

		try {
			imissueheaderRepo.IM_ConfirmIssue(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xissuenum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + xissuenum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+xissuenum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM13/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
