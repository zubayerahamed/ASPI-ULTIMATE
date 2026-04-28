package com.zayaanit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.XwhsPK;

/**
 * @author Zubayer Ahaned
 * @since Dec 31, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XwhsRepo extends JpaRepository<Xwhs, XwhsPK> {

}
