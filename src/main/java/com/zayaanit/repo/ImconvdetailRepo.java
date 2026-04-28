package com.zayaanit.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imconvdetail;
import com.zayaanit.entity.pk.ImconvdetailPK;

/**
 * @author Zubayer Ahaned
 * @since Nov 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImconvdetailRepo extends JpaRepository<Imconvdetail, ImconvdetailPK> {

	List<Imconvdetail> findAllByZidAndXconvnum(Integer zid, Integer xconvnum);

	void deleteAllByZidAndXconvnum(Integer zid, Integer xbatch);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imconvdetail where zid=?1 and xconvnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xbatch);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from imconvdetail where zid=?1 and xconvnum=?2 ", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xbatch);

}
