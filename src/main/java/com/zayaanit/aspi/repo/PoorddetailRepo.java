package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Poorddetail;
import com.zayaanit.aspi.entity.pk.PoorddetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 2, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PoorddetailRepo extends JpaRepository<Poorddetail, PoorddetailPK> {

	List<Poorddetail> findAllByZidAndXpornum(Integer zid, Integer xpornum);

	void deleteAllByZidAndXpornum(Integer zid, Integer xpornum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from poorddetail where zid=?1 and xpornum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xpornum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from poorddetail where zid=?1 and xpornum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xpornum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from poorddetail where zid=?1 and xpornum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xpornum);
}
