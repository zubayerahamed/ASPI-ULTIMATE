package com.zayaanit.controller;

import java.util.ArrayList;
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

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwidgetsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repo.XwidgetsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/SA14")
public class SA14 extends KitController {

	@Autowired private XwidgetsRepo xwidgetsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA14";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xwidget, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xwidget)) {
				model.addAttribute("xwidgets", Xwidgets.getDefaultInstance());
				return "pages/SA14/SA14-fragments::main-form";
			}

			Optional<Xwidgets> op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), xwidget));
			model.addAttribute("xwidgets", op.isPresent() ? op.get() : Xwidgets.getDefaultInstance());
			return "pages/SA14/SA14-fragments::main-form";
		}

		Optional<Xwidgets> op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), xwidget));
		model.addAttribute("xwidgets", op.isPresent() ? op.get() : Xwidgets.getDefaultInstance());
		return "pages/SA14/SA14";
	}

	@GetMapping("/header-table")
	public String headerTable(Model model) {
		return "pages/SA14/SA14-fragments::header-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xwidgets xwidgets, BindingResult bindingResult){

		if(StringUtils.isBlank(xwidgets.getXwidget())) {
			responseHelper.setErrorStatusAndMessage("Widget code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xwidgets.getXtitle())) {
			responseHelper.setErrorStatusAndMessage("Widget title required");
			return responseHelper.getResponse();
		}

		if(xwidgets.getXdefault() == null) {
			responseHelper.setErrorStatusAndMessage("Default value required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateXwidgets(xwidgets, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xwidgets.getSubmitFor())) {
			xwidgets.setZid(sessionManager.getBusinessId());
			try {
				xwidgets = xwidgetsRepo.save(xwidgets);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA14?xwidget=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA14/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xwidgets> op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), xwidgets.getXwidget()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xwidgets existObj = op.get();
		BeanUtils.copyProperties(xwidgets, existObj, "zid", "zuserid", "ztime", "xwidget");
		try {
			existObj = xwidgetsRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA14?xwidget=" + xwidgets.getXwidget()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA14/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}


	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xwidget){
		Optional<Xwidgets> op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), xwidget));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xwidgets obj = op.get();
		try {
			xwidgetsRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA14?xwidget=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA14/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}