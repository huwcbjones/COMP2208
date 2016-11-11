package blocksworld;

import blocksworld.exceptions.InvalidDirectionException;
import blocksworld.exceptions.InvalidPositionException;

import java.util.ArrayList;

/**
 * Grid Controller
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class GridController {

    public static Grid placeBlock(Grid grid, char blockID, Position position) throws InvalidPositionException {
        grid.placeBlock(blockID, position);
        return grid;
    }

    public static Grid placeBlock(Grid grid, char blockID, int x, int y) throws InvalidPositionException {
        grid.placeBlock(blockID, new Position(x, y));
        return grid;
    }

    public static Grid placeBlock(Grid grid, Block block) throws InvalidPositionException {
        grid.placeBlock(block.getID(), block.getPosition());
        return grid;
    }

    public static Grid placeAgent(Grid grid, int x, int y) throws InvalidPositionException {
        grid.placeAgent(x, y);
        return grid;
    }

    public static Grid placeAgent(Grid grid, Position position) throws InvalidPositionException {
        grid.placeAgent(position);
        return grid;
    }

    public static Grid move(Grid grid, DIRECTION direction) throws InvalidDirectionException {
        if (!canMove(grid, direction))
            throw new InvalidDirectionException(direction, grid.getAgent().getPosition(), getNewAgentPosition(grid, direction));

        // Get position where agent is *going* to move to
        Position newAgentPosition = getNewAgentPosition(grid, direction);

        Grid newGrid = GridController.createGrid(grid.getWidth(), grid.getHeight());

        ArrayList<Block> oldBlocks = grid.getBlocks();
        ArrayList<Block> newBlocks = new ArrayList<>();

        Block oldBlock = null;
        Block oldAgent = null;

        for (Block block : oldBlocks) {
            if (block.getID() == '*') {
                oldAgent = block;
            } else if (block.getPosition().equals(newAgentPosition)) {
                oldBlock = block;
            } else {
                newBlocks.add(block);
            }
        }
        if (oldAgent != null) {
            newBlocks.add(new Block('*', newAgentPosition));
            if (oldBlock != null) {
                newBlocks.add(new Block(oldBlock.getID(), oldAgent.getPosition()));
            }
        }

        for (Block block : newBlocks) {
            try {
                newGrid.placeBlock(block);
            } catch (InvalidPositionException e) {
                e.printStackTrace();
            }
        }

        return newGrid;
    }

    public static boolean canMove(Grid grid, DIRECTION direction) {
        try {
            getNewAgentPosition(grid, direction);
        } catch (InvalidDirectionException ex) {
            return false;
        }
        return true;
    }

    private static Position getNewAgentPosition(Grid grid, DIRECTION direction) throws InvalidDirectionException {
        Position oldPosition = grid.getAgent().getPosition();
        int old_x = oldPosition.getX();
        int old_y = oldPosition.getY();

        int new_x = -1;
        int new_y = -1;

        switch (direction) {
            case NORTH:
                new_x = old_x;
                new_y = old_y - 1;
                break;
            case EAST:
                new_x = old_x + 1;
                new_y = old_y;
                break;
            case SOUTH:
                new_x = old_x;
                new_y = old_y + 1;
                break;
            case WEST:
                new_x = old_x - 1;
                new_y = old_y;
                break;
        }

        Position newPosition = new Position(new_x, new_y);

        if (!grid.isPositionValid(new_x, new_y))
            throw new InvalidDirectionException(direction, oldPosition, newPosition);

        return newPosition;
    }

    public static Grid createGrid(int width, int height) {
        return new Grid(width, height);
    }

    public enum DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
