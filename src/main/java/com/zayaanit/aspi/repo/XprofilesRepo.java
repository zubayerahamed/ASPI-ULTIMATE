package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.entity.pk.XprofilesPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XprofilesRepo extends JpaRepository<Xprofiles, XprofilesPK>{

	public List<Xprofiles> findAllByZid(Integer zid);
}
