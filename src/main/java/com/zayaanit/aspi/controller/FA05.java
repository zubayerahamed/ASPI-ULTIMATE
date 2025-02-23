 package com.zayaanit.aspi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.exceptions.ResourceNotFoundException;
import com.zayaanit.aspi.model.MyUserDetails;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.ZbusinessRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2025
 */
@Controller
@RequestMapping("/FA05")
public class FA05 extends KitController{

	private String pageTitle = null;

	@Autowired private ZbusinessRepo businessRepo;

	@Override
	protected String screenCode() {
		return "FA05";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA05"));
		if(!op.isPresent()) return "Business Profile Management";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) throws ResourceNotFoundException {
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) throw new ResourceNotFoundException();

		Zbusiness zb = op.get();
		if(zb.getXfilesize() == null) zb.setXfilesize(0);
		if(StringUtils.isBlank(zb.getXdocpath())) zb.setXdocpath("C:\\Contents\\");
		if(StringUtils.isBlank(zb.getXdoctypes())) zb.setXdoctypes(".jpg,.jpeg,.png,.pdf");
		if(StringUtils.isBlank(zb.getXrptpath())) zb.setXrptpath("C:\\Reports");
		if(zb.getXlogo() != null && zb.getXlogo().length > 0) {
			zb.setImageBase64(Base64.getEncoder().encodeToString(zb.getXlogo()));
		}

		model.addAttribute("business", zb);

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/FA05/FA05-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/FA05/FA05";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Zbusiness zbusiness, @RequestParam(value= "files[]", required = false) MultipartFile[] files, BindingResult bindingResult){

		if(zbusiness.getXfilesize() == null) zbusiness.setXfilesize(1024);
		if(StringUtils.isBlank(zbusiness.getXdocpath())) zbusiness.setXdocpath("C:\\Contents\\");
		if(StringUtils.isBlank(zbusiness.getXrptpath())) {
			responseHelper.setErrorStatusAndMessage("Report path required");
			return responseHelper.getResponse();
		}

		// Process image first
		boolean imageChanged = false;
		if(files != null && files.length > 0) {
			try {
				zbusiness.setXlogo(files[0].getBytes());
				imageChanged = true;
			} catch (IOException e) {
				responseHelper.setErrorStatusAndMessage("Something wrong in image, please try again with new one");
				return responseHelper.getResponse();
			}
		}

		// VALIDATE XSCREENS
		modelValidator.validateZbusiness(zbusiness, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Update existing
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		zbusiness.setXdocpath(zbusiness.getXdocpath().trim());
		zbusiness.setXdoctypes(zbusiness.getXdoctypes().trim());

		Zbusiness existObj = op.get();
		if(imageChanged) {
			BeanUtils.copyProperties(zbusiness, existObj, "zid", "zactive", "zuserid", "ztime", "xrptdefautl");
		} else {
			BeanUtils.copyProperties(zbusiness, existObj, "zid", "zactive", "xlogo", "zuserid", "ztime", "xrptdefautl");
		}
		try {
			existObj = businessRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Load newly updated zbusiness into session manager object
		MyUserDetails my = sessionManager.getLoggedInUserDetails();
		my.setZbusiness(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA05"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}
}
