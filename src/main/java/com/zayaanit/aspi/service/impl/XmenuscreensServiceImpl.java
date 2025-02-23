package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Xmenuscreens;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.XmenuscreensService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class XmenuscreensServiceImpl extends AbstractService implements XmenuscreensService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Xmenuscreens> LSA13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xmenuscreens im"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xmenuscreens> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LSA13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xmenuscreens im"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xmenuscreens constractListOfXscreens(Map<String, Object> row) {
		Xmenuscreens em = new Xmenuscreens();
		em.setXrow((Integer) row.get("xrow"));
		em.setXmenu((String) row.get("xmenu"));
		em.setXscreen((String) row.get("xscreen"));
		em.setXsequence((Integer) row.get("xsequence"));
		em.setXmenuTitle((String) row.get("xmenutitle"));
		em.setXscreenTitle((String) row.get("xscreentitle"));
		em.setXscreenType((String) row.get("xscreentype"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, xm.title as xmenutitle, xs.title as xscreentitle, xs.xtype as xscreentype ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
							.append(" LEFT JOIN xmenus xm ON xm.xmenu = im.xmenu AND xm.zid = im.zid ")
							.append(" LEFT JOIN xscreens xs ON xs.xscreen = im.xscreen AND xs.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty()) return sql;
		return sql.append(" AND (im.xmenu LIKE '%" + searchText + "%' "
				+ "OR im.xscreen LIKE '%" + searchText + "%' "
				+ "OR xscreentype LIKE '%" + searchText + "%' "
				+ "OR xscreentitle LIKE '%" + searchText + "%' "
				+ "OR xmenutitle LIKE '%"+ searchText +"%' ) ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
