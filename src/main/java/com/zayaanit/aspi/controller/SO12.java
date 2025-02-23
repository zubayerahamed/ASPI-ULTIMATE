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
import com.zayaanit.aspi.entity.Oporddetail;
import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.OporddetailPK;
import com.zayaanit.aspi.entity.pk.OpordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.OporddetailRepo;
import com.zayaanit.aspi.repo.OpordheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2025
 */
@Controller
@RequestMapping("/SO12")
public class SO12 extends KitController {

	@Autowired private OpordheaderRepo opordheaderRepo;
	@Autowired private OporddetailRepo oporddetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xordernum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xordernum)) {
				model.addAttribute("opordheader", Opordheader.getDefaultInstance());
				xlogsdtService.save(new Xlogsdt("SO12", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/SO12/SO12-fragments::main-form";
			}

			Optional<Opordheader> op = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xordernum)));
			Opordheader opordheader = null;
			if(op.isPresent()) {
				opordheader = op.get();

				if(opordheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), opordheader.getXbuid()));
					if(cabunitOp.isPresent()) opordheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(opordheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opordheader.getXcus()));
					if(acsubOp.isPresent()) opordheader.setCustomerName(acsubOp.get().getXname());
				}

				if(opordheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opordheader.getXwh()));
					if(xwhsOp.isPresent()) opordheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(opordheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opordheader.getXstaff()));
					if(acsubOp.isPresent()) opordheader.setStaffName(acsubOp.get().getXname());
				}

				if(opordheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opordheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) opordheader.setSubmitStaffName(acsubOp.get().getXname());
				}

				if(opordheader.getXstaffappr() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opordheader.getXstaffappr()));
					if(acsubOp.isPresent()) opordheader.setApprStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("opordheader", opordheader != null ? opordheader : Opordheader.getDefaultInstance());

			xlogsdtService.save(new Xlogsdt("SO12", "View", this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));
			return "pages/SO12/SO12-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("opordheader", Opordheader.getDefaultInstance());
		return "pages/SO12/SO12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xordernum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xordernum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opordheader", Opordheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("SO12", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/SO12/SO12-fragments::detail-table";
		}

		Optional<Opordheader> oph = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xordernum)));
		if(!oph.isPresent()) {
			model.addAttribute("opordheader", Opordheader.getDefaultInstance());
			return "pages/SO12/SO12-fragments::detail-table";
		}
		model.addAttribute("opordheader", oph.get());

		List<Oporddetail> detailList = oporddetailRepo.findAllByZidAndXordernum(sessionManager.getBusinessId(), Integer.parseInt(xordernum));
		for(Oporddetail detail : detailList) {
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
			Oporddetail oporddetail = Oporddetail.getDefaultInstance(Integer.parseInt(xordernum));
			if(caitem != null) {
				oporddetail.setXitem(xitem);
				oporddetail.setItemName(caitem.getXdesc());
				oporddetail.setXunit(caitem.getXunit());
				oporddetail.setXrate(caitem.getXrate());
				oporddetail.setXlineamt(oporddetail.getXqty().multiply(oporddetail.getXrate()));
			}

			model.addAttribute("oporddetail", oporddetail);
			xlogsdtService.save(new Xlogsdt("SO12", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/SO12/SO12-fragments::detail-table";
		}

		Optional<Oporddetail> oporddetailOp = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), Integer.parseInt(xordernum), Integer.parseInt(xrow)));
		Oporddetail oporddetail = oporddetailOp.isPresent() ? oporddetailOp.get() : Oporddetail.getDefaultInstance(Integer.parseInt(xordernum));
		if(oporddetail != null && oporddetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), oporddetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && oporddetail != null) {
			oporddetail.setXitem(caitem.getXitem());
			oporddetail.setItemName(caitem.getXdesc());
			oporddetail.setXunit(caitem.getXunit());
			if(oporddetail.getXrow() == 0) {
				oporddetail.setXrate(caitem.getXrate());
				oporddetail.setXlineamt(oporddetail.getXqty().multiply(oporddetail.getXrate()));
			}
		}

		model.addAttribute("oporddetail", oporddetail);
		xlogsdtService.save(new Xlogsdt("SO12", "View", this.pageTitle, oporddetail.getXrow().toString(), oporddetail.toString(), true, 0));
		return "pages/SO12/SO12-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opordheader opordheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateOpordheader(opordheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(opordheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		opordheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(opordheader.getSubmitFor())) {
			opordheader.setXlineamt(BigDecimal.ZERO);
			opordheader.setXdiscamt(BigDecimal.ZERO);
			opordheader.setXtotamt(BigDecimal.ZERO);
			opordheader.setXstatus("Open");
			opordheader.setXstatusord("Open");
			opordheader.setXordernum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO12"));
			opordheader.setZid(sessionManager.getBusinessId());
			try {
				opordheader = opordheaderRepo.save(opordheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO12", "Add", this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=" + opordheader.getXordernum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum="+ opordheader.getXordernum() +"&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Sales order created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opordheader> op = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), opordheader.getXordernum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Opordheader existObj = op.get();

		if(opordheader.getXdiscamt() == null) opordheader.setXdiscamt(BigDecimal.ZERO);

		if(opordheader.getXdiscamt().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount.");
			return responseHelper.getResponse();
		}

		if(opordheader.getXdiscamt().compareTo(existObj.getXlineamt()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount. Discount amount must be less or equal to Sub Total");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xordernum", 
			"xlineamt",
			"xtotamt",
			"xdornum",
			"xstatus", 
			"xstatusord",
			"xstaffsubmit", 
			"xsubmittime", 
			"xstaffappr", 
			"xapprovertime"
		};
		BeanUtils.copyProperties(opordheader, existObj, ignoreProperties);

		// Calculate line and total amount
		BigDecimal lineAmt = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXordernum());
		existObj.setXlineamt(lineAmt);
		existObj.setXtotamt(existObj.getXlineamt().subtract(existObj.getXdiscamt()));
		try {
			existObj = opordheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Update", this.pageTitle, existObj.getXordernum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=" + existObj.getXordernum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum="+ opordheader.getXordernum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Sales order updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Oporddetail oporddetail, BindingResult bindingResult){
		if(oporddetail.getXordernum() == null) {
			responseHelper.setErrorStatusAndMessage("Purchase order not found");
			return responseHelper.getResponse();
		}

		Optional<Opordheader> oph = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), oporddetail.getXordernum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Purchase order not found");
			return responseHelper.getResponse();
		}

		Opordheader opordheader = oph.get();
		if(!"Open".equals(opordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Purchase order not open");
			return responseHelper.getResponse();
		}

		if(oporddetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), oporddetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(oporddetail.getXrate() == null || oporddetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(oporddetail.getXqty() == null || oporddetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		oporddetail.setXlineamt(oporddetail.getXrate().multiply(oporddetail.getXqty()));

		// Create new
		if(SubmitFor.INSERT.equals(oporddetail.getSubmitFor())) {
			oporddetail.setXrow(oporddetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), oporddetail.getXordernum()));
			oporddetail.setZid(sessionManager.getBusinessId());
			oporddetail.setXqtydel(BigDecimal.ZERO);
			try {
				oporddetail = oporddetailRepo.save(oporddetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			BigDecimal lineAmt = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), oporddetail.getXordernum());
			opordheader.setXlineamt(lineAmt);
			opordheader.setXtotamt(opordheader.getXlineamt().subtract(opordheader.getXdiscamt()));
			try {
				opordheaderRepo.save(opordheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO12", "Add", this.pageTitle, oporddetail.getXrow().toString(), oporddetail.toString(), true, 0));
			xlogsdtService.save(new Xlogsdt("SO12", "Update", this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=" + oporddetail.getXordernum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum=" + oporddetail.getXordernum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Order detail added successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not applicatble here");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xordernum){
		Optional<Opordheader> op = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), xordernum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			oporddetailRepo.deleteAllByZidAndXordernum(sessionManager.getBusinessId(), xordernum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Delete", this.pageTitle, xordernum.toString(), null, true, 0).setMessage("Delete all details"));

		Opordheader obj = op.get();
		Opordheader copy = SerializationUtils.clone(obj);
		try {
			opordheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Delete", this.pageTitle, copy.getXordernum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xordernum, @RequestParam Integer xrow) throws Exception{
		Optional<Opordheader> oph = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), xordernum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Sales order not found");
			return responseHelper.getResponse();
		}

		Opordheader opordheader = oph.get();

		if(!"Open".equals(opordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Order status not open");
			return responseHelper.getResponse();
		}

		Optional<Oporddetail> op = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), xordernum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Oporddetail obj = op.get();
		Oporddetail copy = SerializationUtils.clone(obj);
		try {
			oporddetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Delete", this.pageTitle, copy.getXrow().toString(), copy.toString(), true, 0));

		// Update line amount and total amount of header
		BigDecimal lineAmt = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opordheader.getXordernum());
		if(opordheader.getXdiscamt().compareTo(lineAmt) == 1) {
			throw new IllegalStateException("Can't delete this item. After delete this item, discount amount will be greater than Subtotal amount. You should update discount amount first");
		}
		opordheader.setXlineamt(lineAmt);
		opordheader.setXtotamt(opordheader.getXlineamt().subtract(opordheader.getXdiscamt()));
		try {
			opordheaderRepo.save(opordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Update", this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=" + xordernum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum="+xordernum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xordernum) {
		Optional<Opordheader> oph = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), xordernum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found");
			return responseHelper.getResponse();
		}

		Opordheader opordheader = oph.get();

		if(!"Open".equals(opordheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Order status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(opordheader.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Sales order Order status not open");
			return responseHelper.getResponse();
		}

		// Check screen data
		if(opordheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		// validate quantity
		BigDecimal totalQty = oporddetailRepo.getTotalQty(sessionManager.getBusinessId(), xordernum);
		if(totalQty.compareTo(BigDecimal.ZERO) == 0 || totalQty.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please add item");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		opordheader.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		opordheader.setXsubmittime(new Date());
		opordheader.setXstatus("Confirmed");
		try {
			opordheaderRepo.save(opordheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO12", "Confirm", this.pageTitle, opordheader.getXordernum().toString(), opordheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO12?xordernum=" + xordernum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO12/detail-table?xordernum="+xordernum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
