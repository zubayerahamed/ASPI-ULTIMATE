package com.zayaanit.aspi.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2021
 */
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	//@Autowired private UserAuditRecordService auditService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

//		LoggedInUserDetails user = new LoggedInUserDetails();
//
//		Object principal = authentication.getPrincipal();
//		if(principal instanceof MyUserDetails) {
//			MyUserDetails mud = (MyUserDetails) principal;
//			user.setUsername(mud.getUsername());
//			user.setEmailAddress(mud.getEmailAddress());
//			user.setBusinessId(mud.getBusinessId());
//			Arrays.stream(mud.getRoles().split(",")).forEach(role -> user.getRoles().add(role));
//		}
//
//		Object details = authentication.getDetails();
//		if (details instanceof WebAuthenticationDetails) {
//			String ip = ((WebAuthenticationDetails) details).getRemoteAddress();
//			if(StringUtils.isNotBlank(ip)) user.setIpAddress(ip);
//		}
//
//		Date date = new Date();
//		UserAuditRecord uar = user.getAuditRecord(user);
//		uar.setLoginDate(null);
//		uar.setLogoutDate(date);
//		uar.setRecordDate(date);
//		auditService.save(uar);

		super.onLogoutSuccess(request, response, authentication);
	}
}
