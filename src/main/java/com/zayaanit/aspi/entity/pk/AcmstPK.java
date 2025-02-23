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
public class AcmstPK implements Serializable {

	private static final long serialVersionUID = 6848666677729035782L;

	private Integer zid;
	private Integer xacc;
}
