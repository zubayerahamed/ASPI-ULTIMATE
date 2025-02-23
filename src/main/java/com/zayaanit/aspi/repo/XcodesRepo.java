package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.entity.pk.XcodesPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XcodesRepo extends JpaRepository<Xcodes, XcodesPK> {

	public List<Xcodes> findAllByXtypeAndZid(String xtype, Integer zid);
	
	public List<Xcodes> findAllByXtypeAndZactiveAndZid(String xtype, Boolean zactive, Integer zid);
}
