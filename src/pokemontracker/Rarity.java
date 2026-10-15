package pokemontracker;

/**
 * The fixed list of rarities a Pokemon card can have. InputReader shows
 * these as a numbered list in this order, so the order matters.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public enum Rarity
{
    /** The most common cards in a pack. */
    COMMON("Common"),
    /** Less common than COMMON. */
    UNCOMMON("Uncommon"),
    /** Usually one per pack. */
    RARE("Rare"),
    /** A rare card with a holographic picture. */
    HOLO_RARE("Holo Rare");

    private final String displayName;

    /**
     * Creates a rarity with a readable name.
     *
     * @param displayName the name shown to the user
     */
    Rarity(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Returns the readable name, like "Holo Rare".
     *
     * @return the display name
     */
    @Override
    public String toString()
    {
        return displayName;
    }
}
