package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for PokemonCard and Rarity. Each public method has a normal case
 * and a bad-input case, following the test plan in our specification.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public class PokemonCardTest
{
    // Normal: (" Pikachu ", RARE) -> name is "Pikachu"
    @Test
    public void testConstructorTrimsName()
    {
        PokemonCard card = new PokemonCard(" Pikachu ", Rarity.RARE);

        assertEquals("Pikachu", card.getName());
        assertEquals(Rarity.RARE, card.getRarity());
    }

    // Bad input: blank name -> IllegalArgumentException
    @Test
    public void testConstructorBlankName()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new PokemonCard("   ", Rarity.RARE));
        assertThrows(IllegalArgumentException.class,
            () -> new PokemonCard("", Rarity.RARE));
    }

    // Bad input: null name or null rarity -> IllegalArgumentException
    @Test
    public void testConstructorNulls()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new PokemonCard(null, Rarity.RARE));
        assertThrows(IllegalArgumentException.class,
            () -> new PokemonCard("Pikachu", null));
    }

    // Normal: getName and getRarity return what was given
    @Test
    public void testGetters()
    {
        PokemonCard card = new PokemonCard("Eevee", Rarity.COMMON);

        assertEquals("Eevee", card.getName());
        assertEquals(Rarity.COMMON, card.getRarity());
    }

    // Normal: "Pikachu" equals "pikachu", even with a different rarity
    @Test
    public void testEqualsIgnoresCaseAndRarity()
    {
        PokemonCard a = new PokemonCard("Pikachu", Rarity.RARE);
        PokemonCard b = new PokemonCard("pikachu", Rarity.COMMON);
        PokemonCard c = new PokemonCard("  PIKACHU ", Rarity.HOLO_RARE);

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(a));
    }

    // Bad input: compared to null, an EnergyCard, or a different name
    @Test
    public void testEqualsNotEqual()
    {
        PokemonCard card = new PokemonCard("Pikachu", Rarity.RARE);

        assertFalse(card.equals(null));
        assertFalse(card.equals(new EnergyCard(EnergyType.LIGHTNING)));
        assertFalse(card.equals("Pikachu"));
        assertFalse(card.equals(new PokemonCard("Raichu", Rarity.RARE)));
    }

    // Normal: equal cards have the same hash code
    @Test
    public void testHashCodeMatchesEquals()
    {
        assertEquals(new PokemonCard("Pikachu", Rarity.RARE).hashCode(),
            new PokemonCard("PIKACHU", Rarity.COMMON).hashCode());
    }

    // Bad input: different names don't crash and usually differ
    @Test
    public void testHashCodeDifferentNames()
    {
        assertNotEquals(new PokemonCard("Pikachu", Rarity.RARE).hashCode(),
            new PokemonCard("Eevee", Rarity.RARE).hashCode());
    }

    // Normal: toString -> "Pikachu (Rare)"
    @Test
    public void testToString()
    {
        assertEquals("Pikachu (Rare)",
            new PokemonCard("Pikachu", Rarity.RARE).toString());
        assertEquals("Charizard (Holo Rare)",
            new PokemonCard("Charizard", Rarity.HOLO_RARE).toString());
    }

    // Bad input: names with odd characters are printed as is
    @Test
    public void testToStringSpecialCharacters()
    {
        assertEquals("Farfetch'd (Common)",
            new PokemonCard("Farfetch'd", Rarity.COMMON).toString());
    }

    // Normal: Rarity order matches the numbered menu in InputReader
    @Test
    public void testRarityOrder()
    {
        Rarity[] values = Rarity.values();

        assertEquals(4, values.length);
        assertEquals(Rarity.COMMON, values[0]);
        assertEquals(Rarity.HOLO_RARE, values[3]);
    }

    // Bad input: a rarity name that doesn't exist -> exception
    @Test
    public void testRarityValueOfBadName()
    {
        assertThrows(IllegalArgumentException.class,
            () -> Rarity.valueOf("MYTHIC"));
    }

    // Normal: the same LinkedChain finds, counts, and removes cards
    // using PokemonCard.equals
    @Test
    public void testWorksInLinkedChain()
    {
        LinkedChain<PokemonCard> chain = new LinkedChain<PokemonCard>();
        chain.add(new PokemonCard("Pikachu", Rarity.RARE));
        chain.add(new PokemonCard("Eevee", Rarity.COMMON));
        chain.add(new PokemonCard("pikachu", Rarity.COMMON));

        PokemonCard search = new PokemonCard(" PIKACHU ", Rarity.COMMON);

        assertEquals(2, chain.count(search));
        assertTrue(chain.remove(search));
        assertEquals(1, chain.count(search));
    }

    // Bad input: removing a card that isn't in the chain changes nothing
    @Test
    public void testRemoveMissingFromLinkedChain()
    {
        LinkedChain<PokemonCard> chain = new LinkedChain<PokemonCard>();
        chain.add(new PokemonCard("Pikachu", Rarity.RARE));

        assertFalse(chain.remove(new PokemonCard("Mewtwo", Rarity.RARE)));
        assertEquals(1, chain.size());
    }
}
