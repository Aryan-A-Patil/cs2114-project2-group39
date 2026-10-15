package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for EnergyCard and EnergyType. Each public method has a normal
 * case and a bad-input case, following the test plan in our
 * specification.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public class EnergyCardTest
{
    // Normal: FIRE -> type is FIRE
    @Test
    public void testConstructor()
    {
        assertEquals(EnergyType.FIRE,
            new EnergyCard(EnergyType.FIRE).getType());
    }

    // Bad input: null -> IllegalArgumentException
    @Test
    public void testConstructorNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new EnergyCard(null));
    }

    // Normal: getType returns each type it was given
    @Test
    public void testGetType()
    {
        for (EnergyType type : EnergyType.values())
        {
            assertEquals(type, new EnergyCard(type).getType());
        }
    }

    // Normal: FIRE equals FIRE
    @Test
    public void testEquals()
    {
        EnergyCard fire = new EnergyCard(EnergyType.FIRE);

        assertTrue(fire.equals(new EnergyCard(EnergyType.FIRE)));
        assertTrue(fire.equals(fire));
    }

    // Bad input: FIRE vs WATER, null, or a PokemonCard -> false
    @Test
    public void testEqualsNotEqual()
    {
        EnergyCard fire = new EnergyCard(EnergyType.FIRE);

        assertFalse(fire.equals(new EnergyCard(EnergyType.WATER)));
        assertFalse(fire.equals(null));
        assertFalse(fire.equals(new PokemonCard("Charmander", Rarity.COMMON)));
    }

    // Normal: equal cards have the same hash code
    @Test
    public void testHashCodeMatchesEquals()
    {
        assertEquals(new EnergyCard(EnergyType.GRASS).hashCode(),
            new EnergyCard(EnergyType.GRASS).hashCode());
    }

    // Bad input: different types don't crash and differ
    @Test
    public void testHashCodeDifferentTypes()
    {
        assertNotEquals(new EnergyCard(EnergyType.GRASS).hashCode(),
            new EnergyCard(EnergyType.METAL).hashCode());
    }

    // Normal: toString -> "Fire Energy"
    @Test
    public void testToString()
    {
        assertEquals("Fire Energy",
            new EnergyCard(EnergyType.FIRE).toString());
        assertEquals("Lightning Energy",
            new EnergyCard(EnergyType.LIGHTNING).toString());
    }

    // Normal: EnergyType order matches the numbered menu in InputReader
    @Test
    public void testEnergyTypeOrder()
    {
        EnergyType[] values = EnergyType.values();

        assertEquals(8, values.length);
        assertEquals(EnergyType.GRASS, values[0]);
        assertEquals(EnergyType.FIRE, values[1]);
        assertEquals(EnergyType.WATER, values[2]);
        assertEquals(EnergyType.METAL, values[7]);
    }

    // Bad input: an energy type that doesn't exist ("Fairy") -> exception
    @Test
    public void testEnergyTypeValueOfBadName()
    {
        assertThrows(IllegalArgumentException.class,
            () -> EnergyType.valueOf("FAIRY"));
    }

    // Normal: the same generic LinkedChain stores and counts Energy cards
    @Test
    public void testWorksInLinkedChain()
    {
        LinkedChain<EnergyCard> chain = new LinkedChain<EnergyCard>();
        chain.add(new EnergyCard(EnergyType.FIRE));
        chain.add(new EnergyCard(EnergyType.WATER));
        chain.add(new EnergyCard(EnergyType.FIRE));

        assertEquals(2, chain.count(new EnergyCard(EnergyType.FIRE)));
        assertTrue(chain.contains(new EnergyCard(EnergyType.WATER)));
    }

    // Bad input: counting a type that was never added -> 0
    @Test
    public void testCountMissingInLinkedChain()
    {
        LinkedChain<EnergyCard> chain = new LinkedChain<EnergyCard>();

        assertEquals(0, chain.count(new EnergyCard(EnergyType.PSYCHIC)));
        assertFalse(chain.remove(new EnergyCard(EnergyType.PSYCHIC)));
    }
}
