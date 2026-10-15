package pokemontracker;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The text menu. Gets input from InputReader, asks CardCollection to
 * do the work, and prints the results. It holds no cards itself.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @version 2026.10.14
 */
public class CollectionApp
{
    /** Largest quantity accepted in one add or remove. */
    public static final int MAX_QUANTITY = 100;

    private static final int ADD_POKEMON = 1;
    private static final int ADD_ENERGY = 2;
    private static final int REMOVE = 3;
    private static final int COUNT = 4;
    private static final int SHOW_ALL = 5;
    private static final int QUIT = 6;

    private CardCollection collection;
    private InputReader input;
    private PrintStream out;
    private boolean running;

    /**
     * Creates the app, printing to System.out.
     *
     * @param collection the collection being edited
     * @param input where all keyboard input comes from
     */
    public CollectionApp(CardCollection collection, InputReader input)
    {
        this(collection, input, System.out);
    }

    /**
     * Creates the app, printing to the given stream. Tests use this.
     *
     * @param collection the collection being edited
     * @param input where all keyboard input comes from
     * @param out where results are printed
     */
    public CollectionApp(CardCollection collection, InputReader input,
        PrintStream out)
    {
        if (collection == null || input == null || out == null)
        {
            throw new IllegalArgumentException(
                "Collection, input, and output must not be null");
        }
        this.collection = collection;
        this.input = input;
        this.out = out;
        this.running = false;
    }

    /**
     * Starts the program: asks for the owner's name, then runs the menu.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        InputReader reader = new InputReader(new Scanner(System.in));
        try
        {
            String owner = reader.readName("What's your name? ");
            new CollectionApp(new CardCollection(owner), reader).run();
        }
        catch (NoSuchElementException e)
        {
            System.out.println();
            System.out.println("Goodbye!");
        }
    }

    /**
     * Shows the menu and handles choices until the user quits or the
     * input ends.
     */
    public void run()
    {
        running = true;
        try
        {
            while (running)
            {
                printMenu();
                int choice = input.readInt("Choose an option: ", 1, QUIT);
                handleChoice(choice);
            }
        }
        catch (NoSuchElementException e)
        {
            out.println();
            running = false;
        }
        out.println("Goodbye!");
    }

    /**
     * Prints the main menu.
     */
    private void printMenu()
    {
        out.println();
        out.println("1) Add Pokemon card");
        out.println("2) Add Energy card");
        out.println("3) Remove a card");
        out.println("4) Count a card");
        out.println("5) Show collection");
        out.println("6) Quit");
    }

    /**
     * Runs the action for a menu choice.
     *
     * @param choice a number from 1 to 6
     */
    private void handleChoice(int choice)
    {
        switch (choice)
        {
            case ADD_POKEMON:
                handleAddPokemon();
                break;
            case ADD_ENERGY:
                handleAddEnergy();
                break;
            case REMOVE:
                handleRemove();
                break;
            case COUNT:
                handleCount();
                break;
            case SHOW_ALL:
                handleShowAll();
                break;
            default:
                running = false;
                break;
        }
    }

    /**
     * Reads a name, rarity, and quantity, then adds the Pokemon cards.
     */
    private void handleAddPokemon()
    {
        String name = input.readName("Card name: ");
        Rarity rarity = input.readRarity();
        int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
        PokemonCard card = new PokemonCard(name, rarity);
        collection.addPokemon(card, quantity);
        out.println("Added " + quantity + " " + card + ". You now have "
            + collection.countPokemon(name) + ".");
    }

    /**
     * Reads an energy type and quantity, then adds the Energy cards.
     */
    private void handleAddEnergy()
    {
        EnergyType type = input.readEnergyType();
        int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
        EnergyCard card = new EnergyCard(type);
        collection.addEnergy(card, quantity);
        out.println("Added " + quantity + " " + card + ". You now have "
            + collection.countEnergy(type) + ".");
    }

    /**
     * Asks whether to remove a Pokemon or Energy card, then removes it.
     * Nothing is removed if the user asks for more than they own.
     */
    private void handleRemove()
    {
        if (collection.isEmpty())
        {
            out.println("Your collection is empty.");
            return;
        }
        if (askPokemonOrEnergy("remove") == 1)
        {
            String name = input.readName("Card name: ");
            int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
            if (collection.removePokemon(name, quantity))
            {
                out.println("Removed " + quantity + " " + name
                    + ". You now have " + collection.countPokemon(name)
                    + ".");
            }
            else
            {
                out.println("You only have "
                    + collection.countPokemon(name) + " " + name + ".");
            }
        }
        else
        {
            EnergyType type = input.readEnergyType();
            int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
            EnergyCard card = new EnergyCard(type);
            if (collection.removeEnergy(type, quantity))
            {
                out.println("Removed " + quantity + " " + card
                    + ". You now have " + collection.countEnergy(type)
                    + ".");
            }
            else
            {
                out.println("You only have " + collection.countEnergy(type)
                    + " " + card + ".");
            }
        }
    }

    /**
     * Asks whether to count a Pokemon or Energy card, then prints it.
     */
    private void handleCount()
    {
        if (askPokemonOrEnergy("count") == 1)
        {
            String name = input.readName("Card name: ");
            out.println("You have " + collection.countPokemon(name) + " "
                + name + ".");
        }
        else
        {
            EnergyType type = input.readEnergyType();
            out.println("You have " + collection.countEnergy(type) + " "
                + new EnergyCard(type) + ".");
        }
    }

    /**
     * Prints the whole collection.
     */
    private void handleShowAll()
    {
        out.println(collection);
    }

    /**
     * Asks whether the action is for a Pokemon card or an Energy card.
     *
     * @param action the word to show, like "remove" or "count"
     * @return 1 for Pokemon, 2 for Energy
     */
    private int askPokemonOrEnergy(String action)
    {
        out.println("What do you want to " + action + "?");
        out.println("  1) Pokemon card");
        out.println("  2) Energy card");
        return input.readInt("Pick one: ", 1, 2);
    }
}