package com.zayaanit.aspi.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Acbal;
import com.zayaanit.aspi.entity.pk.AcbalPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcbalRepo extends JpaRepository<Acbal, AcbalPK> {

	@Query(value = "select distinct xyear from acbal where zid=?1 order by xyear desc", nativeQuery = true)
	public List<Integer> getDistinctYears(Integer zid);

	@Transactional
	@Procedure(name = "FA_YearEnd")
	void FA_YearEnd(@Param("zid") Integer zid, @Param("user") String user, @Param("year") Integer year, @Param("date") Date date);
}
