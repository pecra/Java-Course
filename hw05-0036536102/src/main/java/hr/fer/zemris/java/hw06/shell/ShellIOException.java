package hr.fer.zemris.java.hw06.shell;

/**
 * Iznimka u shellu.
 * @author Petra
 *
 */
public class ShellIOException extends RuntimeException {
    public ShellIOException() {
    	super();
    }
    public ShellIOException(String msg) {
    	super(msg);
    }
}
