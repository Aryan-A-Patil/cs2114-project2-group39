package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for CollectionApp. Each test runs the whole menu with fake
 * keyboard input, then checks what was printed and what ended up in the
 * collection. Follows the test plan in our specification.
 *
 * Menu: 1 add Pokemon, 2 add Energy, 3 remove, 4 count, 5 show, 6 quit.
 * Rarity list: 1 Common ... 3 Rare. Energy list: 1 Grass, 2 Fire, 3 Water.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.15
 */
public class CollectionAppTest
{
    // Normal: constructor builds an app
    @Test
    public void testConstructor()
    {
        InputReader reader = new InputReader(new java.util.Scanner("6\n"));

        assertNotNull(new CollectionApp(new CardCollection("Sam"), reader));
    }

    // Bad input: null collection or null reader -> IllegalArgumentException
    @Test
    public void testConstructorNull()
    {
        InputReader reader = new InputReader(new java.util.Scanner(""));

        assertThrows(IllegalArgumentException.class,
            () -> new CollectionApp(null, reader));
        assertThrows(IllegalArgumentException.class,
            () -> new CollectionApp(new CardCollection("Sam"), null));
    }

    // Normal: choose 6 -> program quits
    @Test
    public void testRunQuit()
    {
        String output = runWith(new CardCollection("Sam"), "6\n");

        assertTrue(output.contains("Goodbye!"));
    }

    // Normal: add 3 Rare Pikachu, then count Pikachu -> 3
    @Test
    public void testRunAddAndCountPokemon()
    {
        CardCollection collection = new CardCollection("Sam");

        String output = runWith(collection,
            "1\nPikachu\n3\n3\n4\n1\nPikachu\n6\n");

        assertEquals(3, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You have 3 Pikachu."));
    }

    // Normal: "Pikachu" and " pikachu " count as the same card
    @Test
    public void testRunDifferentCapitalization()
    {
        CardCollection collection = new CardCollection("Sam");

        runWith(collection, "1\nPikachu\n1\n2\n1\n pikachu \n1\n1\n6\n");

        assertEquals(3, collection.countPokemon("PIKACHU"));
    }

    // Normal: add 10 Fire Energy, then count Fire -> 10
    @Test
    public void testRunAddAndCountEnergy()
    {
        CardCollection collection = new CardCollection("Sam");

        String output = runWith(collection, "2\n2\n10\n4\n2\n2\n6\n");

        assertEquals(10, collection.countEnergy(EnergyType.FIRE));
        assertTrue(output.contains("You have 10"));
    }

    // Normal: own 3 Eevee, remove 2 -> 1 left
    @Test
    public void testRunRemovePokemon()
    {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 3);

        String output = runWith(collection, "3\n1\nEevee\n2\n6\n");

        assertEquals(1, collection.countPokemon("Eevee"));
        assertTrue(output.contains("Removed 2 Eevee"));
    }

    // Normal: own 5 Water Energy, remove 5 -> 0 left
    @Test
    public void testRunRemoveEnergy()
    {
        CardCollection collection = new CardCollection("Sam");
        collection.addEnergy(new EnergyCard(EnergyType.WATER), 5);

        runWith(collection, "3\n2\n3\n5\n6\n");

        assertEquals(0, collection.countEnergy(EnergyType.WATER));
    }

    // Normal: show collection prints the cards
    @Test
    public void testRunShowAll()
    {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 1);

        String output = runWith(collection, "5\n6\n");

