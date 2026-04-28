package com.zayaanit.model;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.service.KitSessionManager;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 20, 2025
 */
@Data
public class XlogsdtEvent {

	private Xlogsdt xlogsdt;
	private Integer zid;
	private String xsession;
	private String zemail;
	private Integer xstaff;
	private boolean admin;
	private String xlogtype;

	public XlogsdtEvent(Xlogsdt xlogsdt, KitSessionManager sessionManager) {
		this.xlogsdt = xlogsdt;
		this.zid = sessionManager.getBusinessId();
		this.xsession = sessionManager.sessionId();
		this.zemail = sessionManager.getLoggedInUserDetails().getUsername();
		this.xstaff = sessionManager.getLoggedInUserDetails().getXstaff();
		this.admin = sessionManager.getLoggedInUserDetails().isAdmin();
		this.xlogtype = sessionManager.getZbusiness().getXlogtype();
	}
}
