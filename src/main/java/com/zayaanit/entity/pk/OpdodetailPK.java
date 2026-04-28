package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpdodetailPK implements Serializable {

	private static final long serialVersionUID = 7224567783002084166L;

	private Integer zid;
	private Integer xdornum;
	private Integer xrow;
}
