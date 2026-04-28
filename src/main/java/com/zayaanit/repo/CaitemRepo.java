package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.pk.CaitemPK;

/**
 * @author Zubayer Ahaned
 * @since Dec 31, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface CaitemRepo extends JpaRepository<Caitem, CaitemPK> {

	@Query(value = "select count(*) from caitem where zid=?1 and xisop=true and xgitem != 'Services' and (xbarcode=?2 or xitem=?2)", nativeQuery = true)
	public long searchItemCount(Integer zid, String searchtext);

	@Query("SELECT c FROM Caitem c WHERE c.zid = :zid AND c.xisop=true AND c.xgitem != 'Services'")
	List<Caitem> findByZidAndXisopAndNotXgitem(@Param("zid") Integer zid);

	List<Caitem> findAllByZid(Integer zid);
}
