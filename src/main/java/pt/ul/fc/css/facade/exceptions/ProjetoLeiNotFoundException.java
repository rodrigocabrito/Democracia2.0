package pt.ul.fc.css.facade.exceptions;

public class ProjetoLeiNotFoundException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = 2254114874713257473L;

	public ProjetoLeiNotFoundException (String message) {
		super(message);
	}
	
	/**
	 * Creates an exception wrapping a lower level exception
	 * @param message The error message
	 * @param e		  The wrapped exception
	 */
	public ProjetoLeiNotFoundException (String message, Exception e) {
		super(message, e);
	}
}
