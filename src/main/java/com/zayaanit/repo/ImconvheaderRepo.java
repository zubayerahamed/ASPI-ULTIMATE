package com.zayaanit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imconvheader;
import com.zayaanit.entity.pk.ImconvheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Nov 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImconvheaderRepo extends JpaRepository<Imconvheader, ImconvheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmConversion")
	public void IM_ConfirmConversion(@Param("zid") Integer zid, @Param("user") String user,@Param("conversion") Integer conversion);

}
