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
import com.zayaanit.aspi.entity.Opdodetail;
import com.zayaanit.aspi.entity.Opdoheader;
import com.zayaanit.aspi.entity.Oporddetail;
import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.OpdodetailPK;
import com.zayaanit.aspi.entity.pk.OpdoheaderPK;
import com.zayaanit.aspi.entity.pk.OporddetailPK;
import com.zayaanit.aspi.entity.pk.OpordheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.OpdodetailRepo;
import com.zayaanit.aspi.repo.OpdoheaderRepo;
import com.zayaanit.aspi.repo.OporddetailRepo;
import com.zayaanit.aspi.repo.OpordheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SO15")
public class SO15 extends KitController {

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private OporddetailRepo oporddetailRepo;
	@Autowired private OpordheaderRepo opordheaderRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xdornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xdornum)) {
				model.addAttribute("opdoheader", Opdoheader.getDefaultInstance());
				xlogsdtService.save(new Xlogsdt("SO15", "Clear", this.pageTitle, null, null, false, 0));
				return "pages/SO15/SO15-fragments::main-form";
			}

			Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum)));
			Opdoheader opdoheader = null;
			if(op.isPresent()) {
				opdoheader = op.get();

				if(opdoheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), opdoheader.getXbuid()));
					if(cabunitOp.isPresent()) opdoheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(opdoheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXcus()));
					if(acsubOp.isPresent()) opdoheader.setCustomerName(acsubOp.get().getXname());
				}

				if(opdoheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
					if(xwhsOp.isPresent()) opdoheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(opdoheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXstaff()));
					if(acsubOp.isPresent()) opdoheader.setStaffName(acsubOp.get().getXname());
				}

				if(opdoheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) opdoheader.setSubmitStaffName(acsubOp.get().getXname());
				}

			}
			model.addAttribute("opdoheader", opdoheader != null ? opdoheader : Opdoheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("SO15", "View", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0));
			return "pages/SO15/SO15-fragments::main-form";
		}

		if(frommenu == null) return "blank";

		if(isAjaxRequest(request) && StringUtils.isNotBlank(xdornum) && !"RESET".equalsIgnoreCase(xdornum)) {
			Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum)));
			Opdoheader opdoheader = null;
			if(op.isPresent()) {
				opdoheader = op.get();

				if(opdoheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), opdoheader.getXbuid()));
					if(cabunitOp.isPresent()) opdoheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(opdoheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXcus()));
					if(acsubOp.isPresent()) opdoheader.setCustomerName(acsubOp.get().getXname());
				}

				if(opdoheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
					if(xwhsOp.isPresent()) opdoheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(opdoheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXstaff()));
					if(acsubOp.isPresent()) opdoheader.setStaffName(acsubOp.get().getXname());
				}

				if(opdoheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) opdoheader.setSubmitStaffName(acsubOp.get().getXname());
				}

			}
			model.addAttribute("opdoheader", opdoheader != null ? opdoheader : Opdoheader.getDefaultInstance());

			List<Opdodetail> detailList = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), Integer.parseInt(xdornum));
			for(Opdodetail detail : detailList) {
				Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), detail.getXitem()));
				if(caitemOp.isPresent()) {
					detail.setItemName(caitemOp.get().getXdesc());
					detail.setXunit(caitemOp.get().getXunit());
				}
			}
			model.addAttribute("detailList", detailList);

			model.addAttribute("opdodetail", Opdodetail.getDefaultInstance(Integer.parseInt(xdornum)));

			xlogsdtService.save(new Xlogsdt("SO15", "View", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0).setSource("SO13").setMessage("Redirected from SO13 to SO15"));

			return "pages/SO15/SO15";
		}

		model.addAttribute("opdoheader", Opdoheader.getDefaultInstance());
		return "pages/SO15/SO15";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opdoheader", Opdoheader.getDefaultInstance());
			xlogsdtService.save(new Xlogsdt("SO15", "Clear", this.pageTitle, null, null, true, 0));
			return "pages/SO15/SO15-fragments::detail-table";
		}

		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum)));
		if(!oph.isPresent()) {
			model.addAttribute("opdoheader", Opdoheader.getDefaultInstance());
			return "pages/SO15/SO15-fragments::detail-table";
		}
		model.addAttribute("opdoheader", oph.get());

		List<Opdodetail> detailList = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), Integer.parseInt(xdornum));
		for(Opdodetail detail : detailList) {
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
			Opdodetail opdodetail = Opdodetail.getDefaultInstance(Integer.parseInt(xdornum));
			if(caitem != null) {
				opdodetail.setXitem(xitem);
				opdodetail.setItemName(caitem.getXdesc());
				opdodetail.setXunit(caitem.getXunit());
				opdodetail.setXrate(caitem.getXrate());
				opdodetail.setXlineamt(opdodetail.getXqty().multiply(opdodetail.getXrate()));
			}

			model.addAttribute("opdodetail", opdodetail);
			xlogsdtService.save(new Xlogsdt("SO15", "Clear", this.pageTitle, opdodetail.getXrow().toString(), opdodetail.toString(), true, 0));
			return "pages/SO15/SO15-fragments::detail-table";
		}

		Optional<Opdodetail> opdodetailOp = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum), Integer.parseInt(xrow)));
		Opdodetail opdodetail = opdodetailOp.isPresent() ? opdodetailOp.get() : Opdodetail.getDefaultInstance(Integer.parseInt(xdornum));
		if(opdodetail != null && opdodetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), opdodetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && opdodetail != null) {
			opdodetail.setXitem(caitem.getXitem());
			opdodetail.setItemName(caitem.getXdesc());
			opdodetail.setXunit(caitem.getXunit());
			if(opdodetail.getXrow() == 0) {
				opdodetail.setXrate(caitem.getXrate());
				opdodetail.setXlineamt(opdodetail.getXqty().multiply(opdodetail.getXrate()));
			}
		}

		model.addAttribute("opdodetail", opdodetail);
		xlogsdtService.save(new Xlogsdt("SO15", "View", this.pageTitle, opdodetail.getXrow().toString(), opdodetail.toString(), true, 0));
		return "pages/SO15/SO15-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateOpdoheader(opdoheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(opdoheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opdoheader.getSubmitFor())) {
			responseHelper.setSuccessStatusAndMessage("Insert new record is not applicable here.");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opdoheader.getXdornum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Opdoheader existObj = op.get();

		if(opdoheader.getXdiscamt() == null) opdoheader.setXdiscamt(BigDecimal.ZERO);

		if(opdoheader.getXdiscamt().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount.");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXdiscamt().compareTo(existObj.getXlineamt()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount amount. Discount amount must be less or equal to Sub Total");
			return responseHelper.getResponse();
		}

		existObj.setXdate(opdoheader.getXdate());
		existObj.setXbuid(opdoheader.getXbuid());
		existObj.setXwh(opdoheader.getXwh());
		existObj.setXref(opdoheader.getXref());
		existObj.setXdiscamt(opdoheader.getXdiscamt());
		existObj.setXnote(opdoheader.getXnote());

		// Calculate line and total amount
		BigDecimal lineAmt = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXdornum());
		existObj.setXlineamt(lineAmt);
		existObj.setXtotamt(existObj.getXlineamt().subtract(existObj.getXdiscamt()));
		try {
			existObj = opdoheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Update", this.pageTitle, existObj.getXdornum().toString(), existObj.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum="+ opdoheader.getXdornum() +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoice updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opdodetail opdodetail, BindingResult bindingResult){
		if(opdodetail.getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opdodetail.getXdornum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = oph.get();
		if(!"Open".equals(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Invoice status not open");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), opdodetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXrate() == null || opdodetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXqty() == null || opdodetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		opdodetail.setXlineamt(opdodetail.getXrate().multiply(opdodetail.getXqty()));

		// Create new
		if(SubmitFor.INSERT.equals(opdodetail.getSubmitFor())) {
			opdodetail.setXdocrow(0);
			opdodetail.setXrow(opdodetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), opdodetail.getXdornum()));
			opdodetail.setZid(sessionManager.getBusinessId());
			opdodetail.setXqtyord(BigDecimal.ZERO);
			opdodetail.setXqtycrn(BigDecimal.ZERO);
			opdodetail.setXrategrn(BigDecimal.ZERO);
			try {
				opdodetail = opdodetailRepo.save(opdodetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO15", "Add", this.pageTitle, opdodetail.getXrow().toString(), opdodetail.toString(), true, 0));

			BigDecimal lineAmt = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
			opdoheader.setXlineamt(lineAmt);
			opdoheader.setXtotamt(opdoheader.getXlineamt().subtract(opdoheader.getXdiscamt()));
			try {
				opdoheaderRepo.save(opdoheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			xlogsdtService.save(new Xlogsdt("SO15", "Clear", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=" + opdodetail.getXdornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum=" + opdodetail.getXdornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Invoice detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Opdodetail> detailOp = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), opdodetail.getXdornum(), opdodetail.getXrow()));
		if(!detailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Opdodetail existObj = detailOp.get();
		if(existObj.getXdocrow() == 0) {
			responseHelper.setErrorStatusAndMessage("This detail is not for update");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXqty().compareTo(existObj.getXqtyord()) == 1) {
			responseHelper.setErrorStatusAndMessage("Quantity must be less thant Order quantity");
			return responseHelper.getResponse();
		}

		// cross checking
		Optional<Opordheader> orderOp = opordheaderRepo.findById(new OpordheaderPK(sessionManager.getBusinessId(), opdoheader.getXordernum()));
		if(!orderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order not found related with this order number");
			return responseHelper.getResponse();
		}
		Opordheader order = orderOp.get();

		Optional<Oporddetail> oporddetailOp = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), opdoheader.getXordernum(), existObj.getXdocrow()));
		if(!oporddetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Order detail not found for this detail row");
			return responseHelper.getResponse();
		}

		Oporddetail oporddetail = oporddetailOp.get();

		BigDecimal difference = opdodetail.getXqty().subtract(existObj.getXqty());
		if(difference.compareTo(BigDecimal.ZERO) == 0) {
			// Do nothing
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1 && difference.add(oporddetail.getXqtydel()).compareTo(oporddetail.getXqty()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invoice quantity should not more than Order quantity!");
			return responseHelper.getResponse();
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1) {
			oporddetail.setXqtydel(oporddetail.getXqtydel().add(opdodetail.getXqty().subtract(existObj.getXqty())));
			oporddetailRepo.save(oporddetail);
		}

		if(difference.compareTo(BigDecimal.ZERO) == -1) {
			oporddetail.setXqtydel(oporddetail.getXqtydel().subtract(existObj.getXqty().subtract(opdodetail.getXqty())));
			oporddetailRepo.save(oporddetail);
		}
		// Recheck complete here

		existObj.setXqty(opdodetail.getXqty());
		existObj.setXrate(opdodetail.getXrate());
		existObj.setXlineamt(existObj.getXqty().multiply(existObj.getXrate()));
		existObj.setXnote(opdodetail.getXnote());
		try {
			existObj = opdodetailRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Update", this.pageTitle, existObj.getXrow().toString(), existObj.toString(), true, 0));

		// update sales order status here
		List<Oporddetail> oporddetails = oporddetailRepo.findAllByZidAndXordernum(sessionManager.getBusinessId(), opdoheader.getXordernum());
		BigDecimal tord = oporddetails.stream().map(Oporddetail::getXqty).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal tgrn = oporddetails.stream().map(Oporddetail::getXqtydel).reduce(BigDecimal.ZERO, BigDecimal::add);

		long count = opdoheaderRepo.findAllByZidAndXordernum(sessionManager.getBusinessId(), opdoheader.getXordernum()).stream().count();

		if(tord.compareTo(tgrn) == 0) {
			order.setXstatusord("Full Delivered");
		} else if (tgrn.compareTo(BigDecimal.ZERO) == 0 && count == 0) {
			order.setXstatusord("Open");
		} else {
			order.setXstatusord("Invoice Created");
		}
		try {
			opordheaderRepo.save(order);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Update", this.pageTitle, order.getXordernum().toString(), order.toString(), false, 0).setMessage("Update sales order for invoice " + opdoheader.getXdornum()));

		BigDecimal lineAmt = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
		opdoheader.setXlineamt(lineAmt);
		opdoheader.setXtotamt(opdoheader.getXlineamt().subtract(opdoheader.getXdiscamt()));
		try {
			opdoheaderRepo.save(opdoheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Update", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoice detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xdornum){
		Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(opdodetailRepo.getTotalQty(sessionManager.getBusinessId(), xdornum).compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Please delete detail record first, or make the total quantity 0");
			return responseHelper.getResponse();
		}

		try {
			opdodetailRepo.deleteAllByZidAndXdornum(sessionManager.getBusinessId(), xdornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Delete", this.pageTitle, xdornum.toString(), null, true, 0).setMessage("Delete all details"));

		Opdoheader obj = op.get();
		Opdoheader copy = SerializationUtils.clone(obj);
		try {
			opdoheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Delete", this.pageTitle, copy.getXdornum().toString(), copy.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xdornum, @RequestParam Integer xrow) throws Exception{
		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = oph.get();

		if(!"Open".equals(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), xdornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Opdodetail obj = op.get();
		Opdodetail copy = SerializationUtils.clone(obj);

		if(obj.getXdocrow() != 0) {
			responseHelper.setErrorStatusAndMessage("Order reference detail item can't be deleted");
			return responseHelper.getResponse();
		}

		try {
			opdodetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Delete", this.pageTitle, copy.getXrow().toString(), copy.toString(), true, 0));

		// Update line amount and total amount of header
		BigDecimal lineAmt = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdoheader.getXdornum());
		if(opdoheader.getXdiscamt().compareTo(lineAmt) == 1) {
			throw new IllegalStateException("Can't delete this item. After delete this item, discount amount will be greater than Subtotal amount. You should update discount amount first");
		}
		opdoheader.setXlineamt(lineAmt);
		opdoheader.setXtotamt(opdoheader.getXlineamt().subtract(opdoheader.getXdiscamt()));
		try {
			opdoheaderRepo.save(opdoheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Update", this.pageTitle, opdoheader.getXdornum().toString(), opdoheader.toString(), false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xdornum) {
		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = oph.get();

		if(!"Open".equals(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(opdoheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), xdornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add item!");
			return responseHelper.getResponse();
		}

		// screen data validation
		if(opdoheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(opdoheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Opdodetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Opdodetail item : details) {
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

		prepareUnavailableStockList(qtyMap, opdoheader.getXbuid(), opdoheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/SO15/error-details");
			return responseHelper.getResponse();
		}

		try {
			opdoheaderRepo.SO_ConfirmInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		xlogsdtService.save(new Xlogsdt("SO15", "Confirm", this.pageTitle, xdornum.toString(), "SO_ConfirmInvoice("+ sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xdornum +")" , false, 0));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
