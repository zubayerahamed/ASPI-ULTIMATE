package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imissuedetail;
import com.zayaanit.aspi.entity.pk.ImissuedetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Repository
public interface ImissuedetailRepo extends JpaRepository<Imissuedetail, ImissuedetailPK> {

	List<Imissuedetail> findAllByZidAndXissuenum(Integer zid, Integer xissuenum);

	void deleteAllByZidAndXissuenum(Integer zid, Integer xissuenum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imissuedetail where zid=?1 and xissuenum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xissuenum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from imissuedetail where zid=?1 and xissuenum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xissuenum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from imissuedetail where zid=?1 and xissuenum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xissuenum);
}
