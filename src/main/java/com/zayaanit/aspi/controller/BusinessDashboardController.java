package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.repo.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Controller
@RequestMapping("/business")
public class BusinessDashboardController extends BaseController {

	@Autowired
	private ZbusinessRepo zbusinessRepo;
	@Autowired
	private XusersRepo xusersRepo;

	@GetMapping
	public String loadBusinessDashboard(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		List<Xusers> list = xusersRepo.findAllByZemailAndZactive(username, Boolean.TRUE);

		List<Zbusiness> businesses = new ArrayList<>();
		for (Xusers user : list) {
			Optional<Zbusiness> zbop = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
			if (!zbop.isPresent())
				continue;

			Zbusiness zb = zbop.get();
			businesses.add(zb);
		}

		if(!businesses.isEmpty() && businesses.size() == 1) {
			sessionManager.getLoggedInUserDetails().setZbusiness(businesses.get(0));
			sessionManager.addToMap("LOGIN_FLAG", "Y");
			return "redirect:/";
		}

		model.addAttribute("businesses", businesses);

		return "pages/business/business-dashboard";
	}
}
