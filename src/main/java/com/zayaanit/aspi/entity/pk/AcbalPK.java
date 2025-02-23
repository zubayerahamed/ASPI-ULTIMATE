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
@NoArgsConstructor
@AllArgsConstructor
public class AcbalPK implements Serializable {

	private static final long serialVersionUID = -1038237708258853278L;

	private String zid;
	private Integer xvoucher;
	private Integer xrow;
}
