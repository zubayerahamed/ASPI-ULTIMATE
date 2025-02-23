package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.pk.XusersPK;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.XusersRepo;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/CP")
public class ChangePasswordController extends KitController {

	@Autowired private XusersRepo xuserRepo;

	@Override
	protected String screenCode() {
		return "Change Password";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		return "Change Password";
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			return "pages/CP/CP-fragments::main-form";
		}

		return "pages/CP/CP";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(String oldPass, String newPass, String confirmPass){

		// VALIDATE XSCREENS
		if(StringUtils.isBlank(oldPass)) {
			responseHelper.setErrorStatusAndMessage("Old password required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(newPass)) {
			responseHelper.setErrorStatusAndMessage("New password required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(confirmPass)) {
			responseHelper.setErrorStatusAndMessage("Confirm password required");
			return responseHelper.getResponse();
		}

		if(!newPass.equals(confirmPass)) {
			responseHelper.setErrorStatusAndMessage("New Password and Confirm password not matched");
			return responseHelper.getResponse();
		}

		Optional<Xusers> xusersOp = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
		if(!xusersOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("User not found");
			return responseHelper.getResponse();
		}

		Xusers user = xusersOp.get();

		if(!oldPass.equals(user.getXpassword())) {
			responseHelper.setErrorStatusAndMessage("Old password not valid");
			return responseHelper.getResponse();
		}

		user.setXpassword(newPass);
		user.setXoldpassword(oldPass);
		user = xuserRepo.save(user);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/CP"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Password changed successfully");
		return responseHelper.getResponse();
	}
}
