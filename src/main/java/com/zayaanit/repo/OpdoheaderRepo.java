package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.pk.OpdoheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface OpdoheaderRepo extends JpaRepository<Opdoheader, OpdoheaderPK> {

	@Transactional
	@Procedure(name = "SO_ConfirmInvoice")
	public void SO_ConfirmInvoice(@Param("zid") Integer zid, @Param("user") String user, @Param("dornum") Integer dornum);

	public List<Opdoheader> findAllByZidAndXdornum(Integer zid, Integer xdornum);

	public List<Opdoheader> findAllByZidAndXordernum(Integer zid, Integer xordernum);

	@Query(value = "select count(*) from opdoheader h join opdodetail d on h.zid=d.zid and h.xdornum=d.xdornum where h.zid=?1 and h.xdornum=?2 and h.xstatus='Confirmed' and h.xstatusim='Confirmed' and (d.xqty-d.xqtycrn)>0", nativeQuery = true)
	public Long isInvalidSalesNumber(Integer zid, Integer xdornum);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(:xfdate AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, 1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate < :xtdate "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(a.xtotamt), 0) AS amount "
			+ "FROM DateSeries d "
			+ "LEFT JOIN opdoheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN :xfdate and :xtdate "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' and (-1 = :xcus OR a.xcus =:xcus)  "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getCustomerSalesStatementForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xcus") Integer xcus, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(GETDATE() AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, -1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(a.xtotamt), 0) AS amount "
			+ "FROM DateSeries d "
			+ "LEFT JOIN opdoheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' and (-1=:xcus OR a.xcus = :xcus)  "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getCustomerSalesStatementForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xcus") Integer xcus, @Param("days") Integer days);





	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(:xfdate AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, 1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate < :xtdate "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(b.xqty), 0) AS quantity "
			+ "FROM DateSeries d "
			+ "LEFT JOIN opdoheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN :xfdate and :xtdate "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ " Left Join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum and (-1=:xitem OR b.xitem=:xitem) "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getItemSalesStatementForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xitem") Integer xitem, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(GETDATE() AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, -1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(b.xqty), 0) AS quantity "
			+ "FROM DateSeries d "
			+ "LEFT JOIN opdoheader a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "     AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() "
			+ "	AND (-1=:xbuid OR a.xbuid = :xbuid) "
			+ "	 and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ " Left Join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum and (-1=:xitem OR b.xitem=:xitem) "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getItemSalesStatementForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xitem") Integer xitem, @Param("days") Integer days);








	@Query(value = "select top (:#{#topxcus}) c.xname Customer, sum(a.xtotamt) Amount  "
			+ "from opdoheader a  "
			+ "join acsub c on a.zid=c.zid and a.xcus=c.xsub where a.zid=:zid "
			+ "and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ "and a.xdate between :xfdate and :xtdate "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by c.xname order by sum(a.xtotamt) desc", nativeQuery = true)
	List<Object[]> getTopCustomerTransactionForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxcus") Integer topxcus, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "select top (:#{#topxcus}) c.xname Customer, sum(a.xtotamt) amount  "
			+ "from opdoheader a  "
			+ "join acsub c on a.zid=c.zid and a.xcus=c.xsub where a.zid=:zid "
			+ "and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ "AND a.xdate >= DATEADD(DAY, -:days, GETDATE()) "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by c.xname order by sum(a.xtotamt) desc", nativeQuery = true)
	List<Object[]> getTopCustomerTransactionForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxcus") Integer topxcus, @Param("days") Integer days);







	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xlineamt) Amount from opdoheader a  "
			+ "join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ "and a.xdate between :xfdate and :xtdate  "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by i.xdesc order by sum(b.xlineamt) desc ", nativeQuery = true)
	List<Object[]> getTopItemSalesForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xlineamt) Amount from opdoheader a  "
			+ "join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ "AND a.xdate >= DATEADD(DAY, -:days, GETDATE()) "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid)  "
			+ "group by i.xdesc order by sum(b.xlineamt) desc ", nativeQuery = true)
	List<Object[]> getTopItemSalesForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("days") Integer days);







	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xqty*b.xrategrn) Profit from opdoheader a  "
			+ "join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed'  "
			+ "and a.xdate between :xfdate and :xtdate  "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "group by i.xdesc order by sum(b.xqty*b.xrategrn) desc ", nativeQuery = true)
	List<Object[]> getTopProfitableitemsForDateBetween(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("xfdate") String xfdate, @Param("xtdate") String xtdate);

	@Query(value = "select top (:#{#topxitem}) i.xdesc Item, sum(b.xqty) Quantity, sum(b.xqty*b.xrategrn) Profit from opdoheader a  "
			+ "join opdodetail b on a.zid=b.zid and a.xdornum=b.xdornum  "
			+ "join caitem i on b.zid=i.zid and b.xitem=i.xitem  "
			+ "where a.zid=:zid and a.xstatus='Confirmed' and a.xstatusim='Confirmed' "
			+ "AND a.xdate >= DATEADD(DAY, -:days, GETDATE()) "
			+ "and (-1=:xbuid OR a.xbuid=:xbuid)  "
			+ "group by i.xdesc order by sum(b.xqty*b.xrategrn) desc ", nativeQuery = true)
	List<Object[]> getTopProfitableitemsForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("topxitem") Integer topxitem, @Param("days") Integer days);
}
