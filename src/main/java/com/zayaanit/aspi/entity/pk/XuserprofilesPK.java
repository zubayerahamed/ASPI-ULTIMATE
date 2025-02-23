package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XuserprofilesPK implements Serializable {

	private static final long serialVersionUID = -9093833348897975617L;

	private Integer zid;
	private String zemail;
	private String xprofile;
}
