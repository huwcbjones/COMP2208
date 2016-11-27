package blocksworld;

/**
 * Node of Moves
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Node {

    private Grid grid = null;
    private Node parent;
    private int depth;
    private Integer priority = null;

    private Node() {
        this.depth = 0;
    }

    public Node(Node parent) {
        this.parent = parent;
        this.depth = parent.getDepth() + 1;
    }

    public int getDepth() {
        return depth;
    }

    public static Node createRootNode() {
        return new Node();
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        if (grid != null) this.grid = grid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (this.priority == null) {
            this.priority = priority;
        }
    }

    public Node getParent() {
        return parent;
    }
}
