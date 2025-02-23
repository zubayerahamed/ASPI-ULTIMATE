package com.zayaanit.aspi.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.model.MyUserDetails;
import com.zayaanit.aspi.service.KitSessionManager;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KitSessionManagerImpl implements KitSessionManager {

	private Map<String, Object> sessionMap;

	public KitSessionManagerImpl() {
		this.sessionMap = new HashMap<>();
	}

	@Override
	public void addToMap(String key, Object value) {
		sessionMap.put(key, value);
	}

	@Override
	public Object getFromMap(String key) {
		return sessionMap.get(key);
	}

	@Override
	public void removeFromMap(String key) {
		if (sessionMap.containsKey(key))
			sessionMap.remove(key);
	}

	@Override
	public Integer getBusinessId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated())
			return null;

		Object principal = auth.getPrincipal();
		if (principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getZbusiness() != null ? mud.getZbusiness().getZid() : null;
		}

		return null;
	}

	@Override
	public Zbusiness getZbusiness() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated())
			return null;

		Object principal = auth.getPrincipal();
		if (principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getZbusiness();
		}

		return null;
	}

	@Override
	public Xprofiles getXprofile() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated())
			return null;

		Object principal = auth.getPrincipal();
		if (principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getXprofile();
		}

		return null;
	}

	@Override
	public MyUserDetails getLoggedInUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated())
			return null;

		Object principal = auth.getPrincipal();

		if (principal instanceof MyUserDetails) {
			return (MyUserDetails) principal;
		}

		return null;
	}

	@Override
	public String sessionId() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String sessionId = request.getSession().getId();
		return sessionId;
	}

	@Override
	public String remoteIp() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String remoteIp = request.getHeader("X-Forwarded-For");
		if (remoteIp == null || remoteIp.isEmpty()) {
			remoteIp = request.getRemoteAddr();
		}
		return remoteIp;
	}

	@Override
	public String serverIp() {
		String serverIp = null;
		try {
			serverIp = InetAddress.getLocalHost().getHostAddress(); // Server IP address
		} catch (UnknownHostException e) {
			serverIp = "Unknown";
		}
		return serverIp;
	}

	@Override
	public String userAgent() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String userAgent = request.getHeader("User-Agent");
		return userAgent;
	}

	
}
