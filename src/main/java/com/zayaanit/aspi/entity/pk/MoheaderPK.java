package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoheaderPK implements Serializable {

	private static final long serialVersionUID = 2170844024916752971L;

	private Integer zid;
	private Integer xbatch;
}
