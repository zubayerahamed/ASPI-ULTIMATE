package com.zayaanit.aspi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@NoArgsConstructor
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 8427072428347786341L;

	public UnauthorizedException(String msg) {
		super(msg);
	}
}