package pokemontracker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This is the test class for CollectionApp. Each test runs the whole
 * menu with fake keyboard input, then checks what was printed and what
 * ended up in the collection.
 *
 * Menu: 1 add Pokemon, 2 add Energy, 3 remove, 4 count, 5 show, 6 quit.
 * Rarity list: 1 Common ... 3 Rare. Energy list: 1 Grass, 2 Fire, 3 Water.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class CollectionAppTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testConstructor_normal();
        testConstructor_null();
        testRun_quit();
        testRun_addAndCountPokemon();
        testRun_differentCapitalization();
        testRun_addAndCountEnergy();
        testRun_removePokemon();
        testRun_removeEnergy();
        testRun_showAll();
        testRun_removeFromEmpty();
        testRun_removeMoreThanOwned();
        testRun_removeCardNotOwned();
        testRun_badMenuChoices();
        testRun_badQuantities();
        testRun_blankName();
        testRun_badRarityAndEnergy();
        testRun_inputEndsSuddenly();
        testRun_emptyInput();

        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }

    // Normal: constructor builds an app
    static void testConstructor_normal() {
        CardCollection collection = new CardCollection("Sam");
        InputReader reader = new InputReader(new Scanner("6\n"));

        CollectionApp app = new CollectionApp(collection, reader);

        check("constructor - normal", app != null);
    }

    // Bad input: null collection or null reader -> IllegalArgumentException
    static void testConstructor_null() {
        InputReader reader = new InputReader(new Scanner(""));

        boolean nullCollectionThrew = false;
        try {
            new CollectionApp(null, reader);
        }
        catch (IllegalArgumentException e) {
            nullCollectionThrew = true;
        }

        boolean nullReaderThrew = false;
        try {
            new CollectionApp(new CardCollection("Sam"), null);
        }
        catch (IllegalArgumentException e) {
            nullReaderThrew = true;
        }

        check("constructor - null",
            nullCollectionThrew && nullReaderThrew);
    }

    // Normal: choose 6 -> program quits
    static void testRun_quit() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "6\n");

        check("run - quit", output.contains("Goodbye!"));
    }

    // Normal: add 3 Rare Pikachu, then count Pikachu -> 3
    static void testRun_addAndCountPokemon() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection,
            "1\nPikachu\n3\n3\n4\n1\nPikachu\n6\n");

        boolean correct =
            collection.countPokemon("Pikachu") == 3
            && output.contains("You have 3 Pikachu.");

        check("run - add and count Pokemon", correct);
    }

    // Normal: "Pikachu" and " pikachu " count as the same card
    static void testRun_differentCapitalization() {
        CardCollection collection = new CardCollection("Sam");

        runWithInput(collection, "1\nPikachu\n1\n2\n1\n pikachu \n1\n1\n6\n");

        check("run - different capitalization",
            collection.countPokemon("PIKACHU") == 3);
    }

    // Normal: add 10 Fire Energy, then count Fire -> 10
    static void testRun_addAndCountEnergy() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "2\n2\n10\n4\n2\n2\n6\n");

        boolean correct =
            collection.countEnergy(EnergyType.FIRE) == 10
            && output.contains("You have 10");

        check("run - add and count Energy", correct);
    }

    // Normal: own 3 Eevee, remove 2 -> 1 left
    static void testRun_removePokemon() {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 3);

        String output = runWithInput(collection, "3\n1\nEevee\n2\n6\n");

        boolean correct =
            collection.countPokemon("Eevee") == 1
            && output.contains("Removed 2 Eevee");

        check("run - remove Pokemon", correct);
    }

    // Normal: own 5 Water Energy, remove 5 -> 0 left
    static void testRun_removeEnergy() {
        CardCollection collection = new CardCollection("Sam");
        collection.addEnergy(new EnergyCard(EnergyType.WATER), 5);

        runWithInput(collection, "3\n2\n3\n5\n6\n");

        check("run - remove Energy",
            collection.countEnergy(EnergyType.WATER) == 0);
    }

    // Normal: show collection prints the cards
    static void testRun_showAll() {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Eevee", Rarity.COMMON), 1);

        String output = runWithInput(collection, "5\n6\n");

        check("run - show all", output.contains("Eevee"));
    }

    // Bad input: remove from an empty collection -> message, no crash
    static void testRun_removeFromEmpty() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "3\n6\n");

        check("run - remove from empty",
            output.contains("Your collection is empty."));
    }

    // Bad input: own 2 Pikachu, try to remove 5 -> nothing removed
    static void testRun_removeMoreThanOwned() {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 2);

        String output = runWithInput(collection, "3\n1\nPikachu\n5\n6\n");

        boolean correct =
            collection.countPokemon("Pikachu") == 2
            && output.contains("You only have 2 Pikachu.");

        check("run - remove more than owned", correct);
    }

    // Bad input: remove Mewtwo when we only own Pikachu
    static void testRun_removeCardNotOwned() {
        CardCollection collection = new CardCollection("Sam");
        collection.addPokemon(new PokemonCard("Pikachu", Rarity.RARE), 1);

        String output = runWithInput(collection, "3\n1\nMewtwo\n1\n6\n");

        boolean correct =
            collection.countPokemon("Pikachu") == 1
            && output.contains("You only have 0 Mewtwo.");

        check("run - remove card not owned", correct);
    }

    // Bad input: menu choices 9, 0, and "abc" -> rejected, then 6 quits
    static void testRun_badMenuChoices() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "9\n0\nabc\n6\n");

        boolean correct =
            output.contains("Please enter a number from 1 to 6.")
            && output.contains("Please enter a number.")
            && output.contains("Goodbye!");

        check("run - bad menu choices", correct);
    }

    // Bad input: quantities -2, 0, "abc" -> rejected, then 4 is added
    static void testRun_badQuantities() {
        CardCollection collection = new CardCollection("Sam");

        runWithInput(collection, "1\nPikachu\n1\n-2\n0\nabc\n4\n6\n");

        check("run - bad quantities",
            collection.countPokemon("Pikachu") == 4);
    }

    // Bad input: blank name -> rejected, then "Pikachu" is added
    static void testRun_blankName() {
        CardCollection collection = new CardCollection("Sam");

        String output =
            runWithInput(collection, "1\n   \nPikachu\n1\n1\n6\n");

        boolean correct =
            output.contains("Card name can't be empty.")
            && collection.countPokemon("Pikachu") == 1;

        check("run - blank name", correct);
    }

    // Bad input: rarity 7 and energy "Fairy" -> rejected, then valid picks
    static void testRun_badRarityAndEnergy() {
        CardCollection collection = new CardCollection("Sam");

        runWithInput(collection,
            "1\nPikachu\n7\n1\n1\n2\nFairy\n1\n2\n6\n");

        boolean correct =
            collection.countPokemon("Pikachu") == 1
            && collection.countEnergy(EnergyType.GRASS) == 2;

        check("run - bad rarity and energy", correct);
    }

    // Bad input: input stops in the middle of adding a card (like Ctrl+D)
    static void testRun_inputEndsSuddenly() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "1\nPikachu\n");

        boolean correct =
            output.contains("Goodbye!")
            && collection.countPokemon("Pikachu") == 0;

        check("run - input ends suddenly", correct);
    }

    // Bad input: no input at all -> exits cleanly
    static void testRun_emptyInput() {
        CardCollection collection = new CardCollection("Sam");

        String output = runWithInput(collection, "");

        check("run - empty input", output.contains("Goodbye!"));
    }

    // Runs the menu using fake keyboard input and returns what it printed
    static String runWithInput(CardCollection collection, String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        InputReader reader = new InputReader(new Scanner(input), out);
        CollectionApp app = new CollectionApp(collection, reader, out);

        app.run();

        return output.toString();
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