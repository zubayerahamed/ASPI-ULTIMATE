package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imtordetail;
import com.zayaanit.aspi.entity.pk.ImtordetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface ImtordetailRepo extends JpaRepository<Imtordetail, ImtordetailPK> {

	List<Imtordetail> findAllByZidAndXtornum(Integer zid, Integer xtornum);

	void deleteAllByZidAndXtornum(Integer zid, Integer xtornum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imtordetail where zid=?1 and xtornum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xtornum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from imtordetail where zid=?1 and xtornum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xtornum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from imtordetail where zid=?1 and xtornum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xtornum);
}
