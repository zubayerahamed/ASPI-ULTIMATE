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

import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.CabunitRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD17")
public class AD17 extends KitController {

	@Autowired private CabunitRepo cabunitRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xbuid, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xbuid)) {
				model.addAttribute("cabunit", Cabunit.getDefaultInstance());
				return "pages/AD17/AD17-fragments::main-form";
			}

			Optional<Cabunit> op = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), Integer.parseInt(xbuid)));
			model.addAttribute("cabunit", op.isPresent() ? op.get() : Cabunit.getDefaultInstance());
			return "pages/AD17/AD17-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("cabunit", Cabunit.getDefaultInstance());
		return "pages/AD17/AD17";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/AD17/AD17-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Cabunit cabunit, BindingResult bindingResult){

		if(cabunit.getXbuid() == null) {
			Integer xbuid = cabunitRepo.getNextAvailableId(sessionManager.getBusinessId());
			cabunit.setXbuid(xbuid);
		}

		if(StringUtils.isBlank(cabunit.getXname())) {
			responseHelper.setErrorStatusAndMessage("Business unit name required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateCabunit(cabunit, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(cabunit.getSubmitFor())) {
			cabunit.setZid(sessionManager.getBusinessId());
			try {
				cabunit = cabunitRepo.save(cabunit);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD17?xbuid=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/AD17/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Cabunit> op = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), cabunit.getXbuid()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Cabunit existObj = op.get();
		BeanUtils.copyProperties(cabunit, existObj, "zid", "zuserid", "ztime", "xbuid");

		try {
			existObj = cabunitRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xbuid=" + existObj.getXbuid()));
		reloadSections.add(new ReloadSection("list-table-container", "/AD17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xbuid){
		Optional<Cabunit> op = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), xbuid));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Cabunit obj = op.get();
		try {
			cabunitRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xbuid=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/AD17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
