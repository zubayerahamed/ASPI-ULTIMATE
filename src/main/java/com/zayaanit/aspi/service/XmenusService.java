package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xmenus;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XmenusService {

	public List<Xmenus> LSA11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LSA11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
