package blocksworld.search;

import blocksworld.*;
import blocksworld.exceptions.InvalidPositionException;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 11/10/2016
 */
public abstract class Search {

    protected Grid startGrid;
    protected long randomSeed;
    protected Random random;
    protected int numberOfNodes = 0;
    protected Grid exitGrid;
    private boolean completed = false;
    private long startTime;

    protected Node rootNode;
    protected Node currentNode;
    protected Pair<Node, GridController.DIRECTION> currentPair;
    protected GridController.DIRECTION currentDirection = null;

    public Search() {
        this.randomSeed = new Random().nextLong();
        this.buildGrid();
        this.createExitGrid();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.print("\r\n\r\n\r\n\r\n");
            }
        });
    }

    private void buildGrid() {
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

    public void setSeed(long seed){
        this.randomSeed = seed;
    }

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
            t.start();
            this.startTime = System.nanoTime();
            this.runSearch();
        } catch (Exception ex) {
            System.out.println("Error running search.");
            ex.printStackTrace();
        }
    }

    protected void completed(Node exitNode){
        this.completed = true;
        System.out.println("Solution found.");
        System.out.println("Expanded " + numberOfNodes);

        System.out.println("Solution as follows:");
        System.out.println(this.getSolution(exitNode));

        System.out.println("Start State:");
        System.out.println(this.startGrid.toString());
        System.out.println("Exit State:");
        System.out.println(this.exitGrid.toString());
        System.out.println("Random Seed:");
        System.out.println(this.randomSeed);
    }
    abstract protected void preRun();

    abstract protected void runSearch() throws Exception;

    public String getSolution(Node endNode) {
        Stack<String> states = new Stack<>();
        Node currentNode = endNode;
        do {
            if(currentNode != null) {
                if(currentNode.getGrid() != null) {
                    states.add(currentNode.getGrid().toString());
                }
            }
        } while ((currentNode = currentNode.getParent()) != null);
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

    protected boolean checkExitCondition(Grid grid) {
        List<Block> blocks = grid.getBlocks().stream().filter(b -> b.getID() != '*').collect(Collectors.toList());

        boolean exitReached = true;
        Block comparisonBlock;
        for (Block block : blocks) {
            comparisonBlock = this.exitGrid.getBlock(block.getID());
            exitReached &= comparisonBlock.getPosition().equals(block.getPosition());
        }

        return exitReached;
    }

    public void setStartState(Grid startGrid) {
        this.startGrid = startGrid;
    }

    public void setExitState(Grid exitGrid) {
        this.exitGrid = exitGrid;
    }

    private class Monitor implements Runnable {
        @Override
        public void run() {
            long time;
            long minutes;
            long oldSeconds = 0;
            long seconds;
            long memory;
            while (!completed) {
                time = System.nanoTime() - startTime;
                memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
                seconds = time / 1000000000;
                minutes = seconds / 60;
                seconds -= minutes * 60;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                if(oldSeconds != seconds) {
                    System.out.print(String.format("\rExpanded Nodes: %12s\t\tElapsed Time [%s:%s]\t\tUsed Memory: %6sMB\t",
                            NumberFormat.getNumberInstance(Locale.getDefault()).format(numberOfNodes),
                            String.format("%2d", minutes).replace(' ', '0'),
                            String.format("%2d", seconds).replace(' ', '0'),
                            NumberFormat.getNumberInstance(Locale.getDefault()).format(memory)));
                } else {
                    System.out.print(String.format("\rExpanded Nodes: %12s\t\tElapsed Time [%s:%s]\t\tUsed Memory: ",
                            NumberFormat.getNumberInstance(Locale.getDefault()).format(numberOfNodes),
                            String.format("%2d", minutes).replace(' ', '0'),
                            String.format("%2d", seconds).replace(' ', '0')));
                }
                oldSeconds = seconds;
            }
        }
    }

    protected abstract void nextNode();
}
