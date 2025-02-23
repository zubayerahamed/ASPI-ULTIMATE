package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImadjheaderPK implements Serializable {

	private static final long serialVersionUID = -4162001801485204580L;

	private Integer zid;
	private Integer xadjnum;
}
