package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotogliPK implements Serializable {

	private static final long serialVersionUID = -3273524600628592834L;

	private Integer zid;
	private String xtype;
	private String xgsup;
	private String xgitem;
}
