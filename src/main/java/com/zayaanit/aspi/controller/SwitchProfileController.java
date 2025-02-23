package com.zayaanit.aspi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.pk.XprofilesPK;
import com.zayaanit.aspi.repo.XprofilesRepo;
import com.zayaanit.aspi.repo.XprofilesdtRepo;

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
	public String loadProfile(@RequestParam String xprofile) {
		Optional<Xprofiles> profileOp = xprofileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		if(!profileOp.isPresent()) return "redirect:/profiles";

		Xprofiles profile = profileOp.get();
		profile.setDetails(xprofilesdtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId()));

		sessionManager.getLoggedInUserDetails().setXprofile(profile);
		return "redirect:/";
	}

}
