package com.zayaanit.aspi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.exceptions.UnauthorizedException;
import com.zayaanit.aspi.model.QueryWindow;
import com.zayaanit.aspi.model.ReloadSection;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahaned
 * @since Oct 28, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/SA15")
public class SA15 extends KitController {

	@Autowired protected JdbcTemplate jdbcTemplate;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA15";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if (this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA15"));
		if (!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) throws UnauthorizedException {
		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorize Access");
		}

		QueryWindow qw = new QueryWindow();
		model.addAttribute("qw", qw);
		model.addAttribute("displayTable", false);

		if (isAjaxRequest(request)) {
			return "pages/SA15/SA15-fragments::main-form";
		}

		return "pages/SA15/SA15";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(QueryWindow qw) {

		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Unauthorized Access");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(qw.getStatement())) {
			responseHelper.setErrorStatusAndMessage("Statement required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getFromMap("QW_SQL") != null) {
			sessionManager.removeFromMap("QW_SQL");
		}

		String sql = qw.getStatement().trim();

		if("UPDATE QUERY".equalsIgnoreCase(qw.getType()) || "DELETE QUERY".equalsIgnoreCase(qw.getType()) || "INSERT QUERY".equalsIgnoreCase(qw.getType())) {
			try {
				int effectedRows = jdbcTemplate.update(sql);
				List<ReloadSection> reloadSections = new ArrayList<>();
				reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
				responseHelper.setReloadSections(reloadSections);
				responseHelper.setSuccessStatusAndMessage("Process Successfull. Number of effected rows : " + effectedRows);
				return responseHelper.getResponse();
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : " + e.getCause().getMessage());
				return responseHelper.getResponse();
			}
		} else if ("CLEAN BUSINESS".equalsIgnoreCase(qw.getType())) {
			String[] params = sql.split(";");
			if(params.length != 2) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : Invalid Statement");
				return responseHelper.getResponse();
			}
			if(StringUtils.isBlank(params[0].trim()) || StringUtils.isBlank(params[1].trim())) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : Invalid Statement");
				return responseHelper.getResponse();
			}
			try {
				String procedureCall = "{call SA_CleanBusinessData(?, ?)}";
				jdbcTemplate.update(procedureCall, params[0].trim(), params[1].trim());
				List<ReloadSection> reloadSections = new ArrayList<>();
				reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
				responseHelper.setReloadSections(reloadSections);
				responseHelper.setSuccessStatusAndMessage("Process Successfull");
				return responseHelper.getResponse();
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : " + e.getCause().getMessage());
				return responseHelper.getResponse();
			}
		} else if ("CLONE DATA".equalsIgnoreCase(qw.getType())) {
			String[] params = sql.split(";");
			if(params.length != 3) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : Invalid Statement");
				return responseHelper.getResponse();
			}
			if(StringUtils.isBlank(params[0].trim()) || StringUtils.isBlank(params[1].trim()) || StringUtils.isBlank(params[2].trim())) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : Invalid Statement");
				return responseHelper.getResponse();
			}
			try {
				String procedureCall = "{call SA_CloneBusinessData(?, ?, ?)}";
				jdbcTemplate.update(procedureCall, params[0].trim(), params[1].trim(), params[2].trim());
				List<ReloadSection> reloadSections = new ArrayList<>();
				reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
				responseHelper.setReloadSections(reloadSections);
				responseHelper.setSuccessStatusAndMessage("Process Successfull");
				return responseHelper.getResponse();
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : " + e.getCause().getMessage());
				return responseHelper.getResponse();
			}
		} else if ("EXECUTE QUERY".equalsIgnoreCase(qw.getType())) {
			try {
				jdbcTemplate.execute(sql);
				List<ReloadSection> reloadSections = new ArrayList<>();
				reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
				responseHelper.setReloadSections(reloadSections);
				responseHelper.setSuccessStatusAndMessage("Process Successfull");
				return responseHelper.getResponse();
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : " + e.getCause().getMessage());
				return responseHelper.getResponse();
			}
		} 

		sessionManager.addToMap("QW_SQL", sql);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Process Successfully");
		responseHelper.setDisplayMessage(false);
		return responseHelper.getResponse();
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam(required = false) String clear, Model model) throws Exception {
		if("CLEAR".equals(clear)) {
			model.addAttribute("detailSection", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		model.addAttribute("detailSection", true);

		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			model.addAttribute("dataFound", false);
			model.addAttribute("displayTable", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		if(sessionManager.getFromMap("QW_SQL") == null) {
			model.addAttribute("dataFound", false);
			model.addAttribute("displayTable", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		model.addAttribute("displayTable", true);

		String sql = (String) sessionManager.getFromMap("QW_SQL");
		if (sql.endsWith(";")) sql = sql.substring(0, sql.length() - 1);
		if(!sql.toLowerCase().startsWith("select top")) {
			sql = "SELECT TOP 1000 " + sql.substring("select ".length());
		}
		sessionManager.removeFromMap("QW_SQL");

		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			throw new Exception(e.getCause().getMessage());
		}
		if(resultList == null || resultList.isEmpty()) {
			model.addAttribute("dataFound", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}
		model.addAttribute("dataFound", true);

		int totalColumns = resultList.get(0).size();
		List<String> columns = new ArrayList<>();
		resultList.get(0).entrySet().stream().forEach(f -> {
			columns.add(f.getKey());
		});

		model.addAttribute("totalColumns", totalColumns);
		model.addAttribute("columns", columns);
		model.addAttribute("data", resultList);
		return "pages/SA15/SA15-fragments::detail-table";
	}
}
