package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.model.MenuTree;

/**
 * @author Zubayer Ahamed
 * @since Feb 4, 2025
 */
public interface MenuTreeService {

	List<MenuTree> getMenuTree(String menucode);
}
