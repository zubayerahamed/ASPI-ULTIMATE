package com.zayaanit.service.rp;

import java.util.Map;

/**
 * @author Zubayer Ahamed
 * @since Jun 27, 2025
 */
public interface ReportMenuBase {
	String getFileName();

	String getGroup();

	String getDescription();

	Map<String, String> getParamMap();

	String getDefaultAccess();

	boolean isEnabledFop();

	String getType();
}
