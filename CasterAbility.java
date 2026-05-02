/**
 * Interface for characters that can cast spells using mana.
 * Any class that implements this must have castSpell() and getMana().
 */
public interface CasterAbility {

    void castSpell(String spellName);
    int getMana();
}
