package com.zayaanit.aspi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.CaitemService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class CaitemServiceImpl extends AbstractService implements CaitemService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Caitem> LMD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("caitem im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Caitem> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfCaitem(row)));

		return list;
	}

	@Override
	public int LMD12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("caitem im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Caitem constractListOfCaitem(Map<String, Object> row) {
		Caitem em = new Caitem();
		em.setXitem((Integer) row.get("xitem"));
		em.setXdesc((String) row.get("xdesc"));
		em.setXunit((String) row.get("xunit"));
		em.setXrate((BigDecimal) row.get("xrate"));
		em.setXgitem((String) row.get("xgitem"));
		em.setXcatitem((String) row.get("xcatitem"));
		em.setXnote((String) row.get("xnote"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.* ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xgitem <> 'Services' ");
		} else if(suffix == 2) {
			sql = sql.append(" AND im.xgitem = 'Services' ");
		} else if(suffix == 3) {
			sql = sql.append(" AND im.xispo = '1' ");
		} else if(suffix == 4) {
			sql = sql.append(" AND im.xisop = '1' ");
		} else if(suffix == 5) {
			sql = sql.append(" AND im.xisop = '1' AND im.xgitem <> 'Services' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xitem LIKE '%" + searchText + "%' "
				+ "OR im.xdesc LIKE '%" + searchText + "%' "
				+ "OR im.xbarcode LIKE '%" + searchText + "%' "
				+ "OR im.xgitem LIKE '%" + searchText + "%' "
				+ "OR im.xcatitem LIKE '%" + searchText + "%' "
				+ "OR im.xnote LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
