package blocksworld;

/**
 * Block on the Grid
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Block {
    private char blockID;
    private int x;
    private int y;

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public char getID () {
        return blockID;
    }

    public Position getPosition () {
        return new Position(x, y);
    }

    public Block (char blockID, int x, int y) {
        this.blockID = blockID;
        this.x = x;
        this.y = y;
    }

    public Block (char blockID, Position position) {
        this.blockID = blockID;
        this.x = position.getX();
        this.y = position.getY();
    }
}
