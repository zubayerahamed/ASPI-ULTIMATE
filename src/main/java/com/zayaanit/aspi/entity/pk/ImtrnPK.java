package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImtrnPK implements Serializable {

	private static final long serialVersionUID = 8306741596720442740L;

	private Integer zid;
	private Integer ximtrnnum;
}
