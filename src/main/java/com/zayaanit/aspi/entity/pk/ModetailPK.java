package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModetailPK implements Serializable {

	private static final long serialVersionUID = -6824595076160671087L;

	private Integer zid;
	private Integer xbatch;
	private Integer xrow;
}
