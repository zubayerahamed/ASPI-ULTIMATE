package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Poordheader;
import com.zayaanit.aspi.entity.pk.PoordheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 2, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PoordheaderRepo extends JpaRepository<Poordheader, PoordheaderPK> {

	@Transactional
	@Procedure(name = "PO_CreateGRNfromOrder")
	public void PO_CreateGRNfromOrder(@Param("zid") Integer zid, @Param("user") String user, @Param("pornum") Integer pornum);

	@Query(value = "select count(*) from pogrnheader h where h.zid=?1 and h.xpornum=?2 and h.xstatusim='Open'", nativeQuery = true)
	public Long getOpenGRNCount(Integer zid, Integer xpornum); 

	@Query(value = "select count(*) from pogrnheader h where h.zid=?1 and h.xpornum=?2 and h.xstatusim='Confirmed'", nativeQuery = true)
	public Long getConfirmedGRNCount(Integer zid, Integer xpornum); 
}
