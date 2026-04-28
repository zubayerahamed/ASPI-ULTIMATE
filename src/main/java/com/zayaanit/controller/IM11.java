package com.zayaanit.controller;

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

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imtordetail;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.ImtordetailPK;
import com.zayaanit.entity.pk.ImtorheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.CaitemRepo;
import com.zayaanit.repo.ImtordetailRepo;
import com.zayaanit.repo.ImtorheaderRepo;
import com.zayaanit.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM11")
public class IM11 extends KitController {

	@Autowired private ImtorheaderRepo imtorheaderRepo;
	@Autowired private ImtordetailRepo imtordetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM11";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xtornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		List<Cabunit> cabunits = cabunitRepo.findAllByZid(sessionManager.getBusinessId());

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtornum)) {
				model.addAttribute("imtorheader", Imtorheader.getDefaultInstance(cabunits));
				return "pages/IM11/IM11-fragments::main-form";
			}

			Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum)));
			Imtorheader imtorheader = null;
			if(op.isPresent()) {
				imtorheader = op.get();

				if(imtorheader.getXfbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imtorheader.getXfbuid()));
					if(cabunitOp.isPresent()) imtorheader.setFromBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imtorheader.getXtbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imtorheader.getXtbuid()));
					if(cabunitOp.isPresent()) imtorheader.setToBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imtorheader.getXfwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXfwh()));
					if(xwhsOp.isPresent()) imtorheader.setFromWarehouseName(xwhsOp.get().getXname());
				}

				if(imtorheader.getXtwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXtwh()));
					if(xwhsOp.isPresent()) imtorheader.setToWarehouseName(xwhsOp.get().getXname());
				}

				if(imtorheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imtorheader.getXstaff()));
					if(acsubOp.isPresent()) imtorheader.setStaffName(acsubOp.get().getXname());
				}

				if(imtorheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imtorheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imtorheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imtorheader", imtorheader != null ? imtorheader : Imtorheader.getDefaultInstance(cabunits));

			if(imtorheader != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("IM11")
							.xfunc("View Data")
							.xsource("IM11")
							.xtable(null)
							.xdata(imtorheader.getXtornum().toString())
							.xstatement("View Data : " + imtorheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}

			return "pages/IM11/IM11-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imtorheader", Imtorheader.getDefaultInstance(cabunits));
		return "pages/IM11/IM11";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xtornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xtornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
			return "pages/IM11/IM11-fragments::detail-table";
		}

		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum)));
		if(!oph.isPresent()) {
			model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
			return "pages/IM11/IM11-fragments::detail-table";
		}
		model.addAttribute("imtorheader", oph.get());

		List<Imtordetail> detailList = imtordetailRepo.findAllByZidAndXtornum(sessionManager.getBusinessId(), Integer.parseInt(xtornum));
		for(Imtordetail detail : detailList) {
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
			Imtordetail imtrodetail = Imtordetail.getDefaultInstance(Integer.parseInt(xtornum));
			if(caitem != null) {
				imtrodetail.setXitem(xitem);
				imtrodetail.setItemName(caitem.getXdesc());
				imtrodetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imtordetail", imtrodetail);
			return "pages/IM11/IM11-fragments::detail-table";
		}

		Optional<Imtordetail> imtrodetailOp = imtordetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum), Integer.parseInt(xrow)));
		Imtordetail imtordetail = imtrodetailOp.isPresent() ? imtrodetailOp.get() : Imtordetail.getDefaultInstance(Integer.parseInt(xtornum));
		if(imtordetail != null && imtordetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtordetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imtordetail != null) {
			imtordetail.setXitem(caitem.getXitem());
			imtordetail.setItemName(caitem.getXdesc());
			imtordetail.setXunit(caitem.getXunit());
		}

		if(imtordetail != null && imtordetail.getXrow() != 0) {
			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM11")
						.xfunc("View Detail")
						.xsource("IM11")
						.xtable(null)
						.xdata(imtordetail.getXtornum().toString() + "/" + imtordetail.getXrow())
						.xstatement("View Detail Data : " + imtordetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		}

		model.addAttribute("imtordetail", imtordetail);
		return "pages/IM11/IM11-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM11/IM11-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imtorheader imtorheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImtorheader(imtorheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imtorheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh() == null) {
			responseHelper.setErrorStatusAndMessage("From store required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXtwh() == null) {
			responseHelper.setErrorStatusAndMessage("To store required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh().equals(imtorheader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Inventory cannot be transferred to same store");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imtorheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		imtorheader.setXtbuid(imtorheader.getXfbuid());

		// Create new
		if(SubmitFor.INSERT.equals(imtorheader.getSubmitFor())) {
			imtorheader.setXtotamt(BigDecimal.ZERO);
			imtorheader.setXstatus("Open");
			imtorheader.setXstatusim("Open");
			imtorheader.setXtornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM11"));
			imtorheader.setZid(sessionManager.getBusinessId());
			imtorheader.setXtype("Direct Transfer");
			try {
				imtorheader = imtorheaderRepo.save(imtorheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM11")
						.xfunc("Add Data")
						.xsource("IM11")
						.xtable(null)
						.xdata(imtorheader.getXtornum().toString())
						.xstatement("Add Data : " + imtorheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + imtorheader.getXtornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+ imtorheader.getXtornum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventory transfer created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), imtorheader.getXtornum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open to do update");
			return responseHelper.getResponse();
		}


		Imtorheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xtornum", 
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xfvoucher",
			"xtvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
			"xtype",
		};
		BeanUtils.copyProperties(imtorheader, existObj, ignoreProperties);
		try {
			existObj = imtorheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM11")
					.xfunc("Update Data")
					.xsource("IM11")
					.xtable(null)
					.xdata(existObj.getXtornum().toString())
					.xstatement("Update Data : " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + existObj.getXtornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+ imtorheader.getXtornum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory transfer updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imtordetail imtordetail, BindingResult bindingResult){
		if(imtordetail.getXtornum() == null) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), imtordetail.getXtornum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();
		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Transfer not open");
			return responseHelper.getResponse();
		}

		if(imtordetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtordetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imtordetail.getXqty() == null || imtordetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imtordetail.getSubmitFor())) {
			imtordetail.setXrow(imtordetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imtordetail.getXtornum()));
			imtordetail.setZid(sessionManager.getBusinessId());
			imtordetail.setXrate(BigDecimal.ZERO);
			imtordetail.setXlineamt(BigDecimal.ZERO);
			try {
				imtordetail = imtordetailRepo.save(imtordetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM11")
						.xfunc("Add Detail")
						.xsource("IM11")
						.xtable(null)
						.xdata(imtordetail.getXtornum().toString() + "/" + imtordetail.getXrow())
						.xstatement("Add Detail Data : " + imtordetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + imtordetail.getXtornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum=" + imtordetail.getXtornum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Transfer detail added successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not applicatble here");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xtornum){
		Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		try {
			imtordetailRepo.deleteAllByZidAndXtornum(sessionManager.getBusinessId(), xtornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM11")
					.xfunc("Delete All Detail")
					.xsource("IM11")
					.xtable(null)
					.xdata(xtornum.toString())
					.xstatement("Delete All Detail Data : " + op.get().toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		Imtorheader obj = op.get();
		try {
			imtorheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM11")
					.xfunc("Delete Data")
					.xsource("IM11")
					.xtable(null)
					.xdata(xtornum.toString())
					.xstatement("Delete Data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xtornum, @RequestParam Integer xrow) throws Exception{
		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();

		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imtordetail> op = imtordetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), xtornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imtordetail obj = op.get();
		try {
			imtordetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM11")
					.xfunc("Delete Detail")
					.xsource("IM11")
					.xtable(null)
					.xdata(xtornum.toString() + "/" + obj.getXrow())
					.xstatement("Delete Detail Data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + xtornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xtornum) {
		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();

		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(imtorheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh().equals(imtorheader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Inventory cannot be transferred to same store");
			return responseHelper.getResponse();
		}

		List<Imtordetail> details = imtordetailRepo.findAllByZidAndXtornum(sessionManager.getBusinessId(), xtornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(imtorheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imtordetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imtordetail item : details) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, imtorheader.getXfbuid(), imtorheader.getXfwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM11/error-details");
			return responseHelper.getResponse();
		}

		try {
			imtorheaderRepo.IM_ConfirmDirectTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtornum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM11")
					.xfunc("Confirm Transfer")
					.xsource("IM11")
					.xtable(null)
					.xdata(xtornum.toString())
					.xstatement("Confirm Transfer Data : IM_ConfirmDirectTO(" + sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xtornum +")")
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + xtornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
