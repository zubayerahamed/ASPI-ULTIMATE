package com.zayaanit.aspi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.model.AD05WG05;
import com.zayaanit.aspi.repo.XlogsRepo;
import com.zayaanit.aspi.repo.XusersRepo;
import com.zayaanit.aspi.service.WidgetService;

/**
 * @author Zubayer Ahaned
 * @since Oct 28, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WidgetServiceImpl extends AbstractGenericService implements WidgetService {

	@Autowired private XusersRepo xusersRepo;
	@Autowired private XlogsRepo xlogsRepo;

	/**
	 * Total Users
	 */
	@Override
	public long AD05WG01() {
		return xusersRepo.countByZid(sessionManager.getBusinessId());
	}

	/**
	 * Total Active Users
	 */
	@Override
	public long AD05WG02() {
		return xusersRepo.countByZidAndZactive(sessionManager.getBusinessId(), Boolean.TRUE);
	}

	/**
	 * Today Logged-In Users
	 */
	@Override
	public long AD05WG03() {
		return xlogsRepo.getTodaysLoggedInUsers(sessionManager.getBusinessId());
	}

	/**
	 * Current Logged-In Users
	 */
	@Override
	public long AD05WG04() {
		return xlogsRepo.getCurrentLoggedInUsers(sessionManager.getBusinessId());
	}

	/**
	 * Profile Wise Users
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AD05WG05> AD05WG05() {
		String query = "SELECT p.xprofile as profile, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid) AS total, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup "
				+ "LEFT JOIN xusers x ON x.zemail = xup.zemail AND x.zid = xup.zid "
				+ "WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid AND x.zactive = 1) AS active "
				+ "FROM xprofiles p WHERE p.zid = :zid";

		List<Object[]> results = em.createNativeQuery(query).setParameter("zid", sessionManager.getBusinessId()).getResultList();

		List<AD05WG05> profileWiseUsers = new ArrayList<>();
		for (Object[] row : results) {
			AD05WG05 ad05wg05 = new AD05WG05();
			ad05wg05.setProfile((String) row[0]);
			ad05wg05.setTotal(((Number) row[1]).longValue());
			ad05wg05.setActive(((Number) row[2]).longValue());
			profileWiseUsers.add(ad05wg05);
		}

		return profileWiseUsers;
	}

	
}
