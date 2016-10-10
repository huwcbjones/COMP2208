package blocksworld;

import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidDirectionException;
import blocksworld.exceptions.InvalidPositionException;

import java.util.HashMap;

/**
 * Agent Controller
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class Controller {

    private Grid grid;
    private HashMap<Character, Block> blocks;
    private Position agent;

    public Controller() {
        this.blocks = new HashMap<>();
    }

    public void createGrid(int width, int height) {
        this.grid = new Grid(width, height);
    }

    public void addBlock(char blockID, int x, int y) throws InvalidPositionException, InvalidBlockIDException {
        Block block = new Block(blockID, x, y);
        this.grid.addBlock(blockID, x, y);
        this.blocks.put(block.getID(), block);
    }

    public void addAgent(Position position) throws InvalidPositionException {
        this.grid.addAgent(position);
        this.agent = position;
    }

    public void addAgent(int x, int y) throws InvalidPositionException {
        this.addAgent(new Position(x, y));
    }

    public void move(DIRECTION direction) throws InvalidDirectionException {
        if (!canMove(direction)) throw new InvalidDirectionException(direction, agent, this.getNewPosition(direction));

        // New Agent Position
        Position newPosition = getNewPosition(direction);

        // Check if there is a block in the new position
        Block block = new Block(this.grid.getPosition(newPosition), newPosition);
        Block agent = new Block('*', this.agent);

        Grid newGrid = Grid.getCopy(this.grid);

        try {
            newGrid.removeBlock(agent.getID(), agent.getPosition());
            newGrid.removeBlock(block.getID(), block.getPosition());

            newGrid.addBlock(agent.getID(), block.getPosition());
            newGrid.addBlock(block.getID(), agent.getPosition());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        this.agent = grid.getBlock('*').getPosition();
        this.grid = newGrid;
    }

    public boolean canMove(DIRECTION direction) {
        Position newPosition = getNewPosition(direction);
        return this.grid.isPositionValid(newPosition.getX(), newPosition.getY());
    }

    private Position getNewPosition(DIRECTION direction) {
        int x = agent.getX();
        int y = agent.getY();

        int new_x = -1;
        int new_y = -1;

        switch (direction) {
            case NORTH:
                new_x = x;
                new_y = y - 1;
                break;
            case EAST:
                new_x = x + 1;
                new_y = y;
                break;
            case SOUTH:
                new_x = x;
                new_y = y + 1;
                break;
            case WEST:
                new_x = x - 1;
                new_y = y;
                break;
        }

        return new Position(new_x, new_y);
    }

    public String getGrid() {
        return this.grid.toString();
    }

    public void printGrid() {
        System.out.println(this.getGrid());
    }

    public enum DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
