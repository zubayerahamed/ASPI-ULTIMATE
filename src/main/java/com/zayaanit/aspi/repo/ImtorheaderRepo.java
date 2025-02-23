package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imtorheader;
import com.zayaanit.aspi.entity.pk.ImtorheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface ImtorheaderRepo extends JpaRepository<Imtorheader, ImtorheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmDirectTO")
	public void IM_ConfirmDirectTO(@Param("zid") Integer zid, @Param("user") String user, @Param("tornum") Integer tornum);

	@Transactional
	@Procedure(name = "IM_ConfirmBusinessTO")
	public void IM_ConfirmBusinessTO(@Param("zid") Integer zid, @Param("user") String user, @Param("tornum") Integer tornum);
}
