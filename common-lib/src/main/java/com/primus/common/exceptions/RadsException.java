package com.primus.common.exceptions;

public class RadsException extends Exception {

	protected String message ;
	
	public RadsException() {
		super ("Error in Rads Framework");
	}
	
	public RadsException (String str) {
		super ("Error in Rads Frame work :" + str);
	}
	
	
	public RadsException(Exception ex) {
		super (ex);
	}
}
