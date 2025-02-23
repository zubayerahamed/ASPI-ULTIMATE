package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Xfavourites;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.XfavouritesPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XusersPK;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.XfavouritesRepo;
import com.zayaanit.aspi.repo.XscreensRepo;
import com.zayaanit.aspi.repo.XusersRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Slf4j
@Controller
@RequestMapping("/AD15")
public class AD15 extends KitController {

	@Autowired private XusersRepo xusersRepo;
	@Autowired private XfavouritesRepo xfavouritesRepo;
	@Autowired private XscreensRepo xscreenRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		Optional<Xusers> usersOp = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
		if(!usersOp.isPresent()) return "redirect:/";

		Xusers user = usersOp.get(); 
		if(user.getXstaff() != null) {
			Optional<Acsub> employeeOp = acsubRepo.findById(new AcsubPK(loggedInZbusiness().getZid(), user.getXstaff()));
			if(employeeOp.isPresent()) user.setEmployeeName(employeeOp.get().getXname());
		}

		model.addAttribute("user", user);

		if(isAjaxRequest(request) && frommenu == null) return "pages/AD15/AD15-fragments::cp-form";
		if(frommenu == null) return "redirect:/";
		return "pages/AD15/AD15";
	}

	@Transactional
	@PostMapping("/cp/store")
	public @ResponseBody Map<String, Object> store(String oldPass, String newPass, String confirmPass){

		// VALIDATE DATA
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

		Optional<Xusers> xusersOp = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
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
		try {
			user = xusersRepo.save(user);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// get all the users with this zeamil and change password for them
		List<Xusers> usersList = xusersRepo.findAllByZemail(loggedInUser().getUsername());
		try {
			usersList.stream().forEach(u -> {
				u.setXpassword(newPass);
				u.setXoldpassword(oldPass);
				xusersRepo.save(u);
			});
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("cp-form-container", "/AD15"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Password changed successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/add-to-favourite")
	public @ResponseBody Map<String, Object> addToFavorite(@RequestParam String screen, @RequestParam String pagetitle){

		if(loggedInUser().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Admin user not allowed to add or remove favorite");
			return responseHelper.getResponse();
		}

		Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen));
		if(!xscreenOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid screen");
			return responseHelper.getResponse();
		}

		if(loggedInUser().getXprofile() == null) {
			responseHelper.setErrorStatusAndMessage("You don't have any profile.");
			return responseHelper.getResponse();
		}

		// Check already added to fav
		Optional<Xfavourites> favOp = xfavouritesRepo.findById(new XfavouritesPK(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), screen));
		if(favOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Screen already added to favorite list");
			return responseHelper.getResponse();
		}

		// Check total no of favorites in db
		List<Xfavourites> list = xfavouritesRepo.findAllByZidAndZemailAndXprofile(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile());
		if(list.size() == 6) {
			responseHelper.setErrorStatusAndMessage("You already reached the maximum number of favorite links. Please remove previous any one to add new.");
			return responseHelper.getResponse();
		}

		Xfavourites fav = new Xfavourites();
		fav.setZid(sessionManager.getBusinessId());
		fav.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		fav.setXprofile(loggedInUser().getXprofile().getXprofile());
		fav.setXscreen(xscreenOp.get().getXscreen());
		fav.setXtype(xscreenOp.get().getXtype());
		fav.setXsequence(xfavouritesRepo.getNextAvailableSequence(loggedInZbusiness().getZid()));
		fav.setXisdefault(false);
		try {
			fav = xfavouritesRepo.save(fav);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("page-header-container", "/AD15/page-header?screen=" + screen + "&pagetitle=" + pagetitle + "&favorite=YES"));
		reloadSections.add(new ReloadSection("favorite-links-container", "/AD15/favorite-links"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Added to favorite");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/remove-from-favourite")
	public @ResponseBody Map<String, Object> removeFromFavorite(@RequestParam String screen, @RequestParam String pagetitle){

		if(loggedInUser().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Admin user not allowed to add or remove favorite");
			return responseHelper.getResponse();
		}

		Optional<Xfavourites> favOp = xfavouritesRepo.findById(new XfavouritesPK(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), screen));
		if(!favOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Screen not found in favorite list");
			return responseHelper.getResponse();
		}

		try {
			xfavouritesRepo.delete(favOp.get());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("page-header-container", "/AD15/page-header?screen=" + screen + "&pagetitle=" + pagetitle + "&favorite=NO"));
		reloadSections.add(new ReloadSection("favorite-links-container", "/AD15/favorite-links"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Removed from favorite");
		return responseHelper.getResponse();
	}

	@GetMapping("/page-header")
	public String loadPageHeader(@RequestParam String screen, @RequestParam String pagetitle, @RequestParam String favorite, Model model) {
		model.addAttribute("screenCode", screen);
		model.addAttribute("pageTitle", pagetitle);
		model.addAttribute("isFavorite", "YES".equalsIgnoreCase(favorite));
		return "commons::page-header";
	}

	@GetMapping("/favorite-links")
	public String loadFavoriteLinks(Model model) {
		model.addAttribute("favouriteMenus", favouriteMenus());
		return "commons::favorite-links";
	}

	@Transactional
	@PostMapping("/make-default")
	public @ResponseBody Map<String, Object> makeDefaultFavorite(@RequestParam String screen){

		if(loggedInUser().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Admin user not allowed to add default favorite");
			return responseHelper.getResponse();
		}

		Optional<Xfavourites> favOp = xfavouritesRepo.findById(new XfavouritesPK(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), screen));
		if(!favOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Screen not found in favorite list");
			return responseHelper.getResponse();
		}

		// find the previous default and remove it
		List<Xfavourites> favList = xfavouritesRepo.findAllByZidAndZemailAndXprofileAndXisdefault(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), true);
		try {
			favList.stream().forEach(f -> {
				f.setXisdefault(false);
				xfavouritesRepo.save(f);
			});
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Xfavourites fav = favOp.get();
		fav.setXisdefault(true);
		try {
			xfavouritesRepo.save(fav);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		responseHelper.setDisplayMessage(false);
		responseHelper.setSuccessStatusAndMessage("Success");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/switch-color-mode")
	public @ResponseBody Map<String, Object> switchColorMode(@RequestParam String colormode){

		Optional<Xusers> userOp = xusersRepo.findById(new XusersPK(loggedInZbusiness().getZid(), loggedInUser().getUsername()));
		if(!userOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid user");
			return responseHelper.getResponse();
		}

		Xusers user = userOp.get();
		user.setXtheme(colormode);
		try {
			xusersRepo.save(user);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		responseHelper.setDisplayMessage(false);
		responseHelper.setSuccessStatusAndMessage("Color mode changed successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/favorite-reorder")
	public String reorderFavoriteLinks(String screenDatas, Model model){

		if(StringUtils.isBlank(screenDatas)) {
			model.addAttribute("favouriteMenus", favouriteMenus());
			return "commons::favorite-links";
		}

		try {
			String[] pairs = screenDatas.split("\\|");

			Map<String, Integer> resultMap = new HashMap<>();
			for (String pair : pairs) {
				// Split each pair by ':' to get the key and value
				String[] keyValue = pair.split(":");
				if(keyValue.length == 2) {
					String key = keyValue[0];
					Integer value = Integer.parseInt(keyValue[1]);
					resultMap.put(key, value);
				}
			}

			List<Xfavourites> favList = xfavouritesRepo.findAllByZidAndZemailAndXprofile(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile());
			try {
				favList.stream().forEach(f -> {
					f.setXsequence(resultMap.get(f.getXscreen()));
					xfavouritesRepo.save(f);
				});
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

		} catch (Exception e) {
			log.error(ERROR, e.getMessage());
			model.addAttribute("favouriteMenus", favouriteMenus());
			return "commons::favorite-links";
		}

		model.addAttribute("favouriteMenus", favouriteMenus());
		return "commons::favorite-links";
	}
}