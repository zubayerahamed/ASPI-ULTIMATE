package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.zayaanit.aspi.entity.Pogrndetail;
import com.zayaanit.aspi.entity.Pogrnheader;
import com.zayaanit.aspi.entity.Poorddetail;
import com.zayaanit.aspi.entity.Poordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.PogrndetailPK;
import com.zayaanit.aspi.entity.pk.PogrnheaderPK;
import com.zayaanit.aspi.entity.pk.PoorddetailPK;
import com.zayaanit.aspi.entity.pk.PoordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.PogrndetailRepo;
import com.zayaanit.aspi.repo.PogrnheaderRepo;
import com.zayaanit.aspi.repo.PoorddetailRepo;
import com.zayaanit.aspi.repo.PoordheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/PO15")
public class PO15 extends KitController {

	@Autowired private PogrnheaderRepo pogrnheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private PogrndetailRepo pogrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private PoorddetailRepo poorddetailRepo;
	@Autowired private PoordheaderRepo poordheaderRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "PO15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "PO15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xgrnnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("voucherTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xgrnnum)) {
				model.addAttribute("pogrnheader", Pogrnheader.getDefaultInstance());
				xlogsdtService.save(new Xlogsdt("PO15", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/PO15/PO15-fragments::main-form";
			}

			Optional<Pogrnheader> op = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum)));
			Pogrnheader pogrnheader = null;
			if(op.isPresent()) {
				pogrnheader = op.get();

				if(pogrnheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), pogrnheader.getXbuid()));
					if(cabunitOp.isPresent()) pogrnheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(pogrnheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pogrnheader.getXcus()));
					if(acsubOp.isPresent()) pogrnheader.setSupplierName(acsubOp.get().getXname());
				}

				if(pogrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), pogrnheader.getXwh()));
					if(xwhsOp.isPresent()) pogrnheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(pogrnheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pogrnheader.getXstaff()));
					if(acsubOp.isPresent()) pogrnheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("pogrnheader", pogrnheader != null ? pogrnheader : Pogrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("PO15", "View", this.pageTitle, pogrnheader.getXgrnnum().toString(), pogrnheader.toString(), false, 0));
			return "pages/PO15/PO15-fragments::main-form";
		}

		if(frommenu == null) return "blank";

		if(isAjaxRequest(request) && StringUtils.isNotBlank(xgrnnum) && !"RESET".equalsIgnoreCase(xgrnnum)) {
			Optional<Pogrnheader> op = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum)));
			Pogrnheader pogrnheader = null;
			if(op.isPresent()) {
				pogrnheader = op.get();

				if(pogrnheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), pogrnheader.getXbuid()));
					if(cabunitOp.isPresent()) pogrnheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(pogrnheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pogrnheader.getXcus()));
					if(acsubOp.isPresent()) pogrnheader.setSupplierName(acsubOp.get().getXname());
				}

				if(pogrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), pogrnheader.getXwh()));
					if(xwhsOp.isPresent()) pogrnheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(pogrnheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pogrnheader.getXstaff()));
					if(acsubOp.isPresent()) pogrnheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("pogrnheader", pogrnheader != null ? pogrnheader : Pogrnheader.getDefaultInstance());

			// Details
			List<Pogrndetail> detailList = pogrndetailRepo.findAllByZidAndXgrnnum(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum));
			for(Pogrndetail detail : detailList) {
				Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), detail.getXitem()));
				if(caitemOp.isPresent()) {
					detail.setItemName(caitemOp.get().getXdesc());
					detail.setXunit(caitemOp.get().getXunit());
				}
			}
			model.addAttribute("detailList", detailList);

			model.addAttribute("pogrndetail", Pogrndetail.getDefaultInstance(Integer.valueOf(xgrnnum)));

			xlogsdtService.save(new Xlogsdt("PO15", "View", this.pageTitle, pogrnheader.getXgrnnum().toString(), pogrnheader.toString(), false, 0).setSource("PO13").setMessage("Redirected from PO13 to PO15"));
			return "pages/PO15/PO15";
		}

		model.addAttribute("pogrnheader", Pogrnheader.getDefaultInstance());
		return "pages/PO15/PO15";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xgrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xgrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("pogrnheader", Pogrnheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("PO15", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/PO15/PO15-fragments::detail-table";
		}

		Optional<Pogrnheader> oph = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum)));
		if(!oph.isPresent()) {
			model.addAttribute("pogrnheader", Pogrnheader.getDefaultInstance());
			return "pages/PO15/PO15-fragments::detail-table";
		}
		model.addAttribute("pogrnheader", oph.get());

		List<Pogrndetail> detailList = pogrndetailRepo.findAllByZidAndXgrnnum(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum));
		for(Pogrndetail detail : detailList) {
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
			Pogrndetail pogrndetail = Pogrndetail.getDefaultInstance(Integer.parseInt(xgrnnum));
			if(caitem != null) {
				pogrndetail.setXitem(xitem);
				pogrndetail.setItemName(caitem.getXdesc());
				pogrndetail.setXunit(caitem.getXunit());
				pogrndetail.setXrate(caitem.getXcost());
				pogrndetail.setXlineamt(pogrndetail.getXqty().multiply(pogrndetail.getXrate()));
			}

			model.addAttribute("pogrndetail", pogrndetail);
			xlogsdtService.save(new Xlogsdt("PO15", "Clear", this.pageTitle, pogrndetail.getXrow().toString(), pogrndetail.toString(), true, 0));
			return "pages/PO15/PO15-fragments::detail-table";
		}

		Optional<Pogrndetail> pogrndetailOp = pogrndetailRepo.findById(new PogrndetailPK(sessionManager.getBusinessId(), Integer.parseInt(xgrnnum), Integer.parseInt(xrow)));
		Pogrndetail pogrndetail = pogrndetailOp.isPresent() ? pogrndetailOp.get() : Pogrndetail.getDefaultInstance(Integer.parseInt(xgrnnum));
		if(pogrndetail != null && pogrndetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pogrndetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && pogrndetail != null) {
			pogrndetail.setXitem(caitem.getXitem());
			pogrndetail.setItemName(caitem.getXdesc());
			pogrndetail.setXunit(caitem.getXunit());
			if(pogrndetail.getXrow() == 0) {
				pogrndetail.setXrate(caitem.getXcost());
				pogrndetail.setXlineamt(pogrndetail.getXqty().multiply(pogrndetail.getXrate()));
			}
		}

		model.addAttribute("pogrndetail", pogrndetail);
		xlogsdtService.save(new Xlogsdt("PO15", "View", this.pageTitle, pogrndetail.getXrow().toString(), pogrndetail.toString(), true, 0));
		return "pages/PO15/PO15-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Pogrnheader pogrnheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validatePogrnheader(pogrnheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(pogrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pogrnheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(pogrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(pogrnheader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Supplier Bill No. required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(pogrnheader.getSubmitFor())) {
			responseHelper.setErrorStatusAndMessage("Insert is not acceptable for this functional screen");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Pogrnheader> op = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), pogrnheader.getXgrnnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Pogrnheader existObj = op.get();
		existObj.setXtotamt(pogrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXgrnnum()));
		existObj.setXdate(pogrnheader.getXdate());
		existObj.setXwh(pogrnheader.getXwh());
		existObj.setXref(pogrnheader.getXref());
		existObj.setXinvnum(pogrnheader.getXinvnum());
		existObj.setXnote(pogrnheader.getXnote());
		try {
			existObj = pogrnheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, existObj.getXgrnnum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=" + existObj.getXgrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum="+ pogrnheader.getXgrnnum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Pogrndetail pogrndetail, BindingResult bindingResult){
		if(pogrndetail.getXgrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("Header data not found");
			return responseHelper.getResponse();
		}

		Optional<Pogrnheader> oph = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), pogrndetail.getXgrnnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header data not found");
			return responseHelper.getResponse();
		}

		Pogrnheader pogrnheader = oph.get();
		if(!"Open".equals(pogrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(pogrndetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pogrndetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(pogrndetail.getXrate() == null || pogrndetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(pogrndetail.getXqty() == null || pogrndetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		pogrndetail.setXlineamt(pogrndetail.getXrate().multiply(pogrndetail.getXqty()));

		// Create new
		if(SubmitFor.INSERT.equals(pogrndetail.getSubmitFor())) {
			pogrndetail.setXdocrow(0);
			pogrndetail.setXrow(pogrndetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), pogrndetail.getXgrnnum()));
			pogrndetail.setXqtyord(BigDecimal.ZERO);
			pogrndetail.setXqtycrn(BigDecimal.ZERO);
			pogrndetail.setZid(sessionManager.getBusinessId());
			try {
				pogrndetail = pogrndetailRepo.save(pogrndetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO15", "Add", this.pageTitle, pogrndetail.getXrow().toString(), pogrndetail.toString(), true, 0));

			BigDecimal xtotamt = pogrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pogrndetail.getXgrnnum());
			pogrnheader.setXtotamt(xtotamt);
			try {
				pogrnheaderRepo.save(pogrnheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, pogrnheader.getXgrnnum().toString(), pogrnheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=" + pogrndetail.getXgrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum=" + pogrndetail.getXgrnnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Pogrndetail> detailOp = pogrndetailRepo.findById(new PogrndetailPK(sessionManager.getBusinessId(), pogrndetail.getXgrnnum(), pogrndetail.getXrow()));
		if(!detailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Pogrndetail existObj = detailOp.get();
		if(existObj.getXdocrow() == 0) {
			responseHelper.setErrorStatusAndMessage("This detail is not for update");
			return responseHelper.getResponse();
		}

		if(pogrndetail.getXqty().compareTo(existObj.getXqtyord()) == 1) {
			responseHelper.setErrorStatusAndMessage("GRN quantity must be less thant order quantity");
			return responseHelper.getResponse();
		}

		// cross checking
		Optional<Poordheader> orderOp = poordheaderRepo.findById(new PoordheaderPK(sessionManager.getBusinessId(), pogrnheader.getXpornum()));
		if(!orderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found related with this GRN");
			return responseHelper.getResponse();
		}
		Poordheader order = orderOp.get();

		Optional<Poorddetail> poorddetailOp = poorddetailRepo.findById(new PoorddetailPK(sessionManager.getBusinessId(), pogrnheader.getXpornum(), existObj.getXdocrow()));
		if(!poorddetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order detail not found for this detail row");
			return responseHelper.getResponse();
		}
		Poorddetail orderdetail = poorddetailOp.get();

		BigDecimal difference = pogrndetail.getXqty().subtract(existObj.getXqty());
		if(difference.compareTo(BigDecimal.ZERO) == 0) {
			// Do nothing
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1 && difference.add(orderdetail.getXqtygrn()).compareTo(orderdetail.getXqty()) == 1) {
			responseHelper.setErrorStatusAndMessage("GRN quantity should not more than Order quantity!");
			return responseHelper.getResponse();
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1) {
			orderdetail.setXqtygrn(orderdetail.getXqtygrn().add(pogrndetail.getXqty().subtract(existObj.getXqty())));
			poorddetailRepo.save(orderdetail);
		}

		if(difference.compareTo(BigDecimal.ZERO) == -1) {
			orderdetail.setXqtygrn(orderdetail.getXqtygrn().subtract(existObj.getXqty().subtract(pogrndetail.getXqty())));
			poorddetailRepo.save(orderdetail);
		}
		// Recheck complete here

		existObj.setXqty(pogrndetail.getXqty());
		existObj.setXrate(pogrndetail.getXrate());
		existObj.setXlineamt(existObj.getXqty().multiply(existObj.getXrate()));
		existObj.setXnote(pogrndetail.getXnote());
		try {
			existObj = pogrndetailRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, existObj.getXrow().toString(), existObj.toString(), true, 0));

		// Update purchase order status here
		List<Poorddetail> poorddetails = poorddetailRepo.findAllByZidAndXpornum(sessionManager.getBusinessId(), pogrnheader.getXpornum());
		BigDecimal tord = poorddetails.stream().map(Poorddetail::getXqty).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal tgrn = poorddetails.stream().map(Poorddetail::getXqtygrn).reduce(BigDecimal.ZERO, BigDecimal::add);

		long count = pogrnheaderRepo.findAllByZidAndXpornum(sessionManager.getBusinessId(), pogrnheader.getXpornum()).stream().count();

		if(tord.compareTo(tgrn) == 0) {
			order.setXstatusord("Full Received");
		} else if (tgrn.compareTo(BigDecimal.ZERO) == 0 && count == 0) {
			order.setXstatusord("Open");
		} else {
			order.setXstatusord("GRN Created");
		}
		try {
			poordheaderRepo.save(order);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, order.getXpornum().toString(), order.toString(), false, 0).setMessage("Update purchase order for GRN " + pogrnheader.getXgrnnum()));

		BigDecimal xtotamt = pogrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXgrnnum());
		pogrnheader.setXtotamt(xtotamt);
		try {
			pogrnheaderRepo.save(pogrnheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, pogrnheader.getXgrnnum().toString(), pogrnheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=" + existObj.getXgrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum=" + existObj.getXgrnnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xgrnnum){
		Optional<Pogrnheader> op = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), xgrnnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(pogrndetailRepo.getTotalQty(sessionManager.getBusinessId(), xgrnnum).compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Please delete detail record first, or make the total quantity 0");
			return responseHelper.getResponse();
		}

		try {
			pogrndetailRepo.deleteAllByZidAndXgrnnum(sessionManager.getBusinessId(), xgrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Delete", this.pageTitle, xgrnnum.toString(), null, true, 0).setMessage("Delete all details"));

		Pogrnheader obj = op.get();
		Pogrnheader copy = SerializationUtils.clone(obj);
		try {
			pogrnheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Delete", this.pageTitle, copy.getXgrnnum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xgrnnum, @RequestParam Integer xrow){
		Optional<Pogrnheader> oph = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), xgrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header data not found");
			return responseHelper.getResponse();
		}

		Pogrnheader pogrnheader = oph.get();

		if(!"Open".equals(pogrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Pogrndetail> op = pogrndetailRepo.findById(new PogrndetailPK(sessionManager.getBusinessId(), xgrnnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail data not found");
			return responseHelper.getResponse();
		}

		Pogrndetail obj = op.get();
		Pogrndetail copy = SerializationUtils.clone(obj);

		if(obj.getXdocrow() != 0) {
			responseHelper.setErrorStatusAndMessage("Order reference detail item can't be deleted");
			return responseHelper.getResponse();
		}

		try {
			pogrndetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Delete", this.pageTitle, copy.getXrow().toString(), copy.toString(), true, 0));

		// Update line amount and total amount of header
		BigDecimal xtotamt = pogrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pogrnheader.getXgrnnum());
		pogrnheader.setXtotamt(xtotamt);
		try {
			pogrnheaderRepo.save(pogrnheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Update", this.pageTitle, pogrnheader.getXgrnnum().toString(), pogrnheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=" + xgrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum="+xgrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xgrnnum) {
		Optional<Pogrnheader> oph = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), xgrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header data not found");
			return responseHelper.getResponse();
		}

		Pogrnheader pogrnheader = oph.get();

		if(!"Open".equals(pogrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(pogrnheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		// validate screen data
		if(pogrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pogrnheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(pogrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(pogrnheader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Supplier Bill No. required, Please add supplier bill no and update the header");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// validate quantity
		BigDecimal totalQty = pogrndetailRepo.getTotalQty(sessionManager.getBusinessId(), xgrnnum);
		if(totalQty.compareTo(BigDecimal.ZERO) == 0 || totalQty.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please add item");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(pogrnheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Call the procedure
		try {
			pogrnheaderRepo.PO_ConfirmGRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xgrnnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("PO15", "Confirm", this.pageTitle, xgrnnum.toString(), "PO_ConfirmGRN("+ sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xgrnnum +")" , false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO15?xgrnnum=" + xgrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO15/detail-table?xgrnnum="+xgrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
