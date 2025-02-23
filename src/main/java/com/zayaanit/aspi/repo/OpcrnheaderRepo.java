package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Opcrnheader;
import com.zayaanit.aspi.entity.pk.OpcrnheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 16, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface OpcrnheaderRepo extends JpaRepository<Opcrnheader, OpcrnheaderPK> {

	@Transactional
	@Procedure(name = "SO_ConfirmReturn")
	public void SO_ConfirmReturn(@Param("zid") Integer zid, @Param("user") String user, @Param("crnnum") Integer crnnum);

	@Transactional
	@Procedure(name = "SO_CreateReturnfromInvoice")
	public void SO_CreateReturnfromInvoice(@Param("zid") Integer zid, @Param("user") String user, @Param("crnnum") Integer crnnum, @Param("dornum") Integer dornum);

	@Query(value = "select count(*) from opcrnheader where zid=?1 and xdornum=?2 AND xstatusim='Open'", nativeQuery = true)
	public Long getTotalPendingReturn(Integer zid, Integer xdornum);

	@Query(value = "select count(*) from opcrnheader where zid=?1 and xdornum=?2 AND xstatusim='Open' AND xcrnnum!=?3", nativeQuery = true)
	public Long getTotalPendingReturn(Integer zid, Integer xdornum, Integer xcrnnum);
}
