package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoordheaderPK implements Serializable {

	private static final long serialVersionUID = -7750714595004628092L;

	private Integer zid;
	private Integer xpornum;
}
