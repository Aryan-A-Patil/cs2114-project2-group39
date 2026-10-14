package pokemontracker;

/**
 * This is a test class for the InputReader class. Each test feeds the
 * reader a string instead of the keyboard, like "abc\n5\n", and
 * captures what it prints.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class InputReaderTest
    extends student.TestCase
{
    //~ Fields ................................................................
    private java.io.ByteArrayOutputStream printed;

    //~ Constructor ...........................................................
    // ----------------------------------------------------------
    /**
     * Creates a new InputReaderTest test object.
     */
    public InputReaderTest()
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
        printed = new java.io.ByteArrayOutputStream();
    }

    // ----------------------------------------------------------
    /**
     * Makes a reader whose "keyboard" is the given text.
     *
     * @param text the lines the fake user types
     * @return a reader over that text
     */
    private InputReader readerFor(String text)
    {
        return new InputReader(new java.util.Scanner(text),
            new java.io.PrintStream(printed));
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor creates a reader that can read input.
     */
    public void testConstructor()
    {
        InputReader reader = readerFor("hi\n");
        assertTrue(reader.hasMoreInput());
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor rejects a null scanner.
     */
    public void testConstructorNullScanner()
    {
        Exception thrown = null;
        try
        {
            new InputReader(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that the constructor rejects a null output stream.
     */
    public void testConstructorNullOutput()
    {
        Exception thrown = null;
        try
        {
            new InputReader(new java.util.Scanner(""), null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName returns a normal name.
     */
    public void testReadName()
    {
        assertEquals("Pikachu", readerFor("Pikachu\n").readName("> "));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName trims extra spaces off the name.
     */
    public void testReadNameTrimsSpaces()
    {
        assertEquals("Pikachu",
            readerFor("   Pikachu  \n").readName("> "));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName accepts names with unusual characters.
     */
    public void testReadNameSpecialCharacters()
    {
        assertEquals("Farfetch'd",
            readerFor("Farfetch'd\n").readName("> "));
        assertEquals("Mr. Mime", readerFor("Mr. Mime\n").readName("> "));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName rejects a blank name and asks again.
     */
    public void testReadNameBlankThenValid()
    {
        assertEquals("Eevee", readerFor("   \nEevee\n").readName("> "));
        assertTrue(printed.toString().contains("can't be empty"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName rejects a name over 40 characters.
     */
    public void testReadNameTooLongThenValid()
    {
        String tooLong = "a".repeat(41);
        assertEquals("Eevee",
            readerFor(tooLong + "\nEevee\n").readName("> "));
        assertTrue(printed.toString().contains("40"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName accepts a name of exactly 40 characters.
     */
    public void testReadNameExactlyFortyCharacters()
    {
        String forty = "a".repeat(40);
        assertEquals(forty, readerFor(forty + "\n").readName("> "));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readName throws when the input ends.
     */
    public void testReadNameInputEnds()
    {
        Exception thrown = null;
        try
        {
            readerFor("").readName("> ");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof java.util.NoSuchElementException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt returns a normal number.
     */
    public void testReadInt()
    {
        assertEquals(5, readerFor("5\n").readInt("> ", 1, 100));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt accepts the smallest and largest values.
     */
    public void testReadIntEdges()
    {
        assertEquals(1, readerFor("1\n").readInt("> ", 1, 100));
        assertEquals(100, readerFor("100\n").readInt("> ", 1, 100));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt rejects letters and negative numbers.
     */
    public void testReadIntLettersThenNegativeThenValid()
    {
        assertEquals(7,
            readerFor("abc\n-3\n7\n").readInt("> ", 1, 100));
        String output = printed.toString();
        assertTrue(output.contains("Please enter a number."));
        assertTrue(output.contains("from 1 to 100"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt rejects zero and very large numbers.
     */
    public void testReadIntZeroAndHugeRejected()
    {
        assertEquals(2, readerFor("0\n999999999999\n101\n2\n")
            .readInt("> ", 1, 100));
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt throws when min is bigger than max.
     */
    public void testReadIntBadRange()
    {
        Exception thrown = null;
        try
        {
            readerFor("5\n").readInt("> ", 10, 1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that readInt throws when the input ends.
     */
    public void testReadIntInputEnds()
    {
        Exception thrown = null;
        try
        {
            readerFor("abc\n").readInt("> ", 1, 100);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof java.util.NoSuchElementException);
    }

    // ----------------------------------------------------------
    /**
     * Tests that readRarity returns the picked rarity.
     */
    public void testReadRarity()
    {
        assertEquals(Rarity.RARE, readerFor("3\n").readRarity());
    }

    // ----------------------------------------------------------
    /**
     * Tests that readRarity rejects a number that isn't an option.
     */
    public void testReadRarityOutOfRangeThenValid()
    {
        assertEquals(Rarity.COMMON, readerFor("9\n1\n").readRarity());
    }

    // ----------------------------------------------------------
    /**
     * Tests that readEnergyType returns the picked energy type.
     */
    public void testReadEnergyType()
    {
        assertEquals(EnergyType.FIRE, readerFor("2\n").readEnergyType());
    }

    // ----------------------------------------------------------
    /**
     * Tests that readEnergyType rejects a typed word like "Fairy".
     */
    public void testReadEnergyTypeWordThenValid()
    {
        assertEquals(EnergyType.WATER,
            readerFor("Fairy\n3\n").readEnergyType());
    }

    // ----------------------------------------------------------
    /**
     * Tests that hasMoreInput is true when there is input left.
     */
    public void testHasMoreInput()
    {
        assertTrue(readerFor("Pikachu\n").hasMoreInput());
    }

    // ----------------------------------------------------------
    /**
     * Tests that hasMoreInput is false when input has ended.
     */
    public void testHasMoreInputEmpty()
    {
        assertFalse(readerFor("").hasMoreInput());
    }
}