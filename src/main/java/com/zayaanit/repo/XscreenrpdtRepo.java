package com.zayaanit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xscreenrpdt;
import com.zayaanit.entity.pk.XscreenrpdtPK;

/**
 * @author Zubayer Ahaned
 * @since May 8, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XscreenrpdtRepo extends JpaRepository<Xscreenrpdt, XscreenrpdtPK> {

	List<Xscreenrpdt> findAllByZidAndXscreen(Integer zid, String xscreen);

	void deleteAllByZidAndXscreen(Integer zid, String xscreen);

	Optional<Xscreenrpdt> findByZidAndXscreenAndXseqn(Integer zid, String xscreen, Integer xseqn);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from xscreenrpdt where zid=?1 and xscreen=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, String xscreen);

	@Query(value = "select isnull(max(COALESCE(xseqn,0)) + 1, 1) from xscreenrpdt where zid=?1 and xscreen=?2", nativeQuery = true)
	public Integer getNextAvailableSequence(Integer zid, String xscreen);
}
