package com.zayaanit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.pk.AcheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcheaderRepo extends JpaRepository<Acheader, AcheaderPK>{

	public long countByZidAndXdate(Integer zid, Date xdate);
	public long countByZidAndXper(Integer zid, Integer xper);
	public long countByZidAndXyear(Integer zid, Integer xyear);

	public long countByZidAndXdateAndXstatusjv(Integer zid, Date xdate, String xstatusjv);
	public long countByZidAndXperAndXstatusjv(Integer zid, Integer xper, String xstatusjv);
	public long countByZidAndXyearAndXstatusjv(Integer zid, Integer xyear, String xstatusjv);

	@Transactional
	@Procedure(name = "FA_VoucherPost")
	void FA_VoucherPost(@Param("zid") Integer zid, @Param("user") String user, @Param("voucher") Integer voucher);

	@Transactional
	@Procedure(name = "FA_VoucherUnPost")
	void FA_VoucherUnPost(@Param("zid") Integer zid, @Param("user") String user, @Param("voucher") Integer voucher);

	List<Acheader> findAllByZidAndXstatusjv(Integer zid, String xstatusjv);
}
