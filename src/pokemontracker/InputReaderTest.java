package pokemontracker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This is the test class for InputReader. Each test gives the reader
 * fake keyboard input, like "abc\n5\n", instead of the real keyboard.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class InputReaderTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testConstructor_normal();
        testConstructor_null();
        testReadName_normal();
        testReadName_trimsSpaces();
        testReadName_specialCharacters();
        testReadName_blank();
        testReadName_tooLong();
        testReadName_inputEnds();
        testReadInt_normal();
        testReadInt_edges();
        testReadInt_lettersAndNegative();
        testReadInt_zeroAndHuge();
        testReadInt_badRange();
        testReadInt_inputEnds();
        testReadRarity_normal();
        testReadRarity_outOfRange();
        testReadEnergyType_normal();
        testReadEnergyType_word();
        testHasMoreInput_normal();
        testHasMoreInput_empty();

        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }

    // Normal: a reader with input says it has more input
    static void testConstructor_normal() {
        InputReader reader = createReader("hi\n", new ByteArrayOutputStream());

        check("constructor - normal", reader.hasMoreInput());
    }

    // Bad input: null scanner or null output -> IllegalArgumentException
    static void testConstructor_null() {
        boolean nullScannerThrew = false;
        try {
            new InputReader(null);
        }
        catch (IllegalArgumentException e) {
            nullScannerThrew = true;
        }

        boolean nullOutputThrew = false;
        try {
            new InputReader(new Scanner(""), null);
        }
        catch (IllegalArgumentException e) {
            nullOutputThrew = true;
        }

        check("constructor - null", nullScannerThrew && nullOutputThrew);
    }

    // Normal: "Pikachu" -> "Pikachu"
    static void testReadName_normal() {
        InputReader reader =
            createReader("Pikachu\n", new ByteArrayOutputStream());

        check("readName - normal",
            reader.readName("> ").equals("Pikachu"));
    }

    // Normal: "   Pikachu  " -> "Pikachu"
    static void testReadName_trimsSpaces() {
        InputReader reader =
            createReader("   Pikachu  \n", new ByteArrayOutputStream());

        check("readName - trims spaces",
            reader.readName("> ").equals("Pikachu"));
    }

    // Normal: real card names with odd characters are accepted
    static void testReadName_specialCharacters() {
        InputReader reader = createReader(
            "Farfetch'd\nMr. Mime\n", new ByteArrayOutputStream());

        boolean correct =
            reader.readName("> ").equals("Farfetch'd")
            && reader.readName("> ").equals("Mr. Mime");

        check("readName - special characters", correct);
    }

    // Bad input: blank name -> asks again, then "Eevee"
    static void testReadName_blank() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputReader reader = createReader("   \nEevee\n", output);

        boolean correct =
            reader.readName("> ").equals("Eevee")
            && output.toString().contains("Card name can't be empty");

        check("readName - blank", correct);
    }

    // Bad input: 41 characters -> asks again; exactly 40 is fine
    static void testReadName_tooLong() {
        String tooLong = "a".repeat(41);
        String forty = "a".repeat(40);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputReader reader =
            createReader(tooLong + "\n" + forty + "\n", output);

        boolean correct =
            reader.readName("> ").equals(forty)
            && output.toString().contains("40 characters");

        check("readName - too long", correct);
    }

    // Bad input: no input at all (like Ctrl+D) -> NoSuchElementException
    static void testReadName_inputEnds() {
        InputReader reader = createReader("", new ByteArrayOutputStream());

        boolean threw = false;
        try {
            reader.readName("> ");
        }
        catch (NoSuchElementException e) {
            threw = true;
        }

        check("readName - input ends", threw);
    }

    // Normal: "5" -> 5
    static void testReadInt_normal() {
        InputReader reader = createReader("5\n", new ByteArrayOutputStream());

        check("readInt - normal", reader.readInt("> ", 1, 100) == 5);
    }

    // Normal: the smallest and largest allowed values are accepted
    static void testReadInt_edges() {
        InputReader reader =
            createReader("1\n100\n", new ByteArrayOutputStream());

        boolean correct =
            reader.readInt("> ", 1, 100) == 1
            && reader.readInt("> ", 1, 100) == 100;

        check("readInt - edges", correct);
    }

    // Bad input: "abc" then "-3" -> both rejected, then 7
    static void testReadInt_lettersAndNegative() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputReader reader = createReader("abc\n-3\n7\n", output);

        boolean correct =
            reader.readInt("> ", 1, 100) == 7
            && output.toString().contains("Please enter a number.")
            && output.toString().contains("from 1 to 100");

        check("readInt - letters and negative", correct);
    }

    // Bad input: 0, a huge number, and 101 -> all rejected, then 2
    static void testReadInt_zeroAndHuge() {
        InputReader reader = createReader(
            "0\n999999999999\n101\n2\n", new ByteArrayOutputStream());

        check("readInt - zero and huge", reader.readInt("> ", 1, 100) == 2);
    }

    // Bad input: min bigger than max -> IllegalArgumentException
    static void testReadInt_badRange() {
        InputReader reader = createReader("5\n", new ByteArrayOutputStream());

        boolean threw = false;
        try {
            reader.readInt("> ", 10, 1);
        }
        catch (IllegalArgumentException e) {
            threw = true;
        }

        check("readInt - bad range", threw);
    }

    // Bad input: "abc" and then input ends -> NoSuchElementException
    static void testReadInt_inputEnds() {
        InputReader reader =
            createReader("abc\n", new ByteArrayOutputStream());

        boolean threw = false;
        try {
            reader.readInt("> ", 1, 100);
        }
        catch (NoSuchElementException e) {
            threw = true;
        }

        check("readInt - input ends", threw);
    }

    // Normal: "3" -> RARE
    static void testReadRarity_normal() {
        InputReader reader = createReader("3\n", new ByteArrayOutputStream());

        check("readRarity - normal", reader.readRarity() == Rarity.RARE);
    }

    // Bad input: "9" isn't an option -> asks again, then "1" -> COMMON
    static void testReadRarity_outOfRange() {
        InputReader reader =
            createReader("9\n1\n", new ByteArrayOutputStream());

        check("readRarity - out of range",
            reader.readRarity() == Rarity.COMMON);
    }

    // Normal: "2" -> FIRE
    static void testReadEnergyType_normal() {
        InputReader reader = createReader("2\n", new ByteArrayOutputStream());

        check("readEnergyType - normal",
            reader.readEnergyType() == EnergyType.FIRE);
    }

    // Bad input: typing "Fairy" -> asks again, then "3" -> WATER
    static void testReadEnergyType_word() {
        InputReader reader =
            createReader("Fairy\n3\n", new ByteArrayOutputStream());

        check("readEnergyType - typed word",
            reader.readEnergyType() == EnergyType.WATER);
    }

    // Normal: input left -> true
    static void testHasMoreInput_normal() {
        InputReader reader =
            createReader("Pikachu\n", new ByteArrayOutputStream());

        check("hasMoreInput - normal", reader.hasMoreInput());
    }

    // Bad input: no input left (like Ctrl+D) -> false
    static void testHasMoreInput_empty() {
        InputReader reader = createReader("", new ByteArrayOutputStream());

        check("hasMoreInput - empty", !reader.hasMoreInput());
    }

    // Creates an InputReader using fake keyboard input
    static InputReader createReader(String input,
        ByteArrayOutputStream output) {

        return new InputReader(new Scanner(input), new PrintStream(output));
    }

    // Prints PASS or FAIL and keeps track of totals
    static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        }
        else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}