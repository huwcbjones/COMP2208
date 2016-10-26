import blocksworld.BDF;
import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidPositionException;

/**
 * BlocksWorld
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class BlocksWorld {

    public static void main(String[] args){
        BDF bdfSearch = new BDF();

        Grid g = GridController.createGrid(4, 4);
        try {
            GridController.placeBlock(g, 'a', 0, 0);
            GridController.placeBlock(g, 'b', 1, 2);
            GridController.placeBlock(g, 'c', 1, 3);
            GridController.placeAgent(g, 0, 1);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        //bdfSearch.setStartState(g);

        bdfSearch.run();
    }
}
