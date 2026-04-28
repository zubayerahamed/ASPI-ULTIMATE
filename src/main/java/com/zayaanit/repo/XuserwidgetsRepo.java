package com.zayaanit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.pk.XuserwidgetsPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jul 20, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XuserwidgetsRepo extends JpaRepository<Xuserwidgets, XuserwidgetsPK> {

	List<Xuserwidgets> findAllByZidAndZemail(Integer zid, String zemail);

	@Query(value = "select isnull(max(COALESCE(xsequence,0)) + 1, 1) from xuserwidgets where zid=?1 and zemail=?2", nativeQuery = true)
	public Integer getNextAvailableSeqn(Integer zid, String zemail);

	// Update all xdefault = false where zid=?
	@Modifying
	@Transactional
	@Query(value = "update xuserwidgets set xisdefault = 0 where zid = ?1", nativeQuery = true)
	int resetAllDefaults(Integer zid);

	public Optional<Xuserwidgets> findByZidAndZemailAndXisdefaultTrue(Integer zid, String zemail);
}
