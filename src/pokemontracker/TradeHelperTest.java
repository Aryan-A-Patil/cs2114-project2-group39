package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TradeHelper.trade. Covers normal trades and the bad-input
 * cases: not owning enough, trading with yourself, an empty collection,
 * and null arguments. A failed trade must change nothing on either side.
 *
 * @author Aryan Patil
 * @version 2026.10.22
 */
public class TradeHelperTest
{
    private CardCollection aryan;
    private CardCollection sam;

    @BeforeEach
    public void setUp()
    {
        aryan = new CardCollection("Aryan");
        sam = new CardCollection("Sam");
        aryan.addPokemon(new PokemonCard("Pikachu", Rarity.HOLO_RARE), 3);
        sam.addPokemon(new PokemonCard("Charizard", Rarity.RARE), 1);
        sam.addEnergy(new EnergyCard(EnergyType.FIRE), 5);
    }

    // Normal: 2 Pikachu for 1 Charizard -> both sides updated
    @Test
    public void testTradePokemonForPokemon()
    {
        assertTrue(TradeHelper.trade(aryan, TradeOffer.pokemon("Pikachu", 2),
            sam, TradeOffer.pokemon("Charizard", 1)));

        assertEquals(1, aryan.countPokemon("Pikachu"));
        assertEquals(1, aryan.countPokemon("Charizard"));
        assertEquals(2, sam.countPokemon("Pikachu"));
        assertEquals(0, sam.countPokemon("Charizard"));
    }

    // Normal: Pokemon for Energy works too, and totals are kept
    @Test
    public void testTradePokemonForEnergy()
    {
        assertTrue(TradeHelper.trade(aryan, TradeOffer.pokemon("pikachu", 1),
            sam, TradeOffer.energy(EnergyType.FIRE, 3)));

        assertEquals(2, aryan.countPokemon("Pikachu"));
        assertEquals(3, aryan.countEnergy(EnergyType.FIRE));
        assertEquals(1, sam.countPokemon("Pikachu"));
        assertEquals(2, sam.countEnergy(EnergyType.FIRE));
        assertEquals(9, aryan.totalCards() + sam.totalCards());
    }

    // Normal: traded Pokemon keep their rarity
    @Test
    public void testTradeKeepsRarity()
    {
        TradeHelper.trade(aryan, TradeOffer.pokemon("Pikachu", 1),
            sam, TradeOffer.pokemon("Charizard", 1));

        assertTrue(sam.toString().contains("Pikachu (Holo Rare)"));
        assertTrue(aryan.toString().contains("Charizard (Rare)"));
    }

    // Bad input: the first side doesn't own enough -> false, no change
    @Test
    public void testTradeFirstSideNotEnough()
    {
        assertFalse(TradeHelper.trade(aryan, TradeOffer.pokemon("Pikachu", 4),
            sam, TradeOffer.pokemon("Charizard", 1)));

        assertEquals(3, aryan.countPokemon("Pikachu"));
        assertEquals(1, sam.countPokemon("Charizard"));
    }

    // Bad input: the second side offers a card it doesn't own -> no change
    @Test
    public void testTradeCardNotOwned()
    {
        assertFalse(TradeHelper.trade(aryan, TradeOffer.pokemon("Pikachu", 1),
            sam, TradeOffer.pokemon("Mewtwo", 1)));

        assertEquals(3, aryan.countPokemon("Pikachu"));
        assertEquals(0, sam.countPokemon("Pikachu"));
    }

    // Bad input: trading from an empty collection -> false, no change
    @Test
    public void testTradeFromEmptyCollection()
    {
        CardCollection empty = new CardCollection("Misty");

        assertFalse(TradeHelper.trade(empty, TradeOffer.pokemon("Pikachu", 1),
            sam, TradeOffer.energy(EnergyType.FIRE, 1)));

        assertTrue(empty.isEmpty());
        assertEquals(5, sam.countEnergy(EnergyType.FIRE));
    }

    // Bad input: trading with yourself -> IllegalArgumentException
    @Test
    public void testTradeWithYourself()
    {
        assertThrows(IllegalArgumentException.class,
            () -> TradeHelper.trade(aryan, TradeOffer.pokemon("Pikachu", 1),
                aryan, TradeOffer.pokemon("Pikachu", 1)));
        assertEquals(3, aryan.countPokemon("Pikachu"));
    }

    // Bad input: any null argument -> IllegalArgumentException
    @Test
    public void testTradeNulls()
    {
        TradeOffer offer = TradeOffer.pokemon("Pikachu", 1);

        assertThrows(IllegalArgumentException.class,
            () -> TradeHelper.trade(null, offer, sam, offer));
        assertThrows(IllegalArgumentException.class,
            () -> TradeHelper.trade(aryan, null, sam, offer));
        assertThrows(IllegalArgumentException.class,
            () -> TradeHelper.trade(aryan, offer, null, offer));
        assertThrows(IllegalArgumentException.class,
            () -> TradeHelper.trade(aryan, offer, sam, null));
    }
}
