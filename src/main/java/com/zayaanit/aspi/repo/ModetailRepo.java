package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Modetail;
import com.zayaanit.aspi.entity.pk.ModetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2025
 */
@Repository
public interface ModetailRepo extends JpaRepository<Modetail, ModetailPK> {

	List<Modetail> findAllByZidAndXbatchAndXsign(Integer zid, Integer xbatch, Integer xsign);

	void deleteAllByZidAndXbatchAndXsign(Integer zid, Integer xbatch, Integer xsign);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from modetail where zid=?1 and xbatch=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xbatch);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from modetail where zid=?1 and xbatch=?2 and xsign=?3", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xbatch, Integer xsign);
}
