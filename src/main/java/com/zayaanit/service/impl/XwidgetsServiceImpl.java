package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xwidgets;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XwidgetsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class XwidgetsServiceImpl extends AbstractService implements XwidgetsService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Xwidgets> LSA14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xwidgets"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xwidgets> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@SuppressWarnings("null")
	@Override
	public int LSA14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xwidgets"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xwidgets constractListOfXscreens(Map<String, Object> row) {
		Xwidgets em = new Xwidgets();
		em.setXwidget((String) row.get("xwidget"));
		em.setXtitle((String) row.get("xtitle"));
		em.setXdefault((Integer) row.get("xdefault"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty()) return sql;
		return sql.append(" AND (xwidget LIKE '%" + searchText + "%' OR xtitle LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
