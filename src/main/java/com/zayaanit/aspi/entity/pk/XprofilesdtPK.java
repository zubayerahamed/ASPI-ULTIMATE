package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed Jul 2, 2023
 */
@Data
public class XprofilesdtPK implements Serializable {

	private static final long serialVersionUID = -1542794175138185880L;

	private Integer zid;
	private String xprofile;
	private String xmenu;
	private String xscreen;
}
