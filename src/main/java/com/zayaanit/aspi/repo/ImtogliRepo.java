package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imtogli;
import com.zayaanit.aspi.entity.pk.ImtogliPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 13, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImtogliRepo extends JpaRepository<Imtogli, ImtogliPK> {

}
