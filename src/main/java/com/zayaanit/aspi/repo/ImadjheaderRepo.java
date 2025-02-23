package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imadjheader;
import com.zayaanit.aspi.entity.pk.ImadjheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImadjheaderRepo extends JpaRepository<Imadjheader, ImadjheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmAdjustment")
	public void IM_ConfirmAdjustment(@Param("zid") Integer zid, @Param("user") String user, @Param("adjnum") Integer adjnum);
}
