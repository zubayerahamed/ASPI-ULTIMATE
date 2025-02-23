package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xprofiles;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XprofilesService {

	public List<Xprofiles> LAD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LAD12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
