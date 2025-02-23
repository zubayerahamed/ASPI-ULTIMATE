package com.zayaanit.aspi.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xuserprofiles;
import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.XprofilesPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XuserprofilesPK;
import com.zayaanit.aspi.entity.pk.XusersPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.XprofilesRepo;
import com.zayaanit.aspi.repo.XuserprofilesRepo;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.repo.XwhsRepo;

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
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(zemail)) {
				model.addAttribute("xusers", Xusers.getDefaultInstance());
				return "pages/AD13/AD13-fragments::main-form";
			}

			Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
			Xusers user = op.isPresent() ? op.get() : Xusers.getDefaultInstance();
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
			return "pages/AD13/AD13-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("xusers", Xusers.getDefaultInstance());
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
		}
		
		model.addAttribute("xuserprofiles", userProfile);
		return "pages/AD13/AD13-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/AD13/AD13-fragments::list-table";
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

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xusers.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xusers.getZemail() + "&xprofile=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/AD13/list-table"));
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

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + existObj.getZemail()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + existObj.getZemail() + "&xprofile=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/AD13/list-table"));
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

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xuserprofiles.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xuserprofiles.getZemail() + "&xprofile=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/AD13/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not allowed");
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

		Xusers obj = op.get();
		try {
			xuserRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=RESET&xprofile=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/AD13/list-table"));
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

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + zemail));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + zemail + "&xprofile=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/AD13/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
