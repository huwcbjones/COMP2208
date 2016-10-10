package blocksworld;

import blocksworld.exceptions.InvalidPositionException;
import org.omg.CORBA.CharHolder;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

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
    private char[][] grid;

    private HashMap<Character, Position> blocks;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.isInfiniteGrid = (this.width == -1 || this.height == -1);
        this.blocks = new HashMap<>();
        if(!isInfiniteGrid){
            this.grid = new char[width][height];
        }
    }

    public void addAgent (Position position) throws InvalidPositionException {
        if (!isPositionValid(position.getX(), position.getY())) {
            throw new InvalidPositionException(position);
        }
        this.grid[position.getX()][position.getY()] = '*';
        this.blocks.put('*', position);
    }

    public void addAgent (int x, int y) throws InvalidPositionException {
        addAgent(new Position(x, y));
    }

    public boolean isPositionValid (Position position) {
        return isPositionValid(position.getX(), position.getY());
    }
    public boolean isPositionValid(int x, int y) {
        return isInfiniteGrid || (x < this.width && y < this.height);
    }

    /**
     * Adds a block to the grid at position (x, y) with the ID blockID.
     * Throws an exception if the position is invalid
     *
     * @param blockID  ID of the block, must be unique
     * @param position Position of the block
     * @throws InvalidPositionException Thrown if the position is invalid
     */
    public void addBlock (char blockID, Position position) throws InvalidPositionException {
        if (!isPositionValid(position)) {
            throw new InvalidPositionException(position);
        }
        if (blockID == Character.MIN_VALUE) return;
        this.blocks.put(blockID, position);
        this.grid[position.getX()][position.getY()] = blockID;
    }

    /**
     * Adds a block to the grid at position (x, y) with the ID blockID.
     * Throws an exception if the position is invalid
     *
     * @param blockID ID of the block, must be unique
     * @param x       x position of the block
     * @param y       y position of the block
     * @throws InvalidPositionException Thrown if the position is invalid
     */
    public void addBlock (char blockID, int x, int y) throws InvalidPositionException {
        addBlock(blockID, new Position(x, y));
    }

    public void removeBlock (char blockID, int x, int y) throws InvalidPositionException {
        removeBlock(blockID, new Position(x, y));
    }

    public void removeBlock (char blockID, Position position) throws InvalidPositionException {
        if (!isPositionValid(position)) {
            throw new InvalidPositionException(position);
        }
        this.blocks.remove(blockID);
        this.grid[position.getX()][position.getY()] = '\u0000';
    }

    public Block getBlock(char blockID) throws NoSuchElementException {
        if (!this.blocks.containsKey(blockID)) {
            throw new NoSuchElementException("No such block with ID: " + blockID);
        }
        return new Block(blockID, this.blocks.get(blockID));
    }

    public Block getAgent() throws NoSuchElementException {
        return getBlock('*');
    }

    public char getPosition (int x, int y) {
        return this.grid[x][y];
    }

    public char getPosition (Position position) { return this.grid[position.getX()][position.getY()];}

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
        Character block;
        for (int y = 0; y < this.width; y++) {
            for (int x = 0; x < this.height; x++) {
                block = this.grid[x][y];
                grid += ( block != Character.MIN_VALUE ) ? block : "-";

            }
            grid += "\n";
        }

        return grid;
    }

    public static Grid getCopy (Grid grid) {
        Grid newGrid = new Grid(grid.width, grid.height);
        for (Map.Entry<Character, Position> block : grid.blocks.entrySet()) {
            try {
                newGrid.addBlock(block.getKey(), block.getValue());
            } catch (InvalidPositionException e) {
                e.printStackTrace();
            }
        }
        return newGrid;
    }
}
