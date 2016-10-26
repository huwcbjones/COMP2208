package blocksworld;

import blocksworld.GridController.DIRECTION;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 21/10/2016
 */
public class BDF extends Search {

    private Queue<Node> nodeQueue;
    private Node rootNode;

    @Override
    void preRun() {
        this.nodeQueue = new ConcurrentLinkedQueue<>();
        this.rootNode = new Node(null, null);
        this.rootNode.setGrid(this.startGrid);
        this.nodeQueue.add(this.rootNode);
    }

    @Override
    void runSearch() {
        int numberOfNodes = 0;
        Node currentNode = nodeQueue.poll();
        Grid currentGrid = currentNode.getGrid();
        while ((!this.checkExitCondition(currentGrid))) {

            numberOfNodes++;
            if (currentNode.getDirection() != null) {
                try {
                    currentGrid = GridController.move(currentNode.getParent().getGrid(), currentNode.getDirection());
                    currentNode.setGrid(currentGrid);
                } catch (InvalidDirectionException e) {
                    continue;
                }
            }

            if (this.checkExitCondition(currentNode.getGrid())) {
                break;
            }

            Node n;
            for (DIRECTION d : DIRECTION.values()) {
                if (GridController.canMove(currentGrid, d)) {
                    n = new Node(currentNode, d);
                    n.setGrid(currentNode.getGrid());
                    nodeQueue.add(n);
                }
            }
            currentNode = nodeQueue.poll();

            if ((numberOfNodes % 5000) == 0){
                System.out.println(String.format("Expanded %d nodes...", numberOfNodes));
            }
        }

        System.out.println("Found solution. Expanded " + numberOfNodes);

        //System.out.println("Solution as follows:");
    }
}