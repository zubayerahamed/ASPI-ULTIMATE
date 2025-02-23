package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Imtogli;
import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.AcmstPK;
import com.zayaanit.aspi.entity.pk.ImtogliPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcmstRepo;
import com.zayaanit.aspi.repo.ImtogliRepo;
import com.zayaanit.aspi.service.ImtogliService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 13, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/FA33")
public class FA33 extends KitController {

	@Autowired private ImtogliRepo imtogliRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private ImtogliService imtogliService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA33";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA33"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(
			@RequestParam(required = false) String xtype,  
			@RequestParam(required = false) String xgitem,
			@RequestParam(required = false) String frommenu,
			HttpServletRequest request, 
			Model model
		) {

		model.addAttribute("issueTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Issue Type", Boolean.TRUE, sessionManager.getBusinessId()));

		List<Xcodes> itemGroups = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
		itemGroups = itemGroups.stream().filter(f-> !"Services".equals(f.getXcode())).collect(Collectors.toList());
		model.addAttribute("itemGrps", itemGroups);

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtype) && "RESET".equalsIgnoreCase(xgitem)) {
				model.addAttribute("imtogli", Imtogli.getDefaultInstance());
				return "pages/FA33/FA33-fragments::main-form";
			}

			Optional<Imtogli> imtogliOp = imtogliRepo.findById(new ImtogliPK(sessionManager.getBusinessId(), xtype, xgitem));
			if(!imtogliOp.isPresent()) {
				model.addAttribute("imtogli", Imtogli.getDefaultInstance());
				return "pages/FA33/FA33-fragments::main-form";
			}

			Imtogli imtogli = imtogliOp.get();
			if(imtogli.getXaccdr() != null) {
				Optional<Acmst> debitacOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), imtogli.getXaccdr()));
				if(debitacOp.isPresent()) {
					imtogli.setDebitAccount(debitacOp.get().getXdesc());
				}
			}

			if(imtogli.getXacccr() != null) {
				Optional<Acmst> creditacOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), imtogli.getXacccr()));
				if(creditacOp.isPresent()) {
					imtogli.setCreditAccount(creditacOp.get().getXdesc());
				}
			}

			model.addAttribute("imtogli", imtogli);
			return "pages/FA33/FA33-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imtogli", Imtogli.getDefaultInstance());
		return "pages/FA33/FA33";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/FA33/FA33-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Imtogli> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imtogli> list = imtogliService.getAll(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());
		int totalRows = imtogliService.countAll(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());

		DatatableResponseHelper<Imtogli> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imtogli imtogli, BindingResult bindingResult){

		if(StringUtils.isBlank(imtogli.getXtype())){
			responseHelper.setErrorStatusAndMessage("Type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(imtogli.getXgitem())){
			responseHelper.setErrorStatusAndMessage("Item group required");
			return responseHelper.getResponse();
		}

		if(imtogli.getXaccdr() == null){
			responseHelper.setErrorStatusAndMessage("Debit account required");
			return responseHelper.getResponse();
		}

		if(imtogli.getXacccr() == null){
			responseHelper.setErrorStatusAndMessage("Credit account required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateImtogli(imtogli, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(imtogli.getSubmitFor())) {
			imtogli.setZid(sessionManager.getBusinessId());
			try {
				imtogli = imtogliRepo.save(imtogli);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA33"));
			reloadSections.add(new ReloadSection("header-table-container", "/FA33/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imtogli> existOp = imtogliRepo.findById(new ImtogliPK(sessionManager.getBusinessId(), imtogli.getXtype(), imtogli.getXgitem()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imtogli existObj = existOp.get();
		BeanUtils.copyProperties(imtogli, existObj, "zid", "zuserid", "ztime", "xtype", "xgitem");
		try {
			existObj = imtogliRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtype=" + existObj.getXtype() + "&xgitem=" + existObj.getXgitem()));
		reloadSections.add(new ReloadSection("header-table-container", "/FA33/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xtype, String xgsup, String xgitem){
		Optional<Imtogli> existOp = imtogliRepo.findById(new ImtogliPK(sessionManager.getBusinessId(), xtype, xgitem));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Imtogli existObj = existOp.get();
		try {
			imtogliRepo.delete(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtype=REST&xgitem=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/FA33/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
