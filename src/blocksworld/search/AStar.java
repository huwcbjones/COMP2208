package blocksworld.search;

import blocksworld.*;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.*;

/**
 * A* Search
 *
 * @author Huw Jones
 * @since 27/11/2016
 */
public class AStar extends Search {

    PriorityQueue<Node> nodeQueue;

    @Override
    protected void preRun() {
        this.nodeQueue = new PriorityQueue<>(new PriorityComparator());
        this.rootNode = Node.createRootNode();
        this.rootNode.setGrid(this.startGrid);
    }

    @Override
    protected void runSearch() throws Exception {
        this.currentNode = rootNode;
        // Init
        ArrayList<GridController.DIRECTION> directions = new ArrayList<>(4);
        Arrays.stream(GridController.DIRECTION.values()).forEach(directions::add);

        while_loop:
        while (true) {
            for (GridController.DIRECTION direction : directions) {
                try {
                    Node newNode = new Node(currentNode);
                    newNode.setGrid(
                            GridController.move(
                                    newNode.getParent().getGrid(),
                                    direction
                            )
                    );
                    numberOfNodes++;
                    newNode.setPriority(
                            calculatePriority(newNode.getGrid())
                    );
                    if (this.checkExitCondition(newNode.getGrid())) {
                        completed(newNode);
                        break while_loop;
                    }
                    nodeQueue.add(newNode);
                } catch (InvalidDirectionException e) {
                }
            }
            nextNode();
        }
    }

    @Override
    protected void nextNode() {
        currentNode = nodeQueue.poll();
    }

    private int calculatePriority(Grid grid) {
        int score = 0;
        ArrayList<Block> blocks = grid.getBlocks();
        for(Block block: blocks){
            try {
                Position difference = this.exitGrid.getBlock(block.getID()).getPosition().subtract(block.getPosition());
                score += Math.abs(difference.getX());
                score += Math.abs(difference.getY());
            } catch (NoSuchElementException ex) {

            }
        }
        return score;
    }

    private class PriorityComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }
}
