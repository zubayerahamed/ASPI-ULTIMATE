package com.zayaanit.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Xlogs;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.model.SA17SearchParam;
import com.zayaanit.model.SA17SearchTable;
import com.zayaanit.service.XlogsService;
import com.zayaanit.service.XlogsdtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SA17")
public class SA17 extends KitController {

	private String pageTitle = null;

	@Autowired private XlogsService xlogsService;
	@Autowired private XlogsdtService xlogsdtService;

	@Override
	protected String screenCode() {
		return "SA17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xscreen, HttpServletRequest request, Model model) {
		model.addAttribute("searchParam", SA17SearchParam.getDefaultInstance());

		if(isAjaxRequest(request)) {
			return "pages/SA17/SA17-fragments::main-form";
		}

		return "pages/SA17/SA17";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SA17SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SA17/SA17-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<SA17SearchTable> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) String zemail,
		@RequestParam(required = false) Integer xstaff,
		@RequestParam String xtype
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SA17SearchParam param = new SA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setZemail(zemail);
		param.setXstaff(xstaff);
		param.setXtype(xtype);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<SA17SearchTable> list = new ArrayList<>();
		int	totalRows = 0;
		if("Moderate".equals(xtype)){
			List<Xlogs> resultList = xlogsService.SA17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

			resultList.stream().forEach(r -> {
				list.add(
					SA17SearchTable.builder()
					.xid(r.getXid())
					.zid(r.getZid())
					.xsession(r.getXsession())
					.xcip(r.getXcip())
					.zemail(r.getZemail())
					.xprofile(r.getXprofile())
					.xstaff(r.getXstaff())
					.xaction(r.getXaction())
					.ztime(r.getZtime())
					.xdate(r.getXdate())
					.xuseragent(r.getXuseragent())
					.build()
				);
			});

			totalRows = xlogsService.SA17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		} else {
			List<Xlogsdt> resultList = xlogsdtService.SA17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

			resultList.stream().forEach(r -> {
				list.add(
					SA17SearchTable.builder()
					.xid(r.getXid())
					.zid(r.getZid())
					.xsession(r.getXsession())
					.zemail(r.getZemail())
					.xstaff(r.getXstaff())
					.ztime(r.getZtime())
					.xdate(r.getXdate())
					.xscreen(r.getXscreen())
					.xfunc(r.getXfunc())
					.xsource(r.getXsource())
					.xtable(r.getXtable())
					.xdata(r.getXdata())
					.xstatement(r.getXstatement())
					.xresult(r.getXresult())
					.build()
				);
			});

			totalRows = xlogsdtService.SA17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		}

//		xlogsdtService.save(new Xlogsdt("PO13", "Search", this.pageTitle, param.toString(), null , false, 0));

		DatatableResponseHelper<SA17SearchTable> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/delete")
	public @ResponseBody Map<String, Object> voucherDelete(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) String zemail,
		@RequestParam(required = false) Integer xstaff,
		@RequestParam String xtype
	) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SA17SearchParam param = new SA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setZemail(zemail);
		param.setXstaff(xstaff);
		param.setXtype(xtype);

		try {
			if("Moderate".equals(xtype)){
				xlogsService.SA17delete(param);
			} else {
				xlogsdtService.SA17delete(param);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("zemail", zemail != null ? zemail.toString() : ""));
		postData.add(new ReloadSectionParams("xstaff", xstaff != null ? xstaff.toString(): ""));
		postData.add(new ReloadSectionParams("xtype", xtype));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Data deleted successfully");
		return responseHelper.getResponse();
	}
}