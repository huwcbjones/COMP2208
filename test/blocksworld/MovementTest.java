package blocksworld;

import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidDirectionException;
import blocksworld.exceptions.InvalidPositionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class MovementTest {

    Controller c;
    @Before
    public void setUp() throws Exception {
        c = new Controller();
        c.createGrid(4, 4);

        // Create grid
        c.addBlock('a', 0, 3);
        c.addBlock('b', 1, 3);
        c.addBlock('c', 2, 3);
        c.addAgent(3, 3);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void moveNorth() throws Exception {
        c.move(Controller.DIRECTION.NORTH);
        assertEquals("Failed to move north.", "----\n" +
                "----\n" +
                "---*\n" +
                "abc-\n", c.getGrid());
    }

    @Test
    public void moveSouth() throws Exception {
        thrown.expect(InvalidDirectionException.class);
        c.move(Controller.DIRECTION.SOUTH);
    }

    @Test
    public void moveEast() throws Exception {
        thrown.expect(InvalidDirectionException.class);
        c.move(Controller.DIRECTION.EAST);
    }

    @Test
    public void moveWest() throws Exception {
        c.move(Controller.DIRECTION.WEST);
        assertEquals("Failed to move west.", "----\n" +
                "----\n" +
                "----\n" +
                "ab*c\n", c.getGrid());
    }

}