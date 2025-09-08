/**
 * 
 */
package com.zayaanit.aspi.model;

import java.util.Map;

import com.zayaanit.aspi.enums.ReportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
	private String fromApp;
	private String reportPath;
	private String defaultReportPath;
	private String reportName;
	private String reportTitle;
	private String reportEngine;  // CRYSTAL // FOP // JASPER // BIRT
	private ReportType reportType;
	private Map<String, Object> reportParams;
}
