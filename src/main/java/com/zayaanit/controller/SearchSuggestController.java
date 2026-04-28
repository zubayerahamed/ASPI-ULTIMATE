package com.zayaanit.controller;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.entity.Acgroup;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imadjheader;
import com.zayaanit.entity.Imconvheader;
import com.zayaanit.entity.Imissueheader;
import com.zayaanit.entity.Imopenheader;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Moheader;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Pocrnheader;
import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.service.AcgroupService;
import com.zayaanit.service.AcheaderService;
import com.zayaanit.service.AcmstService;
import com.zayaanit.service.AcsubService;
import com.zayaanit.service.CabunitService;
import com.zayaanit.service.CaitemService;
import com.zayaanit.service.ImadjheaderService;
import com.zayaanit.service.ImconvheaderService;
import com.zayaanit.service.ImissueheaderService;
import com.zayaanit.service.ImopenheaderService;
import com.zayaanit.service.ImtorheaderService;
import com.zayaanit.service.MoheaderService;
import com.zayaanit.service.OpcrnheaderService;
import com.zayaanit.service.OpdoheaderService;
import com.zayaanit.service.OpordheaderService;
import com.zayaanit.service.PocrnheaderService;
import com.zayaanit.service.PogrnheaderService;
import com.zayaanit.service.PoordheaderService;
import com.zayaanit.service.XmenusService;
import com.zayaanit.service.XprofilesService;
import com.zayaanit.service.XscreensService;
import com.zayaanit.service.XusersService;
import com.zayaanit.service.XwhsService;
import com.zayaanit.service.XwidgetsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController {

	@Autowired private XmenusService xmenusService;
	@Autowired private XscreensService xscreensService;
	@Autowired private XprofilesService profileService;
	@Autowired private XusersService xusersService;
	@Autowired private CabunitService cabunitService;
	@Autowired private AcgroupService acgroupService;
	@Autowired private AcmstService acmstService;
	@Autowired private AcsubService acsubService;
	@Autowired private AcheaderService acheaderService;
	@Autowired private XwhsService xwhsService;
	@Autowired private CaitemService caitemService;
	@Autowired private PoordheaderService poordheaderService;
	@Autowired private PogrnheaderService pogrnheaderService;
	@Autowired private PocrnheaderService pocrnheaderService;
	@Autowired private OpordheaderService opordheaderService;
	@Autowired private OpdoheaderService opdoheaderService;
	@Autowired private ImtorheaderService imtorheaderService;
	@Autowired private ImissueheaderService imissueheaderService;
	@Autowired private ImadjheaderService imadjheaderService;
	@Autowired private ImopenheaderService imopenheaderService;
	@Autowired private MoheaderService moheaderService;
	@Autowired private ImconvheaderService imconvheaderService;
	@Autowired private OpcrnheaderService opcrnheaderService;
	@Autowired private XwidgetsService xwidgetsService;

	@GetMapping("/menus")
	public String loadSearchdMenus(@RequestParam String hint, Model model) {
		model.addAttribute("menus", StringUtils.isBlank(hint) ? Collections.emptyList() : xscreensService.searchMenus(hint));
		return "search-fragments::menus-table";
	}

	@PostMapping("/table/{fragmentcode}/{suffix}")
	public String loadHeaderTableFragment(
			@RequestParam(required = false) String hint, 
			@RequestParam(required = false) String dependentparam,
			@RequestParam(required = false) String resetparam, 
			@PathVariable String fragmentcode, 
			@PathVariable int suffix, 
			String fieldId, 
			boolean mainscreen,
			String mainreloadurl,
			String mainreloadid,
			String extrafieldcontroller,
			String detailreloadurl,
			String detailreloadid,
			String additionalreloadurl,
			String additionalreloadid,
			@RequestParam(required = false) boolean hasdeletebtn,
			Model model){

		model.addAttribute("suffix", suffix);
		model.addAttribute("searchValue", hint);
		model.addAttribute("dependentParam", dependentparam);
		model.addAttribute("resetParam", resetparam);
		model.addAttribute("fieldId", fieldId);
		model.addAttribute("mainscreen", mainscreen);
		model.addAttribute("mainreloadurl", mainreloadurl);
		model.addAttribute("mainreloadid", mainreloadid);
		model.addAttribute("extrafieldcontroller", extrafieldcontroller);
		model.addAttribute("detailreloadurl", detailreloadurl);
		model.addAttribute("detailreloadid", detailreloadid);
		model.addAttribute("additionalreloadurl", additionalreloadurl);
		model.addAttribute("additionalreloadid", additionalreloadid);
		model.addAttribute("tablename", System.currentTimeMillis());
		model.addAttribute("fragmentcode", fragmentcode);
		model.addAttribute("hasdeletebtn", hasdeletebtn);
		return "search-fragments::" + fragmentcode + "-table";
	}

	@PostMapping("/LSA11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xmenus> LSA11(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xmenus> list = xmenusService.LSA11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xmenusService.LSA11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xmenus> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}


	@PostMapping("/LSA12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xscreens> LSA12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xscreens> list = xscreensService.LSA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xscreensService.LSA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xscreens> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LSA14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xwidgets> LSA14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xwidgets> list = xwidgetsService.LSA14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xwidgetsService.LSA14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xwidgets> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LAD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xprofiles> LAD12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xprofiles> list = profileService.LAD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = profileService.LAD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xprofiles> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LAD13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xusers> LAD13(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xusers> list = xusersService.LAD13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xusersService.LAD13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xusers> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LAD17/{suffix}")
	public @ResponseBody DatatableResponseHelper<Cabunit> LAD17(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Cabunit> list = cabunitService.LAD17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = cabunitService.LAD17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Cabunit> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LMD11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xwhs> LMD11(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xwhs> list = xwhsService.LMD11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xwhsService.LMD11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xwhs> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LMD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Caitem> LMD12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Caitem> list = caitemService.LMD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = caitemService.LMD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Caitem> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LFA12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acgroup> LFA12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acgroup> list = acgroupService.LFA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acgroupService.LFA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acgroup> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LFA13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acmst> LFA13(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acmst> list = acmstService.LFA13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acmstService.LFA13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acmst> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LFA14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acsub> LFA14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acsub> list = acsubService.LFA14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acsubService.LFA14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acsub> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LFA15/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acheader> LFA15(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acheader> list = acheaderService.LFA15(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acheaderService.LFA15(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LPO12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Poordheader> LPO12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Poordheader> list = poordheaderService.LPO12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = poordheaderService.LPO12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Poordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LPO14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Pogrnheader> LPO14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Pogrnheader> list = pogrnheaderService.LPO14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = pogrnheaderService.LPO14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Pogrnheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LPO16/{suffix}")
	public @ResponseBody DatatableResponseHelper<Pocrnheader> LPO16(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Pocrnheader> list = pocrnheaderService.LPO16(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = pocrnheaderService.LPO16(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Pocrnheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imtorheader> LIM11(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imtorheader> list = imtorheaderService.LIM11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = imtorheaderService.LIM11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Imtorheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imissueheader> LIM13(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imissueheader> list = imissueheaderService.LIM13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = imissueheaderService.LIM13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Imissueheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Moheader> LIM14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Moheader> list = moheaderService.LIM14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = moheaderService.LIM14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Moheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM15/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imadjheader> LIM15(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imadjheader> list = imadjheaderService.LIM15(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = imadjheaderService.LIM15(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Imadjheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM16/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imopenheader> LIM16(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imopenheader> list = imopenheaderService.LIM16(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = imopenheaderService.LIM16(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Imopenheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LIM17/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imconvheader> LIM17(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imconvheader> list = imconvheaderService.LIM17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = imconvheaderService.LIM17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Imconvheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LSO12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opordheader> LSO12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opordheader> list = opordheaderService.LSO12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opordheaderService.LSO12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Opordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LSO14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opdoheader> LSO14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opdoheader> list = opdoheaderService.LSO14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opdoheaderService.LSO14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Opdoheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/LSO16/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opcrnheader> LSO16(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opcrnheader> list = opcrnheaderService.LSO16(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opcrnheaderService.LSO16(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Opcrnheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}
}
