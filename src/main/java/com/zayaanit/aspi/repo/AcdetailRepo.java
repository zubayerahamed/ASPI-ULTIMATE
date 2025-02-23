package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Acdetail;
import com.zayaanit.aspi.entity.pk.AcdetailPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcdetailRepo extends JpaRepository<Acdetail, AcdetailPK> {

	List<Acdetail> findAllByZidAndXvoucher(Integer zid, Integer xvoucher);

	void deleteAllByZidAndXvoucher(Integer zid, Integer xvoucher);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from acdetail where zid=?1 and xvoucher=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xvoucher);

	@Query(value = "select isnull(sum(COALESCE(xprime,0)), 0) from acdetail where zid=?1 and xvoucher=?2", nativeQuery = true)
	public BigDecimal getTotalPrimeAmount(Integer zid, Integer xvoucher);
}
