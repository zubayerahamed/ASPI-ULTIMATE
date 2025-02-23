package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.SerializationUtils;
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
import com.zayaanit.aspi.entity.Opcrndetail;
import com.zayaanit.aspi.entity.Opcrnheader;
import com.zayaanit.aspi.entity.Opdodetail;
import com.zayaanit.aspi.entity.Opdoheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.OpcrndetailPK;
import com.zayaanit.aspi.entity.pk.OpcrnheaderPK;
import com.zayaanit.aspi.entity.pk.OpdodetailPK;
import com.zayaanit.aspi.entity.pk.OpdoheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.OpcrndetailRepo;
import com.zayaanit.aspi.repo.OpcrnheaderRepo;
import com.zayaanit.aspi.repo.OpdodetailRepo;
import com.zayaanit.aspi.repo.OpdoheaderRepo;
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
@RequestMapping("/SO17")
public class SO17 extends KitController {

	@Autowired private OpcrnheaderRepo opcrnheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private OpcrndetailRepo opcrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xcrnnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xcrnnum)) {
				model.addAttribute("opcrnheader", Opcrnheader.getDefaultInstance());
				xlogsdtService.save(new Xlogsdt("SO17", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/SO17/SO17-fragments::main-form";
			}

			Optional<Opcrnheader> op = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
			Opcrnheader opcrnheader = null;
			if(op.isPresent()) {
				opcrnheader = op.get();

				if(opcrnheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), opcrnheader.getXbuid()));
					if(cabunitOp.isPresent()) opcrnheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(opcrnheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opcrnheader.getXcus()));
					if(acsubOp.isPresent()) opcrnheader.setCustomerName(acsubOp.get().getXname());
				}

				if(opcrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opcrnheader.getXwh()));
					if(xwhsOp.isPresent()) opcrnheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(opcrnheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opcrnheader.getXstaff()));
					if(acsubOp.isPresent()) opcrnheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("opcrnheader", opcrnheader != null ? opcrnheader : Opcrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("SO17", "View", this.pageTitle, opcrnheader.getXcrnnum().toString(), opcrnheader.toString(), false, 0));
			return "pages/SO17/SO17-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("opcrnheader", Opcrnheader.getDefaultInstance());
		return "pages/SO17/SO17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xcrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xcrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opcrnheader", Opcrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("SO17", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/SO17/SO17-fragments::detail-table";
		}

		Optional<Opcrnheader> oph = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
		if(!oph.isPresent()) {
			model.addAttribute("opcrnheader", Opcrnheader.getDefaultInstance());
			return "pages/SO17/SO17-fragments::detail-table";
		}
		model.addAttribute("opcrnheader", oph.get());

		List<Opcrndetail> detailList = opcrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum));
		for(Opcrndetail detail : detailList) {
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
			Opcrndetail opcrndetail = Opcrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
			if(caitem != null) {
				opcrndetail.setXitem(xitem);
				opcrndetail.setItemName(caitem.getXdesc());
				opcrndetail.setXunit(caitem.getXunit());
				opcrndetail.setXrate(BigDecimal.ZERO);
				//opcrndetail.setXrate(caitem.getXcost());
				opcrndetail.setXlineamt(opcrndetail.getXqty().multiply(opcrndetail.getXrate()));
			}

			model.addAttribute("opcrndetail", opcrndetail);
			xlogsdtService.save(new Xlogsdt("SO17", "Clear", this.pageTitle, opcrndetail.getXrow().toString(), opcrndetail.toString(), true, 0));
			return "pages/SO17/SO17-fragments::detail-table";
		}

		Optional<Opcrndetail> opcrndetailOp = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum), Integer.parseInt(xrow)));
		Opcrndetail opcrndetail = opcrndetailOp.isPresent() ? opcrndetailOp.get() : Opcrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
		if(opcrndetail != null && opcrndetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), opcrndetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && opcrndetail != null) {
			opcrndetail.setXitem(caitem.getXitem());
			opcrndetail.setItemName(caitem.getXdesc());
			opcrndetail.setXunit(caitem.getXunit());
			if(opcrndetail.getXrow() == 0) {
				//opcrndetail.setXrate(caitem.getXcost());
				opcrndetail.setXrate(BigDecimal.ZERO);
				opcrndetail.setXlineamt(opcrndetail.getXqty().multiply(opcrndetail.getXrate()));
			}
		}

		model.addAttribute("opcrndetail", opcrndetail);
		xlogsdtService.save(new Xlogsdt("SO17", "View", this.pageTitle, opcrndetail.getXrow().toString(), opcrndetail.toString(), true, 0));
		return "pages/SO17/SO17-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opcrnheader opcrnheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateOpcrnheader(opcrnheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(opcrnheader.getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("Invoice No. required");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opcrnheader.getXdornum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid Invoice number");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = opdoheaderOp.get();

		// check pending return
		if(opcrnheader.getXcrnnum() == null) {
			if(opcrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), opdoheader.getXdornum()) > 0) {
				responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
				return responseHelper.getResponse();
			}

			if(opdoheaderRepo.isInvalidSalesNumber(sessionManager.getBusinessId(), opdoheader.getXdornum()) == 0) {
				responseHelper.setErrorStatusAndMessage("Invalid Sales Number");
				return responseHelper.getResponse();
			}
		} else {
			if(opcrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), opdoheader.getXdornum(), opcrnheader.getXcrnnum()) > 0) {
				responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
				return responseHelper.getResponse();
			}
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		opcrnheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(opcrnheader.getSubmitFor())) {
			Integer xcrnnum = xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO16");
			try {
				opcrnheaderRepo.SO_CreateReturnfromInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, opdoheader.getXdornum());
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO17", "Add", this.pageTitle, xcrnnum.toString(), "SO_CreateReturnfromInvoice(" + sessionManager.getBusinessId() + "," + sessionManager.getLoggedInUserDetails().getUsername() + "," + xcrnnum + "," + opdoheader.getXdornum() + ")", false, 0));

			Optional<Opcrnheader> opcrnheaderOp = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
			if(!opcrnheaderOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Header data not found with Return No. " + xcrnnum);
				return responseHelper.getResponse();
			}

			Opcrnheader exist = opcrnheaderOp.get();

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO17?xcrnnum=" + exist.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xcrnnum="+ exist.getXcrnnum() +"&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Return created successfully");
			return responseHelper.getResponse();
		}


		if(opcrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdate().before(opdoheader.getXdate())) {
			responseHelper.setErrorStatusAndMessage("Date can't be before Invoice date");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opcrnheader> op = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), opcrnheader.getXcrnnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Opcrnheader existObj = op.get();

		if(opcrnheader.getXdiscamt() == null) opcrnheader.setXdiscamt(BigDecimal.ZERO);

		if(opcrnheader.getXdiscamt().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount.");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscamt().compareTo(existObj.getXlineamt()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount. Discount amount must be less or equal to Sub Total");
			return responseHelper.getResponse();
		}

		existObj.setXdate(opcrnheader.getXdate());
		existObj.setXwh(opcrnheader.getXwh());
		existObj.setXref(opcrnheader.getXref());
		existObj.setXnote(opcrnheader.getXnote());
		existObj.setXstaff(opcrnheader.getXstaff());
		existObj.setXdiscamt(opcrnheader.getXdiscamt());

		// Calculate total amount
		BigDecimal lineAmt = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXcrnnum());
		existObj.setXlineamt(lineAmt);
		existObj.setXtotamt(existObj.getXlineamt().subtract(existObj.getXdiscamt()));
		try {
			existObj = opcrnheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Update", this.pageTitle, existObj.getXcrnnum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xcrnnum="+ existObj.getXcrnnum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Return updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opcrndetail opcrndetail, BindingResult bindingResult){
		if(opcrndetail.getXcrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("Return header not found");
			return responseHelper.getResponse();
		}

		Optional<Opcrnheader> oph = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), opcrndetail.getXcrnnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Return header not found");
			return responseHelper.getResponse();
		}

		Opcrnheader opcrnheader = oph.get();
		if(!"Open".equals(opcrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(opcrndetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), opcrndetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(opcrndetail.getXqty() == null || opcrndetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(opcrndetail.getSubmitFor())) {
			responseHelper.setSuccessStatusAndMessage("Insert is not applicable");
			return responseHelper.getResponse();
		}

		Optional<Opcrndetail> opcrndetailOp = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), opcrndetail.getXcrnnum(), opcrndetail.getXrow()));
		if(!opcrndetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Opcrndetail existObj = opcrndetailOp.get();

		if(opcrndetail.getXqty().compareTo(existObj.getXqtyinv()) == 1) {
			responseHelper.setErrorStatusAndMessage("Return quantity can't be greater than Eligible quantity");
			return responseHelper.getResponse();
		}

		opcrndetail.setXlineamt(existObj.getXrate().multiply(opcrndetail.getXqty()));

		// Do some cross check work here first
		Optional<Opdodetail> pogrndetailOp = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), opcrnheader.getXdornum(), existObj.getXdocrow()));
		if(!pogrndetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("GRN detail not found for this detail row");
			return responseHelper.getResponse();
		}
		Opdodetail pogrndetail = pogrndetailOp.get();

		BigDecimal difference = opcrndetail.getXqty().subtract(existObj.getXqty());  // 5
		if(difference.compareTo(BigDecimal.ZERO) == 0) {
			// Do nothing
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1 && difference.add(pogrndetail.getXqtycrn()).compareTo(pogrndetail.getXqty()) == 1) {
			responseHelper.setErrorStatusAndMessage("Return quantity should not more than Invoice quantity!");
			return responseHelper.getResponse();
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1) {
			pogrndetail.setXqtycrn(pogrndetail.getXqtycrn().add(opcrndetail.getXqty().subtract(existObj.getXqty())));
			opdodetailRepo.save(pogrndetail);
		}

		if(difference.compareTo(BigDecimal.ZERO) == -1) {
			pogrndetail.setXqtycrn(pogrndetail.getXqtycrn().subtract(existObj.getXqty().subtract(opcrndetail.getXqty())));
			opdodetailRepo.save(pogrndetail);
		}

		existObj.setXqty(opcrndetail.getXqty());
		existObj.setXlineamt(opcrndetail.getXlineamt());
		existObj.setXnote(opcrndetail.getXnote());
		try {
			existObj = opcrndetailRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Update", this.pageTitle, existObj.getXrow().toString(), existObj.toString(), true, 0));

		BigDecimal xtotamt = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opcrndetail.getXcrnnum());
		opcrnheader.setXtotamt(xtotamt);
		try {
			opcrnheaderRepo.save(opcrnheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Update", this.pageTitle, opcrnheader.getXcrnnum().toString(), opcrnheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xcrnnum=" + opcrndetail.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xcrnnum=" + opcrndetail.getXcrnnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcrnnum){
		Optional<Opcrnheader> op = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(opcrndetailRepo.getTotalQty(sessionManager.getBusinessId(), xcrnnum).compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Please delete detail record first! or make quantity 0");
			return responseHelper.getResponse();
		}

		try {
			opcrndetailRepo.deleteAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Delete", this.pageTitle, xcrnnum.toString(), null, true, 0).setMessage("Delete all details"));

		Opcrnheader obj = op.get();
		Opcrnheader copy = SerializationUtils.clone(obj);
		try {
			opcrnheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Delete", this.pageTitle, copy.getXcrnnum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xcrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xcrnnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xcrnnum) {
		Optional<Opcrnheader> oph = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Opcrnheader opcrnheader = oph.get();

		if(!"Open".equals(opcrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(opcrnheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Opcrndetail> details = opcrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add item");
			return responseHelper.getResponse();
		}

		// screen data validation
		if(opcrnheader.getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("GRN No. required");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opcrnheader.getXdornum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid Invoice number");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = opdoheaderOp.get();

		// check pending return
		if(opcrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), opdoheader.getXdornum(), xcrnnum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdate().before(opdoheader.getXdate())) {
			responseHelper.setErrorStatusAndMessage("Date can't be before Invoice date");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscamt().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscamt().compareTo(opcrnheader.getXlineamt()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount");
			return responseHelper.getResponse();
		}

		// Date validation
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(opcrnheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Opcrndetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// Call the procedure
		try {
			opcrnheaderRepo.SO_ConfirmReturn(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO17", "Confirm", this.pageTitle, xcrnnum.toString(), "SO_ConfirmReturn("+ sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xcrnnum +")" , false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
