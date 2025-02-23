package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Pocrnheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface PocrnheaderService {

	public List<Pocrnheader> LPO16(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LPO16(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
