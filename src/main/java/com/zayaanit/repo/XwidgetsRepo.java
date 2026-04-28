package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.XwidgetsPK;

/**
 * @author Zubayer Ahaned
 * @since Feb 23, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XwidgetsRepo extends JpaRepository<Xwidgets, XwidgetsPK> {

	List<Xwidgets> findAllByZid(Integer zid);
}
