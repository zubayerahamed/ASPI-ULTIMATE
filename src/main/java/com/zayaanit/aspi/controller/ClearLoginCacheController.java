package com.zayaanit.aspi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/clearlogincache")
public class ClearLoginCacheController extends BaseController {

	@GetMapping
	public String clearCache() {
		if(sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
			sessionManager.removeFromMap("FAKE_LOGIN_USER");
			sessionManager.removeFromMap(ALL_BUSINESS);
		}
		return "redirect:/login";
	}
}
