package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.MenuResDto;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XscreensService {

	public List<Xscreens> LSA12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam);

	public int LSA12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam);

	public List<MenuResDto> searchMenus(String hint);
}
