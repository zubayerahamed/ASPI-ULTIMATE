package com.zayaanit.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.repo.XprofilesRepo;
import com.zayaanit.repo.XprofilesdtRepo;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2024
 */
@Controller
@RequestMapping("/switchprofile")
public class SwitchProfileController extends BaseController {

	@Autowired private XprofilesRepo xprofileRepo;
	@Autowired private XprofilesdtRepo xprofilesdtRepo;

	@GetMapping
	public String loadProfile(@RequestParam String xprofile, @RequestParam(required = false) String switchprofile, HttpServletRequest request) {
		Optional<Xprofiles> profileOp = xprofileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		if(!profileOp.isPresent()) return "redirect:/profiles";

		Xprofiles profile = profileOp.get();
		profile.setDetails(xprofilesdtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId()));

		sessionManager.getLoggedInUserDetails().setXprofile(profile);

		if("Y".equalsIgnoreCase(switchprofile)) {
			xlogsService.switchProfile(request);
		}

		return "redirect:/";
	}

}
