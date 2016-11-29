package blocksworld.exceptions;

/**
 * Thrown if an invalid Block ID was specified
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class InvalidBlockIDException extends Exception {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public InvalidBlockIDException(char c) {
        super("Invalid BlockID: " + c);
    }
}
