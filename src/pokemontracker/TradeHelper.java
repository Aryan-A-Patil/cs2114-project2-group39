package pokemontracker;

/**
 * Performs trades between two CardCollections (our stretch goal).
 * A trade only happens if both sides own enough of what they offer.
 * Both sides are checked before anything moves, so a failed trade
 * changes nothing.
 *
 * Pokemon cards keep their rarity when they change hands.
 *
 * @author Aryan Patil
 * @version 2026.10.22
 */
public class TradeHelper
{
    /**
     * No objects needed; every method is static.
     */
    private TradeHelper()
    {
        // not used
    }

    /**
     * Trades cards between two collections: first gives firstGives to
     * second, and second gives secondGives to first.
     *
     * Big-O: O((q1 + q2) * n), where q1 and q2 are the two quantities
     * and n is the number of cards in the larger chain.
     *
     * @param first one collection
     * @param firstGives what first gives
     * @param second the other collection
     * @param secondGives what second gives
     * @return true if the trade happened, false if either side doesn't
     *         own enough (then nothing changes)
     * @throws IllegalArgumentException if any argument is null, or
     *         first and second are the same collection
     */
    public static boolean trade(CardCollection first, TradeOffer firstGives,
        CardCollection second, TradeOffer secondGives)
    {
        if (first == null || firstGives == null
            || second == null || secondGives == null)
        {
            throw new IllegalArgumentException(
                "Trade arguments can't be null.");
        }
        if (first == second)
        {
            throw new IllegalArgumentException(
                "You can't trade with yourself.");
        }

        // Check both sides before moving anything.
        if (firstGives.countOwnedBy(first) < firstGives.getQuantity()
            || secondGives.countOwnedBy(second) < secondGives.getQuantity())
        {
            return false;
        }

        moveCards(first, second, firstGives);
        moveCards(second, first, secondGives);
        return true;
    }

    /**
     * Moves the offered cards from one collection to another. The caller
     * has already checked that from owns enough.
     *
     * @param from the collection giving the cards
     * @param to the collection receiving them
     * @param offer what is moved
     */
    private static void moveCards(CardCollection from, CardCollection to,
        TradeOffer offer)
    {
        if (offer.isPokemon())
        {
            PokemonCard[] cards =
                from.takePokemon(offer.getPokemonName(), offer.getQuantity());
            for (PokemonCard card : cards)
            {
                to.addPokemon(card, 1);
            }
        }
        else
        {
            from.removeEnergy(offer.getEnergyType(), offer.getQuantity());
            to.addEnergy(new EnergyCard(offer.getEnergyType()),
                offer.getQuantity());
        }
    }
}
