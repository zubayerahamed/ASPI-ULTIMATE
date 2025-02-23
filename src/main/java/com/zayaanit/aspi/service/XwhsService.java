package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xwhs;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XwhsService {

	public List<Xwhs> LMD11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LMD11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
