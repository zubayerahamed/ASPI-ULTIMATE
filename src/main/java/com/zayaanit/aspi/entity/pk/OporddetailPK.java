package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OporddetailPK implements Serializable {

	private static final long serialVersionUID = -7062462001933569575L;

	private Integer zid;
	private Integer xordernum;
	private Integer xrow;
}
