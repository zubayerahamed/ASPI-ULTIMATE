package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

import com.zayaanit.aspi.entity.Xmenus;
import com.zayaanit.aspi.entity.Xmenuscreens;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XmenusPK;
import com.zayaanit.aspi.entity.pk.XmenuscreensPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.XmenusRepo;
import com.zayaanit.aspi.repo.XmenuscreensRepo;
import com.zayaanit.aspi.repo.XscreensRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SA13")
public class SA13 extends KitController {

	private String pageTitle = null;

	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XmenusRepo xmenusRepo;
	@Autowired private XscreensRepo xscreensRepo;

	@Override
	protected String screenCode() {
		return "SA13";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xrow, HttpServletRequest request, Model model) {
		generateHeaderData(model);

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xrow)) {
				model.addAttribute("xmenuscreens", Xmenuscreens.getDefaultInstance());
				return "pages/SA13/SA13-fragments::main-form";
			}

			Optional<Xmenuscreens> op = xmenuscreensRepo.findById(new XmenuscreensPK(sessionManager.getBusinessId(), Integer.valueOf(xrow)));
			model.addAttribute("xmenuscreens", op.isPresent() ? op.get() : Xmenuscreens.getDefaultInstance());
			return "pages/SA13/SA13-fragments::main-form";
		}

		if(StringUtils.isNotBlank(xrow)) {
			if("RESET".equalsIgnoreCase(xrow)) {
				model.addAttribute("xmenuscreens", Xmenuscreens.getDefaultInstance());
			} else {
				Optional<Xmenuscreens> op = xmenuscreensRepo.findById(new XmenuscreensPK(sessionManager.getBusinessId(), Integer.valueOf(xrow)));
				model.addAttribute("xmenuscreens", op.isPresent() ? op.get() : Xmenuscreens.getDefaultInstance());
			}
		} else {
			model.addAttribute("xmenuscreens", Xmenuscreens.getDefaultInstance());
		}
		return "pages/SA13/SA13";
	}

	private void generateHeaderData(Model model) {
		List<Xmenuscreens> list = xmenuscreensRepo.findAllByZid(sessionManager.getBusinessId());
		list.stream().forEach(f -> {
			Optional<Xmenus> xmenusOp = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), f.getXmenu()));
			if(xmenusOp.isPresent()) {
				f.setXmenuTitle(xmenusOp.get().getXtitle());
				f.setXmenuSequence(xmenusOp.get().getXsequence());
			}
			Optional<Xscreens> xscreensOp = xscreensRepo.findById(new XscreensPK(sessionManager.getBusinessId(), f.getXscreen()));
			if(xscreensOp.isPresent()) {
				f.setXscreenTitle(xscreensOp.get().getXtitle());
				f.setXscreenType(xscreensOp.get().getXtype());
			}
		});
		list.sort(Comparator.comparing(Xmenuscreens::getXmenuSequence));
		Map<String, List<Xmenuscreens>> groupedByXmenu = new HashMap<>();
		list.stream().forEach(l -> {
			if(groupedByXmenu.get(l.getXmenu() + " - " + l.getXmenuTitle()) != null) {
				groupedByXmenu.get(l.getXmenu() + " - " + l.getXmenuTitle()).add(l);
			} else {
				List<Xmenuscreens> mslist = new ArrayList<>();
				mslist.add(l);
				groupedByXmenu.put(l.getXmenu() + " - " + l.getXmenuTitle(), mslist);
			}
		});
		model.addAttribute("menuscreensGroup", groupedByXmenu);
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		generateHeaderData(model);
		return "pages/SA13/SA13-fragments::header-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xmenuscreens xmenuscreens, BindingResult bindingResult){

		if(StringUtils.isBlank(xmenuscreens.getXmenu())) {
			responseHelper.setErrorStatusAndMessage("Menu code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xmenuscreens.getXscreen())) {
			responseHelper.setErrorStatusAndMessage("Screen code required");
			return responseHelper.getResponse();
		}

		if(xmenuscreens.getXsequence() < 0) {
			responseHelper.setErrorStatusAndMessage("Invalid sequence");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateXmenuscreens(xmenuscreens, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xmenuscreens.getSubmitFor())) {
			xmenuscreens.setXrow(xmenuscreensRepo.getNextAvailableRow(sessionManager.getBusinessId()));
			xmenuscreens.setZid(sessionManager.getBusinessId());
			try {
				xmenuscreens = xmenuscreensRepo.save(xmenuscreens);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA13?xrow=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA13/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xmenuscreens> op = xmenuscreensRepo.findById(new XmenuscreensPK(sessionManager.getBusinessId(), xmenuscreens.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xmenuscreens existObj = op.get();
		BeanUtils.copyProperties(xmenuscreens, existObj, "zid", "zuserid", "ztime", "xrow");
		try {
			existObj = xmenuscreensRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA13?xrow=" + existObj.getXrow()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA13/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xrow){
		Optional<Xmenuscreens> op = xmenuscreensRepo.findById(new XmenuscreensPK(sessionManager.getBusinessId(), xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xmenuscreens obj = op.get();
		try {
			xmenuscreensRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA13?xrow=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA13/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
	
}