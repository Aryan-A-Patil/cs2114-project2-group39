package pokemontracker;

/**
 * One Pokemon card: a name and a rarity. Two cards count as the same
 * card if their names match, ignoring capitalization. Rarity does not
 * matter for equality, so the user only has to type a name to count or
 * remove a card.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public class PokemonCard
{
    private final String name;
    private final Rarity rarity;

    /**
     * Creates a Pokemon card. The name is trimmed, so " Pikachu " is
     * stored as "Pikachu".
     *
     * @param name the card's name
     * @param rarity the card's rarity
     * @throws IllegalArgumentException if name is null or blank, or
     *         rarity is null
     */
    public PokemonCard(String name, Rarity rarity)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Card name can't be empty");
        }
        if (rarity == null)
        {
            throw new IllegalArgumentException("Rarity can't be null");
        }
        this.name = name.trim();
        this.rarity = rarity;
    }

    /**
     * Gets the card's name.
     *
     * @return the trimmed name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the card's rarity.
     *
     * @return the rarity
     */
    public Rarity getRarity()
    {
        return rarity;
    }

    /**
     * Two Pokemon cards are equal if their names match, ignoring case.
     *
     * @param other the object to compare with
     * @return true if other is a PokemonCard with the same name
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof PokemonCard))
        {
            return false;
        }
        return name.equalsIgnoreCase(((PokemonCard)other).name);
    }

    /**
     * Based on the lowercase name, so cards that are equal have the same
     * hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return name.toLowerCase().hashCode();
    }

    /**
     * Returns the card like "Pikachu (Rare)".
     *
     * @return the name and rarity
     */
    @Override
    public String toString()
    {
        return name + " (" + rarity + ")";
    }
}
