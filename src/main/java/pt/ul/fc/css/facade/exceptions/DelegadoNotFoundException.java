package pt.ul.fc.css.facade.exceptions;

public class DelegadoNotFoundException extends Exception {

    /**
     * The serial version id (generated automatically by Eclipse)
     */
    private static final long serialVersionUID = 5165545749165789195L;

    public DelegadoNotFoundException (String message) {
        super(message);
    }

    /**
     * Creates an exception wrapping a lower level exception
     * @param message The error message
     * @param e		  The wrapped exception
     */
    public DelegadoNotFoundException (String message, Exception e) {
        super(message, e);
    }
}
