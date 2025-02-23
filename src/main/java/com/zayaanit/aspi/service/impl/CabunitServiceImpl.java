package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.CabunitService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class CabunitServiceImpl extends AbstractService implements CabunitService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Cabunit> LAD17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("cabunit im"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cabunit> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LAD17(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("cabunit im"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Cabunit constractListOfXscreens(Map<String, Object> row) {
		Cabunit em = new Cabunit();
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXname((String) row.get("xname"));
		em.setXmadd((String) row.get("xmadd"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xbuid LIKE '%" + searchText + "%' "
				+ "OR xname LIKE '%" + searchText + "%' "
				+ "OR xmadd LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
