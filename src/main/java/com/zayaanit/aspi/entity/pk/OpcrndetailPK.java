package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcrndetailPK implements Serializable {

	private static final long serialVersionUID = -3303143085088591777L;

	private Integer zid;
	private Integer xcrnnum;
	private Integer xrow;
}
