package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserprofiles;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserprofilesPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.entity.pk.XuserwidgetsPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.entity.pk.XwidgetsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.XprofilesRepo;
import com.zayaanit.repo.XuserprofilesRepo;
import com.zayaanit.repo.XusersRepo;
import com.zayaanit.repo.XuserwidgetsRepo;
import com.zayaanit.repo.XwhsRepo;
import com.zayaanit.repo.XwidgetsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD13")
public class AD13 extends KitController {

	@Autowired private XusersRepo xuserRepo;
	@Autowired private XuserprofilesRepo xuserprofilesRepo;
	@Autowired private XprofilesRepo xprofilesRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private XuserwidgetsRepo xuserwidgetRepo;
	@Autowired private XwidgetsRepo xwidgetsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD13";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String zemail, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		List<Cabunit> cabunits = cabunitRepo.findAllByZid(sessionManager.getBusinessId());

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(zemail)) {
				model.addAttribute("xusers", Xusers.getDefaultInstance(cabunits));
				return "pages/AD13/AD13-fragments::main-form";
			}

			Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
			Xusers user = op.isPresent() ? op.get() : Xusers.getDefaultInstance(cabunits);
			if(user.getXstaff() != null) {
				Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), user.getXstaff()));
				if(acsubOp.isPresent()) user.setEmployeeName(acsubOp.get().getXname());

				if(user.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), user.getXbuid()));
					if(cabunitOp.isPresent()) user.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(user.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), user.getXwh()));
					if(xwhsOp.isPresent()) user.setWarehouseName(xwhsOp.get().getXname());
				}
			}
			model.addAttribute("xusers", user);

			if(StringUtils.isNotBlank(user.getZemail())) {
				eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("AD13")
						.xfunc("View Data")
						.xsource("AD13")
						.xtable(null)
						.xdata(user.getZemail())
						.xstatement("View Data : " + user.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
			}

			return "pages/AD13/AD13-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("xusers", Xusers.getDefaultInstance(cabunits));
		return "pages/AD13/AD13";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String zemail, @RequestParam String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(zemail) && "RESET".equalsIgnoreCase(xprofile)) {
			return "pages/AD13/AD13-fragments::detail-table";
		}

		Optional<Xusers> userOp = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!userOp.isPresent()) return "pages/AD13/AD13-fragments::detail-table";
		model.addAttribute("xusers", userOp.get());

		List<Xuserprofiles> detailsList = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		detailsList.sort(Comparator.comparing(Xuserprofiles::getXprofile));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("xuserprofiles", Xuserprofiles.getDefaultInstance(zemail));
			return "pages/AD13/AD13-fragments::detail-table";
		}

		Optional<Xuserprofiles> xuserwhOp =  xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), zemail, xprofile));
		Xuserprofiles userProfile = null;
		if(!xuserwhOp.isPresent()) {
			userProfile = Xuserprofiles.getDefaultInstance(zemail);
			Optional<Xprofiles> profilesOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
			if(profilesOp.isPresent()) {
				userProfile.setXprofile(profilesOp.get().getXprofile());
			}
		} else {
			userProfile = xuserwhOp.get();

			eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("View Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(userProfile.getZemail() + "/" + userProfile.getXprofile())
					.xstatement("View Deetail Data : " + userProfile.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		}
		
		model.addAttribute("xuserprofiles", userProfile);
		return "pages/AD13/AD13-fragments::detail-table";
	}

	@GetMapping("/widget-table")
	public String widgetFormFragment(@RequestParam String zemail, @RequestParam String xwidget, Model model) {
		if("RESET".equalsIgnoreCase(zemail) && "RESET".equalsIgnoreCase(xwidget)) {
			return "pages/AD13/AD13-fragments::widget-table";
		}

		Optional<Xusers> userOp = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!userOp.isPresent()) return "pages/AD13/AD13-fragments::widget-table";
		model.addAttribute("xusers", userOp.get());

		List<Xuserwidgets> detailsList = xuserwidgetRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		detailsList.stream().forEach(d -> {
			Optional<Xwidgets> xwidgetOp = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), d.getXwidget()));
			if(xwidgetOp.isPresent()) d.setWidgetTitle(xwidgetOp.get().getXtitle());
		});
		detailsList.sort(Comparator.comparing(Xuserwidgets::getXsequence));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xwidget)) {
			model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
			return "pages/AD13/AD13-fragments::widget-table";
		}

		Optional<Xuserwidgets> xuserwidgetOp =  xuserwidgetRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), zemail, xwidget));
		Xuserwidgets xuserwidgets = null;
		if(!xuserwidgetOp.isPresent()) {
			xuserwidgets = Xuserwidgets.getDefaultInstance(zemail);
			Optional<Xwidgets> xwidgetsOp = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), xwidget));
			if(xwidgetsOp.isPresent()) {
				xuserwidgets.setXwidget(xwidgetsOp.get().getXwidget());
			}
		} else {
			xuserwidgets = xuserwidgetOp.get();

			eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("View Widget Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(xuserwidgets.getZemail() + "/" + xuserwidgets.getXwidget())
					.xstatement("View Widget Detail Data : " + xuserwidgets.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		}
		
		model.addAttribute("xuserwidgets", xuserwidgets);
		return "pages/AD13/AD13-fragments::widget-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xusers xusers, BindingResult bindingResult){

		if(StringUtils.isBlank(xusers.getZemail())) {
			responseHelper.setErrorStatusAndMessage("User ID required");
			return responseHelper.getResponse();
		}

		if(xusers.getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee selection required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xusers.getXsessiontype())) {
			responseHelper.setErrorStatusAndMessage("Session type required");
			return responseHelper.getResponse();
		}

		if(!"Default".equalsIgnoreCase(xusers.getXsessiontype())) {
			if(xusers.getXsessiontime() == null) {
				responseHelper.setErrorStatusAndMessage("For Custom session type, session time is required");
				return responseHelper.getResponse();
			}

			if(xusers.getXsessiontime() <= 0) {
				responseHelper.setErrorStatusAndMessage("Invalid session time");
				return responseHelper.getResponse();
			}
		}

		if(xusers.getXislock().equals(Boolean.TRUE)) {
			if(xusers.getXbuid() == null) {
				responseHelper.setErrorStatusAndMessage("Unit selection required");
				return responseHelper.getResponse();
			}

			if(xusers.getXwh() == null) {
				responseHelper.setErrorStatusAndMessage("Outlet selection required");
				return responseHelper.getResponse();
			}
		}

		// VALIDATE XSCREENS
		modelValidator.validateXusers(xusers, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xusers.getSubmitFor())) {
			if(!sessionManager.getLoggedInUserDetails().isAdmin()) xusers.setZadmin(false);
			xusers.setXtheme("Default");
			xusers.setZid(sessionManager.getBusinessId());
			try {
				xusers = xuserRepo.save(xusers);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("AD13")
						.xfunc("Add Data")
						.xsource("AD13")
						.xtable(null)
						.xdata(xusers.getZemail())
						.xstatement("Add Data : " + xusers.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);


			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xusers.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xusers.getZemail() + "&xprofile=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), xusers.getZemail()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xusers existObj = op.get();
		if(StringUtils.isBlank(xusers.getXpassword())) {
			BeanUtils.copyProperties(xusers, existObj, "zid", "zuserid", "ztime", "zemail", "xtheme", "xpassword", "xoldpassword", "zadmin");
		} else {
			String existPass = existObj.getXpassword();
			BeanUtils.copyProperties(xusers, existObj, "zid", "zuserid", "ztime", "zemail", "xtheme", "zadmin");
			existObj.setXoldpassword(existPass);
		}
		if(sessionManager.getLoggedInUserDetails().isAdmin() && !sessionManager.getLoggedInUserDetails().getUsername().equals(xusers.getZemail())) existObj.setZadmin(xusers.getZadmin());
		try {
			existObj = xuserRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Update Data")
					.xsource("AD13")
					.xtable(null)
					.xdata(existObj.getZemail())
					.xstatement("Update Data : " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);


		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + existObj.getZemail()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + existObj.getZemail() + "&xprofile=RESET"));
		reloadSections.add(new ReloadSection("widget-table-container", "/AD13/widget-table?zemail=" + existObj.getZemail() + "&xwidget=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Xuserprofiles xuserprofiles, BindingResult bindingResult){
		// VALIDATE
		if(xuserprofiles.getXprofile() == null) {
			responseHelper.setErrorStatusAndMessage("Profile required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xuserprofiles.getSubmitFor())) {
			Optional<Xuserprofiles> existOp = xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), xuserprofiles.getZemail(), xuserprofiles.getXprofile()));
			if(existOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Profile already added");
				return responseHelper.getResponse();
			}

			xuserprofiles.setZid(sessionManager.getBusinessId());
			try {
				xuserprofiles = xuserprofilesRepo.save(xuserprofiles);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("AD13")
						.xfunc("Add Detail")
						.xsource("AD13")
						.xtable(null)
						.xdata(xuserprofiles.getZemail() + "/" + xuserprofiles.getXprofile())
						.xstatement("Add Detail Data : " + xuserprofiles.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);


			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xuserprofiles.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xuserprofiles.getZemail() + "&xprofile=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not allowed");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/widget/store")
	public @ResponseBody Map<String, Object> storeWidget(Xuserwidgets xuserwidgets, BindingResult bindingResult){
		// VALIDATE
		if(xuserwidgets.getXwidget() == null) {
			responseHelper.setErrorStatusAndMessage("Wiget required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xuserwidgets.getSubmitFor())) {
			Optional<Xuserwidgets> existOp = xuserwidgetRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), xuserwidgets.getZemail(), xuserwidgets.getXwidget()));
			if(existOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Widget already added");
				return responseHelper.getResponse();
			}

			if(Boolean.TRUE.equals(xuserwidgets.getXisdefault())) {
				// Remove xdefault from others
				xuserwidgetRepo.resetAllDefaults(sessionManager.getBusinessId());
			}

			xuserwidgets.setZid(sessionManager.getBusinessId());
			xuserwidgets.setXsequence(xuserwidgetRepo.getNextAvailableSeqn(sessionManager.getBusinessId(), xuserwidgets.getZemail()));
			try {
				xuserwidgets = xuserwidgetRepo.save(xuserwidgets);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("AD13")
						.xfunc("Add Widget Detail")
						.xsource("AD13")
						.xtable(null)
						.xdata(xuserwidgets.getZemail() + "/" + xuserwidgets.getZemail())
						.xstatement("Add Widget Detail Data : " + xuserwidgets.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);


			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xuserwidgets.getZemail()));
			reloadSections.add(new ReloadSection("widget-table-container", "/AD13/widget-table?zemail=" + xuserwidgets.getZemail() + "&xwidget=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		Optional<Xuserwidgets> existOp = xuserwidgetRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), xuserwidgets.getZemail(), xuserwidgets.getXwidget()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found for update");
			return responseHelper.getResponse();
		}

		if(Boolean.TRUE.equals(xuserwidgets.getXisdefault())) {
			// Remove xdefault from others
			Optional<Xuserwidgets> currentDefaultOp =  xuserwidgetRepo.findByZidAndZemailAndXisdefaultTrue(sessionManager.getBusinessId(), xuserwidgets.getZemail());
			if(currentDefaultOp.isPresent() && !currentDefaultOp.get().getXwidget().equalsIgnoreCase(existOp.get().getXwidget())) {
				Xuserwidgets currentDefault = currentDefaultOp.get();
				currentDefault.setXisdefault(false);
				xuserwidgetRepo.save(currentDefault);
			}
		}

		Xuserwidgets existObj = existOp.get();
		existObj.setXisdefault(xuserwidgets.getXisdefault());
		try {
			existObj = xuserwidgetRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Update Widget Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(existObj.getZemail() + "/" + existObj.getXwidget())
					.xstatement("Update Widget Detail Data : " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);


		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + existObj.getZemail()));
		reloadSections.add(new ReloadSection("widget-table-container", "/AD13/widget-table?zemail=" + existObj.getZemail() + "&xwidget=" + existObj.getXwidget()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String zemail){
		Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		// Delete all access first
		List<Xuserprofiles> details = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		if(!details.isEmpty()) {
			try {
				xuserprofilesRepo.deleteAll(details);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Delete All Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(op.get().getZemail())
					.xstatement("Delete all details")
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		Xusers obj = op.get();
		try {
			xuserRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Delete Data")
					.xsource("AD13")
					.xtable(null)
					.xdata(op.get().getZemail())
					.xstatement("Delete Data : " + op.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=RESET&xprofile=RESET"));
		reloadSections.add(new ReloadSection("widget-table-container", "/AD13/widget-table?zemail=RESET&xwidget=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam String zemail, @RequestParam String xprofile){
		Optional<Xuserprofiles> existOp = xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), zemail, xprofile));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Xuserprofiles obj = existOp.get();
		try {
			xuserprofilesRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Delete Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(obj.getZemail() + "/" + obj.getXprofile())
					.xstatement("Delete Detail Data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + zemail));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + zemail + "&xprofile=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/widget-table")
	public @ResponseBody Map<String, Object> deleteWidget(@RequestParam String zemail, @RequestParam String xwidget){
		Optional<Xuserwidgets> existOp = xuserwidgetRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), zemail, xwidget));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Xuserwidgets obj = existOp.get();
		try {
			xuserwidgetRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Delete Widget Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(obj.getZemail() + "/" + obj.getXwidget())
					.xstatement("Delete Widget Detail Data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		// Resequence every rows
		List<Xuserwidgets> detailsList = xuserwidgetRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		detailsList.sort(Comparator.comparing(Xuserwidgets::getXsequence));
		int i = 1;
		for(Xuserwidgets d : detailsList) {
			d.setXsequence(i);
			xuserwidgetRepo.save(d);
			i++;
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + zemail));
		reloadSections.add(new ReloadSection("widget-table-container", "/AD13/widget-table?zemail=" + zemail + "&xwidget=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/widget-table/seqn/{direction}")
	public String updateSequence(@PathVariable String direction, @RequestParam Integer xsequence, @RequestParam String zemail, @RequestParam String xwidget, Model model){
		List<Xuserwidgets> detailsList = xuserwidgetRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		detailsList.sort(Comparator.comparing(Xuserwidgets::getXsequence));

		// Check the table has only one row, then return without do nothing
		if(detailsList.isEmpty() || detailsList.size() == 1) {
			model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
			model.addAttribute("detailList", detailsList);
			return "pages/AD13/AD13-fragments::widget-table";
		}

		Optional<Xuserwidgets> existOp = xuserwidgetRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), zemail, xwidget));
		// Is current object is not present then do nothing
		if(!existOp.isPresent()) {
			model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
			model.addAttribute("detailList", detailsList);
			return "pages/AD13/AD13-fragments::widget-table";
		}

		Integer min = detailsList.stream().mapToInt(m -> m.getXsequence()).min().orElse(0);
		Integer max = detailsList.stream().mapToInt(m -> m.getXsequence()).max().orElse(0);

		Xuserwidgets currentRow = existOp.get();

		// If first row the do nothing
		if(currentRow.getXsequence() == min && "UP".equalsIgnoreCase(direction)) {
			model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
			model.addAttribute("detailList", detailsList);
			return "pages/AD13/AD13-fragments::widget-table";
		}

		// if last row, then do nothing
		if(currentRow.getXsequence() == max && "DOWN".equalsIgnoreCase(direction)) {
			model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
			model.addAttribute("detailList", detailsList);
			return "pages/AD13/AD13-fragments::widget-table";
		}

		Xuserwidgets sibling = detailsList.stream().filter(f -> f.getXsequence() == xsequence).findFirst().orElse(null);
		sibling.setXsequence(currentRow.getXsequence());
		try {
			xuserwidgetRepo.save(sibling);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Update Widget Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(sibling.getZemail() + "/" + sibling.getXwidget())
					.xstatement("Update Widget Sequence Detail Data : " + sibling.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		currentRow.setXsequence(xsequence);
		try {
			xuserwidgetRepo.save(currentRow);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD13")
					.xfunc("Update Widget Detail")
					.xsource("AD13")
					.xtable(null)
					.xdata(currentRow.getZemail() + "/" + currentRow.getXwidget())
					.xstatement("Update Widget Sequence Detail Data : " + currentRow.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		detailsList.sort(Comparator.comparing(Xuserwidgets::getXsequence));
		model.addAttribute("xuserwidgets", Xuserwidgets.getDefaultInstance(zemail));
		model.addAttribute("detailList", detailsList);
		return "pages/AD13/AD13-fragments::widget-table";
	}
}
