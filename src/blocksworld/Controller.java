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
    private Agent agent;

    public Controller() {
        this.blocks = new HashMap<>();
    }

    public void createGrid(int width, int height) {
        this.grid = new Grid(width, height);
    }

    public void addBlock(char blockID, int x, int y) throws InvalidPositionException, InvalidBlockIDException {
        Block block = new Block(blockID, x, y);
        this.grid.addBlock(block);
        this.blocks.put(block.getID(), block);
    }

    public void addAgent(int x, int y) throws InvalidPositionException {
        Agent agent = new Agent(x, y);
        this.grid.addAgent(agent);
        this.agent = agent;
    }

    public void move(DIRECTION direction) throws InvalidDirectionException {
        if (!canMove(direction)) throw new InvalidDirectionException(direction, agent.getPosition(), this.getNewPosition(direction));

        // New Agent Position
        Position newPosition = getNewPosition(direction);

        // Check if there is a block in the new position
        Token newToken = this.grid.getPosition(newPosition);
        Agent newAgent = new Agent(newPosition.getX(), newPosition.getY());

        Grid newGrid = Grid.getCopy(this.grid);

        Block newBlock = null;
        if (newToken != null) {
            try {
                newBlock = new Block(newToken.getID(), this.agent.getX(), this.agent.getY());
            } catch (InvalidBlockIDException shouldNotHappen) {
                shouldNotHappen.printStackTrace();
            }
        }
        try {
            newGrid.addAgent(newAgent);
            if (newBlock == null) {
                for (Block block : this.blocks.values()) {
                    newGrid.addBlock(block);
                }
            } else {
                Block oldBlock = null;
                for (Block block : this.blocks.values()) {
                    if (block.getID() != newBlock.getID()) {
                        newGrid.addBlock(block);
                    } else {
                        newGrid.addBlock(newBlock);
                        oldBlock = block;
                    }
                }
                if(oldBlock != null){
                    this.blocks.remove(oldBlock.getID());
                    this.blocks.put(newBlock.getID(), newBlock);
                }
            }
            this.grid = newGrid;
            this.agent = newAgent;
        } catch (InvalidPositionException shouldNotHappen) {
            shouldNotHappen.printStackTrace();
        }

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
