package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.entity.pk.OpordheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface OpordheaderRepo extends JpaRepository<Opordheader, OpordheaderPK> {

	@Transactional
	@Procedure(name = "SO_CreateDOfromOrder")
	public void SO_CreateDOfromOrder(@Param("zid") Integer zid, @Param("user") String user, @Param("ordernum") Integer ordernum);

	@Query(value = "select count(*) from opdoheader h where h.zid=?1 and h.xordernum=?2 and h.xstatusim='Open'", nativeQuery = true)
	public Long getOpenInvoiceCount(Integer zid, Integer xordernum); 

	@Query(value = "select count(*) from opdoheader h where h.zid=?1 and h.xordernum=?2 and h.xstatusim='Confirmed'", nativeQuery = true)
	public Long getConfirmedInvoiceCount(Integer zid, Integer xordernum); 
}
