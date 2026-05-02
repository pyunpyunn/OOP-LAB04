// Mage extends GameCharacter and implements CasterAbility
public class Mage extends GameCharacter implements CasterAbility {

    private int mana;

    // Constructor
    public Mage(String name, int hp, int attackPower, int mana) {
        super(name, hp, attackPower);
        this.mana = mana;
    }

    @Override
    public void attack() {
        System.out.println(getName() + " shoots a magic missile and deals " + getAttackPower() + " damage!");
    }

    @Override
    public String describeClass() {
        return "Mage    | Name: " + getName() + " | HP: " + getHp() + " | Attack: " + getAttackPower() + " | Mana: " + mana;
    }

    @Override
    public void castSpell(String spellName) {
        // Deduct mana - display is handled by Main
        if (mana >= 20) {
            mana = mana - 20;
        }
    }

    @Override
    public int getMana() {
        return mana;
    }

    // Getter and setter
    public void setMana(int mana) {
        this.mana = mana;
    }
}
