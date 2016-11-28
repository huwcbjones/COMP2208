package blocksworld.search;

import blocksworld.GridController;
import blocksworld.GridController.DIRECTION;
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

    private Stack<Pair<Node, DIRECTION>> nodeStack;
    private ArrayList<DIRECTION> directions;
    private int depth;

    /**
     * Set up the initial environment before running the search
     */
    @Override
    protected void preRun() {
        this.nodeStack = new Stack<>();
        this.rootNode = Node.createRootNode();
        this.rootNode.setGrid(this.startGrid);
        directions = new ArrayList<>(4);
        Arrays.stream(DIRECTION.values()).forEach(directions::add);

        currentNode = rootNode;
        currentPair = null;
        currentDirection = null;
    }

    /**
     * Where the actual search runs
     */
    @Override
    protected void runSearch() {
        while (true) {
            // Check if we've hit the depth limit
            if (currentNode.getDepth() > depth) {
                // If we have no more nodes in the stack, increase the depth
                if(this.nodeStack.size() == 0) {
                    increaseDepth();
                } else {
                    // Otherwise continue processing the stack
                    nextNode();
                }
                continue;
            }

            numberOfNodes++;

            if (currentDirection != null) {
                try {
                    // Process the move and store the new state in the node
                    currentNode.setGrid(
                            GridController.move(
                                    currentNode.getParent().getGrid(),
                                    currentDirection
                            )
                    );
                    // Check if the grid meets the exit condition, if so, exit the search
                    if (this.checkExitCondition(currentNode.getGrid())) {
                        completed(currentNode);
                        break;
                    }
                } catch (InvalidDirectionException e) {
                    if(nodeStack.size() == 0){
                        increaseDepth();
                    } else {
                        nextNode();
                    }
                    continue;
                }
            }

            Collections.shuffle(directions, this.random);

            // Push new directions on the stack to be processed
            for (DIRECTION direction : directions) {
                nodeStack.push(new Pair<>(new Node(currentNode), direction));
            }

            nextNode();
        }
        System.out.println("Max Iterative Depth:");
        System.out.println(depth);
    }

    /**
     * Gets the next node off of the stack
     */
    @Override
    protected void nextNode() {
        currentPair = nodeStack.pop();
        currentNode = currentPair.getKey();
        currentDirection = currentPair.getValue();
    }

    /**
     * Increases the depth of the search
     */
    private void increaseDepth(){
        // Reset the search environment
        preRun();
        // Increment depth
        depth++;
        // Log new depth
        System.out.println("\r\nDepth increased: "+ depth);
    }
}
