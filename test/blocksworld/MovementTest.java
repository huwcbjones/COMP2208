package blocksworld;

import blocksworld.exceptions.InvalidDirectionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class MovementTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Grid g;

    @Before
    public void setUp() throws Exception {
        g = GridController.createGrid(4, 4);
        // Create grid
        GridController.placeBlock(g, 'a', 0, 3);
        GridController.placeBlock(g, 'b', 1, 3);
        GridController.placeBlock(g, 'c', 2, 3);
        GridController.placeAgent(g, 3, 3);
    }

    @Test
    public void moveNorth() throws Exception {
        g = GridController.move(g, GridController.DIRECTION.NORTH);
        assertEquals("Failed to move north.", "----\n" +
                "----\n" +
                "---*\n" +
                "abc-\n", g.toString());
    }

    @Test
    public void moveSouth() throws Exception {
        thrown.expect(InvalidDirectionException.class);
        GridController.move(g, GridController.DIRECTION.SOUTH);
    }

    @Test
    public void moveEast() throws Exception {
        thrown.expect(InvalidDirectionException.class);
        GridController.move(g, GridController.DIRECTION.EAST);
    }

    @Test
    public void moveWest() throws Exception {
        g = GridController.move(g, GridController.DIRECTION.WEST);
        assertEquals("Failed to move west.", "----\n" +
                "----\n" +
                "----\n" +
                "ab*c\n", g.toString());
    }

}