package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImtorheaderPK implements Serializable {

	private static final long serialVersionUID = -4110354391816164995L;

	private Integer zid;
	private Integer xtornum;
}
