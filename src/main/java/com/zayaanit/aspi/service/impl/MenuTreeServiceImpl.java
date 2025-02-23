package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Xmenus;
import com.zayaanit.aspi.entity.Xmenuscreens;
import com.zayaanit.aspi.entity.Xprofilesdt;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XmenusPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.model.MenuTree;
import com.zayaanit.aspi.model.MyUserDetails;
import com.zayaanit.aspi.repo.XmenusRepo;
import com.zayaanit.aspi.repo.XmenuscreensRepo;
import com.zayaanit.aspi.repo.XprofilesdtRepo;
import com.zayaanit.aspi.repo.XscreensRepo;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.MenuTreeService;

/**
 * @author Zubayer Ahamed
 * @since Feb 4, 2025
 */
@Service
public class MenuTreeServiceImpl extends AbstractService implements MenuTreeService {

	@Autowired private KitSessionManager sessionManager;
	@Autowired private XmenusRepo xmenusRepo;
	@Autowired private XprofilesdtRepo profiledtRepo;
	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XscreensRepo xscreenRepo;

	@Override
	public List<MenuTree> getMenuTree(String menucode) {
		MyUserDetails user = sessionManager.getLoggedInUserDetails();
		if(user == null) return Collections.emptyList();

		List<MenuTree> masterMenus = new ArrayList<MenuTree>();

		if(user.isAdmin()) {
			List<Xmenus> masters = new ArrayList<>();
			if(StringUtils.isBlank(menucode) || "M".equalsIgnoreCase(menucode)) {
				 masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
			} else {
				Optional<Xmenus> xmenusOp = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), menucode));
				if(xmenusOp.isPresent()) masters.add(xmenusOp.get());
			}
			if(StringUtils.isNotBlank(menucode)) masters = masters.stream().filter(f -> f.getXmenu().equalsIgnoreCase(menucode)).collect(Collectors.toList());
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenu(xmenu, null);
				masterMenus.add(mtree);
			}
		} else {
			if(user.getXprofile() == null) return Collections.emptyList();

			List<Xprofilesdt> profileDetails = profiledtRepo.findAllByXprofileAndZid(user.getXprofile().getXprofile(), sessionManager.getBusinessId());
			if(profileDetails.isEmpty()) return Collections.emptyList();

			List<Xmenus> masters = new ArrayList<>();
			if(StringUtils.isBlank(menucode) || "M".equalsIgnoreCase(menucode)) {
				 masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
			} else {
				Optional<Xmenus> xmenusOp = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), menucode));
				if(xmenusOp.isPresent()) masters.add(xmenusOp.get());
			}
			if(StringUtils.isNotBlank(menucode)) masters = masters.stream().filter(f -> f.getXmenu().equalsIgnoreCase(menucode)).collect(Collectors.toList());
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenuUsingProfile(xmenu, profileDetails);
				masterMenus.add(mtree);
			}
		}

		return masterMenus;
	}

	private MenuTree constractTheMenuUsingProfile(Xmenus xmenu, List<Xprofilesdt> profileDetails) {
		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());
		mtree.setParentCode(xmenu.getXpmenu());

		// get all the assigned screens
		List<Xmenuscreens> screens = new ArrayList<>();
		if(profileDetails.stream().filter(f -> f.getXmenu().equals(xmenu.getXmenu())).count() > 0) {
			List<String> approvedScreens = profileDetails.stream().filter(f -> f.getXmenu().equals(xmenu.getXmenu())).collect(Collectors.mapping(Xprofilesdt::getXscreen, Collectors.toList()));
			if(approvedScreens != null && !approvedScreens.isEmpty()) { 
				screens = xmenuscreensRepo.findAllByZidAndXmenuAndXscreenIn(sessionManager.getBusinessId(), xmenu.getXmenu(), approvedScreens);
			}
		}
		screens.sort(Comparator.comparing(Xmenuscreens::getXsequence));
		for(Xmenuscreens screen : screens) {
			Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen.getXscreen()));
			if(xscreenOp.isPresent()) mtree.getScreens().add(xscreenOp.get());
		}

		// get all the submenus
		Set<String> approvedMenus = profileDetails.stream().collect(Collectors.mapping(Xprofilesdt::getXmenu, Collectors.toSet()));
		List<Xmenus> subMenus = xmenusRepo.findAllByZidAndXpmenuAndXmenuIn(sessionManager.getBusinessId(), xmenu.getXmenu(), approvedMenus);
		subMenus.sort(Comparator.comparing(Xmenus::getXsequence));
		for(Xmenus subMenu : subMenus) {
			mtree.getSubMenus().add(constractTheMenuUsingProfile(subMenu, profileDetails));
		}

		return mtree;
	}

	private MenuTree constractTheMenu(Xmenus xmenu, Map<String, List<String>> menuWithScreenMap) {

		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());
		mtree.setParentCode(xmenu.getXpmenu());

		// get all the assigned screens
		List<Xmenuscreens> screens = new ArrayList<>();
		if(menuWithScreenMap == null) {
			screens = xmenuscreensRepo.findAllByZidAndXmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		} else {
			if(menuWithScreenMap.get(xmenu.getXmenu()) != null) {
				screens = xmenuscreensRepo.findAllByZidAndXmenuAndXscreenIn(sessionManager.getBusinessId(), xmenu.getXmenu(), menuWithScreenMap.get(xmenu.getXmenu()));
			}
		}
		screens.sort(Comparator.comparing(Xmenuscreens::getXsequence));
		for(Xmenuscreens screen : screens) {
			Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen.getXscreen()));
			if(xscreenOp.isPresent()) mtree.getScreens().add(xscreenOp.get());
		}

		// get sub menus
		List<Xmenus> subMenus = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		for(Xmenus subMenu : subMenus) {
			mtree.getSubMenus().add(constractTheMenu(subMenu, menuWithScreenMap));
		}

		return mtree;

	}
}
