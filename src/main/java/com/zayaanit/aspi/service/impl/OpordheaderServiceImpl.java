package com.zayaanit.aspi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.model.SO13SearchParam;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.OpordheaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpordheaderServiceImpl extends AbstractService implements OpordheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Opordheader> LSO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LSO12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opordheader> LSO13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO13SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseSO13(searchText, suffix, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LSO13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO13SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseSO13(searchText, suffix, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opordheader constractListOfXwhs(Map<String, Object> row) {
		Opordheader em = new Opordheader();
		em.setXordernum((Integer) row.get("xordernum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXref((String) row.get("xref"));
		em.setXlineamt((BigDecimal) row.get("xlineamt"));
		em.setXdiscamt((BigDecimal) row.get("xdiscamt"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXdornum((Integer) row.get("xdornum"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusord((String) row.get("xstatusord"));
		em.setBusinessUnitName((String) row.get("businessUnitName"));
		em.setCustomerName((String) row.get("customerName"));
		em.setWarehouseName((String) row.get("warehouseName"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, c.xname as businessUnitName, ac.xname as customerName, xw.xname as warehouseName ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit as c ON c.xbuid=im.xbuid and c.zid=im.zid ")
				.append(" LEFT JOIN acsub as ac ON ac.xsub=im.xcus and ac.zid=im.zid ")
				.append(" LEFT JOIN xwhs as xw ON xw.xwh=im.xwh and xw.zid=im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xstatus='Confirmed' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xordernum LIKE '%" + searchText + "%' "
				+ "OR im.xcus LIKE '%" + searchText + "%' "
				+ "OR ac.xname LIKE '%" + searchText + "%' "
				+ "OR xw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO13(String searchText, int suffix, SO13SearchParam param) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		sql.append(" AND (im.xdate between '"+ sdf.format(param.getXfdate()) +"' AND '"+ sdf.format(param.getXtdate()) +"') ");
		if(param.getXbuid() != null) sql.append(" AND im.xbuid = '"+ param.getXbuid() +"' ");
		if(param.getXwh() != null) sql.append(" AND im.xwh = '"+ param.getXwh() +"' ");
		if(param.getXcus() != null) sql.append(" AND im.xcus = '"+ param.getXcus() +"' ");
		if(StringUtils.hasText(param.getXstatusord())) sql.append(" AND im.xstatusord = '"+ param.getXstatusord() +"' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xordernum LIKE '%" + searchText + "%' "
				+ "OR im.xcus LIKE '%" + searchText + "%' "
				+ "OR ac.xname LIKE '%" + searchText + "%' "
				+ "OR xw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
