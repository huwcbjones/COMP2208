/**
 * Depth First Search
 *
 * @author Huw Jones
 * @since 27/10/2016
 */
public class DFS extends Search {

    private Stack<Pair<Node, DIRECTION>> nodeStack;

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
        while (true) {



            if (currentDirection != null) {
                try {
                    currentNode.setGrid(
                            GridController.move(
                                    currentNode.getParent().getGrid(),
                                    currentDirection
                            )
                    );
                    numberOfNodes++;
                    if (this.checkExitCondition(currentNode.getGrid())) {
                        completed(currentNode);
                        break;
                    }
                } catch (InvalidDirectionException e) {
                    nextNode();
                    continue;
                }
            }

            Collections.shuffle(directions, this.random);

            for (DIRECTION direction : directions) {
                nodeStack.push(new Pair<>(new Node(currentNode), direction));
            }

            nextNode();
        }
    }

    @Override
    protected void nextNode() {
        currentPair = nodeStack.pop();
        currentNode = currentPair.getKey();
        currentDirection = currentPair.getValue();
    }
}
