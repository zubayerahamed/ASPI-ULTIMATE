package com.zayaanit.aspi.controller;

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

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.XwhsRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD11")
public class MD11 extends KitController {

	@Autowired private XwhsRepo xwhsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD11";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xwh, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xwh)) {
				model.addAttribute("xwhs", Xwhs.getDefaultInstance());
				return "pages/MD11/MD11-fragments::main-form";
			}

			Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), Integer.parseInt(xwh)));
			model.addAttribute("xwhs", op.isPresent() ? op.get() : Xwhs.getDefaultInstance());
			return "pages/MD11/MD11-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("xwhs", Xwhs.getDefaultInstance());
		return "pages/MD11/MD11";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/MD11/MD11-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xwhs xwhs, BindingResult bindingResult){

		if(xwhs.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xwhs.getXlocation())) {
			responseHelper.setErrorStatusAndMessage("Store location required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xwhs.getXname())) {
			responseHelper.setErrorStatusAndMessage("Store name required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateXwhs(xwhs, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xwhs.getSubmitFor())) {
			xwhs.setZid(sessionManager.getBusinessId());
			try {
				xwhs = xwhsRepo.save(xwhs);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/MD11/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwhs.getXwh()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xwhs existObj = op.get();
		BeanUtils.copyProperties(xwhs, existObj, "zid", "zuserid", "ztime", "xwh");

		try {
			existObj = xwhsRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=" + existObj.getXwh()));
		reloadSections.add(new ReloadSection("list-table-container", "/MD11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xwh){
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwh));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xwhs obj = op.get();
		try {
			xwhsRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/MD11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
