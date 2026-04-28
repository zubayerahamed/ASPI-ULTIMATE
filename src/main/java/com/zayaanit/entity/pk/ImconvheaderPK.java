package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Nov 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImconvheaderPK implements Serializable {

	private static final long serialVersionUID = -6487477645200171137L;

	private Integer zid;
	private Integer xconvnum;
}
