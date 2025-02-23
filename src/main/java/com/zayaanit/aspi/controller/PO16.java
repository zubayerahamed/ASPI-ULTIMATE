package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.zayaanit.aspi.entity.Pocrndetail;
import com.zayaanit.aspi.entity.Pocrnheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.PocrndetailPK;
import com.zayaanit.aspi.entity.pk.PocrnheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.PocrndetailRepo;
import com.zayaanit.aspi.repo.PocrnheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 6, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/PO16")
public class PO16 extends KitController {

	@Autowired private PocrnheaderRepo pocrnheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private PocrndetailRepo pocrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "PO16";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "PO16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xcrnnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xcrnnum)) {
				model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
				xlogsdtService.save(new Xlogsdt("PO16", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/PO16/PO16-fragments::main-form";
			}

			Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
			Pocrnheader pocrnheader = null;
			if(op.isPresent()) {
				pocrnheader = op.get();

				if(pocrnheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), pocrnheader.getXbuid()));
					if(cabunitOp.isPresent()) pocrnheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(pocrnheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pocrnheader.getXcus()));
					if(acsubOp.isPresent()) pocrnheader.setSupplierName(acsubOp.get().getXname());
				}

				if(pocrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), pocrnheader.getXwh()));
					if(xwhsOp.isPresent()) pocrnheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(pocrnheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pocrnheader.getXstaff()));
					if(acsubOp.isPresent()) pocrnheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("pocrnheader", pocrnheader != null ? pocrnheader : Pocrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("PO16", "View", this.pageTitle, pocrnheader.getXcrnnum().toString(), pocrnheader.toString(), false, 0));
			return "pages/PO16/PO16-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
		return "pages/PO16/PO16";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xcrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xcrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("PO16", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/PO16/PO16-fragments::detail-table";
		}

		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
		if(!oph.isPresent()) {
			model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
			return "pages/PO16/PO16-fragments::detail-table";
		}
		model.addAttribute("pocrnheader", oph.get());

		List<Pocrndetail> detailList = pocrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum));
		for(Pocrndetail detail : detailList) {
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
			Pocrndetail pocrndetail = Pocrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
			if(caitem != null) {
				pocrndetail.setXitem(xitem);
				pocrndetail.setItemName(caitem.getXdesc());
				pocrndetail.setXunit(caitem.getXunit());
				pocrndetail.setXrate(BigDecimal.ZERO);
				//pocrndetail.setXrate(caitem.getXcost());
				pocrndetail.setXlineamt(pocrndetail.getXqty().multiply(pocrndetail.getXrate()));
			}

			model.addAttribute("pocrndetail", pocrndetail);
			xlogsdtService.save(new Xlogsdt("PO16", "Clear", this.pageTitle, pocrndetail.getXrow().toString(), pocrndetail.toString(), true, 0));
			return "pages/PO16/PO16-fragments::detail-table";
		}

		Optional<Pocrndetail> pocrndetailOp = pocrndetailRepo.findById(new PocrndetailPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum), Integer.parseInt(xrow)));
		Pocrndetail pocrndetail = pocrndetailOp.isPresent() ? pocrndetailOp.get() : Pocrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
		if(pocrndetail != null && pocrndetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pocrndetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && pocrndetail != null) {
			pocrndetail.setXitem(caitem.getXitem());
			pocrndetail.setItemName(caitem.getXdesc());
			pocrndetail.setXunit(caitem.getXunit());
			if(pocrndetail.getXrow() == 0) {
				//pocrndetail.setXrate(caitem.getXcost());
				pocrndetail.setXrate(BigDecimal.ZERO);
				pocrndetail.setXlineamt(pocrndetail.getXqty().multiply(pocrndetail.getXrate()));
			}
		}

		model.addAttribute("pocrndetail", pocrndetail);
		xlogsdtService.save(new Xlogsdt("PO16", "View", this.pageTitle, pocrndetail.getXrow().toString(), pocrndetail.toString(), true, 0));
		return "pages/PO16/PO16-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Pocrnheader pocrnheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validatePocrnheader(pocrnheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(pocrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		pocrnheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(pocrnheader.getSubmitFor())) {
			pocrnheader.setXtotamt(BigDecimal.ZERO);
			pocrnheader.setXstatus("Open");
			pocrnheader.setXstatusim("Open");
			pocrnheader.setXstatusjv("Open");
			pocrnheader.setXtype("Direct Return");
			pocrnheader.setXcrnnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "PO16"));
			pocrnheader.setZid(sessionManager.getBusinessId());
			try {
				pocrnheader = pocrnheaderRepo.save(pocrnheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO16", "Add", this.pageTitle, pocrnheader.getXcrnnum().toString(), pocrnheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + pocrnheader.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum="+ pocrnheader.getXcrnnum() +"&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Return created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), pocrnheader.getXcrnnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Pocrnheader existObj = op.get();
		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xcrnnum", 
			"xgrnnum",
			"xtotamt",
			"xtotcost",
			"xstatus", 
			"xstatusim", 
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
			"xtype"
		};
		BeanUtils.copyProperties(pocrnheader, existObj, ignoreProperties);

		// Calculate total amount
		BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXcrnnum());
		existObj.setXtotamt(xtotamt);

		try {
			existObj = pocrnheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Update", this.pageTitle, existObj.getXcrnnum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum="+ pocrnheader.getXcrnnum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Return updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Pocrndetail pocrndetail, BindingResult bindingResult){
		if(pocrndetail.getXcrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("Return header data not found");
			return responseHelper.getResponse();
		}

		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), pocrndetail.getXcrnnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Return header data not found");
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnheader = oph.get();
		if(!"Open".equals(pocrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(pocrndetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pocrndetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(pocrndetail.getXrate() == null || pocrndetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(pocrndetail.getXqty() == null || pocrndetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		pocrndetail.setXlineamt(pocrndetail.getXrate().multiply(pocrndetail.getXqty()));

		// Create new
		if(SubmitFor.INSERT.equals(pocrndetail.getSubmitFor())) {
			pocrndetail.setXdocrow(0);
			pocrndetail.setXrow(pocrndetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), pocrndetail.getXcrnnum()));
			pocrndetail.setXqtygrn(BigDecimal.ZERO);
			pocrndetail.setXrategrn(BigDecimal.ZERO);
			pocrndetail.setZid(sessionManager.getBusinessId());
			try {
				pocrndetail = pocrndetailRepo.save(pocrndetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO16", "Add", this.pageTitle, pocrndetail.getXrow().toString(), pocrndetail.toString(), true, 0));

			BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pocrndetail.getXcrnnum());
			pocrnheader.setXtotamt(xtotamt);
			try {
				pocrnheaderRepo.save(pocrnheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO16", "Update", this.pageTitle, pocrnheader.getXcrnnum().toString(), pocrnheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + pocrndetail.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum=" + pocrndetail.getXcrnnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Pocrndetail> pocrndetailOp = pocrndetailRepo.findById(new PocrndetailPK(sessionManager.getBusinessId(), pocrndetail.getXcrnnum(), pocrndetail.getXrow()));
		if(!pocrndetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Pocrndetail existObj = pocrndetailOp.get();
		existObj.setXqty(pocrndetail.getXqty());
		existObj.setXrate(pocrndetail.getXrate());
		existObj.setXlineamt(pocrndetail.getXqty().multiply(pocrndetail.getXrate()));
		existObj.setXnote(pocrndetail.getXnote());
		try {
			existObj = pocrndetailRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Update", this.pageTitle, existObj.getXrow().toString(), existObj.toString(), true, 0));

		BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pocrndetail.getXcrnnum());
		pocrnheader.setXtotamt(xtotamt);
		try {
			pocrnheaderRepo.save(pocrnheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Update", this.pageTitle, pocrnheader.getXcrnnum().toString(), pocrnheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + pocrndetail.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum=" + pocrndetail.getXcrnnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcrnnum){
		Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			pocrndetailRepo.deleteAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Delete", this.pageTitle, xcrnnum.toString(), null, true, 0).setMessage("Delete all details"));

		Pocrnheader obj = op.get();
		Pocrnheader copy = SerializationUtils.clone(obj);
		try {
			pocrnheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Delete", this.pageTitle, copy.getXcrnnum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xcrnnum, @RequestParam Integer xrow){
		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Return header data not found");
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnheader = oph.get();

		if(!"Open".equals(pocrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Pocrndetail> op = pocrndetailRepo.findById(new PocrndetailPK(sessionManager.getBusinessId(), xcrnnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Pocrndetail obj = op.get();
		Pocrndetail copy = SerializationUtils.clone(obj);
		try {
			pocrndetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Delete", this.pageTitle, copy.getXrow().toString(), copy.toString(), true, 0));

		// Update line amount and total amount of header
		BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pocrnheader.getXcrnnum());
		pocrnheader.setXtotamt(xtotamt);
		try {
			pocrnheaderRepo.save(pocrnheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Update", this.pageTitle, pocrnheader.getXcrnnum().toString(), pocrnheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xcrnnum) {
		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnheader = oph.get();

		if(!"Open".equals(pocrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(pocrnheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		// check screen data validation
		if(pocrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// quantity validation
		List<Pocrndetail> details = pocrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(pocrnheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Pocrndetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Pocrndetail item : details) {
			Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), item.getXitem()));
			if(!caitemOp.isPresent()) continue;
			if("Services".equals(caitemOp.get().getXgitem())) continue;

			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, pocrnheader.getXbuid(), pocrnheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/PO16/error-details");
			return responseHelper.getResponse();
		}

		// Call the procedure
		try {
			pocrnheaderRepo.PO_ConfirmReturn(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO16", "Confirm", this.pageTitle, xcrnnum.toString(), "PO_ConfirmReturn("+ sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xcrnnum +")" , false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO16?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO16/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
