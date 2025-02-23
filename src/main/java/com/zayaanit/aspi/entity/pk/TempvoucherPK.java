package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Feb 7, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempvoucherPK implements Serializable {

	private static final long serialVersionUID = 4263231722036576863L;

	private Integer zid;
	private Integer xrow;
}
