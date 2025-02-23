package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcrnheaderPK implements Serializable {

	private static final long serialVersionUID = -635795862013119360L;

	private Integer zid;
	private Integer xcrnnum;
}
