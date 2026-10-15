package pokemontracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for InputReader. Each public method has a normal case and a
 * bad-input case, following the test plan in our specification. Each
 * test gives the reader fake keyboard input, like "abc\n5\n", instead
 * of the real keyboard.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.15
 */
public class InputReaderTest
{
    // Normal: a reader with input says it has more input
    @Test
    public void testConstructor()
    {
        InputReader reader = new InputReader(new java.util.Scanner("hi\n"));

        assertTrue(reader.hasMoreInput());
    }

    // Bad input: null scanner or null output -> IllegalArgumentException
    @Test
    public void testConstructorNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new InputReader(null));
        assertThrows(IllegalArgumentException.class,
            () -> new InputReader(new java.util.Scanner(""), null));
    }

    // Normal: "Pikachu" -> "Pikachu"
    @Test
    public void testReadName()
    {
        assertEquals("Pikachu", readerFor("Pikachu\n").readName("> "));
    }

    // Normal: "   Pikachu  " -> "Pikachu"
    @Test
    public void testReadNameTrimsSpaces()
    {
        assertEquals("Pikachu",
            readerFor("   Pikachu  \n").readName("> "));
    }

    // Normal: real card names with odd characters are accepted
    @Test
    public void testReadNameSpecialCharacters()
    {
        assertEquals("Farfetch'd",
            readerFor("Farfetch'd\n").readName("> "));
        assertEquals("Mr. Mime", readerFor("Mr. Mime\n").readName("> "));
    }

    // Bad input: blank name -> asks again, then "Eevee"
    @Test
    public void testReadNameBlank()
    {
        java.io.ByteArrayOutputStream printed =
            new java.io.ByteArrayOutputStream();
        InputReader reader = readerFor("   \nEevee\n", printed);

        assertEquals("Eevee", reader.readName("> "));
        assertTrue(printed.toString().contains("Card name can't be empty"));
    }

    // Bad input: 41 characters -> asks again; exactly 40 is fine
    @Test
    public void testReadNameTooLong()
    {
        String tooLong = "a".repeat(41);
        String forty = "a".repeat(40);
        java.io.ByteArrayOutputStream printed =
            new java.io.ByteArrayOutputStream();
        InputReader reader = readerFor(tooLong + "\n" + forty + "\n", printed);

        assertEquals(forty, reader.readName("> "));
        assertTrue(printed.toString().contains("40 characters"));
    }

    // Bad input: no input at all (like Ctrl+D) -> NoSuchElementException
    @Test
    public void testReadNameInputEnds()
    {
        assertThrows(java.util.NoSuchElementException.class,
            () -> readerFor("").readName("> "));
    }

    // Normal: "5" -> 5
    @Test
    public void testReadInt()
    {
        assertEquals(5, readerFor("5\n").readInt("> ", 1, 100));
    }

    // Normal: the smallest and largest allowed values are accepted
    @Test
    public void testReadIntEdges()
    {
        assertEquals(1, readerFor("1\n").readInt("> ", 1, 100));
        assertEquals(100, readerFor("100\n").readInt("> ", 1, 100));
    }

    // Bad input: "abc" then "-3" -> both rejected, then 7
    @Test
    public void testReadIntLettersAndNegative()
    {
        java.io.ByteArrayOutputStream printed =
            new java.io.ByteArrayOutputStream();
        InputReader reader = readerFor("abc\n-3\n7\n", printed);

        assertEquals(7, reader.readInt("> ", 1, 100));
        assertTrue(printed.toString().contains("Please enter a number."));
        assertTrue(printed.toString().contains("from 1 to 100"));
    }

    // Bad input: 0, a huge number, and 101 -> all rejected, then 2
    @Test
    public void testReadIntZeroAndHuge()
    {
        assertEquals(2, readerFor("0\n999999999999\n101\n2\n")
            .readInt("> ", 1, 100));
    }

    // Bad input: min bigger than max -> IllegalArgumentException
    @Test
    public void testReadIntBadRange()
    {
        assertThrows(IllegalArgumentException.class,
            () -> readerFor("5\n").readInt("> ", 10, 1));
    }

    // Bad input: "abc" and then input ends -> NoSuchElementException
    @Test
    public void testReadIntInputEnds()
    {
        assertThrows(java.util.NoSuchElementException.class,
            () -> readerFor("abc\n").readInt("> ", 1, 100));
    }

    // Normal: "3" -> RARE
    @Test
    public void testReadRarity()
    {
        assertEquals(Rarity.RARE, readerFor("3\n").readRarity());
    }

    // Bad input: "9" isn't an option -> asks again, then "1" -> COMMON
    @Test
    public void testReadRarityOutOfRange()
    {
        assertEquals(Rarity.COMMON, readerFor("9\n1\n").readRarity());
    }

    // Normal: "2" -> FIRE
    @Test
    public void testReadEnergyType()
    {
        assertEquals(EnergyType.FIRE, readerFor("2\n").readEnergyType());
    }

    // Bad input: typing "Fairy" -> asks again, then "3" -> WATER
    @Test
    public void testReadEnergyTypeWord()
    {
        assertEquals(EnergyType.WATER,
            readerFor("Fairy\n3\n").readEnergyType());
    }

    // Normal: input left -> true
    @Test
    public void testHasMoreInput()
    {
        assertTrue(readerFor("Pikachu\n").hasMoreInput());
    }

    // Bad input: no input left (like Ctrl+D) -> false
    @Test
    public void testHasMoreInputEmpty()
    {
        assertFalse(readerFor("").hasMoreInput());
    }

    /**
     * Makes a reader whose "keyboard" is the given text. Prompts are
     * thrown away.
     *
     * @param input the lines the fake user types
     * @return a reader over that text
     */
    private InputReader readerFor(String input)
    {
        return readerFor(input, new java.io.ByteArrayOutputStream());
    }

    /**
     * Makes a reader whose "keyboard" is the given text and whose
     * prompts and error messages go into printed.
     *
     * @param input the lines the fake user types
     * @param printed where the reader's messages are saved
     * @return a reader over that text
     */
    private InputReader readerFor(String input,
        java.io.ByteArrayOutputStream printed)
    {
        return new InputReader(new java.util.Scanner(input),
            new java.io.PrintStream(printed));
    }
}