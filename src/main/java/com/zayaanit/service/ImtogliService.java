package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imtogli;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jan 8, 2025
 */
public interface ImtogliService {

	public List<Imtogli> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);
}
