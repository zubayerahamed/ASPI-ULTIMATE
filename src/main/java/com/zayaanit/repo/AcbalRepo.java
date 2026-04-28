package com.zayaanit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acbal;
import com.zayaanit.entity.pk.AcbalPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcbalRepo extends JpaRepository<Acbal, AcbalPK> {

	@Query(value = "select distinct xyear from acbal where zid=?1 order by xyear desc", nativeQuery = true)
	public List<Integer> getDistinctYears(Integer zid);

	@Transactional
	@Procedure(name = "FA_YearEnd")
	void FA_YearEnd(@Param("zid") Integer zid, @Param("user") String user, @Param("year") Integer year, @Param("date") Date date);

	@Query(value = "WITH DateSeries AS ( " +
						"SELECT CAST(GETDATE() AS DATE) AS xdate " +
						"UNION ALL " +
						"SELECT DATEADD(DAY, -1, xdate) " +
						"FROM DateSeries " +
						"WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) " +
					") " +
					"SELECT d.xdate AS xdate, COALESCE(SUM(a.xprime), 0) AS amount " +
					"FROM DateSeries d " +
					"LEFT JOIN acbal a " +
						"ON d.xdate = a.xdate " +
						"AND a.zid = :zid " +
						"AND a.xacc = :xacc " +
						"AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() " +
						"AND (-1=:xbuid OR a.xbuid=:xbuid) " + 
					"GROUP BY d.xdate " +
					"ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getAccountTransactionSummaryForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xacc") Integer xacc, @Param("days") Integer days);

	@Query(value = "DECLARE @year int, @per int "
			+ "EXEC [dbo].[FA_GetYearPeriod] :zid, :xdate, @year OUTPUT, @per OUTPUT; "
			+ "WITH YearPer AS ( "
			+ "    SELECT @year AS xyear, @per AS xper, 1 AS xrow "
			+ "    UNION ALL "
			+ "    SELECT "
			+ "        CASE  "
			+ "            WHEN xper = 1 THEN xyear - 1  "
			+ "            ELSE xyear  "
			+ "        END, "
			+ "        CASE  "
			+ "            WHEN xper = 1 THEN 12  "
			+ "            ELSE xper - 1  "
			+ "        END, "
			+ "        xrow + 1 "
			+ "    FROM YearPer "
			+ "    WHERE xrow < :months "
			+ "), "
			+ "TrnData AS ( "
			+ "    SELECT  "
			+ "        xyear,  "
			+ "        xper,   "
			+ "        ISNULL(SUM(xprime), 0) AS xprime "
			+ "    FROM acbal "
			+ "    WHERE zid=:zid "
			+ "			AND xacc = :xacc "
			+ "			AND (-1=:xbuid OR xbuid = :xbuid) "
			+ "    GROUP BY xyear, xper "
			+ ") "
			+ "SELECT  "
			+ "    dr.xyear,  "
			+ "    dr.xper,  "
			+ "    ISNULL(d.xprime, 0) AS xprime "
			+ "FROM YearPer dr "
			+ "LEFT JOIN TrnData d  "
			+ "    ON dr.xyear = d.xyear AND dr.xper = d.xper "
			+ "ORDER BY dr.xyear ASC, dr.xper ASC", nativeQuery = true)
	List<Object[]> getAccountTransactionSummaryForMonths(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xacc") Integer xacc, @Param("months") Integer months, @Param("xdate") String xdate);

	@Query(value = "WITH DateSeries AS ( " +
			"SELECT CAST(GETDATE() AS DATE) AS xdate " +
			"UNION ALL " +
			"SELECT DATEADD(DAY, -1, xdate) " +
			"FROM DateSeries " +
			"WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) " +
		") " +
		"SELECT d.xdate AS xdate, COALESCE(SUM(a.xprime), 0) AS amount " +
		"FROM DateSeries d " +
		"LEFT JOIN acbal a " +
			"ON d.xdate = a.xdate " +
			"AND a.zid = :zid " +
			"AND a.xsub = :xsub " +
			"AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() " +
			"AND (-1=:xbuid OR a.xbuid=:xbuid) " + 
		"GROUP BY d.xdate " +
		"ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getSubAccountTransactionSummaryForDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xsub") Integer xsub, @Param("days") Integer days);

	@Query(value = "DECLARE @year int, @per int "
		+ "EXEC [dbo].[FA_GetYearPeriod] :zid, :xdate, @year OUTPUT, @per OUTPUT; "
		+ "WITH YearPer AS ( "
		+ "    SELECT @year AS xyear, @per AS xper, 1 AS xrow "
		+ "    UNION ALL "
		+ "    SELECT "
		+ "        CASE  "
		+ "            WHEN xper = 1 THEN xyear - 1  "
		+ "            ELSE xyear  "
		+ "        END, "
		+ "        CASE  "
		+ "            WHEN xper = 1 THEN 12  "
		+ "            ELSE xper - 1  "
		+ "        END, "
		+ "        xrow + 1 "
		+ "    FROM YearPer "
		+ "    WHERE xrow < :months "
		+ "), "
		+ "TrnData AS ( "
		+ "    SELECT  "
		+ "        xyear,  "
		+ "        xper,   "
		+ "        ISNULL(SUM(xprime), 0) AS xprime "
		+ "    FROM acbal "
		+ "    WHERE zid=:zid "
		+ "			AND xsub = :xsub "
		+ "			AND (-1=:xbuid OR xbuid = :xbuid) "
		+ "    GROUP BY xyear, xper "
		+ ") "
		+ "SELECT  "
		+ "    dr.xyear,  "
		+ "    dr.xper,  "
		+ "    ISNULL(d.xprime, 0) AS xprime "
		+ "FROM YearPer dr "
		+ "LEFT JOIN TrnData d  "
		+ "    ON dr.xyear = d.xyear AND dr.xper = d.xper "
		+ "ORDER BY dr.xyear ASC, dr.xper ASC", nativeQuery = true)
	List<Object[]> getSubAccountTransactionSummaryForMonths(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("xsub") Integer xsub, @Param("months") Integer months, @Param("xdate") String xdate);

	@Query(value = "select "
			+ "	CAST(a.xacc AS VARCHAR(20)) + ' - ' + a.xdesc AS accountName, "
			+ "	sum(b.xprime) as amount "
			+ "from  "
			+ "	acmst a  "
			+ "	join acbal b on a.zid=b.zid and a.xacc=b.xacc  "
			+ "where  "
			+ "	a.zid=:zid "
			+ "	and b.xyear=:xyear "
			+ "	and a.xacctype=:xacctype "
			+ "	and (-1=:xbuid OR b.xbuid=:xbuid) "
			+ "group by a.xacc, a.xdesc "
			+ "order by a.xacc", nativeQuery = true)
	List<Object[]> getAccountsCurrentBalance(
		@Param("zid") Integer zid, 
		@Param("xbuid") Integer xbuid,
		@Param("xyear") Integer xyear, 
		@Param("xacctype") String xacctype
	);

	@Query(value = "WITH DateSeries AS ( "
			+ "    SELECT CAST(GETDATE() AS DATE) AS xdate "
			+ "    UNION ALL "
			+ "    SELECT DATEADD(DAY, -1, xdate) "
			+ "    FROM DateSeries "
			+ "    WHERE xdate > DATEADD(DAY, -:days+1, GETDATE()) "
			+ ") "
			+ "SELECT d.xdate, COALESCE(SUM(CASE WHEN m.xacctype IN ('Income', 'Expenditure') THEN a.xprime ELSE 0 END), 0) AS Amount "
			+ "FROM DateSeries d "
			+ "LEFT JOIN acbal a "
			+ "    ON d.xdate = a.xdate "
			+ "    AND a.zid = :zid "
			+ "    AND a.xdate BETWEEN DATEADD(DAY, -:days+1, GETDATE()) AND GETDATE() "
			+ "	AND (-1=:xbuid OR a.xbuid=:xbuid) "
			+ "Left Join acmst m "
			+ "	on a.zid=m.zid and a.xacc=m.xacc "
			+ "	and m.xacctype in ('Income', 'Expenditure') "
			+ "GROUP BY d.xdate "
			+ "ORDER BY d.xdate ", nativeQuery = true)
	List<Object[]> profitAndLossViewDays(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("days") Integer days);

	@Query(value = "DECLARE @year int, @month int "
		+ "EXEC [dbo].[FA_GetYearPeriod] :zid, :xdate, @year OUTPUT, @month OUTPUT; "
		+ "WITH YearPer AS ( "
		+ "    SELECT @Year AS xyear, @Month AS xper, 1 AS xrow "
		+ "    UNION ALL "
		+ "    SELECT "
		+ "        CASE  "
		+ "            WHEN xper = 1 THEN xyear - 1  "
		+ "            ELSE xyear  "
		+ "        END, "
		+ "        CASE  "
		+ "            WHEN xper = 1 THEN 12  "
		+ "            ELSE xper - 1  "
		+ "        END, "
		+ "        xrow + 1 "
		+ "    FROM YearPer "
		+ "    WHERE xrow < :months "
		+ "), "
		+ "TrnData AS ( "
		+ "    SELECT  "
		+ "        xyear,  "
		+ "        xper,   "
		+ "        ISNULL(SUM(xprime), 0) AS xprime "
		+ "    FROM acbal a "
		+ "	Join acmst m on a.zid=m.zid and a.xacc=m.xacc "
		+ "    WHERE a.zid=:zid  and m.xacctype in ('Income', 'Expenditure') "
		+ "	AND (-1=:xbuid OR a.xbuid=:xbuid) "
		+ "    GROUP BY xyear, xper) "
		+ "SELECT  "
		+ "    dr.xyear,  "
		+ "    dr.xper,  "
		+ "    ISNULL(d.xprime, 0) AS xprime "
		+ "FROM YearPer dr "
		+ "LEFT JOIN TrnData d  "
		+ "    ON dr.xyear = d.xyear AND dr.xper = d.xper "
		+ "ORDER BY dr.xyear asc, dr.xper asc ", nativeQuery = true)
	List<Object[]> profitAndLossViewMonths(@Param("zid") Integer zid, @Param("xbuid") Integer xbuid, @Param("months") Integer months, @Param("xdate") String xdate);
}
