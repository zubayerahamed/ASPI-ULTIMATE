package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Opdodetail;
import com.zayaanit.aspi.entity.pk.OpdodetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface OpdodetailRepo extends JpaRepository<Opdodetail, OpdodetailPK> {

	List<Opdodetail> findAllByZidAndXdornum(Integer zid, Integer xdornum);

	void deleteAllByZidAndXdornum(Integer zid, Integer xdornum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opdodetail where zid=?1 and xdornum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xdornum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from opdodetail where zid=?1 and xdornum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xdornum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from opdodetail where zid=?1 and xdornum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xdornum);
}
