package blocksworld.search;

import blocksworld.*;
import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidPositionException;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Abstract Search Class
 *
 * @author Huw Jones
 * @since 11/10/2016
 */
public abstract class Search {

    protected Grid startGrid;
    protected long randomSeed;
    protected Random random;
    protected long numberOfNodes = 0;
    protected Grid exitGrid;
    protected Node rootNode;
    protected Node currentNode;
    protected Pair<Node, GridController.DIRECTION> currentPair;
    protected GridController.DIRECTION currentDirection = null;
    private boolean completed = false;
    private long startTime;
    private int refreshTime = 100;

    public Search() {
        this.randomSeed = new Random().nextLong();
        this.buildGrid();
        this.createExitGrid();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.print("\r\n\r\n");
            }
        });
    }

    /**
     * Builds the default 4x4 grid
     */
    private void buildGrid() {
        startGrid = GridController.createGrid(4, 4);
        try {
            GridController.placeBlock(startGrid, 'a', 0, 3);
            GridController.placeBlock(startGrid, 'b', 1, 3);
            GridController.placeBlock(startGrid, 'c', 2, 3);
            GridController.placeAgent(startGrid, 3, 3);
        } catch (InvalidPositionException | InvalidBlockIDException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the defualt exit grid state
     */
    void createExitGrid() {
        this.exitGrid = GridController.createGrid(this.startGrid.getWidth(), this.startGrid.getHeight());

        try {
            GridController.placeBlock(exitGrid, 'a', 1, 1);
            GridController.placeBlock(exitGrid, 'b', 1, 2);
            GridController.placeBlock(exitGrid, 'c', 1, 3);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the random number seed
     * @param seed Seed
     */
    public void setSeed(long seed) {
        this.randomSeed = seed;
    }

    /**
     * Runs the search
     */
    public void run() {
        System.out.println("Creating random seed...");
        this.random = new Random(this.randomSeed);
        System.out.println(String.format("Random seed: %d.", this.randomSeed));
        System.out.println("Running Search::preRun");
        this.preRun();
        System.out.println("Start State:");
        System.out.println(this.startGrid.toString());
        System.out.println("Exit State:");
        System.out.println(this.exitGrid.toString());
        System.out.println("Running Search::runSearch");
        try {
            Thread t = new Thread(new Monitor(), "MonitorThread");
            t.setDaemon(true);
            this.startTime = System.nanoTime();
            t.start();
            this.runSearch();
        } catch (Exception ex) {
            System.out.println("Error running search.");
            ex.printStackTrace();
        }
    }

    /**
     * Called when a search completes.
     * It dumps the solution and stats to console
     * @param exitNode Node that solves the puzzle
     */
    protected void completed(Node exitNode) {
        this.completed = true;
        System.out.println("\r\n\r\n================================");
        System.out.println("Solution found.");

        System.out.println("Solution as follows:");
        System.out.println(this.getSolution(exitNode));

        System.out.println("\r\n================================");
        System.out.println("Start State:\r\n");
        System.out.println(this.startGrid.toString());
        System.out.println("================================");
        System.out.println("Exit State:\r\n");
        System.out.println(this.exitGrid.toString());
        System.out.println("================================");
        System.out.println("Random Seed:");
        System.out.println(this.randomSeed);
        System.out.println("================================");
        System.out.println("Nodes Expanded:");
        System.out.println(this.numberOfNodes);
        System.out.println("================================");
    }

    /**
     * Set up the initial environment before running the search
     */
    abstract protected void preRun();

    /**
     * Where the actual search runs
     */
    abstract protected void runSearch() throws Exception;

    /**
     * Builds the solution from the exit node
     * @param endNode End Node that is in the exit state
     * @return String that is the solution
     */
    public String getSolution(Node endNode) {
        // Using a stack so we can reverse the order of the nodes easier
        Stack<String> states = new Stack<>();
        Node currentNode = endNode;

        // Dump all nodes on the stack whilst the parent isn't null
        // Only the root node has a null parent
        do {
            if (currentNode != null) {
                if (currentNode.getGrid() != null) {
                    states.add(currentNode.getGrid().toString());
                }
            }
        } while ((currentNode = currentNode.getParent()) != null);

        // Count moves whilst looping through the stack and append grid state to the sting
        StringBuilder builder = new StringBuilder();
        String currentString;
        int moves = 0;
        while (states.size() != 0) {
            currentString = states.pop();
            builder.append("\n");
            builder.append(moves);
            builder.append(":");
            builder.append("\n");
            builder.append(currentString);
            moves++;
        }
        return builder.toString();
    }

    /**
     * Checks whether or not a grid meets the exit criteria
     * @param grid Grid to check
     * @return true if the exit condition has been reached
     */
    protected boolean checkExitCondition(Grid grid) {
        // Lambda to get blocks (excluding the agent "*")
        List<Block> blocks = grid.getBlocks().stream().filter(b -> b.getID() != '*').collect(Collectors.toList());

        // Assume we're complete
        boolean exitReached = true;
        Block comparisonBlock;

        // Loop through the block and AND the matching block result
        for (Block block : blocks) {
            comparisonBlock = this.exitGrid.getBlock(block.getID());
            exitReached &= comparisonBlock.getPosition().equals(block.getPosition());
        }

        return exitReached;
    }

    /**
     * Gets the monitor thread interval refresh time
     * @return Interval refresh time (in ms)
     */
    public int getRefreshTime() {
        return refreshTime;
    }

    /**
     * Sets the refresh interval
     * @param time time in ms
     */
    public void setRefreshTime(int time) {
        this.refreshTime = time;
    }

    /**
     * Sets the start grid state
     * @param startGrid Grid to start from
     */
    public void setStartState(Grid startGrid) {
        this.startGrid = startGrid;
    }

    /**
     * Sets the exit grid state
     * @param exitGrid Grid that forms the exit conditions
     */
    public void setExitState(Grid exitGrid) {
        this.exitGrid = exitGrid;
    }

    /**
     * Gets the next node
     */
    protected abstract void nextNode();

    /**
     * Monitor Thread (provides ongoing stats of the search in console)
     */
    private class Monitor implements Runnable {
        @Override
        public void run() {
            long time;
            long minutes;
            long seconds;
            long memory;
            while (!completed) {
                time = System.nanoTime() - startTime;
                memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
                seconds = time / 1000000000;
                minutes = seconds / 60;
                seconds -= minutes * 60;
                System.out.print(String.format("\rExpanded Nodes: %12s\t\tElapsed Time [%s:%s]\t\tUsed Memory: %6sMB",
                        NumberFormat.getNumberInstance(Locale.getDefault()).format(numberOfNodes),
                        String.format("%2d", minutes).replace(' ', '0'),
                        String.format("%2d", seconds).replace(' ', '0'),
                        NumberFormat.getNumberInstance(Locale.getDefault()).format(memory)));
                try {
                    Thread.sleep(Search.this.getRefreshTime());
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
