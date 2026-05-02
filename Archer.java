// Archer extends GameCharacter
public class Archer extends GameCharacter {

    private int arrowCount;

    // Constructor
    public Archer(String name, int hp, int attackPower, int arrowCount) {
        super(name, hp, attackPower);
        this.arrowCount = arrowCount;
    }

    @Override
    public void attack() {
        if (arrowCount > 0) {
            arrowCount = arrowCount - 1;
            System.out.println(getName() + " fires an arrow and deals " + getAttackPower() + " damage! (" + arrowCount + " arrows left)");
        } else {
            System.out.println(getName() + " has no arrows left and punches for " + (getAttackPower() / 2) + " damage!");
        }
    }

    @Override
    public String describeClass() {
        return "Archer  | Name: " + getName() + " | HP: " + getHp() + " | Attack: " + getAttackPower() + " | Arrows: " + arrowCount;
    }

    // Getter and setter
    public int getArrowCount() {
        return arrowCount;
    }

    public void setArrowCount(int arrowCount) {
        this.arrowCount = arrowCount;
    }
}
