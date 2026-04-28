package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.pk.CadocPK;

/**
 * @author Zubayer Ahamed
 * @since Aug 6, 2023
 */
@Repository
public interface CadocRepo extends JpaRepository<Cadoc, CadocPK> {

	public List<Cadoc> findAllByZidAndXscreenAndXtrnnum(Integer zid, String xscreen, Integer xtrnnum);
}
