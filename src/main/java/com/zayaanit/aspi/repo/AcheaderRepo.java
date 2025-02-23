package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Acheader;
import com.zayaanit.aspi.entity.pk.AcheaderPK;

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

	@Transactional
	@Procedure(name = "FA_VoucherPost")
	void FA_VoucherPost(@Param("zid") Integer zid, @Param("user") String user, @Param("voucher") Integer voucher);

	@Transactional
	@Procedure(name = "FA_VoucherUnPost")
	void FA_VoucherUnPost(@Param("zid") Integer zid, @Param("user") String user, @Param("voucher") Integer voucher);

	List<Acheader> findAllByZidAndXstatusjv(Integer zid, String xstatusjv);
}
