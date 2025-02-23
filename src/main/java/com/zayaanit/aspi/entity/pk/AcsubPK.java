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
public class AcsubPK implements Serializable {

	private static final long serialVersionUID = 7718714515401530583L;

	private Integer zid;
	private Integer xsub;
}
