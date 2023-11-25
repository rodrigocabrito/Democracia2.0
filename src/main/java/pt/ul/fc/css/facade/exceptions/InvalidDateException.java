package pt.ul.fc.css.facade.exceptions;

public class InvalidDateException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = 7365481221782753875L;
	
	public InvalidDateException (String message) {
		super(message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception
	 * @param message The error message
	 * @param e		  The wrapped exception
	 */
	public InvalidDateException (String message, Exception e) {
		super(message, e);
	}
}
