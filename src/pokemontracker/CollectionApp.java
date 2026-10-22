package pokemontracker;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The text menu. Gets input from InputReader, asks CardCollection to
 * do the work, and prints the results. It holds no cards itself.
 *
 * Several users can each have a collection. The users are kept in a
 * LinkedChain of CardCollections, and options 1 to 5 always work on the
 * current user's collection. Option 9 trades cards between the current
 * user and another user, using TradeHelper.
 *
 * @author Fernando Zuniga Leon (zfernando)
 * @author Aryan Patil (users and trading, options 7 to 9)
 * @version 2026.10.22
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
    private static final int ADD_USER = 7;
    private static final int SWITCH_USER = 8;
    private static final int TRADE = 9;

    private CardCollection collection; // the current user's collection
    private LinkedChain<CardCollection> users;
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
        this.users = new LinkedChain<CardCollection>();
        this.users.add(collection);
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
                int choice = input.readInt("Choose an option: ", 1, TRADE);
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
        out.println("Current user: " + collection.getOwnerName());
        out.println("1) Add Pokemon card");
        out.println("2) Add Energy card");
        out.println("3) Remove a card");
        out.println("4) Count a card");
        out.println("5) Show collection");
        out.println("6) Quit");
        out.println("7) Add a new user");
        out.println("8) Switch user");
        out.println("9) Trade cards with another user");
    }

    /**
     * Runs the action for a menu choice.
     *
     * @param choice a number from 1 to 9
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
            case ADD_USER:
                handleAddUser();
                break;
            case SWITCH_USER:
                handleSwitchUser();
                break;
            case TRADE:
                handleTrade();
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
     * Reads a name and adds a new user with an empty collection, then
     * switches to that user. Names must be unique, ignoring case.
     */
    private void handleAddUser()
    {
        String name = input.readName("New user's name: ");
        if (findUser(name) != null)
        {
            out.println("A user named " + name + " already exists.");
            return;
        }
        collection = new CardCollection(name);
        users.add(collection);
        out.println("Added " + name + ". Now using " + name
            + "'s collection.");
    }

    /**
     * Switches the current user to another existing user.
     */
    private void handleSwitchUser()
    {
        if (users.size() < 2)
        {
            out.println("There is only one user. Add another with option "
                + ADD_USER + ".");
            return;
        }
        out.println("Users: " + userNames());
        String name = input.readName("Switch to which user? ");
        CardCollection other = findUser(name);
        if (other == null)
        {
            out.println("No user named " + name + ".");
        }
        else if (other == collection)
        {
            out.println("You are already using " + other.getOwnerName()
                + "'s collection.");
        }
        else
        {
            collection = other;
            out.println("Now using " + other.getOwnerName()
                + "'s collection.");
        }
    }

    /**
     * Trades cards between the current user and another user. Asks what
     * each side gives, then lets TradeHelper do the trade. If either side
     * doesn't own enough, nothing changes.
     */
    private void handleTrade()
    {
        if (users.size() < 2)
        {
            out.println("You need at least two users to trade. Add one "
                + "with option " + ADD_USER + ".");
            return;
        }
        out.println("Users: " + userNames());
        String name = input.readName("Trade with which user? ");
        CardCollection other = findUser(name);
        if (other == null)
        {
            out.println("No user named " + name + ".");
            return;
        }
        if (other == collection)
        {
            out.println("You can't trade with yourself.");
            return;
        }

        String me = collection.getOwnerName();
        String them = other.getOwnerName();
        TradeOffer myOffer = readOffer(me);
        TradeOffer theirOffer = readOffer(them);

        if (TradeHelper.trade(collection, myOffer, other, theirOffer))
        {
            out.println("Trade complete: " + me + " gave " + myOffer
                + " to " + them + ", and " + them + " gave " + theirOffer
                + " to " + me + ".");
        }
        else
        {
            printShortfall(me, collection, myOffer);
            printShortfall(them, other, theirOffer);
            out.println("No cards were traded.");
        }
    }

    /**
     * Asks what one side of a trade gives.
     *
     * @param owner the name of the user giving the cards
     * @return what they give
     */
    private TradeOffer readOffer(String owner)
    {
        out.println("What does " + owner + " give?");
        if (askPokemonOrEnergy("give") == 1)
        {
            String name = input.readName("Card name: ");
            int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
            return TradeOffer.pokemon(name, quantity);
        }
        EnergyType type = input.readEnergyType();
        int quantity = input.readInt("How many? ", 1, MAX_QUANTITY);
        return TradeOffer.energy(type, quantity);
    }

    /**
     * Prints a message if a user doesn't own enough of what they offered.
     *
     * @param owner the user's name
     * @param owned the user's collection
     * @param offer what they offered
     */
    private void printShortfall(String owner, CardCollection owned,
        TradeOffer offer)
    {
        int count = offer.countOwnedBy(owned);
        if (count < offer.getQuantity())
        {
            String card = offer.isPokemon()
                ? offer.getPokemonName()
                : new EnergyCard(offer.getEnergyType()).toString();
            out.println(owner + " only has " + count + " " + card + ".");
        }
    }

    /**
     * Finds a user by name, ignoring case and extra spaces.
     *
     * @param name the name to look for
     * @return that user's collection, or null if there is no such user
     */
    private CardCollection findUser(String name)
    {
        CardCollection[] all =
            users.toArray(new CardCollection[users.size()]);
        for (CardCollection user : all)
        {
            if (user.getOwnerName().equalsIgnoreCase(name.trim()))
            {
                return user;
            }
        }
        return null;
    }

    /**
     * Lists every user's name, like "Sam, Aryan".
     *
     * @return the names separated by commas
     */
    private String userNames()
    {
        CardCollection[] all =
            users.toArray(new CardCollection[users.size()]);
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < all.length; i++)
        {
            if (i > 0)
            {
                names.append(", ");
            }
            names.append(all[i].getOwnerName());
        }
        return names.toString();
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