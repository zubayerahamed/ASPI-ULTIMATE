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

import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Optogli;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.AcmstPK;
import com.zayaanit.aspi.entity.pk.OptogliPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.AcmstRepo;
import com.zayaanit.aspi.repo.OptogliRepo;
import com.zayaanit.aspi.service.OptogliService;

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
@RequestMapping("/FA32")
public class FA32 extends KitController {

	@Autowired private OptogliRepo optogliRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private OptogliService optogliService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA32";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA32"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(
			@RequestParam(required = false) String xtype, 
			@RequestParam(required = false) String xgcus, 
			@RequestParam(required = false) String frommenu,
			HttpServletRequest request, 
			Model model
		) {

		model.addAttribute("cusGrps", xcodesRepo.findAllByXtypeAndZactiveAndZid("Customer Group", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtype) && "RESET".equalsIgnoreCase(xgcus)) {
				model.addAttribute("optogli", Optogli.getDefaultInstance());
				return "pages/FA32/FA32-fragments::main-form";
			}

			Optional<Optogli> optogliOp = optogliRepo.findById(new OptogliPK(sessionManager.getBusinessId(), xtype, xgcus));
			if(!optogliOp.isPresent()) {
				model.addAttribute("optogli", Optogli.getDefaultInstance());
				return "pages/FA32/FA32-fragments::main-form";
			}

			Optogli optogli = optogliOp.get();
			if(optogli.getXaccdr() != null) {
				Optional<Acmst> reAccOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), optogli.getXaccdr()));
				if(reAccOp.isPresent()) {
					optogli.setReceivableAccount(reAccOp.get().getXdesc());
				}
			}

			if(optogli.getXacccr() != null) {
				Optional<Acmst> saAccOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), optogli.getXacccr()));
				if(saAccOp.isPresent()) {
					optogli.setSalesAccount(saAccOp.get().getXdesc());
				}
			}

			if(optogli.getXaccdisc() != null) {
				Optional<Acmst> diAccOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), optogli.getXaccdisc()));
				if(diAccOp.isPresent()) {
					optogli.setDiscountAccount(diAccOp.get().getXdesc());
				}
			}

			if(optogli.getXaccvat() != null) {
				Optional<Acmst> vaAccOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), optogli.getXaccvat()));
				if(vaAccOp.isPresent()) {
					optogli.setVatAccount(vaAccOp.get().getXdesc());
				}
			}

			if(optogli.getXaccait() != null) {
				Optional<Acmst> aiAccOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), optogli.getXaccait()));
				if(aiAccOp.isPresent()) {
					optogli.setAitAccount(aiAccOp.get().getXdesc());
				}
			}

			model.addAttribute("optogli", optogli);
			return "pages/FA32/FA32-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("optogli", Optogli.getDefaultInstance());
		return "pages/FA32/FA32";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/FA32/FA32-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Optogli> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Optogli> list = optogliService.getAll(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());
		int totalRows = optogliService.countAll(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());

		DatatableResponseHelper<Optogli> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Optogli optogli, BindingResult bindingResult){

		if(StringUtils.isBlank(optogli.getXtype())){
			responseHelper.setErrorStatusAndMessage("Type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(optogli.getXgcus())){
			responseHelper.setErrorStatusAndMessage("Customer group required");
			return responseHelper.getResponse();
		}

		if(optogli.getXaccdr() == null){
			responseHelper.setErrorStatusAndMessage("Receivable account required");
			return responseHelper.getResponse();
		}

		if(optogli.getXacccr() == null){
			responseHelper.setErrorStatusAndMessage("Sales account required");
			return responseHelper.getResponse();
		}

		if(optogli.getXaccdisc() == null){
			responseHelper.setErrorStatusAndMessage("Discount account required");
			return responseHelper.getResponse();
		}

		if(optogli.getXaccvat() == null){
			responseHelper.setErrorStatusAndMessage("VAT account required");
			return responseHelper.getResponse();
		}

		if(optogli.getXaccait() == null){
			responseHelper.setErrorStatusAndMessage("AIT account required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateOptogli(optogli, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(optogli.getSubmitFor())) {
			optogli.setZid(sessionManager.getBusinessId());
			try {
				optogli = optogliRepo.save(optogli);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA32"));
			reloadSections.add(new ReloadSection("header-table-container", "/FA32/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Optogli> existOp = optogliRepo.findById(new OptogliPK(sessionManager.getBusinessId(), optogli.getXtype(), optogli.getXgcus()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Optogli existObj = existOp.get();
		BeanUtils.copyProperties(optogli, existObj, "zid", "zuserid", "ztime", "xtype", "xgcus");
		try {
			existObj = optogliRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA32?xtype=" + existObj.getXtype() + "&xgcus=" + existObj.getXgcus()));
		reloadSections.add(new ReloadSection("header-table-container", "/FA32/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xtype, String xgcus){
		Optional<Optogli> existOp = optogliRepo.findById(new OptogliPK(sessionManager.getBusinessId(), xtype, xgcus));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Optogli existObj = existOp.get();
		try {
			optogliRepo.delete(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA32?xtype=REST&xgcus=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/FA32/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
