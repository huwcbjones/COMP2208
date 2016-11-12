package blocksworld.search;

import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.GridController.DIRECTION;
import blocksworld.Node;
import blocksworld.Pair;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 21/10/2016
 */
public class BFS extends Search {

    private Queue<Pair<Node, DIRECTION>> nodeQueue;
    private Node rootNode;

    @Override
    protected void preRun() {
        this.nodeQueue = new ConcurrentLinkedQueue<>();
        this.rootNode = new Node(null);
        this.rootNode.setGrid(this.startGrid);
    }

    @Override
    protected void runSearch() {
        currentNode = rootNode;
        DIRECTION currentDirection = null;
        Pair<Node, DIRECTION> currentPair;

        while (true) {

            numberOfNodes++;
            if (currentDirection != null) {
                try {
                    currentNode.setGrid(
                            GridController.move(
                                    currentNode.getParent().getGrid(),
                                    currentDirection
                            )
                    );
                    if (this.checkExitCondition(currentNode.getGrid())) {
                        completed();
                        break;
                    }
                } catch (InvalidDirectionException e) {
                    currentPair = nodeQueue.poll();
                    currentNode = currentPair.getKey();
                    currentDirection = currentPair.getValue();
                    continue;
                }
            }

            for (DIRECTION direction : DIRECTION.values()) {
                nodeQueue.add(new Pair<>(new Node(currentNode), direction));
            }
            currentPair = nodeQueue.poll();
            currentNode = currentPair.getKey();
            currentDirection = currentPair.getValue();
        }

        System.out.println("\r\nFound solution. Expanded " + numberOfNodes);

        System.out.println("Solution as follows:");
        System.out.println(this.getSolution(currentNode));
    }
}