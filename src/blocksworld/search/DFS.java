package blocksworld.search;

import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.GridController.DIRECTION;
import blocksworld.Node;
import blocksworld.Pair;
import blocksworld.exceptions.InvalidDirectionException;

import java.util.*;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 27/10/2016
 */
public class DFS extends Search {


    private Stack<Pair<Node, DIRECTION>> nodeStack;
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
        ArrayList<DIRECTION> directions = new ArrayList<>(4);
        Arrays.stream(DIRECTION.values()).forEach(directions::add);

        currentNode = rootNode;
        Pair<Node, DIRECTION> currentPair;
        DIRECTION currentDirection = null;

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
                    currentPair = nodeStack.pop();
                    currentNode = currentPair.getKey();
                    currentDirection = currentPair.getValue();
                    continue;
                }
            }

            Collections.shuffle(directions, this.random);

            for(DIRECTION direction: directions){
                nodeStack.push(new Pair<>(new Node(currentNode), direction));
            }

            currentPair = nodeStack.pop();
            currentNode = currentPair.getKey();
            currentDirection = currentPair.getValue();
        }
    }
}
