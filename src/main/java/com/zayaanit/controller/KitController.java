package com.zayaanit.controller;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Xfavourites;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.XfavouritesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.entity.validator.ModelValidator;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.CaitemRepo;
import com.zayaanit.repo.ImcurstockviewRepo;
import com.zayaanit.repo.XcodesRepo;
import com.zayaanit.repo.XfavouritesRepo;
import com.zayaanit.repo.XmenusRepo;
import com.zayaanit.repo.XmenuscreensRepo;
import com.zayaanit.repo.XprofilesRepo;
import com.zayaanit.repo.XprofilesdtRepo;
import com.zayaanit.repo.XscreensRepo;
import com.zayaanit.repo.XuserprofilesRepo;
import com.zayaanit.repo.XusersRepo;
import com.zayaanit.repo.XwhsRepo;
import com.zayaanit.repo.ZbusinessRepo;
import com.zayaanit.service.ImportExportService;
import com.zayaanit.service.MenuTreeService;
import com.zayaanit.service.PrintingService;

/**
 * @author Zubayer Ahaned
 * @since Jan 7, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
public abstract class KitController extends BaseController {

	@Autowired protected ApplicationEventPublisher eventPublisher;
	@Autowired protected MenuTreeService menuTreeService;
	@Autowired protected XmenuscreensRepo xmenuscreensRepo;
	@Autowired protected XmenusRepo xmenusRepo;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected XcodesRepo xcodesRepo;
	@Autowired protected ModelValidator modelValidator;
	@Autowired protected Validator validator;
	@Autowired protected XprofilesRepo xprofilesRepo;
	@Autowired protected XprofilesdtRepo profiledtRepo;
	@Autowired protected PrintingService printingService;
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
