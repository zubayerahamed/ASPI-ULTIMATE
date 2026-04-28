package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.entity.pk.PogrnheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PogrnheaderRepo extends JpaRepository<Pogrnheader, PogrnheaderPK> {

	@Transactional
	@Procedure(name = "PO_ConfirmGRN")
	public void PO_ConfirmGRN(@Param("zid") Integer zid, @Param("user") String user, @Param("grnnum") Integer grnnum);

	public List<Pogrnheader> findAllByZidAndXpornum(Integer zid, Integer xpornum);

	@Query(value = "select count(*) from pogrnheader h join pogrndetail d on h.zid=d.zid and h.xgrnnum=d.xgrnnum where h.zid=?1 and h.xgrnnum=?2 and h.xstatus='Confirmed' and h.xstatusim='Confirmed' and (d.xqty-d.xqtycrn)>0", nativeQuery = true)
	public Long isInvalidGRN(Integer zid, Integer xgrnnum);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(:xfdate AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, 1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate < :xtdate "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(a.xtotamt), 0) AS amount "
			+ "FROM DateSeries d "
			+ "LEFT JOIN pogrnheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN :xfdate and :xtdate "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' and (-1 = :xcus OR a.xcus =:xcus)  "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getAccountTransactionSummaryForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xcus") Integer xcus, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(GETDATE() AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, -1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(a.xtotamt), 0) AS amount "
			+ "FROM DateSeries d "
			+ "LEFT JOIN pogrnheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' and (-1=:xcus OR a.xcus = :xcus)  "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getAccountTransactionSummaryForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xcus") Integer xcus, @Param("days") Integer days);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(:xfdate AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, 1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate < :xtdate "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(b.xqty), 0) AS quantity "
			+ "FROM DateSeries d "
			+ "LEFT JOIN pogrnheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN :xfdate and :xtdate "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ " Left Join pogrndetail b on a.zid=b.zid and a.xgrnnum=b.xgrnnum and (-1=:xitem OR b.xitem=:xitem) "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getItemPurchaseStatementForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xitem") Integer xitem, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(GETDATE() AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, -1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(b.xqty), 0) AS quantity "
			+ "FROM DateSeries d "
			+ "LEFT JOIN pogrnheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ " Left Join pogrndetail b on a.zid=b.zid and a.xgrnnum=b.xgrnnum and (-1=:xitem OR b.xitem=:xitem) "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getItemPurchaseStatementForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xitem") Integer xitem, @Param("days") Integer days);

	@Query(value = "select top (:#{#topxcus}) c.xname Supplier, sum(a.xtotamt) Amount  "
			+ "from pogrnheader a  "
			+ "join acsub c on a.zid=c.zid and a.xcus=c.xsub where a.zid=:zid "
			+ "and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ "and a.xdate between :xfdate and :xtdate "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by c.xname order by sum(a.xtotamt) desc", nativeQuery = true)
	List<Object[]> getTopSupplierTransactionForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxcus") Integer topxcus, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "select top (:#{#topxcus}) c.xname supplier, sum(a.xtotamt) amount  "
			+ "from pogrnheader a  "
			+ "join acsub c on a.zid=c.zid and a.xcus=c.xsub where a.zid=:zid "
			+ "and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ "AND a.xdate >= DATEADD(DAY, -:days, GETDATE()) "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by c.xname order by sum(a.xtotamt) desc", nativeQuery = true)
	List<Object[]> getTopSupplierTransactionForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxcus") Integer topxcus, @Param("days") Integer days);




	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xlineamt) Amount from pogrnheader a  "
			+ "join pogrndetail b on a.zid=b.zid and a.xgrnnum=b.xgrnnum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ "and a.xdate between :xfdate and :xtdate  "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by i.xdesc order by sum(b.xlineamt) desc ", nativeQuery = true)
	List<Object[]> getTopItemPurchaseForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xlineamt) Amount from pogrnheader a  "
			+ "join pogrndetail b on a.zid=b.zid and a.xgrnnum=b.xgrnnum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ "AND a.xdate >= DATEADD(DAY, -:days, GETDATE()) "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid)  "
			+ "group by i.xdesc order by sum(b.xlineamt) desc ", nativeQuery = true)
	List<Object[]> getTopItemPurchaseForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("days") Integer days);
}
