package com.zayaanit.aspi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ibm.icu.text.SimpleDateFormat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public abstract class AbstractService {

	@PersistenceContext protected EntityManager em;
	@Autowired protected JdbcTemplate jdbcTemplate;

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
}
