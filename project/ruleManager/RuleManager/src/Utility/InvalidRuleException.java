package Utility;

public class InvalidRuleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String exception;
	
	public String getExceptionMsg()
	{
		return exception;
	}
	
	public InvalidRuleException (String exp)
	{
		this.exception = new String (exp);
	}
}
