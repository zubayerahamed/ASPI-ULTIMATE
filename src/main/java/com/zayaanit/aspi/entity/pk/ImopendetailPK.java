package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImopendetailPK implements Serializable {

	private static final long serialVersionUID = 6864446550002571842L;

	private Integer zid;
	private Integer xopennum;
	private Integer xrow;
}
