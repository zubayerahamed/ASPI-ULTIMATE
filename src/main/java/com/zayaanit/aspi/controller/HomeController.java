package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.aspi.entity.Xfavourites;
import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.Xuserprofiles;
import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.entity.pk.XprofilesPK;
import com.zayaanit.aspi.entity.pk.XusersPK;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Feb 4, 2025
 */
@Controller
@RequestMapping("/")
public class HomeController extends KitController {

	@Override
	protected String screenCode() {
		return "Home";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		return "";
	}

	@ModelAttribute("otherBusinesses")
	protected List<Xusers> otherZbusinesses() {
		List<Xusers> users = xusersRepo.findAllByZemailAndZactive(sessionManager.getLoggedInUserDetails().getUsername(), Boolean.TRUE);
		if (users == null || users.isEmpty()) {
			return Collections.emptyList();
		}

		List<Xusers> allActiveBusinesses = new ArrayList<>();
		for(Xusers user : users) {
			if(!user.getZid().equals(sessionManager.getBusinessId())) {
				Optional<Zbusiness> businessOp = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
				if(businessOp.isPresent()) {
					user.setBusinessName(businessOp.get().getZorg());
					allActiveBusinesses.add(user);
				}
			}
		}

		return allActiveBusinesses;
	}

	@ModelAttribute("otherProfiles")
	protected List<Xprofiles> otherProfiles() {
		Optional<Xusers> userOp = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
		if (!userOp.isPresent()) {
			return Collections.emptyList();
		}

		Xusers user = userOp.get();
		if(Boolean.TRUE.equals(user.getZadmin())) return Collections.emptyList();

		List<Xprofiles> allActiveProfiles = new ArrayList<>();

		List<Xuserprofiles> usersProfiles = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), user.getZemail());
		usersProfiles = usersProfiles.stream().filter(f -> !f.getXprofile().equals(sessionManager.getLoggedInUserDetails().getXprofile().getXprofile())).collect(Collectors.toList());
		usersProfiles.sort(Comparator.comparing(Xuserprofiles::getXprofile));

		for(Xuserprofiles up : usersProfiles) {
			Optional<Xprofiles> profileOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), up.getXprofile()));
			if(profileOp.isPresent()) allActiveProfiles.add(profileOp.get());
		}

		return allActiveProfiles;
	}

	@ModelAttribute("favouriteMenus")
	protected List<Xfavourites> favouriteMenus(){
		return super.favouriteMenus();
	}

	@GetMapping
	public String loadDefaultPage(HttpServletRequest request, @RequestParam(required = false) String frommenu, @RequestParam(required = false) String menucode, Model model) {
		Xfavourites fav = null;
		if(!loggedInUser().isAdmin()) {
			List<Xfavourites> favList = xfavouritesRepo.findAllByZidAndZemailAndXprofileAndXisdefault(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), true);
			fav = favList.stream().findFirst().orElse(null);
		}
		model.addAttribute("favscreen", fav != null ? fav.getXscreen() : "");

		model.addAttribute("masterMenus", menuTreeService.getMenuTree(null));

		return "index";
	}

	@GetMapping("/home")
	public String loadHomePage(HttpServletRequest request, @RequestParam(required = false) String frommenu, @RequestParam(required = false) String menucode, Model model) {

		if(!isAjaxRequest(request)) {
			return "redirect:/";
		}

		if(menucode != null && !"M".equalsIgnoreCase(menucode)) {
			model.addAttribute("menuscreens", menuTreeService.getMenuTree(menucode));
			return "pages/home/home-fragments::screen-form";
		} else if ("M".equalsIgnoreCase(menucode)) {
			model.addAttribute("masterMenus", menuTreeService.getMenuTree(null));
			return "pages/home/home-fragments::main-form";
		}

		model.addAttribute("masterMenus", menuTreeService.getMenuTree(null));

		return "pages/home/home";
	}
}
