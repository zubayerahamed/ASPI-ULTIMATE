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
public class ImconvdetailPK implements Serializable {

	private static final long serialVersionUID = -8348832670294437639L;

	private Integer zid;
	private Integer xconvnum;
	private Integer xrow;
}
