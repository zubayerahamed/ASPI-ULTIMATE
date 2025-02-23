package com.zayaanit.aspi.repo;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Tempvoucher;
import com.zayaanit.aspi.entity.pk.TempvoucherPK;

import jakarta.persistence.TemporalType;
import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface TempvoucherRepo extends JpaRepository<Tempvoucher, TempvoucherPK> {

	@Transactional
	@Procedure(name = "FA_ImportVoucher")
	public void FA_ImportVoucher(@Param("zid") Integer zid, @Param("user") String user, @Param("post") Boolean post);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from tempvoucher where zid=?1", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid);

//	public List<Tempvoucher> findAllByZid(Integer zid);

	public Page<Tempvoucher> findAllByZid(Integer zid, Pageable pageable);

	@Query("SELECT COUNT(t) FROM Tempvoucher t WHERE t.zid = :zid")
	long countByZid(@Param("zid") Integer zid);

	long countByZidAndAllOk(Integer zid, Boolean allOk);

	void deleteByZidAndXrowIn(Integer zid, List<Integer> xrows);

	@Modifying
	@Query(value = "delete from tempvoucher where zid=?1", nativeQuery = true)
	void deleteAllByZid(Integer zid);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO tempvoucher (zid, xrow, Voucher_Date, Business_Unit, Debit_Acc, Debit_SubAcc, Credit_Acc, Credit_SubAcc, Amount, Narration) "
			+ "VALUES (?1, ?2, NULLIF(?3, ''), ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
	int insertTempvoucher(Integer zid, Integer xrow, @org.springframework.data.jpa.repository.Temporal(TemporalType.DATE) Date voucherDate, Integer businessUnit, Integer debitAcc,
			Integer debitSubAcc, Integer creditAcc, Integer creditSubAcc, BigDecimal amount, String narration);

	@Modifying
	@Transactional
	@Query(value = "UPDATE tempvoucher set Status = ?3, ErrorDetails=?4 WHERE zid=?1 and xrow=?2", nativeQuery = true)
	int updateTempvoucher(Integer zid, Integer xrow, int allOk, String errorDetails);
}
