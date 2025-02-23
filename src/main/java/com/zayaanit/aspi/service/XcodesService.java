package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public interface XcodesService {

	public List<Xcodes> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);
}
