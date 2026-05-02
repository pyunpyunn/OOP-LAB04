// Warrior extends GameCharacter and implements Defendable
public class Warrior extends GameCharacter implements Defendable {

    private int armorRating;

    // Constructor
    public Warrior(String name, int hp, int attackPower, int armorRating) {
        super(name, hp, attackPower);
        this.armorRating = armorRating;
    }

    @Override
    public void attack() {
        System.out.println(getName() + " swings a sword and deals " + getAttackPower() + " damage!");
    }

    @Override
    public String describeClass() {
        return "Warrior | Name: " + getName() + " | HP: " + getHp() + " | Attack: " + getAttackPower() + " | Armor: " + armorRating;
    }

    @Override
    public void block() {
        System.out.println(getName() + " raises their shield and blocks the attack!");
    }

    @Override
    public int getDefenseRating() {
        return armorRating;
    }

    // Getter and setter
    public int getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(int armorRating) {
        this.armorRating = armorRating;
    }
}
