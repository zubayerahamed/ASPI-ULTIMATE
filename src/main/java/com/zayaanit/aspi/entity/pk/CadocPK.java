package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Aug 6, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadocPK implements Serializable {

	private static final long serialVersionUID = 2404959473647050039L;

	private Integer zid;
	private Integer xdocid;
}
