package com.zayaanit.repo;

import com.zayaanit.entity.Optogli;
import com.zayaanit.entity.pk.OptogliPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zubayer Ahaned
 * @since Jan 13, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface OptogliRepo extends JpaRepository<Optogli, OptogliPK> {

}
