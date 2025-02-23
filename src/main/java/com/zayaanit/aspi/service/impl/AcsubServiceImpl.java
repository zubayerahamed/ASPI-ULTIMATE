package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.entity.pk.AcmstPK;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.repo.AcmstRepo;
import com.zayaanit.aspi.service.AcsubService;
import com.zayaanit.aspi.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class AcsubServiceImpl extends AbstractService implements AcsubService {
	@Autowired private KitSessionManager sessionManager;
	@Autowired private AcmstRepo acmstRepo;

	@Override
	public List<Acsub> LFA14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acsub im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acsub> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcsub(row)));

		return list;
	}

	@Override
	public int LFA14(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acsub im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Acsub constractListOfAcsub(Map<String, Object> row) {
		Acsub em = new Acsub();
		em.setXacc((Integer) row.get("xacc"));
		em.setXsub((Integer) row.get("xsub"));
		em.setXdesc((String) row.get("xdesc"));
		em.setXname((String) row.get("xname"));
		em.setXtype((String) row.get("xtype"));
		em.setAccountName((String) row.get("accountname"));
		em.setXgcus((String) row.get("xgcus"));
		em.setXgsup((String) row.get("xgsup"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, a.xdesc as accountname ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acmst a ON a.xacc = im.xacc AND a.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 4) {
			String paramsValues[] = dependentParam.split(",");

			if(paramsValues.length == 2 && "Sub Account".equals(paramsValues[0])) {
				sql = sql.append(" AND im.xtype='"+ paramsValues[0] +"' ")
						.append(" AND im.xacc='"+ paramsValues[1] +"' ");   // xacc only match for sub account, otherwise ignore it
			} else {
				sql = sql.append(" AND im.xtype='"+ paramsValues[0] +"' ");
			}
		} else if(suffix == 2) {
			sql = sql.append(" AND im.xtype='Supplier' ");
		} else if(suffix == 1) {
			sql = sql.append(" AND im.xtype='Customer' ");
		} else if(suffix == 3) {
			sql = sql.append(" AND im.xtype='Employee' ");
		} else if (suffix == 5) {
			String paramsValues[] = dependentParam.split(",");
			if(paramsValues.length == 1) {
				Optional<Acmst> acmstOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), Integer.valueOf(paramsValues[0])));
				if(acmstOp.isPresent()) {
					sql = sql.append(" AND im.xtype='"+ acmstOp.get().getXaccusage() +"' ");
				}
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xsub LIKE '%" + searchText + "%' "
				+ "OR im.xname LIKE '%" + searchText + "%' "
				+ "OR im.xdesc LIKE '%" + searchText + "%' "
				+ "OR im.xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
