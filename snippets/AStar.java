/**
 * A* Search
 *
 * @author Huw Jones
 * @since 27/11/2016
 */
public class AStar extends Search {

    PriorityQueue<Node> nodeQueue;

    /**
     * Set up the initial environment before running the search
     */
    @Override
    protected void preRun() {
        this.nodeQueue = new PriorityQueue<>(new PriorityComparator());
        this.rootNode = Node.createRootNode();
        this.rootNode.setGrid(this.startGrid);
    }

    /**
     * Where the actual search runs
     */
    @Override
    protected void runSearch() throws Exception {
        ArrayList<GridController.DIRECTION> directions = new ArrayList<>(4);
        Arrays.stream(GridController.DIRECTION.values()).forEach(directions::add);

        this.currentNode = rootNode;
        while_loop:
        while (true) {
            numberOfNodes++;

            // Check the if the node satisfies the exit condition
            if (this.checkExitCondition(currentNode.getGrid())) {
                completed(currentNode);
                break;
            }

            for (GridController.DIRECTION direction : directions) {
                try {
                    // Process the move and store the new state in the node
                    Node newNode = new Node(currentNode);
                    newNode.setGrid(
                            GridController.move(
                                    newNode.getParent().getGrid(),
                                    direction
                            )
                    );

                    // Calculate the node heuristic score and add it to the queue
                    newNode.setPriority(
                            calculatePriority(newNode)
                    );
                    nodeQueue.add(newNode);
                } catch (InvalidDirectionException e) {
                }
            }
            // Process the next node
            nextNode();
        }
    }

    @Override
    protected void nextNode() {
        currentNode = nodeQueue.poll();
    }

    /**
     * Calculates the priority (heuristic score) of the node
     *
     * @param node Node to calculate score for
     * @return Score for that node
     */
    private int calculatePriority(Node node) {
        int score = 0;
        score += getManhattanDistance(node.getGrid());
        score += getTilesInCorrectPlace(node.getGrid());
        score += node.getDepth();
        return score;
    }

    /**
     * Calculates the Manhattan Distance Heuristic
     *
     * @param grid Grid to calculate
     * @return score
     */
    private int getManhattanDistance(Grid grid) {
        int score = 0;
        ArrayList<Block> blocks = grid.getBlocks();
        for (Block block : blocks) {
            try {
                // Get the difference between the exit position and the current block position
                Position difference = this.exitGrid
                        .getBlock(block.getID())
                        .getPosition()
                        .subtract(block.getPosition());
                // Add the X/Y distance from target block position (distance not displacement, hence Math.abs)
                score += Math.abs(difference.getX());
                score += Math.abs(difference.getY());
            } catch (NoSuchElementException ex) {
            }
        }
        return score;
    }

    /**
     * Calculates the number of tiles in the correct place
     *
     * @param grid Grid to calculate
     * @return score
     */
    private int getTilesInCorrectPlace(Grid grid) {
        ArrayList<Block> blocks = grid.getBlocks();
        int score = 0;
        for (Block block : blocks) {
            try {
                // Increment score for every incorrectly positioned block
                if (!this.exitGrid.getBlock(block.getID()).getPosition().equals(block.getPosition())) score++;
            } catch (NoSuchElementException ex) {

            }
        }
        return score;
    }

    /**
     * Finds the highest priority node (node with lowest score)
     * Used to sort the PriorityQueue
     */
    private class PriorityComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }
}
