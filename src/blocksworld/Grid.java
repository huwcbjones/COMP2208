package blocksworld;

import blocksworld.exceptions.InvalidPositionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Grid
 * Holds the state of the grid
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Grid {

    private int width;
    private int height;
    private char[][] grid;
    private HashMap<Character, Position> blocks;

    /**
     * Creates a new grid with a specified width and height
     *
     * @param width  Width of new grid
     * @param height Height of new grid
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[width][height];
        this.blocks = new HashMap<>();
    }

    /**
     * Returns the width of the grid
     *
     * @return Grid Width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the grid
     *
     * @return Grid Height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Places the agent to the grid at position (x, y)
     *
     * @param x x-coord
     * @param y y-coord
     * @throws InvalidPositionException Thrown if the given position is invalid
     */
    public void placeAgent(int x, int y) throws InvalidPositionException {
        placeAgent(new Position(x, y));
    }

    /**
     * Places the agent to the grid at position P(x, y)
     *
     * @param position Position P(x, y)
     * @throws InvalidPositionException Thrown if the given position is invalid
     */
    public void placeAgent(Position position) throws InvalidPositionException {
        this.placeBlock('*', position);
    }

    /**
     * Adds a block to the grid at position (x, y) with the ID blockID.
     * Throws an exception if the position is invalid
     *
     * @param blockID  ID of the block, must be unique
     * @param position Position of the block
     * @throws InvalidPositionException Thrown if the position is invalid
     */
    public void placeBlock(char blockID, Position position) throws InvalidPositionException {
        if (!isPositionValid(position)) {
            throw new InvalidPositionException(position);
        }
        try {
            this.grid[position.getX()][position.getY()] = blockID;
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        // Prevent adding null chars to the block HashMap
        if (blockID == Character.MIN_VALUE) return;
        this.blocks.put(blockID, position);
    }

    /**
     * Returns whether a position is valid on the grid
     *
     * @param position Position to check
     * @return False if position is not valid
     */
    public boolean isPositionValid(Position position) {
        return isPositionValid(position.getX(), position.getY());
    }

    /**
     * Returns whether a position is valid on the grid
     *
     * @param x x-coord
     * @param y y-coord
     * @return False if position is not valid
     */
    public boolean isPositionValid(int x, int y) {
        return (x < this.width && y < this.height && x >= 0 && y >= 0);
    }

    /**
     * Places a block on the grid
     *
     * @param block Block to place
     * @throws InvalidPositionException Thrown if the position is invalid
     */
    public void placeBlock(Block block) throws InvalidPositionException {
        placeBlock(block.getID(), block.getPosition());
    }

    /**
     * Gets the agent block
     *
     * @return Agent block
     * @throws NoSuchElementException If the block was not found
     */
    public Block getAgent() throws NoSuchElementException {
        return getBlock('*');
    }

    /**
     * Gets the Block with blockID
     *
     * @param blockID Block to fetch
     * @return Block
     * @throws NoSuchElementException If block was not found
     */
    public Block getBlock(char blockID) throws NoSuchElementException {
        if (!this.blocks.containsKey(blockID)) {
            throw new NoSuchElementException("No such block with ID: " + blockID);
        }
        return new Block(blockID, this.blocks.get(blockID));
    }

    /**
     * Gets the list of blocks in the grid
     *
     * @return List of Blocks
     */
    public ArrayList<Block> getBlocks() {
        // Map the HashMap to an ArrayList<Block>
        return this.blocks.entrySet().stream().map(map -> new Block(map.getKey(), map.getValue())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        String grid = "";
        try {
            Character block;
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    block = this.grid[x][y];
                    grid += (block != Character.MIN_VALUE) ? block : "-";

                }
                grid += "\n";
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return grid;
    }
}
