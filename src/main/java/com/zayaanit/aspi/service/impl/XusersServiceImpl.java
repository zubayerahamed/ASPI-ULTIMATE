package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.model.MyUserDetails;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Service
public class XusersServiceImpl extends AbstractService implements XusersService, UserDetailsService {
	
	@Autowired private KitSessionManager sessionManager;

	@Autowired
	private XusersRepo xuserRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("User not found in the system");
		}

		List<Xusers> users = xuserRepo.findAllByZemailAndZactive(username, Boolean.TRUE);
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("User not found in the system");
		}

		Xusers user = users.stream().findFirst().orElse(null);
		if(user == null) {
			throw new UsernameNotFoundException("User not found in the system");
		}

		return new MyUserDetails(user);
	}

	@Override
	public List<Xusers> LAD13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xusers im"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xusers> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LAD13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xusers im"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xusers constractListOfXscreens(Map<String, Object> row) {
		Xusers em = new Xusers();
		em.setZemail((String) row.get("zemail"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setZactive((Boolean) row.get("zactive"));
		em.setEmployeeName((String) row.get("employeename"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, e.xname as employeename ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN acsub e ON e.xsub = im.xstaff AND e.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");
		if (searchText == null || searchText.isEmpty()) return sql;
		
		return sql.append(" AND (im.zemail LIKE '%" + searchText + "%' "
				+ "OR im.xstaff LIKE '%" + searchText + "%' "
				+ "OR e.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
