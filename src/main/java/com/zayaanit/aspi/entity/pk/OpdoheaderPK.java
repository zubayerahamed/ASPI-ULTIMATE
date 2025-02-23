package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpdoheaderPK implements Serializable {

	private static final long serialVersionUID = -5870238080015078436L;

	private Integer zid;
	private Integer xdornum;
}
