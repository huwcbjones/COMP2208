package blocksworld;

import blocksworld.exceptions.InvalidBlockIDException;

/**
 * Block on the Grid
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Block extends Token {

    public Block(char id, int x, int y) throws InvalidBlockIDException {
        super(x, y);
        if (!Character.isAlphabetic(id)) throw new InvalidBlockIDException(id);
        this.id = id;
    }
}
