package blocksworld;

import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidPositionException;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 11/10/2016
 */
public abstract class Search {

    protected Grid startGrid;
    private Grid exitGrid;
    protected long randomSeed;
    protected Random random;

    public Search(){
        this.buildGrid();
        this.createExitGrid();
    }

    abstract protected void preRun();

    private void buildGrid(){
        startGrid = GridController.createGrid(4, 4);
        try {
            GridController.placeBlock(startGrid, 'a', 0, 3);
            GridController.placeBlock(startGrid, 'b', 1, 3);
            GridController.placeBlock(startGrid, 'c', 2, 3);
            GridController.placeAgent(startGrid, 3, 3);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }

    abstract protected void runSearch();

    public void run(){
        System.out.println("Creating random seed...");
        this.randomSeed = new Random().nextLong();
        this.random =  new Random(this.randomSeed);
        System.out.println(String.format("Random seed: %d.", this.randomSeed));
        System.out.println("Running Search::preRun");
        this.preRun();
        System.out.println("Running Search::runSearch");
        this.runSearch();
    }

    void createExitGrid(){
        this.exitGrid = GridController.createGrid(this.startGrid.getWidth(), this.startGrid.getHeight());

        try {
            GridController.placeBlock(exitGrid, 'a', 1, 1);
            GridController.placeBlock(exitGrid, 'b', 1, 2);
            GridController.placeBlock(exitGrid, 'c', 1, 3);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }

    protected boolean checkExitCondition(Grid grid){
        List<Block> blocks = grid.getBlocks().stream().filter(b -> b.getID() != '*').collect(Collectors.toList());

        boolean exitReached = true;
        Block comparisonBlock;
        for(Block block : blocks){
            comparisonBlock = this.exitGrid.getBlock(block.getID());
            exitReached &= comparisonBlock.getPosition().equals(block.getPosition());
        }

        return exitReached;
    }

    public void setStartState(Grid startGrid) { this.startGrid = startGrid; }
    public void setExitState(Grid exitGrid){
        this.exitGrid = exitGrid;
    }
}