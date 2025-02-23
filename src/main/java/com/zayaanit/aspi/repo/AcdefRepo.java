package com.zayaanit.aspi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Acdef;
import com.zayaanit.aspi.entity.pk.AcdefPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 26, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcdefRepo extends JpaRepository<Acdef, AcdefPK> {

}
