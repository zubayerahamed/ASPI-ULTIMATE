package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XmenusPK;
import com.zayaanit.entity.pk.XmenuscreensPK;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.XmenusRepo;
import com.zayaanit.repo.XmenuscreensRepo;
import com.zayaanit.repo.XprofilesRepo;
import com.zayaanit.repo.XprofilesdtRepo;
import com.zayaanit.repo.XscreensRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Slf4j
@Controller
@RequestMapping("/AD12")
public class AD12 extends KitController {

	@Autowired private XprofilesRepo profileRepo;
	@Autowired private XprofilesdtRepo profileDtRepo;
	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XmenusRepo xmenusRepo;
	@Autowired private XscreensRepo xscreensRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xprofile, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xprofile)) {
				model.addAttribute("profile", Xprofiles.getDefaultInstance());
				return "pages/AD12/AD12-fragments::main-form";
			}

			Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
			model.addAttribute("profile", op.isPresent() ? op.get() : Xprofiles.getDefaultInstance());

			if(op.isPresent()) {
				eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("AD12")
						.xfunc("View Data")
						.xsource("AD12")
						.xtable(null)
						.xdata(op.get().getXprofile().toString())
						.xstatement("View Data : " + op.get().toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
			}

			return "pages/AD12/AD12-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		model.addAttribute("profile", op.isPresent() ? op.get() : Xprofiles.getDefaultInstance());
		return "pages/AD12/AD12";
	}

	@GetMapping("/{xprofile}")
	public String loadFormFragment(@PathVariable String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("profile", Xprofiles.getDefaultInstance());
			return "pages/AD12/AD12-fragments::main-form";
		}

		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		model.addAttribute("profile", op.isPresent() ? op.get() : Xprofiles.getDefaultInstance());
		return "pages/AD12/AD12-fragments::main-form";
	}

	@GetMapping("/detail-table")
	public String loadDetailTableFragment(@RequestParam String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("xprofile", xprofile);
			model.addAttribute("mgMap", Collections.emptyMap());
			return "pages/AD12/AD12-fragments::detail-table";
		}

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
		list = list.stream().filter(f -> !"System".equalsIgnoreCase(f.getXscreenType())).collect(Collectors.toList());
		list.sort(Comparator.comparing(Xmenuscreens::getXmenuSequence));

		List<Xprofilesdt> profileDetails =  profileDtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		if(!profileDetails.isEmpty()) {
			// Make checked status
			for (Xprofilesdt dt : profileDetails) {
				Xmenuscreens ms = list.stream().filter(f -> f.getXmenu().equalsIgnoreCase(dt.getXmenu()) && f.getXscreen().equalsIgnoreCase(dt.getXscreen())).findFirst().orElse(null);
				if(ms == null) continue;
				ms.setProfileChecked(true);
			}
		}

		Map<String, MenuScreenGroup> mgMap = new LinkedHashMap<>();
		list.stream().forEach(l -> {
			if(mgMap.get(l.getXmenu() + " - " + l.getXmenuTitle()) != null) {
				MenuScreenGroup msg = mgMap.get(l.getXmenu() + " - " + l.getXmenuTitle());
				msg.getMenus().add(l);
			} else {
				MenuScreenGroup msg = new MenuScreenGroup();
				msg.setMenucode(l.getXmenu());
				msg.setMenuname(l.getXmenuTitle());
				msg.getMenus().add(l);
				mgMap.put(l.getXmenu() + " - " + l.getXmenuTitle(), msg);
			}
		});

		model.addAttribute("xprofile", xprofile);
		model.addAttribute("mgMap", mgMap);
		return "pages/AD12/AD12-fragments::detail-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xprofiles xprofiles, BindingResult bindingResult){

		if(StringUtils.isBlank(xprofiles.getXprofile())) {
			responseHelper.setErrorStatusAndMessage("Profile name required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateProfile(xprofiles, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xprofiles.getSubmitFor())) {
			xprofiles.setZid(sessionManager.getBusinessId());
			try {
				xprofiles = profileRepo.save(xprofiles);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("AD12")
					.xfunc("Add Data")
					.xsource("AD12")
					.xtable(null)
					.xdata(xprofiles.getXprofile().toString())
					.xstatement("Add Data : " + xprofiles.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD12?xprofile=" + xprofiles.getXprofile()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table?xprofile=" + xprofiles.getXprofile()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofiles.getXprofile()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xprofiles existObj = op.get();
		BeanUtils.copyProperties(xprofiles, existObj, "zid", "zuserid", "ztime", "xprofile");
		try {
			existObj = profileRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD12")
				.xfunc("Update Data")
				.xsource("AD12")
				.xtable(null)
				.xdata(existObj.getXprofile().toString())
				.xstatement("Update Data : " + existObj.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD12?xprofile=" + existObj.getXprofile()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table?xprofile=" + xprofiles.getXprofile()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping(value = "/detail/save", headers="Accept=application/json")
	public @ResponseBody Map<String, Object> saveDetail(@RequestBody String json){
		DetailData dd = new DetailData();

		ObjectMapper obm = new ObjectMapper();
		try {
			dd = obm.readValue(json, DetailData.class);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e); 
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		List<Xprofilesdt> existingData = profileDtRepo.findAllByXprofileAndZid(dd.getProfileName(), sessionManager.getBusinessId());
		if(existingData != null && !existingData.isEmpty()) {
			profileDtRepo.deleteAll(existingData);
		}

		List<Xprofilesdt> list = new ArrayList<>();
		for(String xscreen : dd.getXscreens()) {
			Optional<Xmenuscreens> xmenuscreensOp = xmenuscreensRepo.findById(new XmenuscreensPK(sessionManager.getBusinessId(), Integer.valueOf(xscreen)));
			if(!xmenuscreensOp.isPresent()) continue;

			Xprofilesdt dt = new Xprofilesdt();
			dt.setZid(sessionManager.getBusinessId());
			dt.setXprofile(dd.getProfileName());
			dt.setXmenu(xmenuscreensOp.get().getXmenu());
			dt.setXscreen(xmenuscreensOp.get().getXscreen());
			list.add(dt);
		}

		try {
			profileDtRepo.saveAll(list);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD12")
				.xfunc("Update Detail")
				.xsource("AD12")
				.xtable(null)
				.xdata(dd.getProfileName())
				.xstatement("Update Profile Detail Data : " + list.stream().map(Xprofilesdt::toString).collect(Collectors.joining(", ")))
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table?xprofile=" + dd.getProfileName()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Details saved successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xprofile){
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		List<Xprofilesdt> details = profiledtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			try {
				profiledtRepo.deleteAll(details);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}
		}

		Xprofiles obj = op.get();
		try {
			profileRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD12")
				.xfunc("Delete Data")
				.xsource("AD12")
				.xtable(null)
				.xdata(obj.getXprofile().toString())
				.xstatement("Delete Data : " + obj.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD12?xprofile=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table?xprofile=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}

@Data
class DetailData{
	private String[] xscreens;
	private String profileName;
}

@Data
class MenuScreenGroup{
	private String menucode;
	private String menuname;
	private List<Xmenuscreens> menus = new ArrayList<>();
	@SuppressWarnings("unused")
	private boolean allSelected;

	public boolean isAllSelected() {
		if(this.menus == null || this.menus.isEmpty()) return false;

		int menuCount = menus.size();
		int checkedCount = menus.stream().filter(f -> f.isProfileChecked()).collect(Collectors.toList()).size();
		return menuCount == checkedCount;
	}
}
