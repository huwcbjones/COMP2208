package blocksworld;

import blocksworld.exceptions.InvalidPositionException;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Grid
 * Holds the state of the grid
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Grid {

    private int width = -1;
    private int height = -1;
    private boolean isInfiniteGrid = true;
    private Token[][] grid;

    private HashMap<Character, Block> blocks;
    private Agent agent;

    public Grid() {
    }

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.isInfiniteGrid = (this.width == -1 || this.height == -1);
        this.blocks = new HashMap<>();
        if(!isInfiniteGrid){
            this.grid = new Token[width][height];
        }
    }

    public void addAgent(Agent agent) throws InvalidPositionException {
        if (!isPositionValid(agent.getX(), agent.getY())) {
            throw new InvalidPositionException(agent.getX(), agent.getY());
        }
        this.agent = agent;
        this.grid[agent.getX()][agent.getY()] = agent;
    }

    public boolean isPositionValid(int x, int y) {
        return isInfiniteGrid || (x < this.width && y < this.height);
    }

    public void addBlock(Block block) throws InvalidPositionException {
        if (!isPositionValid(block.getX(), block.getY())) {
            throw new InvalidPositionException(block.getX(), block.getY());
        }
        this.blocks.put(block.getID(), block);
        this.grid[block.getX()][block.getY()] = block;
    }

    public Agent getAgent() {
        return agent;
    }

    public Block getBlock(char blockID) throws NoSuchElementException {
        if (!this.blocks.containsKey(blockID)) {
            throw new NoSuchElementException("No such block with ID: " + blockID);
        }
        return this.blocks.get(blockID);
    }

    public Token getPosition(int x, int y){
        return this.grid[x][y];
    }

    public Token getPosition(Position position){
        return getPosition(position.getX(),position.getY());
    }

    @Override
    public String toString() {
        if (isInfiniteGrid) {
            return "";
        } else {
            return getFiniteGrid();
        }

    }

    private String getFiniteGrid() {
        String grid = "";
        Token token;
        yloop:
        for (int y = 0; y < this.width; y++) {
            xloop:
            for (int x = 0; x < this.height; x++) {
                token = this.grid[x][y];
                if(token == null) {
                    grid += "_";
                } else {
                    grid += token.getID();
                }
            }
            grid += "\n";
        }

        return grid;
    }

    public static Grid getCopy(Grid grid) {
        return new Grid(grid.width, grid.height);
    }
}
