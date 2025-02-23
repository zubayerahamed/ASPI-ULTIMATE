package com.zayaanit.aspi.controller;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	private static final String LOGAIN_PAGE_PATH = "pages/login/login";
	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}

	@GetMapping
	public String loadLoginPage(Model model, @RequestParam(required = false) String device, @RequestParam(required = false) String error) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (!OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			if (sessionManager.getZbusiness() == null) {
				return "redirect:/business";
			}

			return "redirect:/";
		}

		model.addAttribute("pageTitle", "Login");
		if(error != null) model.addAttribute("errorMessage", "Invalid username or password.");
		log.debug("Login page called at {}", new Date());
		return LOGAIN_PAGE_PATH;
	}

}
