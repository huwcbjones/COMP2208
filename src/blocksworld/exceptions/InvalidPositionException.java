package blocksworld.exceptions;

import blocksworld.Position;

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

    public InvalidPositionException(Position position) {
        this(position.getX(), position.getY());
    }
}
