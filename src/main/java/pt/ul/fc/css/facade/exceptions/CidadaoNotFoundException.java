package pt.ul.fc.css.facade.exceptions;

public class CidadaoNotFoundException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = 7888547528037690821L;
	
	public CidadaoNotFoundException (String message) {
		super(message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception
	 * @param message The error message
	 * @param e		  The wrapped exception
	 */
	public CidadaoNotFoundException (String message, Exception e) {
		super(message, e);
	}
}
