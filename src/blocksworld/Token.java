package blocksworld;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public abstract class Token {
    protected char id;
    private int x;
    private int y;

    public Token(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getID() {
        return id;
    }

    public Position getPosition() {
        return new Position(x, y);
    }
}
