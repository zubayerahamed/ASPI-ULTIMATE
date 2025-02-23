package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Potogli;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.PotogliService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class PotogliServiceImpl extends AbstractService implements PotogliService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Potogli> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause(" potogli im "))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Potogli> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXcodes(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause(" potogli im "))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Potogli constractListOfXcodes(Map<String, Object> row) {
		Potogli em = new Potogli();
		em.setXtype((String) row.get("xtype"));
		em.setXgsup((String) row.get("xgsup"));
		em.setXgitem((String) row.get("xgitem"));
		em.setXaccdr((Integer) row.get("xaccdr"));
		em.setXacccr((Integer) row.get("xacccr"));
		em.setXaccadj((Integer) row.get("xaccadj"));
		em.setDebitAccount((String) row.get("dabitaccount"));
		em.setCreditAccount((String) row.get("creditaccount"));
		em.setAdjustmentAccount((String) row.get("adjustmentaccount"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, da.xdesc as dabitaccount, ca.xdesc as creditaccount, adj.xdesc as adjustmentaccount ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acmst as da ON da.xacc=im.xaccdr and da.zid = im.zid ")
				.append(" LEFT JOIN acmst as ca ON ca.xacc=im.xacccr and ca.zid = im.zid ")
				.append(" LEFT JOIN acmst as adj ON adj.xacc=im.xaccadj and adj.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xtype LIKE '%" + searchText 
				+ "%' OR im.xgsup LIKE '%" + searchText
				+ "%' OR im.xaccdr LIKE '%" + searchText
				+ "%' OR im.xacccr LIKE '%" + searchText
				+ "%' OR im.xaccadj LIKE '%" + searchText
				+ "%' OR da.xdesc LIKE '%" + searchText
				+ "%' OR ca.xdesc LIKE '%" + searchText
				+ "%' OR adj.xdesc LIKE '%" + searchText
				+ "%' OR im.xgitem LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
