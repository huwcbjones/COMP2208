import blocksworld.Controller;
import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidDirectionException;
import blocksworld.exceptions.InvalidPositionException;

/**
 * BlocksWorld
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class BlocksWorld {

    public static void main(String[] args){
        Controller c = new Controller();
        c.createGrid(4, 4);
        try {
            c.addBlock('a', 0, 3);
            c.addBlock('b', 1, 3);
            c.addBlock('c', 2, 3);
            c.addAgent(3, 3);
        } catch (InvalidPositionException | InvalidBlockIDException e) {
            e.printStackTrace();
        }
        c.printGrid();

        try {
            c.move(Controller.DIRECTION.NORTH);
            c.printGrid();
            c.move(Controller.DIRECTION.WEST);
            c.printGrid();
            c.move(Controller.DIRECTION.WEST);
            c.printGrid();
            c.move(Controller.DIRECTION.SOUTH);
            c.printGrid();
            c.move(Controller.DIRECTION.EAST);
            c.printGrid();
            c.move(Controller.DIRECTION.EAST);
            c.printGrid();
        } catch (InvalidDirectionException e) {
            e.printStackTrace();
        }

    }
}
