package com.zayaanit.aspi.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.repo.ZbusinessRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2024
 */
@Controller
@RequestMapping("/switchbusiness")
public class SwitchBusinessController extends BaseController {

	@Autowired
	private ZbusinessRepo zbusinessRepo;
	@Autowired
	private XusersRepo xusersRepo;

	@GetMapping("/{businessId}")
	public String loadBusiness(@PathVariable Integer businessId) {
		// Find business and check business is active or not
		Optional<Zbusiness> zbOp = zbusinessRepo.findByZidAndZactive(businessId, Boolean.TRUE);
		if (!zbOp.isPresent()) return "redirect:/";

		// need to update user details also based on the business selection row of user
		Optional<Xusers> usersOp = xusersRepo.findByZemailAndZid(sessionManager.getLoggedInUserDetails().getUsername(), zbOp.get().getZid());
		if(!usersOp.isPresent()) return "redirect:/";

		// XLOGS If already logged in and want to switch business only, just add a logout log from previous business
		if(appConfig.isAuditEnable()) {
			if(sessionManager.getFromMap("LOGIN_DONE") != null) {
				xlogsService.logout();
				renewSession();
			}
			sessionManager.addToMap("LOGIN_FLAG", "Y");
		}

		sessionManager.getLoggedInUserDetails().setUserDetails(usersOp.get());
		sessionManager.getLoggedInUserDetails().setZbusiness(zbOp.get());
		sessionManager.getLoggedInUserDetails().setXprofile(null);
		sessionManager.getLoggedInUserDetails().setLoginTime(new Date());
		return "redirect:/";
	}

	private void renewSession() {
		// 1. Get the current session and invalidate it
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidate the session to force new session creation
		}

		// 2. Create a new session
		HttpSession newSession = request.getSession(true); // Create a new session

		// 3. Preserve the SecurityContext (associate it with the new session)
		SecurityContext securityContext = SecurityContextHolder.getContext();
		newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
	}
}
