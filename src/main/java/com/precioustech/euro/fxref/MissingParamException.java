package com.precioustech.euro.fxref;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingParamException extends RuntimeException{

	private static final long serialVersionUID = 130651939152208128L;
	
	public MissingParamException(String msg) {
		super(msg);
	}
}
