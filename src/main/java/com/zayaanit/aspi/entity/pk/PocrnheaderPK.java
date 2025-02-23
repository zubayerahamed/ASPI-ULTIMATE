package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PocrnheaderPK implements Serializable {

	private static final long serialVersionUID = -2148304676841664565L;

	private Integer zid;
	private Integer xcrnnum;
}
