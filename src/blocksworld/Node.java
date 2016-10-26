package blocksworld;

import blocksworld.GridController.DIRECTION;

/**
 * Node of Moves
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Node {

    private Grid grid = null;
    private Node parent;
    private DIRECTION direction;

    public Node(Node parent, DIRECTION direction) {
        this.parent = parent;
        this.direction = direction;
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

    public DIRECTION getDirection() {
        return direction;
    }
}
