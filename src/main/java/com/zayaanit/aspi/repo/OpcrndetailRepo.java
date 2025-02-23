package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Opcrndetail;
import com.zayaanit.aspi.entity.pk.OpcrndetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 16, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface OpcrndetailRepo extends JpaRepository<Opcrndetail, OpcrndetailPK> {

	List<Opcrndetail> findAllByZidAndXcrnnum(Integer zid, Integer xcrnnum);

	void deleteAllByZidAndXcrnnum(Integer zid, Integer xcrnnum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opcrndetail where zid=?1 and xcrnnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xcrnnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from opcrndetail where zid=?1 and xcrnnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xcrnnum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from opcrndetail where zid=?1 and xcrnnum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xcrnnum);
}
