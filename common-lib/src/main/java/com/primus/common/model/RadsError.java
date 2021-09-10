package com.primus.common.model;

public class RadsError {
	String code ;
	String message ;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public RadsError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	

}
