package com.zayaanit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.pk.AcmstPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 27, 2024
 */
@Repository
public interface AcmstRepo extends JpaRepository<Acmst, AcmstPK> {

	@Query(value = "select max(COALESCE(xacc,0)) + 1 from acmst where zid=?1 and xgroup=?2", nativeQuery = true)
	public Integer getNextAvailableId(Integer zid, Integer xgroup);

	List<Acmst> findAllByZid(Integer zid);

	Optional<Acmst> findTopByZidOrderByXaccAsc(Integer zid);
}
