package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Xlogs;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SA17SearchParam;
import com.zayaanit.repo.XlogsRepo;
import com.zayaanit.service.BrowserDetectionService;
import com.zayaanit.service.BrowserDetectionService.BrowserInfo;
import com.zayaanit.service.XlogsService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Service
public class XlogsServiceImpl extends AbstractGenericService implements XlogsService {

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired private XlogsRepo xlogsRepo;
	@Autowired private BrowserDetectionService bdService;

	@Override
	public void SA17delete(SA17SearchParam param) {
		StringBuilder sql = new StringBuilder("DELETE FROM xlogs WHERE zid = ?");
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

	@Override
	public Xlogs switchBusiness(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Switch Business");
		xlogs.setXuseragent(bi.toString());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	

	@Override
	public Xlogs switchProfile(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Switch Profile");
		xlogs.setXuseragent(bi.toString());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}



	@Override
	public Xlogs login(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Login");
		xlogs.setXuseragent(bi.toString());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	@Override
	public Xlogs logout(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Logout");
		xlogs.setXuseragent(bi.toString());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}



	@Override
	public List<Xlogs> SA17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xlogs im"))
		.append(whereClausePO13(searchText, suffix, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xlogs> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	private Xlogs constractListOfXwhs(Map<String, Object> row) {
		Xlogs em = new Xlogs();
		em.setXid((Long) row.get("xid"));
		em.setZid((Integer) row.get("zid"));
		em.setXsession((String) row.get("xsession"));
		em.setXcip((String) row.get("xcip"));
		em.setZemail((String) row.get("zemail"));
		em.setXprofile((String) row.get("xprofile"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXaction((String) row.get("xaction"));
		em.setZtime((Date) row.get("ztime"));
		em.setXdate((Date) row.get("xdate"));
		em.setXuseragent((String) row.get("xuseragent"));
		return em;
	}


	@Override
	public int SA17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xlogs im"))
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
				+ "OR im.xprofile LIKE '%" + searchText + "%' "
				+ "OR im.xstaff LIKE '%" + searchText + "%' "
				+ "OR im.xaction LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
