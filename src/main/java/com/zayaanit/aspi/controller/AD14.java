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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XcodesPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.model.ReloadSectionParams;
import com.zayaanit.aspi.service.XcodesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD14")
public class AD14 extends KitController {

	@Autowired private XcodesService xcodesService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD14";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xtype, @RequestParam(required = false) String xcode, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtype) && "RESET".equalsIgnoreCase(xcode)) {
				model.addAttribute("xcodes", Xcodes.getDefaultInstance());
				model.addAttribute("codeTypes", xcodesRepo.findAllByXtypeAndZid("Code Type", sessionManager.getBusinessId()));
				return "pages/AD14/AD14-fragments::main-form";
			}

			Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xtype, xcode));
			model.addAttribute("xcodes", op.isPresent() ? op.get() : Xcodes.getDefaultInstance());
			model.addAttribute("codeTypes", xcodesRepo.findAllByXtypeAndZid("Code Type", sessionManager.getBusinessId()));
			return "pages/AD14/AD14-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("xcodes", Xcodes.getDefaultInstance());
		model.addAttribute("codeTypes", xcodesRepo.findAllByXtypeAndZid("Code Type", sessionManager.getBusinessId()));
		return "pages/AD14/AD14";
	}

	@PostMapping("/index")
	public String index2(String xtype, String xcode, Model model) {
		Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xtype, xcode));
		model.addAttribute("xcodes", op.isPresent() ? op.get() : Xcodes.getDefaultInstance());
		model.addAttribute("codeTypes", xcodesRepo.findAllByXtypeAndZid("Code Type", sessionManager.getBusinessId()));
		return "pages/AD14/AD14-fragments::main-form";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/AD14/AD14-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Xcodes> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xcodes> list = xcodesService.getAll(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());
		int totalRows = xcodesService.countAll(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());

		DatatableResponseHelper<Xcodes> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xcodes xcodes, BindingResult bindingResult){

		if(StringUtils.isBlank(xcodes.getXtype())){
			responseHelper.setErrorStatusAndMessage("Code type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xcodes.getXcode())){
			responseHelper.setErrorStatusAndMessage("Code required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateXcodes(xcodes, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xcodes.getSubmitFor())) {
			xcodes.setZid(sessionManager.getBusinessId());
			try {
				xcodes = xcodesRepo.save(xcodes);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSectionParams> postData = new ArrayList<>();
			postData.add(new ReloadSectionParams("xtype", xcodes.getXtype()));
			postData.add(new ReloadSectionParams("xcode", xcodes.getXcode()));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD14/index", postData));
			reloadSections.add(new ReloadSection("header-table-container", "/AD14/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xcodes.getXtype(), xcodes.getXcode()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xcodes existObj = op.get();
		BeanUtils.copyProperties(xcodes, existObj, "zid", "zuserid", "ztime", "xtype", "xcode");
		try {
			existObj = xcodesRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}


		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xtype", existObj.getXtype()));
		postData.add(new ReloadSectionParams("xcode", existObj.getXcode()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD14/index", postData));
		reloadSections.add(new ReloadSection("header-table-container", "/AD14/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xtype, String xcode){
		Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xtype, xcode));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xcodes obj = op.get();
		try {
			xcodesRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD14?xtype=REST&xcode=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/AD14/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
