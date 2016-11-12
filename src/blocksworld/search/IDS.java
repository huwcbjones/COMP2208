package blocksworld.search;

import blocksworld.GridController;
import blocksworld.Node;
import blocksworld.Pair;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 12/11/2016
 */
public class IDS extends Search {

    private Stack<Pair<Node, GridController.DIRECTION>> nodeStack;
    private Node rootNode;

    @Override
    protected void preRun() {
        this.nodeStack = new Stack<>();
        this.rootNode = Node.createRootNode();
        this.rootNode.setGrid(this.startGrid);
    }

    @Override
    protected void runSearch() {
        // Init
        ArrayList<GridController.DIRECTION> directions = new ArrayList<>(4);
        Arrays.stream(GridController.DIRECTION.values()).forEach(directions::add);

        currentNode = rootNode;
        Pair<Node, GridController.DIRECTION> currentPair;
        GridController.DIRECTION currentDirection = null;

        int depth = 0;

        while (true) {

            numberOfNodes++;

            if (currentNode.getDepth() >= depth) {
                if(this.nodeStack.size() == 0) {
                    preRun();
                    depth++;
                    continue;
                } else {
                    currentPair = nodeStack.pop();
                    currentNode = currentPair.getKey();
                    currentDirection = currentPair.getValue();
                }
            }

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
                    currentPair = nodeStack.pop();
                    currentNode = currentPair.getKey();
                    currentDirection = currentPair.getValue();
                    continue;
                }
            }

            Collections.shuffle(directions, this.random);

            for (GridController.DIRECTION direction : directions) {
                nodeStack.push(new Pair<>(new Node(currentNode), direction));
            }

            currentPair = nodeStack.pop();
            currentNode = currentPair.getKey();
            currentDirection = currentPair.getValue();
        }
    }
}
