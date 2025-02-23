package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PogrndetailPK implements Serializable {

	private static final long serialVersionUID = -8745647912511165222L;

	private Integer zid;
	private Integer xgrnnum;
	private Integer xrow;
}
