package blocksworld;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 10/10/2016
 */
public class BlockTest {

    Grid g;

    @Before
    public void setUp() throws Exception {
        g = GridController.createGrid(4, 4);
    }


    @Test
    public void testAgentAdd() throws Exception {
        GridController.placeAgent(g, 0, 0);
        assertEquals("Failed to add agent.", "*---\n" +
                "----\n" +
                "----\n" +
                "----\n", g.toString());
    }
}
