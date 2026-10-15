package pokemontracker;

/**
 * One Energy card. Two Energy cards are the same if their types match.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public class EnergyCard
{
    private final EnergyType type;

    /**
     * Creates an Energy card.
     *
     * @param type the energy type
     * @throws IllegalArgumentException if type is null
     */
    public EnergyCard(EnergyType type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Energy type can't be null");
        }
        this.type = type;
    }

    /**
     * Gets the energy type.
     *
     * @return the type
     */
    public EnergyType getType()
    {
        return type;
    }

    /**
     * Two Energy cards are equal if their types match.
     *
     * @param other the object to compare with
     * @return true if other is an EnergyCard with the same type
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof EnergyCard))
        {
            return false;
        }
        return type == ((EnergyCard)other).type;
    }

    /**
     * Based on the type, so cards that are equal have the same hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return type.hashCode();
    }

    /**
     * Returns the card like "Fire Energy".
     *
     * @return the type followed by "Energy"
     */
    @Override
    public String toString()
    {
        return type + " Energy";
    }
}
