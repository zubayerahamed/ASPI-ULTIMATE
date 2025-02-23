package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Pocrnheader;
import com.zayaanit.aspi.entity.pk.PocrnheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PocrnheaderRepo extends JpaRepository<Pocrnheader, PocrnheaderPK> {

	@Transactional
	@Procedure(name = "PO_ConfirmReturn")
	public void PO_ConfirmReturn(@Param("zid") Integer zid, @Param("user") String user, @Param("crnnum") Integer crnnum);

	@Transactional
	@Procedure(name = "PO_CreateReturnfromGRN")
	public void PO_CreateReturnfromGRN(@Param("zid") Integer zid, @Param("user") String user, @Param("crnnum") Integer crnnum, @Param("grnnum") Integer grnnum);

	@Query(value = "select count(*) from pocrnheader where zid=?1 and xgrnnum=?2 AND xstatusim='Open'", nativeQuery = true)
	public Long getTotalPendingReturn(Integer zid, Integer xgrnnum);

	@Query(value = "select count(*) from pocrnheader where zid=?1 and xgrnnum=?2 AND xstatusim='Open' AND xcrnnum!=?3", nativeQuery = true)
	public Long getTotalPendingReturn(Integer zid, Integer xgrnnum, Integer xcrnnum);
}
