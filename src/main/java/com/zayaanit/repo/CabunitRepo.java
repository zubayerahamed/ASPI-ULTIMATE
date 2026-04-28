package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.pk.CabunitPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 26, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface CabunitRepo extends JpaRepository<Cabunit, CabunitPK> {

	@Query(value = "select isnull(max(COALESCE(xbuid,0)) + 1, 101) from cabunit where zid=?1", nativeQuery = true)
	public Integer getNextAvailableId(Integer zid);

	List<Cabunit> findAllByZid(Integer zid);
}
