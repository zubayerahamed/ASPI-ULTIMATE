package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.zayaanit.aspi.entity.Imopendetail;
import com.zayaanit.aspi.entity.Imopenheader;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.ImopendetailPK;
import com.zayaanit.aspi.entity.pk.ImopenheaderPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.ImopendetailRepo;
import com.zayaanit.aspi.repo.ImopenheaderRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM16")
public class IM16 extends KitController {

	@Autowired private ImopenheaderRepo imopenheaderRepo;
	@Autowired private ImopendetailRepo imopendetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM16";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xopennum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xopennum)) {
				model.addAttribute("imopenheader", Imopenheader.getDefaultInstance());
				return "pages/IM16/IM16-fragments::main-form";
			}

			Optional<Imopenheader> op = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xopennum)));
			Imopenheader imopenheader = null;
			if(op.isPresent()) {
				imopenheader = op.get();

				if(imopenheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imopenheader.getXbuid()));
					if(cabunitOp.isPresent()) imopenheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imopenheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imopenheader.getXwh()));
					if(xwhsOp.isPresent()) imopenheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(imopenheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imopenheader.getXstaff()));
					if(acsubOp.isPresent()) imopenheader.setStaffName(acsubOp.get().getXname());
				}

				if(imopenheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imopenheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imopenheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imopenheader", imopenheader != null ? imopenheader : Imopenheader.getDefaultInstance());

			return "pages/IM16/IM16-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imopenheader", Imopenheader.getDefaultInstance());
		return "pages/IM16/IM16";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xopennum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xopennum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imopenheader", Imopenheader.getDefaultInstance());
			return "pages/IM16/IM16-fragments::detail-table";
		}

		Optional<Imopenheader> oph = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xopennum)));
		if(!oph.isPresent()) {
			model.addAttribute("imopenheader", Imopenheader.getDefaultInstance());
			return "pages/IM16/IM16-fragments::detail-table";
		}
		model.addAttribute("imopenheader", oph.get());

		List<Imopendetail> detailList = imopendetailRepo.findAllByZidAndXopennum(sessionManager.getBusinessId(), Integer.parseInt(xopennum));
		for(Imopendetail detail : detailList) {
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
			Imopendetail imopendetail = Imopendetail.getDefaultInstance(Integer.parseInt(xopennum));
			if(caitem != null) {
				imopendetail.setXitem(xitem);
				imopendetail.setItemName(caitem.getXdesc());
				imopendetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imopendetail", imopendetail);
			return "pages/IM16/IM16-fragments::detail-table";
		}

		Optional<Imopendetail> imopendetailOp = imopendetailRepo.findById(new ImopendetailPK(sessionManager.getBusinessId(), Integer.parseInt(xopennum), Integer.parseInt(xrow)));
		Imopendetail imopendetail = imopendetailOp.isPresent() ? imopendetailOp.get() : Imopendetail.getDefaultInstance(Integer.parseInt(xopennum));
		if(imopendetail != null && imopendetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imopendetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imopendetail != null) {
			imopendetail.setXitem(caitem.getXitem());
			imopendetail.setItemName(caitem.getXdesc());
			imopendetail.setXunit(caitem.getXunit());
		}

		model.addAttribute("imopendetail", imopendetail);
		return "pages/IM16/IM16-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM16/IM16-fragments::list-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imopenheader imopenheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImopenheader(imopenheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imopenheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imopenheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(imopenheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imopenheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(imopenheader.getSubmitFor())) {
			imopenheader.setXtotamt(BigDecimal.ZERO);
			imopenheader.setXstatus("Open");
			imopenheader.setXstatusim("Open");
			imopenheader.setXstatusjv("Open");
			imopenheader.setXopennum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM16"));
			imopenheader.setZid(sessionManager.getBusinessId());
			try {
				imopenheader = imopenheaderRepo.save(imopenheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + imopenheader.getXopennum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum="+ imopenheader.getXopennum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventory issue created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imopenheader> op = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), imopenheader.getXopennum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Imopenheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xopennum", 
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
		};
		BeanUtils.copyProperties(imopenheader, existObj, ignoreProperties);

		// update header xtotal
		BigDecimal totalAmount = imopendetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXopennum());
		existObj.setXtotamt(totalAmount);
		imopenheaderRepo.save(imopenheader);
		try {
			existObj = imopenheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + existObj.getXopennum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum="+ imopenheader.getXopennum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory issue updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imopendetail imopendetail, BindingResult bindingResult){
		if(imopendetail.getXopennum() == null) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Optional<Imopenheader> oph = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), imopendetail.getXopennum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Imopenheader imopenheader = oph.get();
		if(!"Open".equals(imopenheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Adjustment status not open");
			return responseHelper.getResponse();
		}

		if(imopendetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imopendetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imopendetail.getXqty() == null || imopendetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(imopendetail.getXrate() == null || imopendetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		imopendetail.setXlineamt(imopendetail.getXqty().multiply(imopendetail.getXrate()));

		// Create new
		if(SubmitFor.INSERT.equals(imopendetail.getSubmitFor())) {
			imopendetail.setXrow(imopendetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imopendetail.getXopennum()));
			imopendetail.setZid(sessionManager.getBusinessId());
			try {
				imopendetail = imopendetailRepo.save(imopendetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			// update header xtotal
			BigDecimal totalAmount = imopendetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), imopendetail.getXopennum());
			imopenheader.setXtotamt(totalAmount);
			try {
				imopenheaderRepo.save(imopenheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + imopendetail.getXopennum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum=" + imopendetail.getXopennum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Imopendetail> existOp = imopendetailRepo.findById(new ImopendetailPK(sessionManager.getBusinessId(), imopendetail.getXopennum(), imopendetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found in this system");
			return responseHelper.getResponse();
		}

		Imopendetail exist = existOp.get();
		String[] ignoreProperties = new String[] {
				"zid", "zuserid", "ztime",
				"xopennum", 
				"xrow",
				"xitem",
			};
		BeanUtils.copyProperties(imopendetail, exist, ignoreProperties);
		try {
			exist = imopendetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// update header xtotal
		BigDecimal totalAmount = imopendetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), imopendetail.getXopennum());
		imopenheader.setXtotamt(totalAmount);
		try {
			imopenheaderRepo.save(imopenheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + imopendetail.getXopennum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum=" + imopendetail.getXopennum() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xopennum){
		Optional<Imopenheader> op = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), xopennum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			imopendetailRepo.deleteAllByZidAndXopennum(sessionManager.getBusinessId(), xopennum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Imopenheader obj = op.get();
		try {
			imopenheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xopennum, @RequestParam Integer xrow) throws Exception{
		Optional<Imopenheader> oph = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), xopennum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imopenheader imopenheader = oph.get();

		if(!"Open".equals(imopenheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imopendetail> op = imopendetailRepo.findById(new ImopendetailPK(sessionManager.getBusinessId(), xopennum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imopendetail obj = op.get();
		try {
			imopendetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// update header xtotal
		BigDecimal totalAmount = imopendetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), obj.getXopennum());
		imopenheader.setXtotamt(totalAmount);
		try {
			imopenheaderRepo.save(imopenheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + xopennum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum="+xopennum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xopennum) {
		Optional<Imopenheader> oph = imopenheaderRepo.findById(new ImopenheaderPK(sessionManager.getBusinessId(), xopennum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Adjustment not found");
			return responseHelper.getResponse();
		}

		Imopenheader imopenheader = oph.get();

		if(!"Open".equals(imopenheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(imopenheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Imopendetail> details = imopendetailRepo.findAllByZidAndXopennum(sessionManager.getBusinessId(), xopennum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imopendetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		try {
			imopenheaderRepo.IM_ConfirmOpening(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xopennum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xopennum=" + xopennum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xopennum="+xopennum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