        assertTrue(output.contains("Eevee"));
    }

    // Bad input: remove from an empty collection -> message, no crash
    @Test
    public void testRunRemoveFromEmpty()
    {
        String output = runWith(new CardCollection("Sam"), "3\n6\n");

        assertTrue(output.contains("Your collection is empty."));
    }

    // Bad input: own 2 Pikachu, try to remove 5 -> nothing removed
    @Test
    public void testRunRemoveMoreThanOwned()
    {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 2);

        String output = runWith(collection, "3\n1\nPikachu\n5\n6\n");

        assertEquals(2, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You only have 2 Pikachu."));
    }

    // Bad input: remove Mewtwo when we only own Pikachu -> nothing removed
    @Test
    public void testRunRemoveCardNotOwned()
    {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 1);

        String output = runWith(collection, "3\n1\nMewtwo\n1\n6\n");

        assertEquals(1, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You only have 0 Mewtwo."));
    }

    // Bad input: menu choices 10, 0, and "abc" -> rejected, then 6 quits
    @Test
    public void testRunBadMenuChoices()
    {
        String output = runWith(new CardCollection("Sam"), "10\n0\nabc\n6\n");

        assertTrue(output.contains("Please enter a number from 1 to 9."));
        assertTrue(output.contains("Please enter a number."));
        assertTrue(output.contains("Goodbye!"));
    }

    // Bad input: quantities -2, 0, "abc" -> rejected, then 4 is added
    @Test
    public void testRunBadQuantities()
    {
        CardCollection collection = new CardCollection("Sam");

        runWith(collection, "1\nPikachu\n1\n-2\n0\nabc\n4\n6\n");

        assertEquals(4, collection.countPokemon("Pikachu"));
    }

    // Bad input: blank name -> rejected, then "Pikachu" is added
    @Test
    public void testRunBlankName()
    {
        CardCollection collection = new CardCollection("Sam");

        String output = runWith(collection, "1\n   \nPikachu\n1\n1\n6\n");

        assertTrue(output.contains("Card name can't be empty."));
        assertEquals(1, collection.countPokemon("Pikachu"));
    }

    // Bad input: rarity 7 and energy "Fairy" -> rejected, then valid picks
    @Test
    public void testRunBadRarityAndEnergy()
    {
        CardCollection collection = new CardCollection("Sam");

        runWith(collection, "1\nPikachu\n7\n1\n1\n2\nFairy\n1\n2\n6\n");

        assertEquals(1, collection.countPokemon("Pikachu"));
        assertEquals(2, collection.countEnergy(EnergyType.GRASS));
    }

    // Bad input: input stops in the middle of adding a card (like Ctrl+D)
    @Test
    public void testRunInputEndsSuddenly()
    {
        CardCollection collection = new CardCollection("Sam");

        String output = runWith(collection, "1\nPikachu\n");

        assertTrue(output.contains("Goodbye!"));
        assertEquals(0, collection.countPokemon("Pikachu"));
    }

    // Bad input: no input at all -> exits cleanly
    @Test
    public void testRunEmptyInput()
    {
        String output = runWith(new CardCollection("Sam"), "");

        assertTrue(output.contains("Goodbye!"));
    }

    // ----- Users and trading (added by Aryan) -----
    // Menu: 7 add user, 8 switch user, 9 trade.

    // Normal: add Sam, give Sam a card, switch back to Aryan
    @Test
    public void testRunAddAndSwitchUser()
    {
        CardCollection aryan = new CardCollection("Aryan");

        String output = runWith(aryan,
            "7\nSam\n1\nEevee\n1\n1\n5\n8\naryan\n6\n");

        assertTrue(output.contains("Added Sam. Now using Sam's collection."));
        assertTrue(output.contains("Sam's collection:"));
        assertTrue(output.contains("Now using Aryan's collection."));
        assertEquals(0, aryan.countPokemon("Eevee"));
    }

    // Bad input: adding a user whose name already exists
    @Test
    public void testRunAddDuplicateUser()
    {
        String output = runWith(new CardCollection("Aryan"), "7\n ARYAN \n6\n");

        assertTrue(output.contains("A user named ARYAN already exists."));
    }

    // Bad input: switching with only one user, or to an unknown user
    @Test
    public void testRunSwitchUserBadInput()
    {
        String output = runWith(new CardCollection("Aryan"),
            "8\n7\nSam\n8\nMisty\n6\n");

        assertTrue(output.contains("There is only one user."));
        assertTrue(output.contains("No user named Misty."));
    }

    // Normal: Aryan trades 2 Pikachu for 3 of Sam's Fire Energy
    @Test
    public void testRunTrade()
    {
        CardCollection aryan = new CardCollection("Aryan");
        aryan.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 3);

        String output = runWith(aryan,
            "7\nSam\n2\n2\n5\n8\nAryan\n"     // Sam gets 5 Fire, back to Aryan
            + "9\nSam\n1\nPikachu\n2\n"       // Aryan gives 2 Pikachu
            + "2\n2\n3\n6\n");                // Sam gives 3 Fire Energy

        assertTrue(output.contains("Trade complete: Aryan gave 2 Pikachu to "
            + "Sam, and Sam gave 3 Fire Energy to Aryan."));
        assertEquals(1, aryan.countPokemon("Pikachu"));
        assertEquals(3, aryan.countEnergy(EnergyType.FIRE));
    }

    // Bad input: trading with only one user
    @Test
    public void testRunTradeNeedsTwoUsers()
    {
        String output = runWith(new CardCollection("Aryan"), "9\n6\n");

        assertTrue(output.contains("You need at least two users to trade."));
    }

    // Bad input: trading with yourself, or with a user who doesn't exist
    @Test
    public void testRunTradeWithYourselfOrUnknown()
    {
        String output = runWith(new CardCollection("Aryan"),
            "7\nSam\n9\nsam\n9\nMisty\n6\n");

        assertTrue(output.contains("You can't trade with yourself."));
        assertTrue(output.contains("No user named Misty."));
    }

    // Bad input: offering more than you own -> nothing is traded
    @Test
    public void testRunTradeNotEnough()
    {
        CardCollection aryan = new CardCollection("Aryan");
        aryan.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 1);

        String output = runWith(aryan,
            "7\nSam\n8\nAryan\n9\nSam\n1\nPikachu\n5\n2\n1\n1\n6\n");

        assertTrue(output.contains("Aryan only has 1 Pikachu."));
        assertTrue(output.contains("Sam only has 0 Grass Energy."));
        assertTrue(output.contains("No cards were traded."));
        assertEquals(1, aryan.countPokemon("Pikachu"));
    }

    /**
     * Runs the menu with fake keyboard input and returns what it printed.
     *
     * @param collection the collection the app edits
     * @param input the lines the fake user types
     * @return everything the app printed
     */
    private String runWith(CardCollection collection, String input)
    {
        java.io.ByteArrayOutputStream printed =
            new java.io.ByteArrayOutputStream();
        java.io.PrintStream out = new java.io.PrintStream(printed);

        InputReader reader = new InputReader(new java.util.Scanner(input), out);
        new CollectionApp(collection, reader, out).run();

        return printed.toString();
    }
}