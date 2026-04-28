package com.zayaanit.security;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zayaanit.model.MyUserDetails;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Nov 27, 2020
 */
@Data
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String username = "ANONYMUS_USER";
		MyUserDetails user = getLoggedInUserDetails();
		if(user != null && StringUtils.isNotBlank(user.getUsername())) username = user.getUsername();
		return Optional.ofNullable(username);
	}

	public MyUserDetails getLoggedInUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(!(principal instanceof MyUserDetails)) return null;
		return (MyUserDetails) principal;
	}
}
