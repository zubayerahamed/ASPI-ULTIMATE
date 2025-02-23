package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Pogrndetail;
import com.zayaanit.aspi.entity.pk.PogrndetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PogrndetailRepo extends JpaRepository<Pogrndetail, PogrndetailPK> {

	List<Pogrndetail> findAllByZidAndXgrnnum(Integer zid, Integer xgrnnum);

	void deleteAllByZidAndXgrnnum(Integer zid, Integer xgrnnum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from pogrndetail where zid=?1 and xgrnnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xgrnnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from pogrndetail where zid=?1 and xgrnnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xgrnnum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from pogrndetail where zid=?1 and xgrnnum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xgrnnum);
}
