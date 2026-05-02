/**
 * Abstract class that represents a game character.
 * All characters have a name, hp, and attackPower.
 * Subclasses must define their own attack() and describeClass() methods.
 */
public abstract class GameCharacter {

    private String name;
    private int hp;
    private int attackPower;

    // Constructor
    public GameCharacter(String name, int hp, int attackPower) {
        this.name = name;
        this.hp = hp;
        this.attackPower = attackPower;
    }

    // Check if character is still alive
    public boolean isAlive() {
        return this.hp > 0;
    }

    // Abstract methods - subclasses must override these
    public abstract void attack();
    public abstract String describeClass();

    // Concrete method - same for all characters
    public void rest() {
        this.hp = this.hp + 15;
        System.out.println(name + " rests and recovers 15 HP. Current HP: " + this.hp);
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
}
