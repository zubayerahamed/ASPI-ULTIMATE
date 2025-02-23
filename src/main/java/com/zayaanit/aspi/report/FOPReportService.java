package com.zayaanit.aspi.report;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author Zubayer Ahamed
 * @since Feb 15, 2021
 */
@Component
public interface FOPReportService {

	public List<Map<String, Object>> getResultData(String procName, Map<String, Object> reportParams);
	public List<Map<String, Object>> getResultMap(String sql);
}
