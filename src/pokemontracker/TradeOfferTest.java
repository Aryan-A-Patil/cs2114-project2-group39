package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for TradeOffer. Each public method has a normal case and a
 * bad-input case.
 *
 * @author Aryan Patil
 * @version 2026.10.22
 */
public class TradeOfferTest
{
    // Normal: a Pokemon offer trims the name and keeps the quantity
    @Test
    public void testPokemon()
    {
        TradeOffer offer = TradeOffer.pokemon("  Pikachu ", 2);

        assertTrue(offer.isPokemon());
        assertEquals("Pikachu", offer.getPokemonName());
        assertNull(offer.getEnergyType());
        assertEquals(2, offer.getQuantity());
    }

    // Bad input: blank or null name -> IllegalArgumentException
    @Test
    public void testPokemonBadName()
    {
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.pokemon("   ", 1));
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.pokemon(null, 1));
    }

    // Bad input: zero or negative quantity -> IllegalArgumentException
    @Test
    public void testPokemonBadQuantity()
    {
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.pokemon("Pikachu", 0));
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.pokemon("Pikachu", -3));
    }

    // Normal: an Energy offer keeps the type and quantity
    @Test
    public void testEnergy()
    {
        TradeOffer offer = TradeOffer.energy(EnergyType.FIRE, 3);

        assertFalse(offer.isPokemon());
        assertEquals(EnergyType.FIRE, offer.getEnergyType());
        assertNull(offer.getPokemonName());
        assertEquals(3, offer.getQuantity());
    }

    // Bad input: null type or zero quantity -> IllegalArgumentException
    @Test
    public void testEnergyBadInput()
    {
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.energy(null, 1));
        assertThrows(IllegalArgumentException.class,
            () -> TradeOffer.energy(EnergyType.FIRE, 0));
    }

    // Normal: counts how many of the offered card a collection owns
    @Test
    public void testCountOwnedBy()
    {
        CardCollection sam = new CardCollection("Sam");
        sam.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 2);
        sam.addEnergy(new EnergyCard(EnergyType.WATER), 4);

        assertEquals(2, TradeOffer.pokemon("pikachu", 1).countOwnedBy(sam));
        assertEquals(4,
            TradeOffer.energy(EnergyType.WATER, 1).countOwnedBy(sam));
    }

    // Bad input: an empty collection owns 0; null -> exception
    @Test
    public void testCountOwnedByEmptyOrNull()
    {
        TradeOffer offer = TradeOffer.pokemon("Pikachu", 1);

        assertEquals(0, offer.countOwnedBy(new CardCollection("Sam")));
        assertThrows(IllegalArgumentException.class,
            () -> offer.countOwnedBy(null));
    }

    // Normal: toString -> "2 Pikachu" and "3 Fire Energy"
    @Test
    public void testToString()
    {
        assertEquals("2 Pikachu", TradeOffer.pokemon("Pikachu", 2).toString());
        assertEquals("3 Fire Energy",
            TradeOffer.energy(EnergyType.FIRE, 3).toString());
    }
}
