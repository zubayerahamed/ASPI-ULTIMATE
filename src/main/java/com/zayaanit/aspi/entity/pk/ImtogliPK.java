package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImtogliPK implements Serializable {

	private static final long serialVersionUID = 8745435794727102816L;

	private Integer zid;
	private String xtype;
	private String xgitem;

}
