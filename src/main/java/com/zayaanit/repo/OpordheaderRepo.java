package com.zayaanit.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.pk.OpordheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface OpordheaderRepo extends JpaRepository<Opordheader, OpordheaderPK> {

	@Transactional
	@Procedure(name = "SO_CreateDOfromOrder")
	public void SO_CreateDOfromOrder(@Param("zid") Integer zid, @Param("user") String user, @Param("ordernum") Integer ordernum);

	@Query(value = "select count(*) from opdoheader h where h.zid=?1 and h.xordernum=?2 and h.xstatusim='Open'", nativeQuery = true)
	public Long getOpenInvoiceCount(Integer zid, Integer xordernum); 

	@Query(value = "select count(*) from opdoheader h where h.zid=?1 and h.xordernum=?2 and h.xstatusim='Confirmed'", nativeQuery = true)
	public Long getConfirmedInvoiceCount(Integer zid, Integer xordernum); 

	@Query(value = "select "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as today "
			+ " from  "
			+ " opordheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysSalesOrder(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as month "
			+ " from  "
			+ " opordheader  "
			+ " where  "
			+ " zid=:zid "
			+ " and Year(xdate)=Year(:xdate) "
			+ " and Month(xdate)=Month(:xdate) "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String monthsSalesOrder(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " opordheader  "
			+ " where "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String yearSalesOrder(Integer zid, LocalDate xdate);




	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " opdoheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate ", nativeQuery = true)
	public String todaysSalesInvoice(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " opdoheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthsSalesInvoice(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " opdoheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearSalesInvoice(Integer zid, LocalDate xdate);



	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " opcrnheader "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysSalesReturn(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " opcrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthsSalesReturn(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " opcrnheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearSalesReturn(Integer zid, LocalDate xdate);
}
