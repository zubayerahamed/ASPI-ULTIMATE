package com.zayaanit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imtrn;
import com.zayaanit.entity.pk.ImtrnPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 20, 2025
 */
@Repository
public interface ImtrnRepo extends JpaRepository<Imtrn, ImtrnPK> {

	@Query(value = "SELECT xdoctype, SUM(xqty) AS Quantity, SUM(xqty*xrate) AS Value "
			+ "FROM imtrn where zid=:zid "
			+ "	and xdate BETWEEN :xfdate and :xtdate "
			+ "	and (-1=:xbuid OR xbuid=:xbuid) "
			+ "	and (-1=:xwh OR xwh=:xwh)	 "
			+ "	and (-1=:xitem OR xitem=:xitem)  "
			+ "	and xsign=1 "
			+ "GROUP BY xdoctype "
			+ "ORDER BY xdoctype", nativeQuery = true)
	List<Object[]> inwardPieChartWG01_WG02(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("xwh") Integer xwh,
		@Param("xfdate") String xfdate,
		@Param("xtdate") String xtdate
	);

	@Query(value = "SELECT xdoctype, SUM(xqty) AS Quantity, SUM(xqty*xrate) AS Value "
			+ "FROM imtrn where zid=:zid "
			+ "	and xdate BETWEEN :xfdate and :xtdate "
			+ "	and (-1=:xbuid OR xbuid=:xbuid) "
			+ "	and (-1=:xwh OR xwh=:xwh)	 "
			+ "	and (-1=:xitem OR xitem=:xitem)  "
			+ "	and xsign=-1 "
			+ "GROUP BY xdoctype "
			+ "ORDER BY xdoctype", nativeQuery = true)
	List<Object[]> outwardPieChartWG03_WG04(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("xwh") Integer xwh,
		@Param("xfdate") String xfdate,
		@Param("xtdate") String xtdate
	);

	@Query(value = "SELECT xdoctype,  "
			+ "    SUM(CASE WHEN xsign = 1 THEN xqty ELSE 0 END) AS Inward_Qty,  "
			+ "    SUM(CASE WHEN xsign = -1 THEN xqty ELSE 0 END) AS Outward_Qty "
			+ "FROM imtrn  "
			+ "WHERE zid=:zid "
			+ "    AND xdate BETWEEN :xfdate AND :xtdate "
			+ "    and (-1=:xbuid OR xbuid=:xbuid) "
			+ "	and (-1=:xwh OR xwh=:xwh)	 "
			+ "	and (-1=:xitem OR xitem=:xitem)  "
			+ "GROUP BY xdoctype "
			+ "ORDER BY xdoctype", nativeQuery = true)
	List<Object[]> barChartWG05(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("xwh") Integer xwh,
		@Param("xfdate") String xfdate,
		@Param("xtdate") String xtdate
	);

	@Query(value = "SELECT xdoctype,  "
			+ "    SUM(CASE WHEN xsign = 1 THEN xqty*xrate ELSE 0 END) AS Inward_Value,  "
			+ "    SUM(CASE WHEN xsign = -1 THEN xqty*xrate ELSE 0 END) AS Outward_Value "
			+ "FROM imtrn  "
			+ "WHERE zid=:zid "
			+ "    AND xdate BETWEEN :xfdate AND :xtdate "
			+ "    and (-1=:xbuid OR xbuid=:xbuid) "
			+ "	and (-1=:xwh OR xwh=:xwh)	 "
			+ "	and (-1=:xitem OR xitem=:xitem) "
			+ "GROUP BY xdoctype "
			+ "ORDER BY xdoctype", nativeQuery = true)
	List<Object[]> barChartWG06(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xitem") Integer xitem, 
		@Param("xwh") Integer xwh,
		@Param("xfdate") String xfdate,
		@Param("xtdate") String xtdate
	);

	@Query(value = "SELECT SUM(t.xqty - t.xcqtyuse) AS Quantity, SUM((t.xqty - t.xcqtyuse) * t.xrate) AS Value "
			+ "FROM imtrn t JOIN caitem c ON t.zid = c.zid AND t.xitem = c.xitem  "
			+ "WHERE t.zid=:zid AND t.xsign = 1   AND (t.xqty - t.xcqtyuse) > 0 "
			+ "  AND ((''=:xcategory AND c.xcatitem BETWEEN '00' AND 'zz') OR (''<>:xcategory AND c.xcatitem=:xcategory)) "
			+ "  AND (-1=:xbuid OR t.xbuid=:xbuid) "
			+ "  AND (-1=:xwh OR t.xwh=:xwh) "
			+ "  AND (-1=:xitem OR t.xitem=:xitem) "
			+ "  AND DATEDIFF(DAY, t.xdate, GETDATE()) BETWEEN :xfdays-1 AND :xtdays-1 ", nativeQuery = true)
	List<Object[]> inventoryAgeView(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid, 
		@Param("xwh") Integer xwh,
		@Param("xcategory") String xcategory,
		@Param("xitem") Integer xitem, 
		@Param("xfdays") Integer xfdays,
		@Param("xtdays") Integer xtdays
	);
}
