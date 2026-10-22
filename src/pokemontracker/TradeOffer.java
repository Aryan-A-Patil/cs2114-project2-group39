package pokemontracker;

/**
 * What one side of a trade gives: either some copies of a Pokemon card
 * (by name) or some copies of an Energy card (by type). Create one with
 * TradeOffer.pokemon(...) or TradeOffer.energy(...).
 *
 * @author Aryan Patil
 * @version 2026.10.22
 */
public class TradeOffer
{
    private final String pokemonName;   // null for an Energy offer
    private final EnergyType energyType; // null for a Pokemon offer
    private final int quantity;

    /**
     * Creates an offer. Use the pokemon and energy factory methods.
     *
     * @param pokemonName the Pokemon name, or null
     * @param energyType the energy type, or null
     * @param quantity how many copies
     */
    private TradeOffer(String pokemonName, EnergyType energyType,
        int quantity)
    {
        if (quantity < 1)
        {
            throw new IllegalArgumentException(
                "Quantity must be at least 1.");
        }
        this.pokemonName = pokemonName;
        this.energyType = energyType;
        this.quantity = quantity;
    }

    /**
     * Creates an offer of Pokemon cards. Rarity is not part of the
     * offer, the same way it isn't when removing a card.
     *
     * @param name the Pokemon name; trimmed
     * @param quantity how many copies
     * @return the offer
     * @throws IllegalArgumentException if name is null or blank, or
     *         quantity is less than 1
     */
    public static TradeOffer pokemon(String name, int quantity)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Card name can't be empty.");
        }
        return new TradeOffer(name.trim(), null, quantity);
    }

    /**
     * Creates an offer of Energy cards.
     *
     * @param type the energy type
     * @param quantity how many copies
     * @return the offer
     * @throws IllegalArgumentException if type is null or quantity is
     *         less than 1
     */
    public static TradeOffer energy(EnergyType type, int quantity)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Energy type can't be null.");
        }
        return new TradeOffer(null, type, quantity);
    }

    /**
     * Checks whether this offer is for Pokemon cards.
     *
     * @return true for Pokemon cards, false for Energy cards
     */
    public boolean isPokemon()
    {
        return pokemonName != null;
    }

    /**
     * Gets the Pokemon name.
     *
     * @return the name, or null for an Energy offer
     */
    public String getPokemonName()
    {
        return pokemonName;
    }

    /**
     * Gets the energy type.
     *
     * @return the type, or null for a Pokemon offer
     */
    public EnergyType getEnergyType()
    {
        return energyType;
    }

    /**
     * Gets how many copies are offered.
     *
     * @return the quantity, at least 1
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Counts how many of the offered card a collection owns.
     *
     * @param collection the collection to check
     * @return how many copies it owns
     * @throws IllegalArgumentException if collection is null
     */
    public int countOwnedBy(CardCollection collection)
    {
        if (collection == null)
        {
            throw new IllegalArgumentException(
                "Collection can't be null.");
        }
        if (isPokemon())
        {
            return collection.countPokemon(pokemonName);
        }
        return collection.countEnergy(energyType);
    }

    /**
     * Returns the offer like "2 Pikachu" or "3 Fire Energy".
     *
     * @return the quantity and card
     */
    @Override
    public String toString()
    {
        if (isPokemon())
        {
            return quantity + " " + pokemonName;
        }
        return quantity + " " + new EnergyCard(energyType);
    }
}
