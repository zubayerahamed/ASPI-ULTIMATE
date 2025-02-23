package com.zayaanit.aspi.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.zayaanit.aspi.enums.ResponseStatus;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ResponseHelper {
	private static final String DEFAULT_SUCCESS_MESSAGE = "Processing was successful";
	private static final String DEFAULT_ERROR_MESSAGE = "Failed to process";
	private static final String APPEND_SEPERATOR = ", ";
	private static final String STATUS_KEY = "status";
	private static final String REDIRECT_KEY = "redirecturl";
	private static final String MESSAGE_KEY = "message";
	private static final String DISPLAY_MESSAGE = "displayMessage";
	private static final String DISPLAY_ERROR_DETAIL_MODAL = "displayErrorDetailModal";
	private static final String FILE_DOENLOAD = "fileDownload";
	private static final String PRINT_REPORT = "printReport";

	private boolean printReport = false;
	private boolean fileDownload = false;
	private boolean displayMessage = true;
	private boolean showErrorDetailModal = false;
	private String statusMessage;
	private ResponseStatus status;
	private Map<String, Object> response = new HashMap<>();

	public void setPrintParam(RequestParameters printParams, String printUrl) {
		this.printReport = true;
		this.response.put("printParams", printParams);
		this.response.put("printUrl", printUrl);
	}

	public void setDisplayMessage(boolean displayMessage) {
		this.displayMessage = displayMessage;
	}

	public void setFileDownload(boolean fileDownload) {
		this.fileDownload = fileDownload;
	}

	public void setShowErrorDetailModal(boolean showErrorDetailModal) {
		this.showErrorDetailModal = showErrorDetailModal;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public void setStatusMessage(String message) {
		this.statusMessage = message;
	}

	public void setRedirectUrl(String url) {
		this.response.put(REDIRECT_KEY, url);
	}

	public void setReloadSectionIdWithUrl(String elementId, String url) {
		this.response.put("reloadelementid", elementId);
		this.response.put("reloadurl", url);
	}

	public void setSecondReloadSectionIdWithUrl(String elementId, String url) {
		this.response.put("secondreloadelementid", elementId);
		this.response.put("secondreloadurl", url);
	}

	public void setThirdReloadSectionIdWithUrl(String elementId, String url) {
		this.response.put("thirdreloadelementid", elementId);
		this.response.put("thirdreloadurl", url);
	}

	/**
	 * Set Reloadable section element id as Key and URL as value in MAP
	 * @param map
	 */
	public void setReloadSections(List<ReloadSection> reloadSections) {
		this.response.put("reloadsections", reloadSections);
	}

	public void setErrorDetailsList(List<?> list) {
		this.response.put("errorDetails", list);
	}

	public void setTriggerModalUrl(String modalId, String url) {
		this.response.put("modalid", modalId);
		this.response.put("triggermodalurl", url);
	}

	public void setSuccessStatusAndMessage(String message) {
		this.status = ResponseStatus.SUCCESS;
		if(StringUtils.isBlank(message)) message = DEFAULT_SUCCESS_MESSAGE;
		this.statusMessage = message;
	}

	public void setErrorStatusAndMessage(String message) {
		this.status = ResponseStatus.ERROR;
		if(StringUtils.isBlank(message)) message = DEFAULT_ERROR_MESSAGE;
		this.statusMessage = message;
	}

	public void setWarningStatusAndMessage(String message) {
		this.status = ResponseStatus.WARNING;
		this.statusMessage = message;
	}

	public void setInfoStatusAndMessage(String message) {
		this.status = ResponseStatus.INFO;
		this.statusMessage = message;
	}

	public void addDataToResponse(String key, String value) {
		if(response.get(key) == null) {
			this.response.put(key, value);
		} else {
			this.response.put(key, response.get(key) + APPEND_SEPERATOR + value);
		}
	}

	public void addDataToResponse(String key, Object value) {
		this.response.put(key, value);
	}

	public Map<String, Object> getResponse(){
		if(this.status == null) return Collections.emptyMap();
		if(StringUtils.isBlank(this.statusMessage)) {
			switch (this.status.name().toUpperCase()) {
				case "SUCCESS":
					this.statusMessage = DEFAULT_SUCCESS_MESSAGE;
					break;
				case "ERROR":
					this.statusMessage = DEFAULT_ERROR_MESSAGE;
					break;
				default :
					break;
			}
		}

		response.put(STATUS_KEY, this.status.name().toUpperCase());
		response.put(MESSAGE_KEY, this.statusMessage);
		response.put(DISPLAY_MESSAGE, this.displayMessage);
		response.put(DISPLAY_ERROR_DETAIL_MODAL, this.showErrorDetailModal);
		response.put(FILE_DOENLOAD, this.fileDownload);
		response.put(PRINT_REPORT, this.printReport);
		return this.response;
	}
}
