package com.zayaanit.aspi.controller;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.Xfavourites;
import com.zayaanit.aspi.entity.Xprofilesdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.entity.pk.AcsubPK;
import com.zayaanit.aspi.entity.pk.CabunitPK;
import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.entity.pk.XfavouritesPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.entity.pk.XwhsPK;
import com.zayaanit.aspi.entity.validator.ModelValidator;
import com.zayaanit.aspi.model.MyUserDetails;
import com.zayaanit.aspi.model.StockDetail;
import com.zayaanit.aspi.repo.AcsubRepo;
import com.zayaanit.aspi.repo.CabunitRepo;
import com.zayaanit.aspi.repo.CaitemRepo;
import com.zayaanit.aspi.repo.ImcurstockviewRepo;
import com.zayaanit.aspi.repo.XcodesRepo;
import com.zayaanit.aspi.repo.XfavouritesRepo;
import com.zayaanit.aspi.repo.XmenusRepo;
import com.zayaanit.aspi.repo.XmenuscreensRepo;
import com.zayaanit.aspi.repo.XprofilesRepo;
import com.zayaanit.aspi.repo.XprofilesdtRepo;
import com.zayaanit.aspi.repo.XscreensRepo;
import com.zayaanit.aspi.repo.XuserprofilesRepo;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.repo.XwhsRepo;
import com.zayaanit.aspi.repo.ZbusinessRepo;
import com.zayaanit.aspi.service.ImportExportService;
import com.zayaanit.aspi.service.MenuTreeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;

