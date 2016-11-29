package blocksworld;

/**
 * Effectively a Co-Ordinate in 2D space
 *
 * @author Huw Jones
 * @since 04/11/2016
 */
public class Position extends Pair<Integer, Integer> {
    public Position(int x, int y) {
        super(x, y);
    }

    public int getX(){
        return this.getKey();
    }

    public int getY(){
        return this.getValue();
    }

    public Position subtract(Position position){
        return new Position(
                this.getX() - position.getX(),
                this.getY() - position.getY()
        );
    }
}
