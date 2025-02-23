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
public class AcdefPK implements Serializable {

	private static final long serialVersionUID = -1686762963181824160L;

	private Integer zid;
}
