package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcgroupPK  implements Serializable{

	private static final long serialVersionUID = 8259038127289984234L;

	private Integer zid;
	private Integer xagcode;
}
