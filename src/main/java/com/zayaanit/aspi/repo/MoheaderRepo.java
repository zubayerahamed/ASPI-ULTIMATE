package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Moheader;
import com.zayaanit.aspi.entity.pk.MoheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2025
 */
@Repository
public interface MoheaderRepo extends JpaRepository<Moheader, MoheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmBatch")
	public void IM_ConfirmBatch(@Param("zid") Integer zid, @Param("user") String user, @Param("batch") Integer batch);
}
