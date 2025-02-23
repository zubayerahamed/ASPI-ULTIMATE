package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PocrndetailPK implements Serializable {

	private static final long serialVersionUID = -1909404950188278940L;

	private Integer zid;
	private Integer xcrnnum;
	private Integer xrow;
}
