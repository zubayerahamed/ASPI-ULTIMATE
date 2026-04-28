package com.zayaanit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResDto {
	private String xicon;
	private String xprofile;
	private String xscreen;
	private String xtitle;
	private String xkeywords;
	private String xtype;
}
