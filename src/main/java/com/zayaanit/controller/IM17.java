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
import com.zayaanit.entity.Imconvdetail;
import com.zayaanit.entity.Imconvheader;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.ImconvdetailPK;
import com.zayaanit.entity.pk.ImconvheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.CaitemRepo;
import com.zayaanit.repo.ImconvdetailRepo;
import com.zayaanit.repo.ImconvheaderRepo;
import com.zayaanit.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM17")
public class IM17 extends KitController {

	@Autowired private ImconvheaderRepo imconvheaderRepo;
	@Autowired private ImconvdetailRepo imconvdetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xconvnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		List<Cabunit> cabunits = cabunitRepo.findAllByZid(sessionManager.getBusinessId());
		model.addAttribute("issueTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Issue Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xconvnum)) {
				model.addAttribute("imconvheader", Imconvheader.getDefaultInstance(cabunits));
				return "pages/IM17/IM17-fragments::main-form";
			}

			Optional<Imconvheader> op = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xconvnum)));
			Imconvheader imconvheader = null;
			if(op.isPresent()) {
				imconvheader = op.get();

				if(imconvheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imconvheader.getXbuid()));
					if(cabunitOp.isPresent()) imconvheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imconvheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imconvheader.getXwh()));
					if(xwhsOp.isPresent()) imconvheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(imconvheader.getXitem() != null) {
					Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imconvheader.getXitem()));
					if(caitemOp.isPresent()) {
						imconvheader.setItemName(caitemOp.get().getXdesc());
						imconvheader.setXunit(caitemOp.get().getXunit());
					}
				}

				if(imconvheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imconvheader.getXstaff()));
					if(acsubOp.isPresent()) imconvheader.setStaffName(acsubOp.get().getXname());
				}

				if(imconvheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imconvheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imconvheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imconvheader", imconvheader != null ? imconvheader : Imconvheader.getDefaultInstance(cabunits));

			if(imconvheader != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("IM17")
							.xfunc("View Data")
							.xsource("IM17")
							.xtable(null)
							.xdata(imconvheader.getXconvnum().toString())
							.xstatement("View Data : " + imconvheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}

			return "pages/IM17/IM17-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imconvheader", Imconvheader.getDefaultInstance(cabunits));
		return "pages/IM17/IM17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xconvnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xconvnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imconvheader", Imconvheader.getDefaultInstance());
			return "pages/IM17/IM17-fragments::detail-table";
		}

		Optional<Imconvheader> oph = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xconvnum)));
		if(!oph.isPresent()) {
			model.addAttribute("imconvheader", Imconvheader.getDefaultInstance());
			return "pages/IM17/IM17-fragments::detail-table";
		}
		model.addAttribute("imconvheader", oph.get());

		List<Imconvdetail> detailList = imconvdetailRepo.findAllByZidAndXconvnum(sessionManager.getBusinessId(), Integer.parseInt(xconvnum));
		for(Imconvdetail detail : detailList) {
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
			Imconvdetail imconvdetail = Imconvdetail.getRAWDefaultInstance(Integer.parseInt(xconvnum));
			if(caitem != null) {
				imconvdetail.setXitem(xitem);
				imconvdetail.setItemName(caitem.getXdesc());
				imconvdetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imconvdetail", imconvdetail);
			return "pages/IM17/IM17-fragments::detail-table";
		}

		Optional<Imconvdetail> ImconvdetailOp = imconvdetailRepo.findById(new ImconvdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xconvnum), Integer.parseInt(xrow)));
		Imconvdetail imconvdetail = ImconvdetailOp.isPresent() ? ImconvdetailOp.get() : Imconvdetail.getRAWDefaultInstance(Integer.parseInt(xconvnum));
		if(imconvdetail != null && imconvdetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imconvdetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imconvdetail != null) {
			imconvdetail.setXitem(caitem.getXitem());
			imconvdetail.setItemName(caitem.getXdesc());
			imconvdetail.setXunit(caitem.getXunit());
		}

		if(imconvdetail != null && imconvdetail.getXrow() != 0) {
			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM17")
						.xfunc("View Detail")
						.xsource("IM17")
						.xtable(null)
						.xdata(imconvdetail.getXconvnum().toString() + "/" + imconvdetail.getXrow())
						.xstatement("View Detail Data : " + imconvdetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		}

		model.addAttribute("imconvdetail", imconvdetail);
		return "pages/IM17/IM17-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM17/IM17-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imconvheader imconvheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImconvheader(imconvheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imconvheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imconvheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(imconvheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(imconvheader.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Received item required");
			return responseHelper.getResponse();
		}

		if(imconvheader.getXqty() == null || imconvheader.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imconvheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(imconvheader.getSubmitFor())) {
			imconvheader.setXrate(BigDecimal.ZERO);
			imconvheader.setXtotamt(BigDecimal.ZERO);
			imconvheader.setXstatus("Open");
			imconvheader.setXstatusjv("Open");
			imconvheader.setXstatusim("Open");
			imconvheader.setXconvnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM17"));
			imconvheader.setZid(sessionManager.getBusinessId());
			try {
				imconvheader = imconvheaderRepo.save(imconvheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM17")
						.xfunc("Add Data")
						.xsource("IM17")
						.xtable(null)
						.xdata(imconvheader.getXconvnum().toString())
						.xstatement("Add Data : " + imconvheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + imconvheader.getXconvnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum="+ imconvheader.getXconvnum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM17/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventort conversion created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imconvheader> op = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), imconvheader.getXconvnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Imconvheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xconvnum", 
			"xrate",
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
		};
		BeanUtils.copyProperties(imconvheader, existObj, ignoreProperties);
		try {
			existObj = imconvheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Update Data")
					.xsource("IM17")
					.xtable(null)
					.xdata(existObj.getXconvnum().toString())
					.xstatement("Update Data : " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + existObj.getXconvnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum="+ imconvheader.getXconvnum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory conversion updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imconvdetail imconvdetail, BindingResult bindingResult){
		if(imconvdetail.getXconvnum() == null) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Optional<Imconvheader> oph = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), imconvdetail.getXconvnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Imconvheader imconvheader = oph.get();
		if(!"Open".equals(imconvheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(imconvdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imconvdetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imconvdetail.getXqty() == null || imconvdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imconvdetail.getSubmitFor())) {
			imconvdetail.setXrow(imconvdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imconvdetail.getXconvnum()));
			imconvdetail.setZid(sessionManager.getBusinessId());
			imconvdetail.setXrate(BigDecimal.ZERO);
			imconvdetail.setXlineamt(BigDecimal.ZERO);
			try {
				imconvdetail = imconvdetailRepo.save(imconvdetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("IM17")
						.xfunc("Add Detail")
						.xsource("IM17")
						.xtable(null)
						.xdata(imconvdetail.getXconvnum().toString() + "/" + imconvdetail.getXrow())
						.xstatement("Add Detail Data : " + imconvdetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + imconvdetail.getXconvnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum=" + imconvdetail.getXconvnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Imconvdetail> existOp = imconvdetailRepo.findById(new ImconvdetailPK(sessionManager.getBusinessId(), imconvdetail.getXconvnum(), imconvdetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Imconvdetail exist = existOp.get();
		String[] ignoreProperties = new String[] {
				"zid", "zuserid", "ztime",
				"xconvnum", 
				"xrow",
				"xitem",
				"xrate",
				"xlineamt",
				"xsign",
			};
		BeanUtils.copyProperties(imconvdetail, exist, ignoreProperties);
		try {
			exist = imconvdetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Update Detail")
					.xsource("IM17")
					.xtable(null)
					.xdata(exist.getXconvnum().toString() + "/" + exist.getXrow())
					.xstatement("Update Detail Data : " + exist.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + exist.getXconvnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum=" + exist.getXconvnum() + "&xrow=" + exist.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xconvnum){
		Optional<Imconvheader> op = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), xconvnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		try {
			imconvdetailRepo.deleteAllByZidAndXconvnum(sessionManager.getBusinessId(), xconvnum);
			imconvdetailRepo.deleteAllByZidAndXconvnum(sessionManager.getBusinessId(), xconvnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Delete All Detail")
					.xsource("IM17")
					.xtable(null)
					.xdata(op.get().getXconvnum().toString())
					.xstatement("Delete All Detail Data : " + op.get().toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		Imconvheader obj = op.get();
		try {
			imconvheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Delete Data")
					.xsource("IM17")
					.xtable(null)
					.xdata(obj.getXconvnum().toString())
					.xstatement("Delete Data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xconvnum, @RequestParam Integer xrow) throws Exception{
		Optional<Imconvheader> oph = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), xconvnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Imconvheader Imconvheader = oph.get();

		if(!"Open".equals(Imconvheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imconvdetail> op = imconvdetailRepo.findById(new ImconvdetailPK(sessionManager.getBusinessId(), xconvnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imconvdetail obj = op.get();
		try {
			imconvdetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Delete Detail")
					.xsource("IM17")
					.xtable(null)
					.xdata(obj.getXconvnum().toString() + "/" + obj.getXrow())
					.xstatement("Delete Detail : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + xconvnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum="+xconvnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xconvnum) {
		Optional<Imconvheader> oph = imconvheaderRepo.findById(new ImconvheaderPK(sessionManager.getBusinessId(), xconvnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Issue not found");
			return responseHelper.getResponse();
		}

		Imconvheader Imconvheader = oph.get();

		if(!"Open".equals(Imconvheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(Imconvheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Imconvdetail> rawDetails = imconvdetailRepo.findAllByZidAndXconvnum(sessionManager.getBusinessId(), xconvnum);
		if(rawDetails == null || rawDetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No consumption data found");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(Imconvheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imconvdetail detail : rawDetails) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No consumption data found");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imconvdetail item : rawDetails) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, Imconvheader.getXbuid(), Imconvheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM17/error-details");
			return responseHelper.getResponse();
		}

		try {
			imconvheaderRepo.IM_ConfirmConversion(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xconvnum);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("IM17")
					.xfunc("Confirm")
					.xsource("IM17")
					.xtable(null)
					.xdata(xconvnum.toString())
					.xstatement("Confirm Batch : IM_ConfirmConversion(" + sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ xconvnum +")")
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM17?xconvnum=" + xconvnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM17/detail-table?xconvnum="+xconvnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/fg-item")
	public String loadFgItemFragment(@RequestParam Integer xitem, Model model) {
		Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));

		Imconvheader imconvheader = Imconvheader.getDefaultInstance();
		if(caitemOp.isPresent()) {
			imconvheader.setXitem(xitem);
			imconvheader.setItemName(caitemOp.get().getXdesc());
			imconvheader.setXunit(caitemOp.get().getXunit());
		}

		model.addAttribute("imconvheader", imconvheader);
		return "pages/IM17/IM17-fragments::fg-item";
	}
}
