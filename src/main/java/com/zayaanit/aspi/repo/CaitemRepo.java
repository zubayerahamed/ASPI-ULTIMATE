package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.entity.pk.CaitemPK;

/**
 * @author Zubayer Ahaned
 * @since Dec 31, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface CaitemRepo extends JpaRepository<Caitem, CaitemPK> {

	@Query(value = "select count(*) from caitem where zid=?1 and xisop=1 and xgitem != 'Services' and (xbarcode=?2 or xitem=?2)", nativeQuery = true)
	public long searchItemCount(Integer zid, String searchtext);

	@Query(value = "SELECT c.* FROM Caitem c WHERE c.zid = ?1 AND c.xisop = 1 AND c.xgitem != 'Services'", nativeQuery = true)
	List<Caitem> findByZidAndXisopAndNotXgitem(Integer zid);

	List<Caitem> findAllByZid(Integer zid);
}
