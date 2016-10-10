package blocksworld;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 10/10/2016
 */
public class BlockTest {

    Controller c;

    @Before
    public void setUp() throws Exception {
        c = new Controller();
        c.createGrid(4, 4);
    }


    @Test
    public void testAgentAdd() throws Exception {
        c.addAgent(0, 0);
        assertEquals("Failed to add agent.", "*---\n" +
                "----\n" +
                "----\n" +
                "----\n", c.getGrid());
    }
}
