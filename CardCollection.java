package pokemontracker;

/**
 * Represents one person's Pokémon and Energy card collection.
 * Uses LinkedChain to store individual physical cards.
 *
 * @author Golden You
 * @version 2026.10.14
 */
public class CardCollection
{
    private String ownerName;
    private LinkedChain<PokemonCard> pokemonCards;
    private LinkedChain<EnergyCard> energyCards;

    /**
     * Creates an empty collection for the specified owner.
     *
     * @param ownerName the collection owner's name
     */
    public CardCollection(String ownerName)
    {
        if (ownerName == null || ownerName.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Owner name cannot be empty.");
        }

        this.ownerName = ownerName.trim();
        pokemonCards = new LinkedChain<PokemonCard>();
        energyCards = new LinkedChain<EnergyCard>();
    }

    /**
     * Adds copies of a Pokémon card.
     *
     * @param card the Pokémon card to add
     * @param quantity the number of copies to add
     * @throws IllegalArgumentException if card is null
     *         or quantity is less than 1
     */
    public void addPokemon(PokemonCard card, int quantity)
    {
        if (card == null)
        {
            throw new IllegalArgumentException(
                "Pokémon card cannot be null.");
        }

        if (quantity < 1)
        {
            throw new IllegalArgumentException(
                "Quantity must be at least 1.");
        }

        for (int i = 0; i < quantity; i++)
        {
            pokemonCards.add(card);
        }
    }

    /**
     * Adds copies of an Energy card.
     *
     * @param card the Energy card to add
     * @param quantity the number of copies to add
     * @throws IllegalArgumentException if card is null
     *         or quantity is less than 1
     */
    public void addEnergy(EnergyCard card, int quantity)
    {
        if (card == null)
        {
            throw new IllegalArgumentException(
                "Energy card cannot be null.");
        }

        if (quantity < 1)
        {
            throw new IllegalArgumentException(
                "Quantity must be at least 1.");
        }

        for (int i = 0; i < quantity; i++)
        {
            energyCards.add(card);
        }
    }

    /**
     * Removes copies of a Pokémon by name.
     * If there are not enough copies, nothing is removed.
     *
     * @param name the Pokémon name
     * @param quantity the number of copies to remove
     * @return true if all requested copies were removed;
     *         false otherwise
     * @throws IllegalArgumentException if name is null or blank,
     *         or quantity is less than 1
     */
    public boolean removePokemon(String name, int quantity)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Pokémon name cannot be empty.");
        }

        if (quantity < 1)
        {
            throw new IllegalArgumentException(
                "Quantity must be at least 1.");
        }

        // Rarity does not matter when matching Pokémon cards.
        PokemonCard target =
            new PokemonCard(name, Rarity.COMMON);

        // Check first so an unsuccessful removal changes nothing.
        if (pokemonCards.count(target) < quantity)
        {
            return false;
        }

        for (int i = 0; i < quantity; i++)
        {
            pokemonCards.remove(target);
        }

        return true;
    }

    /**
     * Removes copies of an Energy type.
     * If there are not enough copies, nothing is removed.
     *
     * @param type the Energy type
     * @param quantity the number of copies to remove
     * @return true if all requested copies were removed;
     *         false otherwise
     * @throws IllegalArgumentException if type is null
     *         or quantity is less than 1
     */
    public boolean removeEnergy(EnergyType type, int quantity)
    {
        if (type == null)
        {
            throw new IllegalArgumentException(
                "Energy type cannot be null.");
        }

        if (quantity < 1)
        {
            throw new IllegalArgumentException(
                "Quantity must be at least 1.");
        }

        EnergyCard target = new EnergyCard(type);

        // Check first so an unsuccessful removal changes nothing.
        if (energyCards.count(target) < quantity)
        {
            return false;
        }

        for (int i = 0; i < quantity; i++)
        {
            energyCards.remove(target);
        }

        return true;
    }

    /**
     * Counts copies of a Pokémon, regardless of rarity.
     *
     * @param name the Pokémon name
     * @return the number of copies owned, or 0 for a null
     *         or blank name
     */
    public int countPokemon(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            return 0;
        }

        PokemonCard target =
            new PokemonCard(name, Rarity.COMMON);

        return pokemonCards.count(target);
    }

    /**
     * Counts copies of an Energy type.
     *
     * @param type the Energy type
     * @return the number of copies owned, or 0 if type is null
     */
    public int countEnergy(EnergyType type)
    {
        if (type == null)
        {
            return 0;
        }

        EnergyCard target = new EnergyCard(type);

        return energyCards.count(target);
    }

    /**
     * Gets the total number of physical cards.
     *
     * @return the total number of Pokémon and Energy cards
     */
    public int totalCards()
    {
        return pokemonCards.size() + energyCards.size();
    }

    /**
     * Checks whether the collection is empty.
     *
     * @return true if both chains are empty
     */
    public boolean isEmpty()
    {
        return pokemonCards.isEmpty()
            && energyCards.isEmpty();
    }

    /**
     * Returns a readable representation of the collection.
     * Pokémon cards are listed before Energy cards.
     *
     * @return the collection listing, or an empty message
     */
    @Override
    public String toString()
    {
        if (isEmpty())
        {
            return ownerName + "'s collection is empty.";
        }

        return ownerName + "'s collection:\n"
            + "Pokémon cards: " + pokemonCards.toString() + "\n"
            + "Energy cards: " + energyCards.toString();
    }
}