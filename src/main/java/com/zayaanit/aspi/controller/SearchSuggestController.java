package com.zayaanit.aspi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.aspi.entity.Acgroup;
import com.zayaanit.aspi.entity.Acheader;
import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.Imadjheader;
import com.zayaanit.aspi.entity.Imissueheader;
import com.zayaanit.aspi.entity.Imopenheader;
import com.zayaanit.aspi.entity.Imtorheader;
import com.zayaanit.aspi.entity.Moheader;
import com.zayaanit.aspi.entity.Opcrnheader;
import com.zayaanit.aspi.entity.Opdoheader;
import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.entity.Pocrnheader;
import com.zayaanit.aspi.entity.Pogrnheader;
import com.zayaanit.aspi.entity.Poordheader;
import com.zayaanit.aspi.entity.Xmenus;
import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.model.DatatableRequestHelper;
import com.zayaanit.aspi.model.DatatableResponseHelper;
import com.zayaanit.aspi.service.AcgroupService;
import com.zayaanit.aspi.service.AcheaderService;
import com.zayaanit.aspi.service.AcmstService;
import com.zayaanit.aspi.service.AcsubService;
import com.zayaanit.aspi.service.CabunitService;
import com.zayaanit.aspi.service.CaitemService;
import com.zayaanit.aspi.service.ImadjheaderService;
import com.zayaanit.aspi.service.ImissueheaderService;
import com.zayaanit.aspi.service.ImopenheaderService;
import com.zayaanit.aspi.service.ImtorheaderService;
import com.zayaanit.aspi.service.MoheaderService;
import com.zayaanit.aspi.service.OpcrnheaderService;
import com.zayaanit.aspi.service.OpdoheaderService;
import com.zayaanit.aspi.service.OpordheaderService;
import com.zayaanit.aspi.service.PocrnheaderService;
import com.zayaanit.aspi.service.PogrnheaderService;
import com.zayaanit.aspi.service.PoordheaderService;
import com.zayaanit.aspi.service.XmenusService;
import com.zayaanit.aspi.service.XprofilesService;
import com.zayaanit.aspi.service.XscreensService;
import com.zayaanit.aspi.service.XusersService;
import com.zayaanit.aspi.service.XwhsService;

import jakarta.servlet.http.HttpServletRequest;

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
	@Autowired private OpcrnheaderService opcrnheaderService;

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
