package com.zayaanit.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imcurstockview;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface ImcurstockviewRepo extends JpaRepository<Imcurstockview, Integer> {


	@Query(value = "SELECT "
			+ "    CASE "
			+ "        WHEN EXISTS ("
			+ "            SELECT 1 "
			+ "            FROM imcurstockview "
			+ "            WHERE zid = ?1 "
			+ "              AND xbuid = ?2 "
			+ "              AND xwh = ?3 "
			+ "              AND xitem = ?4) "
			+ "		THEN ("
			+ "            SELECT ISNULL(xqty, 0) "
			+ "             FROM imcurstockview "
			+ "             WHERE zid = ?1 "
			+ "               AND xbuid = ?2 "
			+ "               AND xwh = ?3 "
			+ "               AND xitem = ?4)"
			+ "        ELSE 0"
			+ "    END AS xqty", nativeQuery = true)
	public BigDecimal getCurrentStock(Integer zid, Integer xbuid, Integer xwh, Integer xitem);






	@Query(value = "SELECT i.xcatitem, SUM(s.xqty) AS Quantity, SUM(s.xval) AS Amount "
			+ "FROM imcurstockview s "
			+ "JOIN caitem i ON s.zid = i.zid AND s.xitem = i.xitem  "
			+ "WHERE s.zid = :zid "
			+ "AND ( (''=:xgroup AND i.xgitem BETWEEN '00' AND 'zz') OR  (''<>:xgroup AND i.xgitem=:xgroup) ) "
			+ "AND (-1=:xbuid OR s.xbuid=:xbuid) "
			+ "AND (-1=:xwh OR s.xwh=:xwh) "
			+ "GROUP BY i.xcatitem  "
			+ "ORDER BY i.xcatitem "
			+ "", nativeQuery = true)
	List<Object[]> currentStockStatusQAV(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xgroup") String xgroup, @Param("xwh") Integer xwh);
}
