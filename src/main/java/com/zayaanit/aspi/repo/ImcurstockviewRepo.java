package com.zayaanit.aspi.repo;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Imcurstockview;

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
}
