package com.zayaanit.aspi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.model.DataDownloadAccount;
import com.zayaanit.aspi.model.DataDownloadAccountsSearchParam;
import com.zayaanit.aspi.service.DataDownloadAccountService;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2024
 */
@Service
public class DataDownloadAccountServiceImpl extends AbstractService implements DataDownloadAccountService {

	@Override
	public List<DataDownloadAccount> AD19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, DataDownloadAccountsSearchParam param, Integer zid) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("zbusiness z"))
		.append(whereClause(searchText, suffix, param, zid))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<DataDownloadAccount> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int AD19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, DataDownloadAccountsSearchParam param, Integer zid) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("zbusiness z"))
		.append(whereClause(searchText, suffix, param, zid));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder selectClause() {
		return new StringBuilder("select ") 
				.append("	Format(a.xdateact ,'dd-MM-yyyy') vdate, ") 
				.append("	a.xtrnnum voucher, ")
				.append("	a.xdoctype doctype, ") 
				.append("	case a.xsign when -1 then b.xcodegl else concat('',a.xcus) end dra, ")
				.append("	a.xprime debitamount, ")
				.append("	case a.xsign when -1 then concat('',a.xcus)  else b.xcodegl end cra, ")
				.append("	a.xprime creditamount, ")
				.append("	concat('Doc:', a.xtrnnum, ', Ref:', a.xdocnum, ', Type: ', a.xdoctype) particular ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" join arhed a on z.zid=a.zid ")
				.append(" join cabank b on a.zid=b.zid and a.xbank=b.xbank ");
	}

	private StringBuilder whereClause(String searchText, int suffix, DataDownloadAccountsSearchParam param, Integer zid) {
		StringBuilder sql = new StringBuilder(" WHERE z.zid="+ zid +" ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		sql = sql.append(" and a.xdateact between '"+ sdf.format(param.getXfdate()) +"' and '"+ sdf.format(param.getXtdate()) +"' ");

		if("All".equals(param.getXtype())) {
			sql = sql.append(" and a.xscreen in('FA31', 'FA32', 'FA36', 'FA37') ");
		} else if ("Money Receipt".equals(param.getXtype())) {
			sql = sql.append(" and a.xscreen in('FA31') ");
		} else if ("Customer Adjustment".equals(param.getXtype())) {
			sql = sql.append(" and a.xscreen in('FA32') ");
		} else if ("Sales Invoice".equals(param.getXtype())) {
			sql = sql.append(" and a.xscreen in('FA36') ");
		} else if ("Sales Return".equals(param.getXtype())) {
			sql = sql.append(" and a.xscreen in('FA37') ");
		}

		sql = sql.append(" and a.xstatus='Confirmed' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (a.xdateact LIKE '%" + searchText
				+ "%' OR a.xtrnnum LIKE '%" + searchText
				+ "%' OR a.xdoctype LIKE '%" + searchText
				+ "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY z.zid, a.xdoctype, a.xdateact, a.xtrnnum ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	private DataDownloadAccount constractListOfArhed(Map<String, Object> row) {
		DataDownloadAccount em = new DataDownloadAccount();
		em.setVdate((String) row.get("vdate"));
		em.setVoucher((Integer) row.get("voucher"));
		em.setDoctype((String) row.get("doctype"));
		em.setDra((String) row.get("dra"));
		em.setDebitAmount((BigDecimal) row.get("debitamount"));
		em.setCra((String) row.get("cra"));
		em.setCreditAmount((BigDecimal) row.get("creditamount"));
		em.setParticular((String) row.get("particular"));
		return em;
	}
}
