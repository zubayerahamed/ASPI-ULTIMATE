package com.zayaanit.aspi.service;

import java.io.InputStream;
import java.util.Map;

import com.zayaanit.aspi.enums.ReportType;

/**
 * @author Zubayer Ahamed
 *
 */
public interface PrintingService {

	public InputStream getDataBytes(String reportName, String reportTitle, boolean attachment, Map<String, Object> reportParams, ReportType reportType);
}
