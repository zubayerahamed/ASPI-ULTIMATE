
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

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.AcmstRepo;
import com.zayaanit.repo.AcsubRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA14")
public class FA14 extends KitController {

	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcmstRepo acmstRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA14";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xsub, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("customerGroups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Customer Group", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("supplierGroups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Supplier Group", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xsub)) {
				model.addAttribute("acsub", Acsub.getDefaultInstance());
				return "pages/FA14/FA14-fragments::main-form";
			}

			Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), Integer.parseInt(xsub)));
			Acsub acsub = null;
			if(op.isPresent()) {
				acsub = op.get();

				Optional<Acmst> groupOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), op.get().getXacc()));
				if(groupOp.isPresent()) {
					acsub.setAccountName(groupOp.get().getXdesc());
				}
			}
			model.addAttribute("acsub", acsub != null ? acsub : Acsub.getDefaultInstance());
			
			if(acsub != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("FA14")
							.xfunc("View Data")
							.xsource("FA14")
							.xtable(null)
							.xdata(acsub.getXsub().toString())
							.xstatement("View data : " + acsub.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}
			
			return "pages/FA14/FA14-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("acsub", Acsub.getDefaultInstance());
		return "pages/FA14/FA14";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/FA14/FA14-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acsub acsub, BindingResult bindingResult){

		if(acsub.getXsub() == null) {
			Integer xsub = acsubRepo.getNextAvailableId(sessionManager.getBusinessId());
			acsub.setXsub(xsub);
		}

		// VALIDATE XSCREENS
		modelValidator.validateAcsub(acsub, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(StringUtils.isBlank(acsub.getXname())) {
			responseHelper.setErrorStatusAndMessage("Account name required");
			return responseHelper.getResponse();
		}

		if(acsub.getXsub() == null && StringUtils.isBlank(acsub.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Account Type required");
			return responseHelper.getResponse();
		}

		if(acsub.getXsub() == null && "Sub Account".equals(acsub.getXtype())) {
			if(acsub.getXacc() == null) {
				responseHelper.setErrorStatusAndMessage("Account selection required if account type is Sub Account");
				return responseHelper.getResponse();
			}
		}

		if("Customer".equals(acsub.getXtype())) {
			if(StringUtils.isBlank(acsub.getXgcus())) {
				responseHelper.setErrorStatusAndMessage("Customer group selection required for Customer Type");
				return responseHelper.getResponse();
			}
		}

		if("Supplier".equals(acsub.getXtype())) {
			if(StringUtils.isBlank(acsub.getXgsup())) {
				responseHelper.setErrorStatusAndMessage("Supplier group selection required for Supplier Type");
				return responseHelper.getResponse();
			}
		}

		// Create new
		if(SubmitFor.INSERT.equals(acsub.getSubmitFor())) {
			acsub.setZid(sessionManager.getBusinessId());
			try {
				acsub = acsubRepo.save(acsub);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA14")
						.xfunc("Add Data")
						.xsource("FA14")
						.xtable(null)
						.xdata(acsub.getXsub().toString())
						.xstatement("Add data : " + acsub.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA14/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acsub.getXsub()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acsub existObj = op.get();
		BeanUtils.copyProperties(acsub, existObj, "zid", "zuserid", "ztime", "xsub", "xacc", "xtype");

		try {
			existObj = acsubRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA14")
					.xfunc("Update Data")
					.xsource("FA14")
					.xtable(null)
					.xdata(existObj.getXsub().toString())
					.xstatement("Update data : " + existObj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=" + existObj.getXsub()));
		reloadSections.add(new ReloadSection("list-table-container", "/FA14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xsub){
		Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), xsub));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Acsub obj = op.get();
		try {
			acsubRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("FA14")
					.xfunc("Delete Data")
					.xsource("FA14")
					.xtable(null)
					.xdata(obj.getXsub().toString())
					.xstatement("Delete data : " + obj.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA14/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xaccfield")
	public String loadXaccFieldFragment(@RequestParam String xtype, Model model){
		Acsub acsub = Acsub.getDefaultInstance();
		acsub.setXtype(xtype);
		model.addAttribute("acsub", acsub);
		if("Customer".equals(xtype)) model.addAttribute("customerGroups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Customer Group", Boolean.TRUE, sessionManager.getBusinessId()));
		if("Supplier".equals(xtype)) model.addAttribute("supplierGroups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Supplier Group", Boolean.TRUE, sessionManager.getBusinessId()));
		return "pages/FA14/FA14-fragments::xacc-field";
	}
}
