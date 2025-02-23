package com.zayaanit.aspi.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imopendetail;
import com.zayaanit.aspi.entity.pk.ImopendetailPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImopendetailRepo extends JpaRepository<Imopendetail, ImopendetailPK> {

	List<Imopendetail> findAllByZidAndXopennum(Integer zid, Integer xopennum);

	void deleteAllByZidAndXopennum(Integer zid, Integer xopennum);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imopendetail where zid=?1 and xopennum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xopennum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from imopendetail where zid=?1 and xopennum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xopennum);

	@Query(value = "select isnull(sum(COALESCE(xqty,0)), 0) from imopendetail where zid=?1 and xopennum=?2", nativeQuery = true)
	public BigDecimal getTotalQty(Integer zid, Integer xopennum);
}
