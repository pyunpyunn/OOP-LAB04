/**
 * Interface for characters that can block attacks.
 * Any class that implements this must have block() and getDefenseRating().
 */
public interface Defendable {

    void block();
    int getDefenseRating();
}
