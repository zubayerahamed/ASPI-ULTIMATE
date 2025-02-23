package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoorddetailPK implements Serializable {

	private static final long serialVersionUID = -3133835482175033233L;

	private Integer zid;
	private Integer xpornum;
	private Integer xrow;
}
