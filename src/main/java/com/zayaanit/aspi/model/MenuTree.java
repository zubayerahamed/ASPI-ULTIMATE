package com.zayaanit.aspi.model;

import java.util.ArrayList;
import java.util.List;

import com.zayaanit.aspi.entity.Xscreens;

import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Oct 9, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
public class MenuTree {

	private String menuCode;
	private String menuTitle;
	private String menuIcon;
	private String parentCode;

	private List<MenuTree> subMenus = new ArrayList<>();
	private List<Xscreens> screens = new ArrayList<>();
}
