package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface PogrnheaderService {

	public List<Pogrnheader> LPO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LPO14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
