package com.zayaanit.aspi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 8427072428347786341L;

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}
