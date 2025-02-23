package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.zayaanit.aspi.entity.Imadjdetail;
import com.zayaanit.aspi.entity.Imadjheader;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.ImadjdetailPK;
import com.zayaanit.aspi.entity.pk.ImadjheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.ImadjdetailRepo;
import com.zayaanit.aspi.repo.ImadjheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM15")
public class IM15 extends KitController {

	@Autowired private ImadjheaderRepo imadjheaderRepo;
	@Autowired private ImadjdetailRepo imadjdetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xadjnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xadjnum)) {
				model.addAttribute("imadjheader", Imadjheader.getDefaultInstance());
				return "pages/IM15/IM15-fragments::main-form";
			}

			Optional<Imadjheader> op = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xadjnum)));
			Imadjheader imadjheader = null;
			if(op.isPresent()) {
				imadjheader = op.get();

				if(imadjheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imadjheader.getXbuid()));
					if(cabunitOp.isPresent()) imadjheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imadjheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imadjheader.getXwh()));
					if(xwhsOp.isPresent()) imadjheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(imadjheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imadjheader.getXstaff()));
					if(acsubOp.isPresent()) imadjheader.setStaffName(acsubOp.get().getXname());
				}

				if(imadjheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imadjheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imadjheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imadjheader", imadjheader != null ? imadjheader : Imadjheader.getDefaultInstance());

			return "pages/IM15/IM15-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imadjheader", Imadjheader.getDefaultInstance());
		return "pages/IM15/IM15";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xadjnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xadjnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imadjheader", Imadjheader.getDefaultInstance());
			return "pages/IM15/IM15-fragments::detail-table";
		}

		Optional<Imadjheader> oph = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xadjnum)));
		if(!oph.isPresent()) {
			model.addAttribute("imadjheader", Imadjheader.getDefaultInstance());
			return "pages/IM15/IM15-fragments::detail-table";
		}
		model.addAttribute("imadjheader", oph.get());

		List<Imadjdetail> detailList = imadjdetailRepo.findAllByZidAndXadjnum(sessionManager.getBusinessId(), Integer.parseInt(xadjnum));
		for(Imadjdetail detail : detailList) {
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
			Imadjdetail imadjdetail = Imadjdetail.getDefaultInstance(Integer.parseInt(xadjnum));
			if(caitem != null) {
				imadjdetail.setXitem(xitem);
				imadjdetail.setItemName(caitem.getXdesc());
				imadjdetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imadjdetail", imadjdetail);
			return "pages/IM15/IM15-fragments::detail-table";
		}

		Optional<Imadjdetail> imadjdetailOp = imadjdetailRepo.findById(new ImadjdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xadjnum), Integer.parseInt(xrow)));
		Imadjdetail imadjdetail = imadjdetailOp.isPresent() ? imadjdetailOp.get() : Imadjdetail.getDefaultInstance(Integer.parseInt(xadjnum));
		if(imadjdetail != null && imadjdetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imadjdetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imadjdetail != null) {
			imadjdetail.setXitem(caitem.getXitem());
			imadjdetail.setItemName(caitem.getXdesc());
			imadjdetail.setXunit(caitem.getXunit());
		}

		model.addAttribute("imadjdetail", imadjdetail);
		return "pages/IM15/IM15-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM15/IM15-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imadjheader imadjheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImadjheader(imadjheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imadjheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imadjheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(imadjheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imadjheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(imadjheader.getSubmitFor())) {
			imadjheader.setXtotamt(BigDecimal.ZERO);
			imadjheader.setXstatus("Open");
			imadjheader.setXstatusim("Open");
			imadjheader.setXstatusjv("Open");
			imadjheader.setXadjnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM15"));
			imadjheader.setZid(sessionManager.getBusinessId());
			try {
				imadjheader = imadjheaderRepo.save(imadjheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=" + imadjheader.getXadjnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum="+ imadjheader.getXadjnum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventory issue created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imadjheader> op = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), imadjheader.getXadjnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Imadjheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xadjnum", 
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
		};
		BeanUtils.copyProperties(imadjheader, existObj, ignoreProperties);
		try {
			existObj = imadjheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=" + existObj.getXadjnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum="+ imadjheader.getXadjnum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory issue updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imadjdetail imadjdetail, BindingResult bindingResult){
		if(imadjdetail.getXadjnum() == null) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Optional<Imadjheader> oph = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), imadjdetail.getXadjnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Imadjheader imadjheader = oph.get();
		if(!"Open".equals(imadjheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Adjustment status not open");
			return responseHelper.getResponse();
		}

		if(imadjdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imadjdetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imadjdetail.getXqty() == null || imadjdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(imadjdetail.getXsign() == null) {
			responseHelper.setErrorStatusAndMessage("Adjustment type required");
			return responseHelper.getResponse();
		}

		if(!(imadjdetail.getXsign().equals(1) || imadjdetail.getXsign().equals(-1))) {
			responseHelper.setErrorStatusAndMessage("Invalid adjustment type");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imadjdetail.getSubmitFor())) {
			imadjdetail.setXrow(imadjdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imadjdetail.getXadjnum()));
			imadjdetail.setZid(sessionManager.getBusinessId());
			imadjdetail.setXrate(BigDecimal.ZERO);
			imadjdetail.setXlineamt(BigDecimal.ZERO);
			try {
				imadjdetail = imadjdetailRepo.save(imadjdetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=" + imadjdetail.getXadjnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum=" + imadjdetail.getXadjnum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Transfer detail added successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not applicatble here");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xadjnum){
		Optional<Imadjheader> op = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), xadjnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			imadjdetailRepo.deleteAllByZidAndXadjnum(sessionManager.getBusinessId(), xadjnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Imadjheader obj = op.get();
		try {
			imadjheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xadjnum, @RequestParam Integer xrow) throws Exception{
		Optional<Imadjheader> oph = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), xadjnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imadjheader imadjheader = oph.get();

		if(!"Open".equals(imadjheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imadjdetail> op = imadjdetailRepo.findById(new ImadjdetailPK(sessionManager.getBusinessId(), xadjnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imadjdetail obj = op.get();
		try {
			imadjdetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=" + xadjnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum="+xadjnum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xadjnum) {
		Optional<Imadjheader> oph = imadjheaderRepo.findById(new ImadjheaderPK(sessionManager.getBusinessId(), xadjnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Imadjheader imadjheader = oph.get();

		if(!"Open".equals(imadjheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(imadjheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Imadjdetail> details = imadjdetailRepo.findAllByZidAndXadjnum(sessionManager.getBusinessId(), xadjnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(imadjheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imadjdetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imadjdetail item : details) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, imadjheader.getXbuid(), imadjheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM15/error-details");
			return responseHelper.getResponse();
		}

		try {
			imadjheaderRepo.IM_ConfirmAdjustment(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xadjnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM15?xadjnum=" + xadjnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM15/detail-table?xadjnum="+xadjnum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM15/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
