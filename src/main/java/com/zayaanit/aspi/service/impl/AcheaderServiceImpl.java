package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zayaanit.aspi.entity.Acdef;
import com.zayaanit.aspi.entity.Acheader;
import com.zayaanit.aspi.entity.pk.AcdefPK;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.model.FA18SearchParam;
import com.zayaanit.aspi.model.YearPeriodResult;
import com.zayaanit.aspi.repo.AcdefRepo;
import com.zayaanit.aspi.service.AcheaderService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class AcheaderServiceImpl extends AbstractService implements AcheaderService {
	@Autowired private KitSessionManager sessionManager;
	@Autowired private AcdefRepo acdefRepo;

	@Override
	public List<Acheader> LFA15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcsub(row)));

		return list;
	}

	@Override
	public int LFA15(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Acheader> LFA18(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, FA18SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acheader im"))
		.append(whereClauseLFA18(searchText, suffix, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcsub(row)));

		return list;
	}

	@Override
	public int LFA18(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, FA18SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acheader im"))
		.append(whereClauseLFA18(searchText, suffix, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Acheader constractListOfAcsub(Map<String, Object> row) {
		Acheader em = new Acheader();
		em.setXvoucher((Integer) row.get("xvoucher"));
		em.setXdate((Date) row.get("xdate"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXvtype((String) row.get("xvtype"));
		em.setXref((String) row.get("xref"));
		em.setXnote((String) row.get("xnote"));
		em.setXyear((Integer) row.get("xyear"));
		em.setXper((Integer) row.get("xper"));
		em.setXstatusjv((String) row.get("xstatusjv"));
		em.setXtype((String) row.get("xtype"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setBusinessUnitName((String) row.get("businessunitname"));
		em.setStaffName((String) row.get("staffname"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, bu.xname as businessunitname, ac.xname as staffname ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit bu ON bu.xbuid = im.xbuid AND bu.zid = im.zid ")
				.append(" LEFT JOIN acsub ac ON ac.xsub = im.xstaff AND ac.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) sql = sql.append(" AND im.xtype='General' ");
		if(suffix == 2) sql = sql.append(" AND im.xtype='Imported' ");
		if(suffix == 3) sql = sql.append(" AND im.xtype='Integrated' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xvoucher LIKE '%" + searchText + "%' "
				+ "OR im.xbuid LIKE '%" + searchText + "%' "
				+ "OR bu.xname LIKE '%" + searchText + "%' "
				+ "OR im.xref LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLFA18(String searchText, int suffix, FA18SearchParam param) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		sql.append(" AND (im.xdate between '"+ sdf.format(param.getXfdate()) +"' AND '"+ sdf.format(param.getXtdate()) +"') ");
		if(param.getXyear() != null) sql.append(" AND im.xyear = '"+ param.getXyear() +"' ");
		if(param.getXper() != null) sql.append(" AND im.xper = '"+ param.getXper() +"' ");
		if(param.getXbuid() != null) sql.append(" AND im.xbuid = '"+ param.getXbuid() +"' ");
		if(StringUtils.hasText(param.getXtype())) sql.append(" AND im.xtype = '"+ param.getXtype() +"' ");
		if(StringUtils.hasText(param.getXstatusjv())) sql.append(" AND im.xstatusjv = '"+ param.getXstatusjv() +"' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xdate LIKE '%" + searchText + "%' "
				+ "OR im.xyear LIKE '%" + searchText + "%' "
				+ "OR im.xper LIKE '%" + searchText + "%' "
				+ "OR im.xbuid LIKE '%" + searchText + "%' "
				+ "OR im.xtype LIKE '%" + searchText + "%' "
				+ "OR bu.xname LIKE '%" + searchText + "%' "
				+ "OR im.xstatusjv LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	@Override
	public YearPeriodResult getYearPeriod(Date date) {
		if(date == null) return null;

		Optional<Acdef> acdefOp = acdefRepo.findById(new AcdefPK(sessionManager.getBusinessId()));
		if(!acdefOp.isPresent()) return null;

		Acdef acdef = acdefOp.get();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int per = 12 + (cal.get(Calendar.MONTH) + 1) - acdef.getXoffset();

		if(per <= 12) {
			year = year - 1;
		} else {
			per = per - 12;
		}

		return new YearPeriodResult(year, per);
	}
}
