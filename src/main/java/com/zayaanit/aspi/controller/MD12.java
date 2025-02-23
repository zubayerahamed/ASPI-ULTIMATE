package com.zayaanit.aspi.controller;

import java.math.BigDecimal;
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

import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.CaitemRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD12")
public class MD12 extends KitController {

	@Autowired
	private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if (this.pageTitle != null)
			return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD12"));
		if (!op.isPresent())
			return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xitem, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("groups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("categories", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("uoms", xcodesRepo.findAllByXtypeAndZactiveAndZid("Unit of Measurement", Boolean.TRUE, sessionManager.getBusinessId()));

		if (isAjaxRequest(request) && frommenu == null) {
			if ("RESET".equalsIgnoreCase(xitem)) {
				model.addAttribute("caitem", Caitem.getDefaultInstance());
				return "pages/MD12/MD12-fragments::main-form";
			}

			Optional<Caitem> op = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), Integer.parseInt(xitem)));
			model.addAttribute("caitem", op.isPresent() ? op.get() : Caitem.getDefaultInstance());
			return "pages/MD12/MD12-fragments::main-form";
		}

		if (frommenu == null)
			return "redirect:/";

		model.addAttribute("caitem", Caitem.getDefaultInstance());
		return "pages/MD12/MD12";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/MD12/MD12-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Caitem caitem, BindingResult bindingResult) {

		if (StringUtils.isBlank(caitem.getXdesc())) {
			responseHelper.setErrorStatusAndMessage("Item name required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(caitem.getXgitem())) {
			responseHelper.setErrorStatusAndMessage("Item group required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(caitem.getXunit())) {
			responseHelper.setErrorStatusAndMessage("Unit of Measurement required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(caitem.getXctype())) {
			responseHelper.setErrorStatusAndMessage("Consumption type required");
			return responseHelper.getResponse();
		}

		if (caitem.getXcost() == null || caitem.getXcost().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Default Purchase Rate");
			return responseHelper.getResponse();
		}

		if (caitem.getXrate() == null || caitem.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Default Sale Rate");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateCaitem(caitem, bindingResult, validator);
		if (bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if (SubmitFor.INSERT.equals(caitem.getSubmitFor())) {
			caitem.setXitem(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD12"));
			caitem.setZid(sessionManager.getBusinessId());
			caitem = caitemRepo.save(caitem);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD12?xitem=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/MD12/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Caitem> op = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), caitem.getXitem()));
		if (!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Caitem existObj = op.get();
		BeanUtils.copyProperties(caitem, existObj, "zid", "zuserid", "ztime", "xitem");

		try {
			existObj = caitemRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD12?xitem=" + existObj.getXitem()));
		reloadSections.add(new ReloadSection("list-table-container", "/MD12/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xitem) {
		Optional<Caitem> op = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
		if (!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Caitem obj = op.get();
		try {
			caitemRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD12?xitem=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/MD12/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
