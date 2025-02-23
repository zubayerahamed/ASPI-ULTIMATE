package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xusers;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public interface XusersService {

	public List<Xusers> LAD13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LAD13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
