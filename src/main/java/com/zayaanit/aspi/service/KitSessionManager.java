package com.zayaanit.aspi.service;

import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.Zbusiness;
import com.zayaanit.aspi.model.MyUserDetails;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
public interface KitSessionManager {

	public void addToMap(String key, Object value);

	public Object getFromMap(String key);

	public void removeFromMap(String key);

	public Integer getBusinessId();

	public Zbusiness getZbusiness();

	public Xprofiles getXprofile();

	public MyUserDetails getLoggedInUserDetails();

	public String sessionId();

	public String remoteIp();

	public String serverIp();

	public String userAgent();
}
