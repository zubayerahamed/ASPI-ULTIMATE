package com.zayaanit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acdef;
import com.zayaanit.entity.pk.AcdefPK;

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