/**
 * @author Zubayer Ahaned
 * @since Jan 7, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
public abstract class KitController extends BaseController {

	@Autowired protected MenuTreeService menuTreeService;
	@Autowired protected XmenuscreensRepo xmenuscreensRepo;
	@Autowired protected XmenusRepo xmenusRepo;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected XcodesRepo xcodesRepo;
	@Autowired protected ModelValidator modelValidator;
	@Autowired protected Validator validator;
	@Autowired protected XprofilesRepo xprofilesRepo;
	@Autowired protected XprofilesdtRepo profiledtRepo;
	@Autowired protected XusersRepo xusersRepo;
	@Autowired protected ZbusinessRepo zbusinessRepo;
	@Autowired protected AcsubRepo acsubRepo;
	@Autowired protected XuserprofilesRepo xuserprofilesRepo;
	@Autowired protected XfavouritesRepo xfavouritesRepo;
	@Autowired protected XprofilesdtRepo xprofilesdtRepo;
	@Autowired protected ImcurstockviewRepo stockRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CabunitRepo cabunitRepo;

	protected List<StockDetail> unavailableStockList = new ArrayList<>();

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}

	@ModelAttribute("pageTitle")
	protected abstract String pageTitle();

	@ModelAttribute("screenCode")
	protected abstract String screenCode();

	@ModelAttribute("isFavorite")
	protected abstract boolean isFavorite();

	@ModelAttribute("loggedInUser")
	protected MyUserDetails loggedInUser() {
		return sessionManager.getLoggedInUserDetails();
	}

	@ModelAttribute("sessionId")
	public String sessionId() {
		return sessionManager.sessionId();
	}

	@ModelAttribute("remoteIp")
	public String remoteIp() {
		return sessionManager.remoteIp();
	}

	@ModelAttribute("userAgent")
	public String userAgent() {
		return sessionManager.userAgent();
	}

	@ModelAttribute("serverIp")
	public String serverIp() {
		return sessionManager.serverIp();
	}


	@ModelAttribute("loginName")
	protected String loginName() {
		MyUserDetails user = sessionManager.getLoggedInUserDetails();
		if(user == null) return "Anonymus User";

		String name = user.getUsername();

		if(user.getXstaff() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), user.getXstaff()));
			if(acsubOp.isPresent()) name = acsubOp.get().getXname();
		}

		if(user.getXprofile() != null) {
			name = name + " - " + user.getXprofile().getXprofile();
		}

		return name;
	}

	@ModelAttribute("loggedInZbusiness")
	protected Zbusiness loggedInZbusiness() {
		return sessionManager.getLoggedInUserDetails().getZbusiness();
	}

	protected List<Xfavourites> favouriteMenus(){
		if(loggedInUser().isAdmin()) return Collections.emptyList();
		if(loggedInUser().getXprofile() == null) return Collections.emptyList();

		List<Xfavourites> favsList = xfavouritesRepo.findAllByZidAndZemailAndXprofile(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile());

		List<Xprofilesdt> profileDetails = xprofilesdtRepo.findAllByXprofileAndZid(loggedInUser().getXprofile().getXprofile(), loggedInZbusiness().getZid());
		List<String> assignedScreens = profileDetails.stream().map(m -> m.getXscreen()).collect(Collectors.toList());

		favsList = favsList.stream().filter(f -> assignedScreens.contains(f.getXscreen())).collect(Collectors.toList());
		favsList.stream().forEach(f -> {
			Optional<Xscreens> sOp = xscreenRepo.findById(new XscreensPK(loggedInZbusiness().getZid(), f.getXscreen()));
			if(sOp.isPresent()) {
				f.setScreenName(sOp.get().getXtitle());
				f.setScreenIcon(sOp.get().getXicon());
			}
		});
		favsList.sort(Comparator.comparing(Xfavourites::getXsequence));
		return favsList;
	}

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}

	protected String filePath(String path) {
		if(StringUtils.isBlank(path)) return "";
		if(!path.endsWith("\\")) return path;
		int lastIndex = path.lastIndexOf("\\");
		if (lastIndex != -1) {
			path = path.substring(0, lastIndex) + path.substring(lastIndex + 1);
		}
		return path;
	}

	protected boolean fileExist(String filePathWithFileName) {
		File file = new File(filePathWithFileName);
		return file.exists();
	}

	protected boolean checkTheScreenIsInFavouriteList(String screenCode) {
		if(StringUtils.isBlank(screenCode)) return false;
		if(loggedInUser().isAdmin()) return false;
		if(loggedInUser().getXprofile() == null) return false;

		Optional<Xfavourites> favOp = xfavouritesRepo.findById(new XfavouritesPK(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), screenCode));
		return favOp.isPresent();
	}

	@GetMapping("/error-details")
	public String errorDetails(Model model) {
		model.addAttribute("stockErrors", unavailableStockList);
		return "commons::error-details";
	}

	protected ImportExportService getImportExportService(String module) {
		if(StringUtils.isBlank(module)) return null;
		try {
			return (ImportExportService) appContext.getBean(module + "ImportExport");
		} catch (Exception e) {
			return null;
		}
	}

	protected String getFileExtension(File file) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf('.');

		if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
			return fileName.substring(lastDotIndex + 1);
		} else {
			return ""; // No extension found
		}
	}

	protected void prepareUnavailableStockList(Map<Integer, BigDecimal> qtyMap, Integer business, Integer store) {
		if(unavailableStockList != null && !unavailableStockList.isEmpty()) {
			unavailableStockList.clear();
		} else {
			unavailableStockList = new ArrayList<>();
		}

		for(Map.Entry<Integer, BigDecimal> itemMap : qtyMap.entrySet()) {
			BigDecimal stock = stockRepo.getCurrentStock(sessionManager.getBusinessId(), business, store, itemMap.getKey());

			if(stock.compareTo(itemMap.getValue()) == -1) {
				StockDetail sd = new StockDetail();
				sd.setItemCode(itemMap.getKey());
				sd.setReqQty(itemMap.getValue());
				sd.setAvailableQty(stock);
				sd.setDeviation(itemMap.getValue().subtract(stock));
				sd.setFromStoreCode(store);
				sd.setFromBusienssCode(business);

				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), itemMap.getKey()));
				if(caitemOp.isPresent()) sd.setItemName(caitemOp.get().getXdesc());

				Optional<Xwhs> storeOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), store));
				if(storeOp.isPresent()) sd.setFromStoreName(storeOp.get().getXname());

				Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), business));
				if(cabunitOp.isPresent()) sd.setFromBusinessUnitName(cabunitOp.get().getXname());

				unavailableStockList.add(sd);
			}
		}
	}
}
