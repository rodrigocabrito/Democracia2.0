package pt.ul.fc.css.facade.exceptions;

public class VotacaoNotFoundException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -2607200440768337258L;
	
	public VotacaoNotFoundException (String message) {
		super(message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception
	 * @param message The error message
	 * @param e		  The wrapped exception
	 */
	public VotacaoNotFoundException (String message, Exception e) {
		super(message, e);
	}
}
