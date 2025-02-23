package com.zayaanit.aspi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zayaanit.aspi.repo.XscreensRepo;
import com.zayaanit.aspi.service.KitSessionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService {

	protected static final String ERROR = "Error is {}, {}";
	@PersistenceContext protected EntityManager em;
	@Autowired protected JdbcTemplate jdbcTemplate;
	@Autowired protected KitSessionManager sessionManager;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected Environment env;

}
