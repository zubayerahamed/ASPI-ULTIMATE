package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Imissueheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.ImissueheaderService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ImissueheaderServiceImpl extends AbstractService implements ImissueheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Imissueheader> LIM13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("imissueheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Imissueheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LIM13(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("imissueheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Imissueheader constractListOfXwhs(Map<String, Object> row) {
		Imissueheader em = new Imissueheader();
		em.setXissuenum((Integer) row.get("xissuenum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXisstype((String) row.get("xisstype"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));

		em.setBusinessUnitName((String) row.get("businessUnitName"));
		em.setWarehouseName((String) row.get("warehouseName"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, fc.xname as businessUnitName, fxw.xname as warehouseName ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit as fc ON fc.xbuid=im.xbuid and fc.zid=im.zid ")
				.append(" LEFT JOIN xwhs as fxw ON fxw.xwh=im.xwh and fxw.zid=im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xstatus='Confirmed' AND im.xstatusim='Confirmed' ");
		} 

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xissuenum LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%' "
				+ "OR fxw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xisstype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
