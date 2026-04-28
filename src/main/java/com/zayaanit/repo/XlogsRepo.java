package com.zayaanit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xlogs;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Repository
public interface XlogsRepo extends JpaRepository<Xlogs, Long> {

	@Query(value = "SELECT COUNT(DISTINCT(zemail)) FROM xlogs WHERE zid = ?1 AND CONVERT(DATE, ztime) = CONVERT(DATE, GETDATE())", nativeQuery = true)
	public Long getTodaysLoggedInUsers(Integer zid);

	@Query(value = "SELECT COUNT(DISTINCT(zemail)) FROM xlogs WHERE zid = ?1 AND CONVERT(DATE, ztime) = CONVERT(DATE, GETDATE()) AND xaction='Login'", nativeQuery = true)
	public Long getCurrentLoggedInUsers(Integer zid);
}
