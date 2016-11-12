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

    public static Node createRootNode(){
          return new Node();
    }

    private Node() {
        this.depth = 0;
    }

    public Node(Node parent){
        this.parent = parent;
        this.depth = parent.getDepth() + 1;
    }

    public int getDepth() { return depth; }

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
