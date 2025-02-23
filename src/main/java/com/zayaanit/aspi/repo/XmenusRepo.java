package com.zayaanit.aspi.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xmenus;
import com.zayaanit.aspi.entity.pk.XmenusPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 24, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XmenusRepo extends JpaRepository<Xmenus, XmenusPK> {

	List<Xmenus> findAllByZidAndXpmenu(Integer zid, String xpmenu);

	List<Xmenus> findAllByZidAndXpmenuAndXmenuIn(Integer zid, String xpmenu, Set<String> xmenus);
}
