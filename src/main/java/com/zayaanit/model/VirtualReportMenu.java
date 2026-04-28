package com.zayaanit.model;

import java.util.Map;

import com.zayaanit.service.rp.ReportMenuBase;

/**
 * @author Zubayer Ahamed
 * @since Jun 27, 2025
 */
public class VirtualReportMenu implements ReportMenuBase {
	private String group;
	private String description;
	private String fileName;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean enabledFop;
	private String type;

	public VirtualReportMenu(String group, String description, String fileName, Map<String, String> paramMap, String defaultAccess, boolean enabledFop, String type) {
		this.group = group;
		this.description = description;
		this.fileName = fileName;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.enabledFop = enabledFop;
		this.type = type;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	@Override
	public String getDefaultAccess() {
		return defaultAccess;
	}

	@Override
	public boolean isEnabledFop() {
		return enabledFop;
	}

	@Override
	public String getType() {
		return this.type;
	}
}
