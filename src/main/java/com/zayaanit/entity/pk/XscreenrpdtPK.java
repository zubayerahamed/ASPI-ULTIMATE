package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since May 8, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XscreenrpdtPK implements Serializable {

	private static final long serialVersionUID = -519996461723964270L;

	private Integer zid;
	private String xscreen;
	private Integer xrow;
}
