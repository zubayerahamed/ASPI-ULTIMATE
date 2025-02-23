package com.zayaanit.aspi.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.ReportMenu;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
@Controller
@RequestMapping("/{rptcode}")
public class ReportController extends AbstractReportController{

	private String pageTitle = null;
	private String screenCode = null;

	@Override
	protected String screenCode() {
		return this.screenCode;
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), this.screenCode));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@PathVariable String rptcode, HttpServletRequest request, Model model) {
		this.screenCode = rptcode;
		model.addAttribute("isFavorite", isFavorite());

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), rptcode));
		if(op.isPresent()) this.pageTitle = op.get().getXtitle();

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("screenCode", rptcode);
		model.addAttribute("xtitle", pageTitle);

		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf(rptcode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			if(!isAjaxRequest(request)) {
				return "redirect:/";
			}

			model.addAttribute("error", "Page Not found");
			model.addAttribute("status", "404");
			return "pages/404";
		}

		model.addAttribute("reportFound", true);
		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", rm.getDescription());
		model.addAttribute("reportCode", rm.name());

		return "pages/RP/RP";
	}

}
