
package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the LinkedChain test class.
 *
 * @author Golden You
 * @version 2026.10.22
 */
public class LinkedChainTest
{
    private LinkedChain<String> chain;

    @BeforeEach
    public void setUp()
    {
        chain = new LinkedChain<String>();
    }

    @Test
    public void testAdd()
    {
        assertTrue(chain.add("Pikachu"));
        assertEquals(1, chain.size());
        assertEquals("Pikachu", chain.get(0));
    }

    @Test
    public void testAddNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> chain.add(null));
    }

    @Test
    public void testAddToEnd()
    {
        chain.addToEnd("Pikachu");
        chain.addToEnd("Eevee");

        assertEquals("[Pikachu, Eevee]",
            chain.toString());
    }

    @Test
    public void testAddToEndNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> chain.addToEnd(null));
    }

    @Test
    public void testRemoveFirst()
    {
        chain.addToEnd("Pikachu");
        chain.addToEnd("Eevee");

        assertEquals("Pikachu", chain.remove());
        assertEquals(1, chain.size());
        assertEquals("Eevee", chain.get(0));
    }

    @Test
    public void testRemoveFirstEmpty()
    {
        assertNull(chain.remove());
        assertEquals(0, chain.size());
    }

    @Test
    public void testRemoveEntry()
    {
        chain.add("Pikachu");
        chain.add("Eevee");
        chain.add("Pikachu");

        assertTrue(chain.remove("Pikachu"));
        assertEquals(1, chain.count("Pikachu"));
        assertEquals(2, chain.size());
    }

    @Test
    public void testRemoveMissing()
    {
        chain.add("Pikachu");

        assertFalse(chain.remove("Charizard"));
        assertEquals(1, chain.size());
    }

    @Test
    public void testRemoveNull()
    {
        chain.add("Pikachu");

        assertFalse(chain.remove(null));
        assertEquals(1, chain.size());
    }

    @Test
    public void testClear()
    {
        chain.add("Pikachu");
        chain.add("Eevee");
        chain.add("Charizard");

        chain.clear();

        assertEquals(0, chain.size());
        assertTrue(chain.isEmpty());
    }

    @Test
    public void testClearEmpty()
    {
        chain.clear();

        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
    }

    @Test
    public void testGet()
    {
        chain.addToEnd("Pikachu");
        chain.addToEnd("Eevee");

        assertEquals("Eevee", chain.get(1));
    }

    @Test
    public void testGetNegative()
    {
        assertThrows(IndexOutOfBoundsException.class,
            () -> chain.get(-1));
    }

    @Test
    public void testGetOutOfBounds()
    {
        chain.add("Pikachu");

        assertThrows(IndexOutOfBoundsException.class,
            () -> chain.get(1));
    }

    @Test
    public void testContains()
    {
        chain.add("Pikachu");

        assertTrue(chain.contains("Pikachu"));
        assertFalse(chain.contains("Eevee"));
    }

    @Test
    public void testContainsNull()
    {
        chain.add("Pikachu");

        assertFalse(chain.contains(null));
    }

    @Test
    public void testCount()
    {
        chain.add("Pikachu");
        chain.add("Eevee");
        chain.add("Pikachu");

        assertEquals(2, chain.count("Pikachu"));
        assertEquals(1, chain.count("Eevee"));
    }

    @Test
    public void testCountMissing()
    {
        chain.add("Pikachu");

        assertEquals(0, chain.count("Charizard"));
        assertEquals(0, chain.count(null));
    }

    @Test
    public void testSize()
    {
        chain.add("Pikachu");
        chain.add("Eevee");
        chain.add("Charizard");

        assertEquals(3, chain.size());
    }

    @Test
    public void testSizeAfterMissingRemove()
    {
        chain.add("Pikachu");
        chain.add("Eevee");
        chain.add("Charizard");

        assertFalse(chain.remove("Mew"));
        assertEquals(3, chain.size());
    }

    @Test
    public void testIsEmpty()
    {
        assertTrue(chain.isEmpty());

        chain.add("Pikachu");

        assertFalse(chain.isEmpty());
    }

    @Test
    public void testIsEmptyAfterRemove()
    {
        chain.add("Pikachu");
        chain.remove();

        assertTrue(chain.isEmpty());
    }

    @Test
    public void testToArray()
    {
        chain.addToEnd("Pikachu");
        chain.addToEnd("Eevee");

        String[] values = new String[2];
        String[] result = chain.toArray(values);

        assertArrayEquals(
            new String[] {"Pikachu", "Eevee"},
            result);
    }

    @Test
    public void testToArrayTooSmall()
    {
        chain.add("Pikachu");
        chain.add("Eevee");

        assertThrows(IllegalArgumentException.class,
            () -> chain.toArray(new String[1]));
    }

    @Test
    public void testToArrayNull()
    {
        chain.add("Pikachu");

        assertThrows(IllegalArgumentException.class,
            () -> chain.toArray(null));
    }

    @Test
    public void testToString()
    {
        chain.addToEnd("Pikachu");
        chain.addToEnd("Eevee");

        assertEquals("[Pikachu, Eevee]",
            chain.toString());
    }

    @Test
    public void testToStringEmpty()
    {
        assertEquals("[]", chain.toString());
    }
}
