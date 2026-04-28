package com.zayaanit.service;

import java.util.List;

import com.zayaanit.model.MenuTree;

/**
 * @author Zubayer Ahamed
 * @since Feb 4, 2025
 */
public interface MenuTreeService {

	List<MenuTree> getMenuTree(String menucode);
}
