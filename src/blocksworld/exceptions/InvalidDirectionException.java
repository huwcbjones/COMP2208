package blocksworld.exceptions;

import blocksworld.GridController;
import blocksworld.Pair;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class InvalidDirectionException extends Exception {

    public InvalidDirectionException(GridController.DIRECTION d, Pair oldPos, Pair newPos) {
        super("Cannot move in direction: " + d + ", from position " + oldPos + ", to " + newPos);
    }
}
