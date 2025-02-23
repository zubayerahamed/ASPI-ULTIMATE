package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Pogrnheader;
import com.zayaanit.aspi.entity.pk.PogrnheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PogrnheaderRepo extends JpaRepository<Pogrnheader, PogrnheaderPK> {

	@Transactional
	@Procedure(name = "PO_ConfirmGRN")
	public void PO_ConfirmGRN(@Param("zid") Integer zid, @Param("user") String user, @Param("grnnum") Integer grnnum);

	public List<Pogrnheader> findAllByZidAndXpornum(Integer zid, Integer xpornum);

	@Query(value = "select count(*) from pogrnheader h join pogrndetail d on h.zid=d.zid and h.xgrnnum=d.xgrnnum where h.zid=?1 and h.xgrnnum=?2 and h.xstatus='Confirmed' and h.xstatusim='Confirmed' and (d.xqty-d.xqtycrn)>0", nativeQuery = true)
	public Long isInvalidGRN(Integer zid, Integer xgrnnum);
}
