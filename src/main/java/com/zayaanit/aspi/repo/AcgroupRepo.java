package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Acgroup;
import com.zayaanit.aspi.entity.pk.AcgroupPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 27, 2024
 */
@Repository
public interface AcgroupRepo extends JpaRepository<Acgroup, AcgroupPK> {

	public List<Acgroup> findAllByZidAndXagparent(Integer zid, Integer xagparent);
}
