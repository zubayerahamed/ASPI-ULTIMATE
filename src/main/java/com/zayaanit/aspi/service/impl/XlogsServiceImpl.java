package com.zayaanit.aspi.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.entity.Xlogs;
import com.zayaanit.aspi.repo.XlogsRepo;
import com.zayaanit.aspi.service.XlogsService;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Service
public class XlogsServiceImpl extends AbstractGenericService implements XlogsService {

	@Autowired private XlogsRepo xlogsRepo;
	@Autowired private AppConfig appConfig;

	@Override
	public Xlogs login() {
		if(!appConfig.isAuditEnable()) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXsip(sessionManager.serverIp());
		xlogs.setXcip(sessionManager.remoteIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff().toString() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(null);
		xlogs.setXexptype("Login");
		xlogs.setXuseragent(sessionManager.userAgent());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	@Override
	public Xlogs logout() {
		if(!appConfig.isAuditEnable()) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXsip(sessionManager.serverIp());
		xlogs.setXcip(sessionManager.remoteIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff().toString() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(new Date());
		xlogs.setXexptype("Logout");
		xlogs.setXuseragent(sessionManager.userAgent());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}
}
