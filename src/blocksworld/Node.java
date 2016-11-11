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

    public Node(Node parent){
        this.parent = parent;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        if (grid != null) this.grid = grid;
    }

    public Node getParent() {
        return parent;
    }
}
