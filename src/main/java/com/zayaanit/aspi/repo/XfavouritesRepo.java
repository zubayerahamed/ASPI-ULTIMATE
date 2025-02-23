package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xfavourites;
import com.zayaanit.aspi.entity.pk.XfavouritesPK;

/**
 * @author Zubayer Ahamed
 * @since Oct 16, 2024
 */
@Repository
public interface XfavouritesRepo extends JpaRepository<Xfavourites, XfavouritesPK> {

	@Query(value = "select isnull(max(COALESCE(xsequence,0)) + 1, 1) from xfavourites where zid=?1", nativeQuery = true)
	public Integer getNextAvailableSequence(Integer zid);

	List<Xfavourites> findAllByZidAndZemailAndXprofile(Integer zid, String xemail, String xprofile);

	List<Xfavourites> findAllByZidAndZemailAndXprofileAndXisdefault(Integer zid, String xemail, String xprofile, Boolean xisdefault);
}
