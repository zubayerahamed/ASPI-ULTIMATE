package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.AcmstService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class AcmstServiceImpl extends AbstractService implements AcmstService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Acmst> LFA13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acmst im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acmst> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcmst(row)));

		return list;
	}

	@Override
	public int LFA13(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acmst im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Acmst constractListOfAcmst(Map<String, Object> row) {
		Acmst em = new Acmst();
		em.setXacc((Integer) row.get("xacc"));
		em.setXgroup((Integer) row.get("xgroup"));
		em.setXdesc((String) row.get("xdesc"));
		em.setXacctype((String) row.get("xacctype"));
		em.setXaccusage((String) row.get("xaccusage"));
		em.setGroupName((String) row.get("groupname"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, a.xagname as groupname ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acgroup a ON a.xagcode = im.xgroup AND a.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND im.xaccusage='"+ paramsValues[0] +"' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xacc LIKE '%" + searchText + "%' "
				+ "OR im.xdesc LIKE '%" + searchText + "%' "
				+ "OR im.xacctype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
