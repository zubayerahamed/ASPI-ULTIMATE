package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Optogli;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.OptogliService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OptogliServiceImpl extends AbstractService implements OptogliService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Optogli> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause(" optogli im "))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Optogli> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXcodes(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause(" optogli im "))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Optogli constractListOfXcodes(Map<String, Object> row) {
		Optogli em = new Optogli();
		em.setXtype((String) row.get("xtype"));
		em.setXgcus((String) row.get("xgcus"));

		em.setXaccdr((Integer) row.get("xaccdr"));
		em.setXacccr((Integer) row.get("xacccr"));
		em.setXaccdisc((Integer) row.get("xaccdisc"));
		em.setXaccvat((Integer) row.get("xaccvat"));
		em.setXaccait((Integer) row.get("xaccait"));

		em.setReceivableAccount((String) row.get("receivableAccount"));
		em.setSalesAccount((String) row.get("salesAccount"));
		em.setDiscountAccount((String) row.get("discountAccount"));
		em.setVatAccount((String) row.get("vatAccount"));
		em.setAitAccount((String) row.get("aitAccount"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, "
				+ " ra.xdesc as receivableAccount, "
				+ " sa.xdesc as salesAccount, "
				+ " di.xdesc as discountAccount, "
				+ " va.xdesc as vatAccount, "
				+ " ai.xdesc as aitAccount ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acmst as ra ON ra.xacc=im.xaccdr and ra.zid = im.zid ")
				.append(" LEFT JOIN acmst as sa ON sa.xacc=im.xacccr and sa.zid = im.zid ")
				.append(" LEFT JOIN acmst as di ON di.xacc=im.xaccdisc and di.zid = im.zid ")
				.append(" LEFT JOIN acmst as va ON va.xacc=im.xaccvat and va.zid = im.zid ")
				.append(" LEFT JOIN acmst as ai ON ai.xacc=im.xaccait and ai.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xtype LIKE '%" + searchText 
				+ "%' OR im.xgcus LIKE '%" + searchText
				+ "%' OR im.xaccdr LIKE '%" + searchText
				+ "%' OR im.xacccr LIKE '%" + searchText
				+ "%' OR im.xaccdisc LIKE '%" + searchText
				+ "%' OR im.xaccvat LIKE '%" + searchText
				+ "%' OR im.xaccait LIKE '%" + searchText
				+ "%' OR ra.xdesc LIKE '%" + searchText
				+ "%' OR sa.xdesc LIKE '%" + searchText
				+ "%' OR di.xdesc LIKE '%" + searchText
				+ "%' OR va.xdesc LIKE '%" + searchText
				+ "%' OR ai.xgitem LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
