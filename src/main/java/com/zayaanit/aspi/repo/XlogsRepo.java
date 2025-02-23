package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xlogs;
import com.zayaanit.aspi.entity.pk.XlogsPK;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Repository
public interface XlogsRepo extends JpaRepository<Xlogs, XlogsPK> {

	@Query(value = "SELECT COUNT(DISTINCT(zemail)) FROM xlogs WHERE zid = ?1 AND CONVERT(DATE, xlogintime) = CONVERT(DATE, GETDATE())", nativeQuery = true)
	public Long getTodaysLoggedInUsers(Integer zid);

	@Query(value = "SELECT COUNT(DISTINCT(zemail)) FROM xlogs WHERE zid = ?1 AND CONVERT(DATE, xlogintime) = CONVERT(DATE, GETDATE()) AND xexptype='Login'", nativeQuery = true)
	public Long getCurrentLoggedInUsers(Integer zid);
}
