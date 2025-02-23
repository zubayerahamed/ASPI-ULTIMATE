package com.zayaanit.aspi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 4, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReloadSection {

	public ReloadSection(String id, String url) {
		this.id = id;
		this.url = url;
	}

	private String id;
	private String url;
	private List<ReloadSectionParams> postData = new ArrayList<>();
}
