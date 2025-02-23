package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Imtorheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.ImtorheaderService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ImtorheaderServiceImpl extends AbstractService implements ImtorheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Imtorheader> LIM11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("imtorheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Imtorheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LIM11(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("imtorheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Imtorheader constractListOfXwhs(Map<String, Object> row) {
		Imtorheader em = new Imtorheader();
		em.setXtornum((Integer) row.get("xtornum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXfbuid((Integer) row.get("xfbuid"));
		em.setXtbuid((Integer) row.get("xtbuid"));
		em.setXfwh((Integer) row.get("xfwh"));
		em.setXtwh((Integer) row.get("xtwh"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));

		em.setFromBusinessUnitName((String) row.get("fromBusinessUnitName"));
		em.setToBusinessUnitName((String) row.get("toBusinessUnitName"));
		em.setFromWarehouseName((String) row.get("fromWarehouseName"));
		em.setToWarehouseName((String) row.get("toWarehouseName"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, fc.xname as fromBusinessUnitName, tc.xname as toBusinessUnitName, fxw.xname as fromWarehouseName, txw.xname as toWarehouseName ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit as fc ON fc.xbuid=im.xfbuid and fc.zid=im.zid ")
				.append(" LEFT JOIN cabunit as tc ON tc.xbuid=im.xtbuid and tc.zid=im.zid ")
				.append(" LEFT JOIN xwhs as fxw ON fxw.xwh=im.xfwh and fxw.zid=im.zid ")
				.append(" LEFT JOIN xwhs as txw ON txw.xwh=im.xtwh and txw.zid=im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xstatus='Confirmed' AND im.xstatusim='Confirmed' ");
		} else if(suffix == 2) {
			sql = sql.append(" AND im.xtype='Direct Transfer' ");
		} else if(suffix == 3) {
			sql = sql.append(" AND im.xtype='Inter Business' ");
		} 

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xtornum LIKE '%" + searchText + "%' "
				+ "OR im.xfwh LIKE '%" + searchText + "%' "
				+ "OR im.xtwh LIKE '%" + searchText + "%' "
				+ "OR fxw.xname LIKE '%" + searchText + "%' "
				+ "OR txw.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
