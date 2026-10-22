package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the test class for the CardCollection class.
 *
 * @author Golden You
 * @version 2026.10.14
 */
public class CardCollectionTest {

    private CardCollection collection;

    @BeforeEach
    public void setUp() {
        collection = new CardCollection("Golden");
    }

    // Test adding cards and counting them
    @Test
    public void testAddAndCountCards() {
        PokemonCard pokemon =
            new PokemonCard("Pikachu", Rarity.COMMON);

        EnergyType type = EnergyType.values()[0];
        EnergyCard energy = new EnergyCard(type);

        collection.addPokemon(pokemon, 2);
        collection.addEnergy(energy, 1);

        assertEquals(2, collection.countPokemon("Pikachu"));
        assertEquals(1, collection.countEnergy(type));
        assertEquals(3, collection.totalCards());
        assertFalse(collection.isEmpty());
    }

    // Test removing cards successfully
    @Test
    public void testRemoveCards() {
        collection.addPokemon(
            new PokemonCard("Pikachu", Rarity.COMMON), 2);

        boolean removed = collection.removePokemon("Pikachu", 1);

        assertTrue(removed);
        assertEquals(1, collection.countPokemon("Pikachu"));
        assertEquals(1, collection.totalCards());
    }

    // Test an empty collection
    @Test
    public void testEmptyCollection() {
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.totalCards());
        assertTrue(collection.toString().contains("empty"));
    }

    // Test invalid quantity when adding cards
    @Test
    public void testInvalidQuantity() {
        PokemonCard pokemon =
            new PokemonCard("Pikachu", Rarity.COMMON);

        assertThrows(IllegalArgumentException.class,
            () -> collection.addPokemon(pokemon, 0));

        EnergyCard energy =
            new EnergyCard(EnergyType.values()[0]);

        assertThrows(IllegalArgumentException.class,
            () -> collection.addEnergy(energy, 0));
    }

    // Test invalid owner names
    @Test
    public void testInvalidOwnerName() {
        assertThrows(IllegalArgumentException.class,
            () -> new CardCollection(null));

        assertThrows(IllegalArgumentException.class,
            () -> new CardCollection("   "));
    }

    // Test null cards
    @Test
    public void testNullCards() {
        assertThrows(IllegalArgumentException.class,
            () -> collection.addPokemon(null, 1));

        assertThrows(IllegalArgumentException.class,
            () -> collection.addEnergy(null, 1));
    }

    // Test invalid input when removing cards
    @Test
    public void testInvalidRemoveInput() {
        assertThrows(IllegalArgumentException.class,
            () -> collection.removePokemon("   ", 1));

        assertThrows(IllegalArgumentException.class,
            () -> collection.removePokemon("Pikachu", 0));

        assertThrows(IllegalArgumentException.class,
            () -> collection.removeEnergy(null, 1));

        assertThrows(IllegalArgumentException.class,
            () -> collection.removeEnergy(
                EnergyType.values()[0], 0));
    }

    // Test removing cards that are not in the collection
    @Test
    public void testRemoveMissingCards() {
        assertFalse(collection.removePokemon("Pikachu", 1));

        assertFalse(collection.removeEnergy(
            EnergyType.values()[0], 1));
    }

    // Test counting with invalid input
    @Test
    public void testCountInvalidInput() {
        assertEquals(0, collection.countPokemon(null));
        assertEquals(0, collection.countPokemon("   "));
        assertEquals(0, collection.countEnergy(null));
    }

    // Test toString when the collection contains cards
    @Test
    public void testToStringWithCards() {
        collection.addPokemon(
            new PokemonCard("Pikachu", Rarity.COMMON), 1);

        collection.addEnergy(
            new EnergyCard(EnergyType.values()[0]), 1);

        String result = collection.toString();

        assertTrue(result.contains("Golden"));
        assertTrue(result.contains("Pokémon cards:"));
        assertTrue(result.contains("Energy cards:"));
    }
    @Test
    public void testRemoveEnergySuccessfully() {
        EnergyType type = EnergyType.values()[0];
        EnergyCard energy = new EnergyCard(type);

        collection.addEnergy(energy, 2);

        boolean removed = collection.removeEnergy(type, 2);

        assertTrue(removed);
        assertEquals(0, collection.countEnergy(type));
        assertEquals(0, collection.totalCards());
        assertTrue(collection.isEmpty());
    }

    // Added by Aryan for trading.
    // Normal: getOwnerName returns the trimmed name
    @Test
    public void testGetOwnerName() {
        assertEquals("Golden", collection.getOwnerName());
        assertEquals("Sam", new CardCollection("  Sam ").getOwnerName());
    }

    // Normal: takePokemon removes and returns the cards, keeping rarity
    @Test
    public void testTakePokemon() {
        collection.addPokemon(
            new PokemonCard("Pikachu", Rarity.HOLO_RARE), 2);
        collection.addPokemon(
            new PokemonCard("Eevee", Rarity.COMMON), 1);

        PokemonCard[] taken = collection.takePokemon("pikachu", 2);

        assertEquals(2, taken.length);
        assertEquals(Rarity.HOLO_RARE, taken[0].getRarity());
        assertEquals(0, collection.countPokemon("Pikachu"));
        assertEquals(1, collection.countPokemon("Eevee"));
    }

    // Bad input: not enough copies -> empty array, nothing removed
    @Test
    public void testTakePokemonNotEnough() {
        collection.addPokemon(
            new PokemonCard("Pikachu", Rarity.COMMON), 1);

        assertEquals(0, collection.takePokemon("Pikachu", 2).length);
        assertEquals(0, collection.takePokemon("Mewtwo", 1).length);
        assertEquals(1, collection.countPokemon("Pikachu"));
    }

    // Bad input: blank name or quantity below 1 -> exception
    @Test
    public void testTakePokemonInvalidInput() {
        assertThrows(IllegalArgumentException.class,
            () -> collection.takePokemon("   ", 1));
        assertThrows(IllegalArgumentException.class,
            () -> collection.takePokemon("Pikachu", 0));
    }
}