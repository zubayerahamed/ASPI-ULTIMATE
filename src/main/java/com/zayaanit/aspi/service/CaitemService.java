package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Caitem;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface CaitemService {

	public List<Caitem> LMD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LMD12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
