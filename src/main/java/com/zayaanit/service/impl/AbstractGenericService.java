package com.zayaanit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zayaanit.repo.XscreensRepo;
import com.zayaanit.service.KitSessionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService {

	protected static final String ERROR = "Error is {}, {}";
//	@PersistenceContext protected EntityManager em;
	@Autowired protected JdbcTemplate jdbcTemplate;
	@Autowired protected KitSessionManager sessionManager;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected Environment env;

	protected String formatValueOfDashboard(String value) {
		if (value == null || !value.contains("/"))
			return value;

		String[] parts = value.split("/");
		try {
			double num = Double.parseDouble(parts[0]);
			long rounded = Math.round(num);
			return rounded + "/" + parts[1];
		} catch (NumberFormatException e) {
			// if parsing fails, return original
			return value;
		}
	}
}
