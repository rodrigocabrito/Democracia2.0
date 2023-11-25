package pt.ul.fc.css.facade.exceptions;

public class TemaNotFoundException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -8333533569506801867L;
	
	public TemaNotFoundException (String message) {
		super(message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception
	 * @param message The error message
	 * @param e		  The wrapped exception
	 */
	public TemaNotFoundException (String message, Exception e) {
		super(message, e);
	}
}
