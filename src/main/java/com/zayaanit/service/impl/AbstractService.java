package com.zayaanit.service.impl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

	protected static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
}
