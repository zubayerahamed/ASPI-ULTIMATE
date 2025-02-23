package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class XcodesServiceImpl extends AbstractService implements XcodesService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Xcodes> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xcodes"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xcodes> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXcodes(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xcodes"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xcodes constractListOfXcodes(Map<String, Object> row) {
		Xcodes em = new Xcodes();
		em.setXtype((String) row.get("xtype"));
		em.setXcode((String) row.get("xcode"));
		em.setXdesc((String) row.get("xdesc"));
		em.setZactive((Boolean) row.get("zactive"));
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
		
		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (xtype LIKE '%" + searchText 
				+ "%' OR xcode LIKE '%" + searchText
				+ "%' OR xdesc LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
