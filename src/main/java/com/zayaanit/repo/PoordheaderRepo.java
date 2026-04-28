package com.zayaanit.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.pk.PoordheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 2, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PoordheaderRepo extends JpaRepository<Poordheader, PoordheaderPK> {

	@Transactional
	@Procedure(name = "PO_CreateGRNfromOrder")
	public void PO_CreateGRNfromOrder(@Param("zid") Integer zid, @Param("user") String user, @Param("pornum") Integer pornum);

	@Query(value = "select count(*) from pogrnheader h where h.zid=?1 and h.xpornum=?2 and h.xstatusim='Open'", nativeQuery = true)
	public Long getOpenGRNCount(Integer zid, Integer xpornum); 

	@Query(value = "select count(*) from pogrnheader h where h.zid=?1 and h.xpornum=?2 and h.xstatusim='Confirmed'", nativeQuery = true)
	public Long getConfirmedGRNCount(Integer zid, Integer xpornum); 

	@Query(value = "select "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as today "
			+ " from  "
			+ " poordheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysPurchaseOrder(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as month "
			+ " from  "
			+ " poordheader  "
			+ " where  "
			+ " zid=:zid "
			+ " and Year(xdate)=Year(:xdate) "
			+ " and Month(xdate)=Month(:xdate) "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String monthsPurchaseOrder(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " poordheader  "
			+ " where "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String yearPurchaseOrder(Integer zid, LocalDate xdate);




	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " pogrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate ", nativeQuery = true)
	public String todaysGRNAndDirectPurchase(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " pogrnheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthsGRNAndDirectPurchase(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " pogrnheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearGRNAndDirectPurchase(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " pocrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysPurchaseReturn(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " pocrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthsPurchaseReturn(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " pocrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearPurchaseReturn(Integer zid, LocalDate xdate);
}
