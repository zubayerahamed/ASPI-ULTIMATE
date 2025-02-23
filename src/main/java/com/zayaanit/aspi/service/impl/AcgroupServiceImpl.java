package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Acgroup;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.AcgroupService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class AcgroupServiceImpl extends AbstractService implements AcgroupService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Acgroup> LFA12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acgroup im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acgroup> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcgroup(row)));

		return list;
	}

	@Override
	public int LFA12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acgroup im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Acgroup constractListOfAcgroup(Map<String, Object> row) {
		Acgroup em = new Acgroup();
		em.setXagcode((Integer) row.get("xagcode"));
		em.setXagname((String) row.get("xagname"));
		em.setXaglevel((Integer) row.get("xaglevel"));
		em.setXagparent((Integer) row.get("xagparent"));
		em.setXagtype((String) row.get("xagtype"));
		em.setParentName((String) row.get("parentname"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, a.xagname as parentname ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acgroup a ON a.xagcode = im.xagparent AND a.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND im.xaglevel='"+ paramsValues[0] +"' ");

			if(paramsValues.length > 1 && !"1".equalsIgnoreCase(paramsValues[0])) {
				sql = sql.append(" AND im.xagparent='"+ paramsValues[1] +"' ");
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xagcode LIKE '%" + searchText + "%' "
				+ "OR im.xagname LIKE '%" + searchText + "%' "
				+ "OR im.xagparent LIKE '%" + searchText + "%' "
				+ "OR a.xagname LIKE '%" + searchText + "%' "
				+ "OR im.xagtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
