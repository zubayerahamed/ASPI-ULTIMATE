package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XwhsPK implements Serializable {

	private static final long serialVersionUID = -6311414924855886724L;
	private Integer zid;
	private Integer xwh;
}
