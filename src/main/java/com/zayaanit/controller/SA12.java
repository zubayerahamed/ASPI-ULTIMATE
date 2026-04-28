package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Xscreenrpdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreenrpdtPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repo.XmenuscreensRepo;
import com.zayaanit.repo.XprofilesdtRepo;
import com.zayaanit.repo.XscreenrpdtRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SA12")
public class SA12 extends KitController {

	private String pageTitle = null;

	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XprofilesdtRepo xprofilesdtRepo;
	@Autowired private XscreenrpdtRepo xscreenrpdtRepo;

	@Override
	protected String screenCode() {
		return "SA12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xscreen, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xscreen)) {
				model.addAttribute("xscreens", Xscreens.getDefaultInstance());
				return "pages/SA12/SA12-fragments::main-form";
			}

			Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
			model.addAttribute("xscreens", op.isPresent() ? op.get() : Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::main-form";
		}

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		model.addAttribute("xscreens", op.isPresent() ? op.get() : Xscreens.getDefaultInstance());
		return "pages/SA12/SA12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xscreen, @RequestParam String xrow, Model model) {
		if("RESET".equalsIgnoreCase(xscreen) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::detail-table";
		}

		Optional<Xscreens> oph = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!oph.isPresent()) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::detail-table";
		}
		model.addAttribute("xscreens", oph.get());

		List<Xscreenrpdt> detailList = xscreenrpdtRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		detailList.sort(Comparator.comparing(Xscreenrpdt::getXseqn));
		model.addAttribute("detailList", detailList);

		if("RESET".equalsIgnoreCase(xrow)) {
			Xscreenrpdt xscreendetail = Xscreenrpdt.getDefaultInstance(xscreen);
			model.addAttribute("xscreendetail", xscreendetail);
			return "pages/SA12/SA12-fragments::detail-table";
		}

		Optional<Xscreenrpdt> xscreendetailOp = xscreenrpdtRepo.findById(new XscreenrpdtPK(sessionManager.getBusinessId(), xscreen, Integer.parseInt(xrow)));
		Xscreenrpdt xscreendetail = xscreendetailOp.isPresent() ? xscreendetailOp.get() : Xscreenrpdt.getDefaultInstance(xscreen);
		model.addAttribute("xscreendetail", xscreendetail);
		return "pages/SA12/SA12-fragments::detail-table";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/SA12/SA12-fragments::header-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xscreens xscreens, BindingResult bindingResult){

		if(StringUtils.isBlank(xscreens.getXscreen())) {
			responseHelper.setErrorStatusAndMessage("Screen code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xscreens.getXtitle())) {
			responseHelper.setErrorStatusAndMessage("Screen title required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xscreens.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Screen type required");
			return responseHelper.getResponse();
		}

		if(xscreens.getXnum() < 0) {
			responseHelper.setErrorStatusAndMessage("Invalid starting from");
			return responseHelper.getResponse();
		}

		if("Report".equalsIgnoreCase(xscreens.getXtype())) {
			if(StringUtils.isBlank(xscreens.getXengine())) {
				xscreens.setXengine("CRYSTAL");
			}
		}

		// VALIDATE XSCREENS
		modelValidator.validateXscreens(xscreens, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xscreens.getSubmitFor())) {
			xscreens.setZid(sessionManager.getBusinessId());
			try {
				xscreens = xscreenRepo.save(xscreens);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreens.getXscreen()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xscreens existObj = op.get();
		BeanUtils.copyProperties(xscreens, existObj, "zid", "zuserid", "ztime", "xscreen");
		try {
			existObj = xscreenRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + existObj.getXscreen()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Xscreenrpdt xscreendetail, BindingResult bindingResult){
		if(xscreendetail.getXscreen() == null) {
			responseHelper.setErrorStatusAndMessage("Screen id not selected");
			return responseHelper.getResponse();
		}

		Optional<Xscreens> oph = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreendetail.getXscreen()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Screen not found");
			return responseHelper.getResponse();
		}

		Xscreens xscreens = oph.get();
		if(!"Report".equals(xscreens.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Selected Screen not a Report type");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xscreendetail.getSubmitFor())) {
			xscreendetail.setXseqn(xscreenrpdtRepo.getNextAvailableSequence(sessionManager.getBusinessId(), xscreendetail.getXscreen()));
			xscreendetail.setXrow(xscreenrpdtRepo.getNextAvailableRow(sessionManager.getBusinessId(), xscreendetail.getXscreen()));
			xscreendetail.setZid(sessionManager.getBusinessId());
			try {
				xscreendetail = xscreenrpdtRepo.save(xscreendetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + xscreendetail.getXscreen()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SA12/detail-table?xscreen=" + xscreendetail.getXscreen() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Field added successfully");
			return responseHelper.getResponse();
		}

		Optional<Xscreenrpdt> existOp = xscreenrpdtRepo.findById(new XscreenrpdtPK(sessionManager.getBusinessId(), xscreendetail.getXscreen(), xscreendetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Field not found in this system");
			return responseHelper.getResponse();
		}

		Xscreenrpdt exist = existOp.get();
		BeanUtils.copyProperties(xscreendetail, exist, "zid", "zuserid", "ztime", "xscreen", "xrow", "xseqn");
		try {
			exist = xscreenrpdtRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + xscreendetail.getXscreen()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SA12/detail-table?xscreen=" + xscreendetail.getXscreen() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Field updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xscreen){
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		try {
			// delete all from profile details
			xprofilesdtRepo.deleteAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
			// delete all from xmenu screens
			xmenuscreensRepo.deleteAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Xscreens obj = op.get();
		try {
			xscreenRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleterptdt(@RequestParam String xscreen, @RequestParam Integer xrow){
		Optional<Xscreenrpdt> existOp = xscreenrpdtRepo.findById(new XscreenrpdtPK(sessionManager.getBusinessId(), xscreen, xrow));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Field not found in this system");
			return responseHelper.getResponse();
		}

		Xscreenrpdt obj = existOp.get();
		try {
			xscreenrpdtRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + xscreen));
		reloadSections.add(new ReloadSection("detail-table-container", "/SA12/detail-table?xscreen=" + xscreen + "&xrow=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Field deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail-table/seqn/{direction}")
	public String updateSequence(@PathVariable String direction, @RequestParam String xscreen, @RequestParam Integer xrow, Model model){
		Optional<Xscreens> oph = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!oph.isPresent()) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());
			model.addAttribute("xscreendetail", Xscreenrpdt.getDefaultInstance(xscreen));
			return "pages/SA12/SA12-fragments::detail-table";
		}
		model.addAttribute("xscreens", oph.get());
		model.addAttribute("xscreendetail", Xscreenrpdt.getDefaultInstance(xscreen));

		Optional<Xscreenrpdt> existOp = xscreenrpdtRepo.findById(new XscreenrpdtPK(sessionManager.getBusinessId(), xscreen, xrow));
		if(!existOp.isPresent()) return "pages/SA12/SA12-fragments::detail-table";

		Xscreenrpdt xscreendetail = existOp.get();

		// Check xscreen has more than one field? if don't then return existing lists only
		List<Xscreenrpdt> detailList = xscreenrpdtRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		if(detailList == null || detailList.isEmpty()) {
			model.addAttribute("detailList", Collections.emptyList());
			return "pages/SA12/SA12-fragments::detail-table";
		}
		if(detailList.size() < 2) {
			detailList.sort(Comparator.comparing(Xscreenrpdt::getXseqn));
			model.addAttribute("detailList", detailList);
			return "pages/SA12/SA12-fragments::detail-table";
		}

		Integer existingSeqn = xscreendetail.getXseqn();
		Integer currentSeqn = null;
		if("UP".equalsIgnoreCase(direction)) {
			currentSeqn = existingSeqn - 1;
		} else {
			currentSeqn = existingSeqn + 1;
		}

		Optional<Xscreenrpdt> effecteddtOp = xscreenrpdtRepo.findByZidAndXscreenAndXseqn(sessionManager.getBusinessId(), xscreen, currentSeqn);
		if(!effecteddtOp.isPresent()) {
			detailList.sort(Comparator.comparing(Xscreenrpdt::getXseqn));
			model.addAttribute("detailList", detailList);
			return "pages/SA12/SA12-fragments::detail-table";
		}

		Xscreenrpdt effecteddt = effecteddtOp.get();
		if("UP".equalsIgnoreCase(direction)) {
			effecteddt.setXseqn(effecteddt.getXseqn() + 1);
		} else {
			effecteddt.setXseqn(effecteddt.getXseqn() - 1);
		}

		xscreendetail.setXseqn(currentSeqn);

		try {
			xscreenrpdtRepo.save(xscreendetail);
			xscreenrpdtRepo.save(effecteddt);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		detailList = xscreenrpdtRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		detailList.sort(Comparator.comparing(Xscreenrpdt::getXseqn));
		model.addAttribute("detailList", detailList);
		return "pages/SA12/SA12-fragments::detail-table";
	}
}