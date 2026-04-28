package com.zayaanit.service.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SA17SearchParam;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XlogsdtService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Slf4j
@Service
public class XlogsdtServiceImpl implements XlogsdtService {

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired protected KitSessionManager sessionManager;
	@Autowired private JdbcTemplate jdbcTemplate;

	@Override
	public void SA17delete(SA17SearchParam param) {
		StringBuilder sql = new StringBuilder("DELETE FROM xlogsdt WHERE zid = ?");
		List<Object> params = new ArrayList<>();
		params.add(sessionManager.getBusinessId());

		// xdate range
		sql.append(" AND xdate BETWEEN ? AND ?");
		params.add(new java.sql.Date(param.getXfdate().getTime()));
		params.add(new java.sql.Date(param.getXtdate().getTime()));

		// zemail condition
		if (StringUtils.isNotBlank(param.getZemail())) {
			sql.append(" AND zemail = ?");
			params.add(param.getZemail());
		}

		// xstaff condition
		if (param.getXstaff() != null) {
			sql.append(" AND xstaff = ?");
			params.add(param.getXstaff());
		}

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Transactional
	@Override
	public void save(XlogsdtEvent event) {
		if (event.isAdmin()) return;
		if (!"Advance".equals(event.getXlogtype())) return;

		Date date = new Date();

		Xlogsdt xlogsdt = event.getXlogsdt();
		xlogsdt.setZid(event.getZid());
		xlogsdt.setXsession(event.getXsession());
		xlogsdt.setZemail(event.getZemail());
		xlogsdt.setXstaff(event.getXstaff());
		xlogsdt.setZtime(date);
		xlogsdt.setXdate(date);

		insertAndReturnId(xlogsdt);

		log.debug("Logs Details Saved : " + xlogsdt.toString());
	}

	public long insertAndReturnId(final Xlogsdt xlogsdt) {
		final String sql = "INSERT INTO xlogsdt (" + "xdata, xdate, xfunc, xresult, xscreen, "
				+ "xsession, xsource, xstaff, xstatement, xtable, " + "zemail, zid, ztime"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, xlogsdt.getXdata());
			ps.setDate(2, new java.sql.Date(xlogsdt.getXdate().getTime())); // java.util.Date -> java.sql.Date
			ps.setString(3, xlogsdt.getXfunc());
			ps.setString(4, xlogsdt.getXresult());
			ps.setString(5, xlogsdt.getXscreen());
			ps.setString(6, xlogsdt.getXsession());
			ps.setString(7, xlogsdt.getXsource());
			if (xlogsdt.getXstaff() != null) {
				ps.setInt(8, xlogsdt.getXstaff());
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}
			ps.setString(9, xlogsdt.getXstatement());
			ps.setString(10, xlogsdt.getXtable());
			ps.setString(11, xlogsdt.getZemail());
			ps.setInt(12, xlogsdt.getZid());
			ps.setTimestamp(13, new Timestamp(xlogsdt.getZtime().getTime())); // java.util.Date -> java.sql.Timestamp
			return ps;
		}, keyHolder);

		Number key = keyHolder.getKey();
		return key != null ? key.longValue() : 0L;
	}

	@Override
	public List<Xlogsdt> SA17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xlogsdt im"))
		.append(whereClausePO13(searchText, suffix, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xlogsdt> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	private Xlogsdt constractListOfXwhs(Map<String, Object> row) {
		Xlogsdt em = new Xlogsdt();
		em.setXid((Long) row.get("xid"));
		em.setZid((Integer) row.get("zid"));
		em.setXsession((String) row.get("xsession"));
		em.setZemail((String) row.get("zemail"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setZtime((Date) row.get("ztime"));
		em.setXdate((Date) row.get("xdate"));
		em.setXscreen((String) row.get("xscreen"));
		em.setXfunc((String) row.get("xfunc"));
		em.setXsource((String) row.get("xsource"));
		em.setXtable((String) row.get("xtable"));
		em.setXdata((String) row.get("xdata"));
		em.setXstatement((String) row.get("xstatement"));
		em.setXresult((String) row.get("xresult"));
		return em;
	}


	@Override
	public int SA17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xlogsdt im"))
		.append(whereClausePO13(searchText, suffix, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.* ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClausePO13(String searchText, int suffix, SA17SearchParam param) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		sql.append(" AND (im.xdate between '"+ sdf.format(param.getXfdate()) +"' AND '"+ sdf.format(param.getXtdate()) +"') ");
		if(StringUtils.isNotBlank(param.getZemail())) sql.append(" AND im.zemail = '"+ param.getZemail() +"' ");
		if(param.getXstaff() != null) sql.append(" AND im.xstaff = '"+ param.getXstaff() +"' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.zemail LIKE '%" + searchText + "%' "
				+ "OR im.xscreen LIKE '%" + searchText + "%' "
				+ "OR im.xstaff LIKE '%" + searchText + "%' "
				+ "OR im.xsource LIKE '%" + searchText + "%' "
				+ "OR im.xtable LIKE '%" + searchText + "%' "
				+ "OR im.xdata LIKE '%" + searchText + "%' "
				+ "OR im.xfunc LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

}
