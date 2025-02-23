package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImtordetailPK implements Serializable {

	private static final long serialVersionUID = 7156793082845726014L;

	private Integer zid;
	private Integer xtornum;
	private Integer xrow;
}
