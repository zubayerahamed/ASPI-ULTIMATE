 package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.exceptions.ResourceNotFoundException;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcbalRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/FA19")
public class FA19 extends KitController{

	private String pageTitle = null;

	@Autowired private AcbalRepo acbalRepo;

	@Override
	protected String screenCode() {
		return "FA19";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA19"));
		if(!op.isPresent()) return "Business Profile Management";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) throws ResourceNotFoundException {
		model.addAttribute("years", acbalRepo.getDistinctYears(sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/FA19/FA19-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/FA19/FA19";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(YearEndProcess yep){

		if(yep.getXyear() == null) {
			responseHelper.setErrorStatusAndMessage("Year required");
			return responseHelper.getResponse();
		}

		if(yep.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("GL date required");
			return responseHelper.getResponse();
		}

		try {
			acbalRepo.FA_YearEnd(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), yep.getXyear(), yep.getXdate());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA19"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Process confirmed successfully!");
		return responseHelper.getResponse();
	}
}

@Data
class YearEndProcess{
	private Integer xyear;
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xdate;
}
