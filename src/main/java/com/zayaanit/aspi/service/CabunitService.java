package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Cabunit;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface CabunitService {

	public List<Cabunit> LAD17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam);

	public int LAD17(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam);
}
