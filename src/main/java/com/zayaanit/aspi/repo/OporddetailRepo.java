package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Oporddetail;
import com.zayaanit.aspi.entity.pk.OporddetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Repository
public interface OporddetailRepo extends JpaRepository<Oporddetail, OporddetailPK> {

	List<Oporddetail> findAllByZidAndXordernum(Integer zid, Integer xordernum);

	void deleteAllByZidAndXordernum(Integer zid, Integer xordernum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from oporddetail where zid=?1 and xordernum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xordernum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from oporddetail where zid=?1 and xordernum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xordernum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from oporddetail where zid=?1 and xordernum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xordernum);
}
