package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jan 21, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdlogsPK implements Serializable {

	private static final long serialVersionUID = 8503658969848296703L;
	private Integer zid;
	private Integer xlogid;
}
