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
    private Node rootNode;
    private ArrayList<DIRECTION> directions;
    private int depth;

    private Node currentNode;
    private Pair<Node, DIRECTION> currentPair;
    private DIRECTION currentDirection = null;

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

    @Override
    protected void runSearch() {
        while (true) {

            if (currentNode.getDepth() > depth) {
                if(this.nodeStack.size() == 0) {
                    increaseDepth();
                } else {
                    nextNode();
                }
                continue;
            }

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

            for (DIRECTION direction : directions) {
                nodeStack.push(new Pair<>(new Node(currentNode), direction));
            }

            nextNode();
        }
        System.out.println("Max Iterative Depth:");
        System.out.println(depth);
    }

    private void increaseDepth(){
        preRun();
        depth++;
        System.out.println("\r\nDepth increased: "+ depth);
    }

    private void nextNode(){
        currentPair = nodeStack.pop();
        currentNode = currentPair.getKey();
        currentDirection = currentPair.getValue();
    }
}
