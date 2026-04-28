package com.zayaanit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @Since Jul 16, 2023
 */
@Controller
@RequestMapping("/report")
public class OnScreenReportController extends AbstractReportController {

	@Override
	protected String screenCode() {
		return null;
	}

	@Override
	protected String pageTitle() {
		return null;
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

}
