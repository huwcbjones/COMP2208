package blocksworld.search;

import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.GridController.DIRECTION;
import blocksworld.Node;
import blocksworld.Search;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 27/10/2016
 */
public class DFS extends Search {


    private Stack<Node> nodeStack;
    private Node rootNode;

    @Override
    protected void preRun() {
        this.nodeStack = new Stack<>();
        this.rootNode = new Node(null, null);
        this.rootNode.setGrid(this.startGrid);
        this.nodeStack.add(this.rootNode);
    }

    @Override
    protected void runSearch() {
        int numberOfNodes = 0;
        Node currentNode = nodeStack.pop();
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

            ArrayList<DIRECTION> directions = new ArrayList<>(4);
            Arrays.stream(DIRECTION.values()).forEach(directions::add);

            Node n;
            DIRECTION direction;
            while (directions.size() != 0) {
                direction = directions.get(this.random.nextInt(directions.size()) - 1);
                if (GridController.canMove(currentGrid, direction)) {
                    n = new Node(currentNode, direction);
                    n.setGrid(currentNode.getGrid());
                    nodeStack.push(n);
                }
            }
            currentNode = nodeStack.pop();
        }

        System.out.println("Found solution. Expanded " + numberOfNodes);

        //System.out.println("Solution as follows:");
    }
}
