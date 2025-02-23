package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImissueheaderPK implements Serializable {

	private static final long serialVersionUID = -4018860529889781551L;

	private Integer zid;
	private Integer xissuenum;
}
