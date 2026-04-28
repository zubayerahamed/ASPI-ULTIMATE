package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.ProfileWiseUser;
import com.zayaanit.repo.XlogsRepo;
import com.zayaanit.repo.XusersRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WA01Service extends AbstractGenericService {

	@Autowired private XusersRepo xusersRepo;
	@Autowired private XlogsRepo xlogsRepo;

	public long totalUsers() {
		return xusersRepo.countByZid(sessionManager.getBusinessId());
	}

	public long activeUsers() {
		return xusersRepo.countByZidAndZactive(sessionManager.getBusinessId(), Boolean.TRUE);
	}

	public long todaysLoggedInUsers() {
		return xlogsRepo.getTodaysLoggedInUsers(sessionManager.getBusinessId());
	}

	public long currentLoggedInUsers() {
		return xlogsRepo.getCurrentLoggedInUsers(sessionManager.getBusinessId());
	}

	@SuppressWarnings("unchecked")
	public List<ProfileWiseUser> profileWiseUsers() {
		String query = "SELECT p.xprofile as profile, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid) AS total, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup "
				+ "LEFT JOIN xusers x ON x.zemail = xup.zemail AND x.zid = xup.zid "
				+ "WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid AND x.zactive = 1) AS active "
				+ "FROM xprofiles p WHERE p.zid = :zid";

		//List<Object[]> results = em.createNativeQuery(query).setParameter("zid", sessionManager.getBusinessId()).getResultList();

		List<ProfileWiseUser> profileWiseUsers = new ArrayList<>();
//		for (Object[] row : results) {
//			ProfileWiseUser ad05wg05 = new ProfileWiseUser();
//			ad05wg05.setProfile((String) row[0]);
//			ad05wg05.setTotal(((Number) row[1]).longValue());
//			ad05wg05.setActive(((Number) row[2]).longValue());
//			profileWiseUsers.add(ad05wg05);
//		}

		return profileWiseUsers;
	}
}
