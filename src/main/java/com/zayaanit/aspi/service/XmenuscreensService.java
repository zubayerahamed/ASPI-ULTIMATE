package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xmenuscreens;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XmenuscreensService {

	public List<Xmenuscreens> LSA13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public int LSA13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
