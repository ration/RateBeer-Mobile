package dk.moerks.ratebeermobile.exceptions;

import android.util.Log;

public class RBException extends Exception {
	private static final long serialVersionUID = -4771460057683617236L;

	private String message = null;
	private String parentClass = null;
	private Exception originalException = null;
	
	public RBException(){
		this.message = "An unexpected exception occured!";
		this.parentClass = "RBException";
		logException();
	}
	
	public RBException(String parentClass, String message, Exception exception){
		this.message = message;
		this.parentClass = parentClass;
		this.originalException = exception;
		logException();
	}
	
	private void logException(){
		//Log Error Message
		Log.e(parentClass, message);
		
		//Log StackTrace if available
		if(originalException != null && originalException.getMessage() != null){
			Log.d(parentClass, originalException.getMessage());
		} else {
			Log.d(parentClass, "Originating Exception unknown!");
		}
	}
	
	public String getAlertMessage(){
		return message;
	}
}
