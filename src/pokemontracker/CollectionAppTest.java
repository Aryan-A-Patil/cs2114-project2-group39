package pokemontracker;

/**
 * This is a test class for the CollectionApp class. Each test runs the
 * whole menu with a script of typed lines, then checks what was printed
 * and what ended up in the collection.
 *
 * Menu: 1 add Pokemon, 2 add Energy, 3 remove, 4 count, 5 show, 6 quit.
 * Rarity list: 1 Common ... 3 Rare. Energy list: 1 Grass, 2 Fire, 3 Water.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class CollectionAppTest
    extends student.TestCase
{
    //~ Fields ................................................................
    private CardCollection                collection;
    private java.io.ByteArrayOutputStream printed;

    //~ Constructor ...........................................................
    // ----------------------------------------------------------
    /**
     * Creates a new CollectionAppTest test object.
     */
    public CollectionAppTest()
    {
        // The constructor is usually empty in unit tests, since it runs
        // once for the whole class, not once for each test method.
        // Per-test initialization should be placed in setUp() instead.
    }

    //~ Methods ...............................................................
    // ----------------------------------------------------------
    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    public void setUp()
    {
        collection = new CardCollection("Sam");
        printed    = new java.io.ByteArrayOutputStream();
    }

    // ----------------------------------------------------------
    /**
     * Runs the app with the given typed lines and returns the output.
     *
     * @param script the lines the fake user types, separated by \n
     * @return everything the app printed
     */
    private String runWith(String script)
    {
        java.io.PrintStream out = new java.io.PrintStream(printed);
        InputReader reader =
            new InputReader(new java.util.Scanner(script), out);
        new CollectionApp(collection, reader, out).run();
        return printed.toString();
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor creates an app.
     */
    public void testConstructor()
    {
        InputReader reader = new InputReader(new java.util.Scanner("6\n"));
        assertNotNull(new CollectionApp(collection, reader));
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor rejects a null collection.
     */
    public void testConstructorNullCollection()
    {
        InputReader reader = new InputReader(new java.util.Scanner(""));
        Exception thrown = null;
        try
        {
            new CollectionApp(null, reader);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor rejects a null input reader.
     */
    public void testConstructorNullInput()
    {
        Exception thrown = null;
        try
        {
            new CollectionApp(collection, null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that choosing quit ends the program.
     */
    public void testRunQuit()
    {
        String output = runWith("6\n");
        assertTrue(output.contains("Goodbye!"));
    }

    // ----------------------------------------------------------
    /**
     * Tests adding 3 Pikachu and then counting them.
     */
    public void testRunAddAndCountPokemon()
    {
        String output = runWith("1\nPikachu\n3\n3\n4\n1\nPikachu\n6\n");
        assertEquals(3, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You have 3 Pikachu."));
    }

    // ----------------------------------------------------------
    /**
     * Tests that "Pikachu" and " pikachu " count as the same card.
     */
    public void testRunAddPokemonDifferentCase()
    {
        runWith("1\nPikachu\n1\n2\n1\n pikachu \n1\n1\n6\n");
        assertEquals(3, collection.countPokemon("PIKACHU"));
    }

    // ----------------------------------------------------------
    /**
     * Tests adding 10 Fire Energy and then counting them.
     */
    public void testRunAddAndCountEnergy()
    {
        String output = runWith("2\n2\n10\n4\n2\n2\n6\n");
        assertEquals(10, collection.countEnergy(EnergyType.FIRE));
        assertTrue(output.contains("You have 10"));
    }

    // ----------------------------------------------------------
    /**
     * Tests removing some Pokemon cards.
     */
    public void testRunRemovePokemon()
    {
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 3);
        String output = runWith("3\n1\nEevee\n2\n6\n");
        assertEquals(1, collection.countPokemon("Eevee"));
        assertTrue(output.contains("Removed 2 Eevee"));
    }

    // ----------------------------------------------------------
    /**
     * Tests removing all of an Energy type.
     */
    public void testRunRemoveEnergy()
    {
        collection.addEnergy(new EnergyCard(EnergyType.WATER), 5);
        runWith("3\n2\n3\n5\n6\n");
        assertEquals(0, collection.countEnergy(EnergyType.WATER));
    }

    // ----------------------------------------------------------
    /**
     * Tests that showing the collection prints the cards.
     */
    public void testRunShowAll()
    {
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 1);
        String output = runWith("5\n6\n");
        assertTrue(output.contains("Eevee"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that removing from an empty collection prints a message.
     */
    public void testRunRemoveFromEmpty()
    {
        String output = runWith("3\n6\n");
        assertTrue(output.contains("Your collection is empty."));
    }

    // ----------------------------------------------------------
    /**
     * Tests that removing more copies than owned removes nothing.
     */
    public void testRunRemoveMoreThanOwned()
    {
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 2);
        String output = runWith("3\n1\nPikachu\n5\n6\n");
        assertEquals(2, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You only have 2 Pikachu."));
    }

    // ----------------------------------------------------------
    /**
     * Tests that removing a card the user doesn't own changes nothing.
     */
    public void testRunRemoveCardNotOwned()
    {
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 1);
        String output = runWith("3\n1\nMewtwo\n1\n6\n");
        assertEquals(1, collection.countPokemon("Pikachu"));
        assertTrue(output.contains("You only have 0 Mewtwo."));
    }

    // ----------------------------------------------------------
    /**
     * Tests that bad menu choices (9, 0, letters) are rejected.
     */
    public void testRunBadMenuChoices()
    {
        String output = runWith("9\n0\nabc\n6\n");
        assertTrue(output.contains("Please enter a number from 1 to 6."));
        assertTrue(output.contains("Please enter a number."));
        assertTrue(output.contains("Goodbye!"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that negative, zero, and letter quantities are rejected.
     */
    public void testRunBadQuantitiesRejected()
    {
        runWith("1\nPikachu\n1\n-2\n0\nabc\n4\n6\n");
        assertEquals(4, collection.countPokemon("Pikachu"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that a blank card name is rejected.
     */
    public void testRunBlankNameRejected()
    {
        String output = runWith("1\n   \nPikachu\n1\n1\n6\n");
        assertTrue(output.contains("Card name can't be empty."));
        assertEquals(1, collection.countPokemon("Pikachu"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that a bad rarity number and a typed energy word are rejected.
     */
    public void testRunBadRarityAndEnergyChoices()
    {
        runWith("1\nPikachu\n7\n1\n1\n2\nFairy\n1\n2\n6\n");
        assertEquals(1, collection.countPokemon("Pikachu"));
        assertEquals(2, collection.countEnergy(EnergyType.GRASS));
    }

    // ----------------------------------------------------------
    /**
     * Tests that input ending in the middle of adding a card (like
     * Ctrl+D) ends the program without crashing.
     */
    public void testRunInputEndsSuddenly()
    {
        String output = runWith("1\nPikachu\n");
        assertTrue(output.contains("Goodbye!"));
        assertEquals(0, collection.countPokemon("Pikachu"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that empty input ends the program without crashing.
     */
    public void testRunEmptyInput()
    {
        String output = runWith("");
        assertTrue(output.contains("Goodbye!"));
    }
}