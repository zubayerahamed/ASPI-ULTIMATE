package com.zayaanit.aspi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.aspi.entity.Xuserprofiles;
import com.zayaanit.aspi.entity.pk.XuserprofilesPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 25, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XuserprofilesRepo extends JpaRepository<Xuserprofiles, XuserprofilesPK> {

	List<Xuserprofiles> findAllByZidAndZemail(Integer zid, String zemail);
}
