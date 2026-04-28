package com.zayaanit.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface ZbusinessRepo extends JpaRepository<Zbusiness, Integer>{

	public Optional<Zbusiness> findByZidAndZactive(Integer zid, Boolean zactive);
}
