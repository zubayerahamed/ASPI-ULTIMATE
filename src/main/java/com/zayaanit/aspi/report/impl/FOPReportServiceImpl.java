package com.zayaanit.aspi.report.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.report.FOPReportService;

/**
 * @author Zubayer Ahamed
 * @since Feb 15, 2021
 */
@Service
public class FOPReportServiceImpl implements FOPReportService {

	@Autowired protected JdbcTemplate jdbcTemplate;

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResultData(String procName, Map<String, Object> reportParams){
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procName);

		MapSqlParameterSource mps = new MapSqlParameterSource();
		for(Map.Entry<String, Object> param : reportParams.entrySet()) {
			mps.addValue(param.getKey(), param.getValue());
		}
		SqlParameterSource in = mps;

		Map<String, Object> out = simpleJdbcCall.execute(in);
		if(out.isEmpty()) return Collections.emptyList();
		return (List<Map<String, Object>>) out.get("#result-set-1");
	}

	@Override
	public List<Map<String, Object>> getResultMap(String sql) {
		if(StringUtils.isBlank(sql)) return Collections.emptyList();
		return jdbcTemplate.queryForList(sql);
	}
}