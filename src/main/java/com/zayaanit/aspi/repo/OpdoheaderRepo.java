package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Opdoheader;
import com.zayaanit.aspi.entity.pk.OpdoheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface OpdoheaderRepo extends JpaRepository<Opdoheader, OpdoheaderPK> {

	@Transactional
	@Procedure(name = "SO_ConfirmInvoice")
	public void SO_ConfirmInvoice(@Param("zid") Integer zid, @Param("user") String user, @Param("dornum") Integer dornum);

	public List<Opdoheader> findAllByZidAndXdornum(Integer zid, Integer xdornum);

	public List<Opdoheader> findAllByZidAndXordernum(Integer zid, Integer xordernum);

	@Query(value = "select count(*) from opdoheader h join opdodetail d on h.zid=d.zid and h.xdornum=d.xdornum where h.zid=?1 and h.xdornum=?2 and h.xstatus='Confirmed' and h.xstatusim='Confirmed' and (d.xqty-d.xqtycrn)>0", nativeQuery = true)
	public Long isInvalidSalesNumber(Integer zid, Integer xdornum);
}
