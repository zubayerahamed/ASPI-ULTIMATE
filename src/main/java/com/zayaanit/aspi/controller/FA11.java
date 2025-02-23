package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.zayaanit.aspi.entity.Acdef;
import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.AcdefPK;
import com.zayaanit.aspi.entity.pk.AcmstPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcdefRepo;
import com.zayaanit.aspi.repo.AcmstRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA11")
public class FA11 extends KitController {

	@Autowired private AcdefRepo acdefRepo;
	@Autowired private AcmstRepo acmstRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA11";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		Optional<Acdef> acdefOp = acdefRepo.findById(new AcdefPK(sessionManager.getBusinessId()));
		Acdef acdef = acdefOp.isPresent() ? acdefOp.get() : Acdef.getDefaultInstance();
		if(acdef.getXaccpl() != null) {
			Optional<Acmst> acmstOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdef.getXaccpl()));
			if(acmstOp.isPresent()) acdef.setAccountName(acmstOp.get().getXdesc());
		}
		if(acdef.getXaccmc() != null) {
			Optional<Acmst> acmstOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdef.getXaccmc()));
			if(acmstOp.isPresent()) acdef.setCostAccountName(acmstOp.get().getXdesc());
		}
		model.addAttribute("acdef", acdef);
		if(isAjaxRequest(request) && frommenu == null) return "pages/FA11/FA11-fragments::main-form";
		if(frommenu == null) return "redirect:/";
		return "pages/FA11/FA11";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acdef acdef, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateAcdef(acdef, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);
	
		if(acdef.getXoffset() == null) {
			responseHelper.setErrorStatusAndMessage("Fiscal Year required");
			return responseHelper.getResponse();
		}

		if(acdef.getXclyear() == null) {
			responseHelper.setErrorStatusAndMessage("Closed year required");
			return responseHelper.getResponse();
		}

		if(acdef.getXaccpl() == null) {
			responseHelper.setErrorStatusAndMessage("Retained Earnings Account required");
			return responseHelper.getResponse();
		}

		acdef.setXcldate(new Date());

		// Create new
		if(SubmitFor.INSERT.equals(acdef.getSubmitFor())) {
			acdef.setZid(sessionManager.getBusinessId());
			try {
				acdef = acdefRepo.save(acdef);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA11"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acdef> op = acdefRepo.findById(new AcdefPK(sessionManager.getBusinessId()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acdef existObj = op.get();
		BeanUtils.copyProperties(acdef, existObj, "zid", "zuserid", "ztime", "xcldate");

		try {
			existObj = acdefRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA11"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}
}
