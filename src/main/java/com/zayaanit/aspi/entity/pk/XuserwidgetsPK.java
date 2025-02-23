package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Dec 30, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XuserwidgetsPK implements Serializable {

	private static final long serialVersionUID = -3310890495056846697L;

	private Integer zid;
	private String zemail;
	private String xwidget;
}
