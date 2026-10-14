package pokemontracker;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Reads and validates everything the user types. Every method keeps
 * asking until the input is valid, so bad input never reaches the
 * rest of the program.
 *
 * Every value is read with nextLine() and then parsed. This avoids the
 * Scanner problem where nextInt() leaves the newline behind and the
 * next read gets skipped.
 *
 * If the input ends (the user presses Ctrl+D), the read methods throw
 * NoSuchElementException. CollectionApp catches it and exits cleanly.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class InputReader
{
    /** Longest card name we accept, after trimming. */
    public static final int MAX_NAME_LENGTH = 40;

    private Scanner scanner;
    private PrintStream out;

    /**
     * Creates a reader that prints prompts to System.out.
     *
     * @param scanner where input comes from
     */
    public InputReader(Scanner scanner)
    {
        this(scanner, System.out);
    }

    /**
     * Creates a reader that prints prompts to the given stream.
     * Tests use this so they can check what was printed.
     *
     * @param scanner where input comes from
     * @param out where prompts and error messages go
     */
    public InputReader(Scanner scanner, PrintStream out)
    {
        if (scanner == null || out == null)
        {
            throw new IllegalArgumentException(
                "Scanner and output must not be null");
        }
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * Checks whether there is more input to read.
     *
     * @return false once input has ended (Ctrl+D), true otherwise
     */
    public boolean hasMoreInput()
    {
        return scanner.hasNextLine();
    }

    /**
     * Reads a card name. Re-prompts until it is 1 to 40 characters
     * after trimming the spaces off both ends.
     *
     * @param prompt the text to show the user
     * @return the trimmed name
     * @throws NoSuchElementException if input ends
     */
    public String readName(String prompt)
    {
        while (true)
        {
            String name = readLine(prompt).trim();
            if (name.isEmpty())
            {
                out.println("Card name can't be empty.");
            }
            else if (name.length() > MAX_NAME_LENGTH)
            {
                out.println("Card name can't be longer than "
                    + MAX_NAME_LENGTH + " characters.");
            }
            else
            {
                return name;
            }
        }
    }

    /**
     * Reads a whole number between min and max (inclusive).
     * Re-prompts if the input isn't a number or is out of range.
     *
     * @param prompt the text to show the user
     * @param min the smallest allowed value
     * @param max the largest allowed value
     * @return the number the user typed
     * @throws IllegalArgumentException if min is greater than max
     * @throws NoSuchElementException if input ends
     */
    public int readInt(String prompt, int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("min must be <= max");
        }
        while (true)
        {
            String line = readLine(prompt).trim();
            try
            {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max)
                {
                    return value;
                }
                out.println("Please enter a number from " + min
                    + " to " + max + ".");
            }
            catch (NumberFormatException e)
            {
                out.println("Please enter a number.");
            }
        }
    }

    /**
     * Shows the rarities as a numbered list and returns the user's pick.
     *
     * @return the chosen rarity
     * @throws NoSuchElementException if input ends
     */
    public Rarity readRarity()
    {
        Rarity[] options = Rarity.values();
        out.println("Rarity:");
        printOptions(options);
        int choice = readInt("Pick a rarity: ", 1, options.length);
        return options[choice - 1];
    }

    /**
     * Shows the energy types as a numbered list and returns the pick.
     *
     * @return the chosen energy type
     * @throws NoSuchElementException if input ends
     */
    public EnergyType readEnergyType()
    {
        EnergyType[] options = EnergyType.values();
        out.println("Energy type:");
        printOptions(options);
        int choice = readInt("Pick an energy type: ", 1, options.length);
        return options[choice - 1];
    }

    /**
     * Prints a prompt and reads one line.
     *
     * @param prompt the text to show
     * @return the line the user typed
     * @throws NoSuchElementException if input ends
     */
    private String readLine(String prompt)
    {
        out.print(prompt);
        if (!scanner.hasNextLine())
        {
            throw new NoSuchElementException("Input ended");
        }
        return scanner.nextLine();
    }

    /**
     * Prints enum constants as a numbered list starting at 1.
     *
     * @param options the choices to print
     */
    private void printOptions(Object[] options)
    {
        for (int i = 0; i < options.length; i++)
        {
            out.println("  " + (i + 1) + ") " + options[i]);
        }
    }
}