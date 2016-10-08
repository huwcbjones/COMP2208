package blocksworld.exceptions;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class InvalidPositionException extends Exception {

    public InvalidPositionException(int x, int y) {
        super(String.format("Invalid position at (%d, %d)", x, y));
    }
}
