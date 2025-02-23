package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Dec 31, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaitemPK implements Serializable {

	private static final long serialVersionUID = 3201189268203895715L;
	private Integer zid;
	private Integer xitem;
}
