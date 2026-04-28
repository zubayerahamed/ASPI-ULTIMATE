package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.MenuResDto;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XscreensService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class XscreensServiceImpl extends AbstractService implements XscreensService {
	@Autowired private KitSessionManager sessionManager;

	private final RowMapper<MenuResDto> ROW_MAPPER = (rs, rowNum) -> new MenuResDto(
		rs.getString("xicon"),
		rs.getString("xprofile"),
		rs.getString("xscreen"),
		rs.getString("xtitle"),
		rs.getString("xkeywords"),
		rs.getString("xtype")
	);

	@Override
	public List<MenuResDto> searchMenus(String hint) {
		List<MenuResDto> results = new ArrayList<>();

		String xprofile = null;
		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			Xprofiles xp = sessionManager.getLoggedInUserDetails().getXprofile();
			if(xp != null) xprofile = xp.getXprofile();
		}

		StringBuilder screenSql = new StringBuilder(); 
		screenSql.append("SELECT xscreen, xprofile,  xicon, xtitle, xtype, xkeywords ");
		screenSql.append("FROM profilescreenv ");
		screenSql.append("WHERE ");
		screenSql.append("    zid = "+ sessionManager.getBusinessId() +" ");
		if(StringUtils.isNotBlank(xprofile)) screenSql.append("    AND xprofile = '"+ xprofile +"' ");
		screenSql.append("    AND xtype = 'Screen' ");
		screenSql.append("    AND LTRIM(RTRIM(xkeywords)) LIKE '%"+ hint.trim() +"%' ");

		List<MenuResDto> screenMenus = jdbcTemplate.query(screenSql.toString(), ROW_MAPPER);
		List<MenuResDto> uniqueScreenMenus = screenMenus.stream().filter(distinctByKey(MenuResDto::getXscreen)).limit(5).collect(Collectors.toList());

		StringBuilder reportSql = new StringBuilder(); 
		reportSql.append("SELECT xscreen, xprofile,  xicon, xtitle, xtype, xkeywords ");
		reportSql.append("FROM profilescreenv ");
		reportSql.append("WHERE ");
		reportSql.append("    zid = "+ sessionManager.getBusinessId() +" ");
		if(StringUtils.isNotBlank(xprofile)) reportSql.append("    AND xprofile = '"+ xprofile +"' ");
		reportSql.append("    AND xtype = 'Report' ");
		reportSql.append("    AND LTRIM(RTRIM(xkeywords)) LIKE '%"+ hint.trim() +"%' ");

		List<MenuResDto> reportMenus = jdbcTemplate.query(reportSql.toString(), ROW_MAPPER);
		List<MenuResDto> uniqueReportMenus = reportMenus.stream().filter(distinctByKey(MenuResDto::getXscreen)).limit(5).collect(Collectors.toList());

		results.addAll(uniqueScreenMenus);
		results.addAll(uniqueReportMenus);
		return results;
//		String sql = "WITH ranked_screens AS ( " +
//				"    SELECT " +
//				"        xscreen, xtitle, xkeywords, xtype, " +
//				"        ROW_NUMBER() OVER ( " +
//				"            PARTITION BY xtype  " +
//				"            ORDER BY xscreen " +
//				"        ) AS rn " +
//				"    FROM " +
//				"        xscreens " +
//				"    WHERE " +
//				"        zid = "+ sessionManager.getBusinessId() +" " +
//				"        AND xtype IN ('Screen', 'Report') " +
//				"        AND ( " +
//				"            xscreen LIKE '%"+ hint +"%' " +
//				"            OR xtitle = '"+ hint +"' " +
//				"            OR xtitle LIKE '"+ hint +" %' " +
//				"            OR xtitle LIKE '% "+ hint +"' " +
//				"            OR xtitle LIKE '% "+ hint +" %' " +
//				"            OR xkeywords = '"+ hint +"' " +
//				"            OR xkeywords LIKE '"+ hint +" %' " +
//				"            OR xkeywords LIKE '% "+ hint +"' " +
//				"            OR xkeywords LIKE '% "+ hint +" %' " +
//				"        ) " +
//				") " +
//				"SELECT xscreen, xtitle, xkeywords, xtype " +
//				"FROM ranked_screens " +
//				"WHERE rn <= 5 " +
//				"ORDER BY  " +
//				"    CASE xtype  " +
//				"        WHEN 'Screen' THEN 1 " +
//				"        WHEN 'Report' THEN 2 " +
//				"        ELSE 3 " +
//				"    END, " +
//				"    xscreen ";
//
//
//
//		
//
//		return jdbcTemplate.query(sql, ROW_MAPPER);
	}

	@Override
	public List<Xscreens> LSA12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xscreens im"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xscreens> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LSA12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xscreens im"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xscreens constractListOfXscreens(Map<String, Object> row) {
		Xscreens em = new Xscreens();
		em.setXscreen((String) row.get("xscreen"));
		em.setXtitle((String) row.get("xtitle"));
		em.setXnum((Integer) row.get("xnum"));
		em.setXtype((String) row.get("xtype"));
		em.setXicon((String) row.get("xicon"));
		em.setXkeywords((String) row.get("xkeywords"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.* ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND xtype in ('Screen', 'Default', 'Report') ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xscreen LIKE '%" + searchText + "%' "
				+ "OR xtitle LIKE '%" + searchText + "%' "
				+ "OR xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
