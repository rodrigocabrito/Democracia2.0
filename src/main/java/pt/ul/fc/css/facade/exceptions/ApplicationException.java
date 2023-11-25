package pt.ul.fc.css.facade.exceptions;

/**
 * The top level application exception.
 * In this simple example this is the only exception we use.
 * In a more involving example, there should be subclasses of this class
 * per exception, making it possible for clients of this layer (for instance, a
 * UI layer) to treat the errors.
 * Note that low level exceptions (like RecordNotFoundException) are wrapped in
 * this
 * exception.
 * 
 * @author fmartins
 * @version 1.1 (5/10/2014)
 *
 */
public class ApplicationException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e       The wrapped exception.
	 */
	public ApplicationException(String message, Exception e) {
		super(message, e);
	}

}
