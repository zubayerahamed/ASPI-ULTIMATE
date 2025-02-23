package com.zayaanit.aspi.converter;

import com.zayaanit.aspi.util.CKTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

/**
 * @author Zubayer Ahamed
 * @since Apr 24, 2024
 */
@Convert
public class CKTimeConverter implements AttributeConverter<CKTime, String> {

	@Override
	public String convertToDatabaseColumn(CKTime time) {
		return time == null ? null : time.getT5Time();
	}

	@Override
	public CKTime convertToEntityAttribute(String data) {
		return data == null || data.isEmpty() ? null : new CKTime(data);
	}
}
