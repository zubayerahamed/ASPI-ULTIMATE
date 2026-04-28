package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Moheader;
import com.zayaanit.entity.pk.MoheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2025
 */
@Repository
public interface MoheaderRepo extends JpaRepository<Moheader, MoheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmBatch")
	public void IM_ConfirmBatch(@Param("zid") Integer zid, @Param("user") String user, @Param("batch") Integer batch);

	@Query(value = "Select top (:#{#last}) xbatch, xqty Quantity, xrate Rate  "
			+ "from moheader  "
			+ "where zid=:zid "
			+ "and xstatus='Confirmed' and xstatusim='Confirmed' "
			+ "and (-1=:xbuid OR xbuid=:xbuid) "
			+ "and (-1=:xitem OR xitem=:xitem) "
			+ "order by xdate, xbatch", nativeQuery = true)
	List<Object[]> batchWI05WG01(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("last") Integer last
	);

	@Query(value = "select xbatch, xqty Quantity, xrate Rate from moheader  "
			+ "where zid=:zid "
			+ "and xstatus='Confirmed' and xstatusim='Confirmed' "
			+ "and xdate between :xfdate and :xtdate "
			+ "and (-1=:xbuid OR xbuid=:xbuid) "
			+ "and (-1=:xitem OR xitem=:xitem) "
			+ "order by xdate, xbatch", nativeQuery = true)
	List<Object[]> dateBetweenWI05WG01(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("xfdate") String xfdate,
		@Param("xtdate") String xtdate
	);
}
