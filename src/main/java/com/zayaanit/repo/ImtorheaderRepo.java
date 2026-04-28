package com.zayaanit.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.pk.ImtorheaderPK;

import jakarta.transaction.Transactional;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface ImtorheaderRepo extends JpaRepository<Imtorheader, ImtorheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmDirectTO")
	public void IM_ConfirmDirectTO(@Param("zid") Integer zid, @Param("user") String user, @Param("tornum") Integer tornum);

	@Transactional
	@Procedure(name = "IM_ConfirmBusinessTO")
	public void IM_ConfirmBusinessTO(@Param("zid") Integer zid, @Param("user") String user, @Param("tornum") Integer tornum);

	@Query(value = "select "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as today "
			+ " from  "
			+ " imtorheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed' "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysWG01(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as month "
			+ " from  "
			+ " imtorheader  "
			+ " where  "
			+ " zid=:zid "
			+ " and Year(xdate)=Year(:xdate) "
			+ " and Month(xdate)=Month(:xdate) "
			+ " and xstatusim='Confirmed' "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String monthWG01(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " imtorheader  "
			+ " where "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatusim='Confirmed' "
			+ " and xstatus='Confirmed'", nativeQuery = true)
	public String yearWG01(Integer zid, LocalDate xdate);




	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " moheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate ", nativeQuery = true)
	public String todaysWG02(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " moheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthWG02(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " moheader  "
			+ " where  "
			+ " zid =:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearWG02(Integer zid, LocalDate xdate);







	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " imissueheader "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysWG03(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " imissueheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthWG03(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " imissueheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearWG03(Integer zid, LocalDate xdate);









	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as todays  "
			+ " from  "
			+ " imadjheader "
			+ " where  "
			+ " zid=:zid  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'  "
			+ " and xdate=:xdate", nativeQuery = true)
	public String todaysWG04(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as months  "
			+ " from  "
			+ " imadjheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and Month(xdate)=Month(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String monthWG04(Integer zid, LocalDate xdate);

	@Query(value = "select  "
			+ " CONCAT(isnull(sum(xtotamt),0), '/', COUNT(*)) as year  "
			+ " from  "
			+ " imadjheader  "
			+ " where  "
			+ " zid=:zid  "
			+ " and Year(xdate)=Year(:xdate)  "
			+ " and xstatus='Confirmed'  "
			+ " and xstatusim='Confirmed'", nativeQuery = true)
	public String yearWG04(Integer zid, LocalDate xdate);
}
