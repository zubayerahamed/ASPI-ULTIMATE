package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Cadoc;
import com.zayaanit.aspi.entity.pk.CadocPK;

/**
 * @author Zubayer Ahamed
 * @since Aug 6, 2023
 */
@Repository
public interface CadocRepo extends JpaRepository<Cadoc, CadocPK> {

	public List<Cadoc> findAllByZidAndXscreenAndXtrnnum(Integer zid, String xscreen, Integer xtrnnum);
}
