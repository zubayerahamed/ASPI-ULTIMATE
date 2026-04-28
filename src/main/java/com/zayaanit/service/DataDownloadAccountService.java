package com.zayaanit.service;

import java.util.List;

import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.DataDownloadAccount;
import com.zayaanit.model.DataDownloadAccountsSearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2024
 */
public interface DataDownloadAccountService {

	public List<DataDownloadAccount> AD19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, DataDownloadAccountsSearchParam param, Integer zid);

	public int AD19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, DataDownloadAccountsSearchParam param, Integer zid);
}
