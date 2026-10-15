package pokemontracker;

/**
 * The fixed list of Energy types. InputReader shows these as a numbered
 * list in this order, so the order matters.
 *
 * @author Aryan Patil
 * @version 2026.10.15
 */
public enum EnergyType
{
    /** Grass Energy. */
    GRASS("Grass"),
    /** Fire Energy. */
    FIRE("Fire"),
    /** Water Energy. */
    WATER("Water"),
    /** Lightning Energy. */
    LIGHTNING("Lightning"),
    /** Psychic Energy. */
    PSYCHIC("Psychic"),
    /** Fighting Energy. */
    FIGHTING("Fighting"),
    /** Darkness Energy. */
    DARKNESS("Darkness"),
    /** Metal Energy. */
    METAL("Metal");

    private final String displayName;

    /**
     * Creates an energy type with a readable name.
     *
     * @param displayName the name shown to the user
     */
    EnergyType(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Returns the readable name, like "Fire".
     *
     * @return the display name
     */
    @Override
    public String toString()
    {
        return displayName;
    }
}
