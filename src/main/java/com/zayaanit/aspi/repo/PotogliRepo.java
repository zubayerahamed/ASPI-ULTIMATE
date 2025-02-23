package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Potogli;
import com.zayaanit.aspi.entity.pk.PotogliPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 8, 2025
 */
@Repository
public interface PotogliRepo extends JpaRepository<Potogli, PotogliPK> {

}
