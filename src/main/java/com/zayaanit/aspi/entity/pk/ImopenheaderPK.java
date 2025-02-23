package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImopenheaderPK implements Serializable {

	private static final long serialVersionUID = 8357575420060006300L;

	private Integer zid;
	private Integer xopennum;
}
