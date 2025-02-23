package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.SerializationUtils;
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
import com.zayaanit.aspi.entity.Poorddetail;
import com.zayaanit.aspi.entity.Poordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.PoorddetailPK;
import com.zayaanit.aspi.entity.pk.PoordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.PoorddetailRepo;
import com.zayaanit.aspi.repo.PoordheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2025
 */
@Controller
@RequestMapping("/PO12")
public class PO12 extends KitController {

	@Autowired private PoordheaderRepo poordheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private PoorddetailRepo poorddetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "PO12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "PO12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xpornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xpornum)) {
				model.addAttribute("poordheader", Poordheader.getDefaultInstance());

				xlogsdtService.save(new Xlogsdt("PO12", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/PO12/PO12-fragments::main-form";
			}

			Optional<Poordheader> op = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xpornum)));
			Poordheader poordheader = null;
			if(op.isPresent()) {
				poordheader = op.get();

				if(poordheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), poordheader.getXbuid()));
					if(cabunitOp.isPresent()) poordheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(poordheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), poordheader.getXcus()));
					if(acsubOp.isPresent()) poordheader.setSupplierName(acsubOp.get().getXname());
				}

				if(poordheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), poordheader.getXwh()));
					if(xwhsOp.isPresent()) poordheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(poordheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), poordheader.getXstaff()));
					if(acsubOp.isPresent()) poordheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("poordheader", poordheader != null ? poordheader : Poordheader.getDefaultInstance());

			xlogsdtService.save(new Xlogsdt("PO12", "View", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));
			return "pages/PO12/PO12-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("poordheader", Poordheader.getDefaultInstance());
		return "pages/PO12/PO12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xpornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xpornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("poordheader", Poordheader.getDefaultInstance());

			xlogsdtService.save(new Xlogsdt("PO12", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/PO12/PO12-fragments::detail-table";
		}

		Optional<Poordheader> oph = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xpornum)));
		if(!oph.isPresent()) {
			model.addAttribute("poordheader", Poordheader.getDefaultInstance());
			return "pages/PO12/PO12-fragments::detail-table";
		}
		model.addAttribute("poordheader", oph.get());

		List<Poorddetail> detailList = poorddetailRepo.findAllByZidAndXpornum(sessionManager.getBusinessId(), Integer.parseInt(xpornum));
		for(Poorddetail detail : detailList) {
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
			Poorddetail poorddetail = Poorddetail.getDefaultInstance(Integer.parseInt(xpornum));
			if(caitem != null) {
				poorddetail.setXitem(xitem);
				poorddetail.setItemName(caitem.getXdesc());
				poorddetail.setXunit(caitem.getXunit());
				poorddetail.setXrate(caitem.getXcost());
				poorddetail.setXlineamt(poorddetail.getXqty().multiply(poorddetail.getXrate()));
			}

			model.addAttribute("poorddetail", poorddetail);

			xlogsdtService.save(new Xlogsdt("PO12", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/PO12/PO12-fragments::detail-table";
		}

		Optional<Poorddetail> poorddetailOp = poorddetailRepo.findById(new PoorddetailPK(sessionManager.getBusinessId(), Integer.parseInt(xpornum), Integer.parseInt(xrow)));
		Poorddetail poorddetail = poorddetailOp.isPresent() ? poorddetailOp.get() : Poorddetail.getDefaultInstance(Integer.parseInt(xpornum));
		if(poorddetail != null && poorddetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), poorddetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && poorddetail != null) {
			poorddetail.setXitem(caitem.getXitem());
			poorddetail.setItemName(caitem.getXdesc());
			poorddetail.setXunit(caitem.getXunit());
			if(poorddetail.getXrow() == 0) {
				poorddetail.setXrate(caitem.getXcost());
				poorddetail.setXlineamt(poorddetail.getXqty().multiply(poorddetail.getXrate()));
			}
		}

		model.addAttribute("poorddetail", poorddetail);
		xlogsdtService.save(new Xlogsdt("PO12", "View", this.pageTitle, poorddetail.getXrow().toString(), poorddetail.toString(), true, 0));
		return "pages/PO12/PO12-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Poordheader poordheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validatePoordheader(poordheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(poordheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		poordheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(poordheader.getSubmitFor())) {
			poordheader.setXtotamt(BigDecimal.ZERO);
			poordheader.setXstatus("Open");
			poordheader.setXstatusord("Open");
			poordheader.setXpornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "PO12"));
			poordheader.setZid(sessionManager.getBusinessId());
			try {
				poordheader = poordheaderRepo.save(poordheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO12", "Add", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + poordheader.getXpornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum="+ poordheader.getXpornum() +"&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Purchase order created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Poordheader> op = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), poordheader.getXpornum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Poordheader existObj = op.get();
		BeanUtils.copyProperties(poordheader, existObj, "zid", "zuserid", "ztime", "xpornum", "xtotamt", "xgrnnum", "xstatus", "xstatusord", "xstaffsubmit", "xsubmittime", "xstaffappr", "xapprovertime");

		// Calculate total amount
		BigDecimal xtotamt = poorddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXpornum());
		existObj.setXtotamt(xtotamt);

		try {
			existObj = poordheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Update", this.pageTitle, existObj.getXpornum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + existObj.getXpornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum="+ poordheader.getXpornum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Purchase order updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Poorddetail poorddetail, BindingResult bindingResult){
		if(poorddetail.getXpornum() == null) {
			responseHelper.setErrorStatusAndMessage("Purchase order not found");
			return responseHelper.getResponse();
		}

		Optional<Poordheader> oph = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), poorddetail.getXpornum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Purchase order not found");
			return responseHelper.getResponse();
		}

		Poordheader poordheader = oph.get();
		if(!"Open".equals(poordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Purchase order not open");
			return responseHelper.getResponse();
		}

		if(poorddetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), poorddetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(poorddetail.getXrate() == null || poorddetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(poorddetail.getXqty() == null || poorddetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		poorddetail.setXlineamt(poorddetail.getXrate().multiply(poorddetail.getXqty()));
		poorddetail.setXqtygrn(BigDecimal.ZERO);

		// Create new
		if(SubmitFor.INSERT.equals(poorddetail.getSubmitFor())) {
			poorddetail.setXrow(poorddetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), poorddetail.getXpornum()));
			poorddetail.setZid(sessionManager.getBusinessId());
			try {
				poorddetail = poorddetailRepo.save(poorddetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			BigDecimal xtotamt = poorddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), poorddetail.getXpornum());
			poordheader.setXtotamt(xtotamt);
			try {
				poordheaderRepo.save(poordheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO12", "Add", this.pageTitle, poorddetail.getXrow().toString(), poorddetail.toString(), true, 0));
			xlogsdtService.save(new Xlogsdt("PO12", "Update", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + poorddetail.getXpornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum=" + poorddetail.getXpornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Purchase order detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Poorddetail> existOp = poorddetailRepo.findById(new PoorddetailPK(sessionManager.getBusinessId(), poorddetail.getXpornum(), poorddetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found in this system");
			return responseHelper.getResponse();
		}

		Poorddetail exist = existOp.get();
		BeanUtils.copyProperties(poorddetail, exist, "zid", "zuserid", "ztime", "xpornum", "xrow", "xitem");
		try {
			exist = poorddetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		BigDecimal xtotamt = poorddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), exist.getXpornum());
		poordheader.setXtotamt(xtotamt);
		try {
			poordheaderRepo.save(poordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Update", this.pageTitle, exist.getXrow().toString(), exist.toString(), true, 0));
		xlogsdtService.save(new Xlogsdt("PO12", "Update", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + poorddetail.getXpornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum=" + poorddetail.getXpornum() + "&xrow=" + exist.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xpornum){
		Optional<Poordheader> op = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), xpornum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			poorddetailRepo.deleteAllByZidAndXpornum(sessionManager.getBusinessId(), xpornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Delete", this.pageTitle, xpornum.toString(), null, true, 0).setMessage("Delete all details"));

		Poordheader obj = op.get();
		Poordheader copy = SerializationUtils.clone(obj);
		try {
			poordheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Delete", this.pageTitle, copy.getXpornum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xpornum, @RequestParam Integer xrow){
		Optional<Poordheader> oph = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), xpornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Poordheader poordheader = oph.get();

		if(!"Open".equals(poordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Purchase order status not open");
			return responseHelper.getResponse();
		}

		Optional<Poorddetail> op = poorddetailRepo.findById(new PoorddetailPK(sessionManager.getBusinessId(), xpornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Poorddetail obj = op.get();
		Poorddetail copy = SerializationUtils.clone(obj);
		try {
			poorddetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Delete", this.pageTitle, copy.getXrow().toString(), copy.toString(), true, 0));


		// Update line amount and total amount of header
		BigDecimal xtotamt = poorddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), poordheader.getXpornum());
		poordheader.setXtotamt(xtotamt);
		try {
			poordheaderRepo.save(poordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Update", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + xpornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum="+xpornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xpornum) {
		Optional<Poordheader> oph = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), xpornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Poordheader poordheader = oph.get();

		if(!"Open".equals(poordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(poordheader.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Order Order status not open");
			return responseHelper.getResponse();
		}

		// Screen data validation
		if(poordheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		if(poordheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Quantity validation
		BigDecimal totalQty = poorddetailRepo.getTotalQty(sessionManager.getBusinessId(), xpornum);
		if(totalQty.compareTo(BigDecimal.ZERO) == 0 || totalQty.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please add item");
			return responseHelper.getResponse();
		}

		poordheader.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		poordheader.setXsubmittime(new Date());
		poordheader.setXstatus("Confirmed");
		try {
			poordheaderRepo.save(poordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO12", "Confirm", this.pageTitle, poordheader.getXpornum().toString(), poordheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO12?xpornum=" + xpornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO12/detail-table?xpornum="+xpornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
