package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xprofilesdt;
import com.zayaanit.aspi.entity.pk.XprofilesdtPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XprofilesdtRepo extends JpaRepository<Xprofilesdt, XprofilesdtPK> {

	public List<Xprofilesdt> findAllByXprofileAndZid(String xprofile, Integer zid);

	public List<Xprofilesdt> findAllByXprofileAndXscreenAndZid(String xprofile, String xscreen, Integer zid);

	public List<Xprofilesdt> findAllByZidAndXprofileAndXmenuIn(Integer zid, String xprofile, List<String> xmenu);

	public List<Xprofilesdt> findAllByXprofileAndXmenuAndZid(String xmenu, String xprofile, Integer zid);

	public void deleteAllByZidAndXmenu(Integer zid, String xmenu);

	public void deleteAllByZidAndXscreen(Integer zid, String xscreen);
}
