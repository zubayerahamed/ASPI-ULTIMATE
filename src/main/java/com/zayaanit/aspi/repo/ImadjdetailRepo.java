package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imadjdetail;
import com.zayaanit.aspi.entity.pk.ImadjdetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Repository
public interface ImadjdetailRepo extends JpaRepository<Imadjdetail, ImadjdetailPK> {

	List<Imadjdetail> findAllByZidAndXadjnum(Integer zid, Integer xadjnum);

	void deleteAllByZidAndXadjnum(Integer zid, Integer xadjnum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imadjdetail where zid=?1 and xadjnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xadjnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from imadjdetail where zid=?1 and xadjnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xadjnum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from imadjdetail where zid=?1 and xadjnum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xadjnum);
}
