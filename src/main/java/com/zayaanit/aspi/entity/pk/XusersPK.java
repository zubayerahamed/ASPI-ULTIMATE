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
public class XusersPK implements Serializable {

	private static final long serialVersionUID = -9024206548799168071L;

	private Integer zid;
	private String zemail;
}
