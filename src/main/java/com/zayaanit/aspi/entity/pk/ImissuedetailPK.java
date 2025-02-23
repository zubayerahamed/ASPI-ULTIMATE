package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImissuedetailPK implements Serializable {

	private static final long serialVersionUID = -6082997097500917411L;

	private Integer zid;
	private Integer xissuenum;
	private Integer xrow;
}
