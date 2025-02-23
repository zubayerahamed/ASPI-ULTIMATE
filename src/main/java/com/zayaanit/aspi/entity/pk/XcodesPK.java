package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed Jul 2, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XcodesPK implements Serializable {

	private static final long serialVersionUID = -881571653699695887L;

	private Integer zid;
	private String xtype;
	private String xcode;
}
