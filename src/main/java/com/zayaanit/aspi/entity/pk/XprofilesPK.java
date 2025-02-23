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
public class XprofilesPK implements Serializable {

	private static final long serialVersionUID = -8098062408075876871L;

	private Integer zid;
	private String xprofile;
}
