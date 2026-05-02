import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {

    // ANSI color codes
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String RED    = "\u001B[31m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BORDER = "\u001B[34m";

    // Box: inner width = 65, total line = 67
    static final int    INNER = 65;
    static final String LINE  = "+" + "-".repeat(INNER) + "+";

    static Scanner scanner = new Scanner(System.in);
    static Random  random  = new Random();

    public static void main(String[] args) {

        boolean playAgain = true;

        // --- FIX: Create party ONCE outside the loop so HP/mana/arrows carry over ---
        ArrayList<GameCharacter> party = new ArrayList<>();
        party.add(new Mage("Riko",      20, 6,  60));
        party.add(new Warrior("Reg",    20, 12,  8));
        party.add(new Archer("Nanachi", 20, 10,  5));

        while (playAgain) {

            showTitle();
            showCharacters(party);

            // Proceed to combat prompt
            System.out.print("  Proceed to Combat (y/n): ");
            boolean proceed = askProceed();
            if (!proceed) {
                printBlank();
                System.out.println("  Thanks for playing! Exiting...");
                printBlank();
                printLine();
                break;
            }

            printBlank();

            // Combat intro
            printLine();
            printCentered("COMBAT ROUND", BOLD);
            printLine();
            printBlank();
            System.out.println("  A monster appeared!");
            printBlank();
            System.out.print("  Click Enter to start combat...");
            scanner.nextLine();
            printBlank();

            // Monster stats - fresh each combat session
            int monsterHp      = 30;
            int monsterAtk     = 10;   // Monster attack power
            String monsterName = "Monster";

            printLine();
            printCentered("!! DEFEAT THE MONSTER !!", BOLD + RED);
            printLine();
            printBlank();
            System.out.println("  " + RED + monsterName + RESET);
            System.out.println("  HP: " + monsterHp);
            System.out.println("  Attack Power: " + monsterAtk);
            printBlank();
            System.out.print("  Press Enter to continue...");
            scanner.nextLine();
            printBlank();

            boolean partyWiped = false;

            // -----------------------------------------------
            // 3 fixed combat rounds
            // Order each round:
            //   1. Monster attacks first
            //   2. Reg attacks
            //   3. Riko attacks
            //   4. Nanachi attacks
            //   5. Monster attacks again
            //   6. HP status shown
            // -----------------------------------------------
            for (int combatRound = 1; combatRound <= 3; combatRound++) {

                if (monsterHp <= 0 || countAlive(party) == 0) break;

                printLine();
                printCentered("COMBAT ROUND " + combatRound, BOLD + YELLOW);
                printLine();
                printBlank();

                // --- Monster attacks FIRST at the start of each round ---
                GameCharacter firstTarget = getRandomTarget(party);
                if (firstTarget != null) {
                    int monsterDmg = monsterAtk;

                    // Warrior armor (Defendable) reduces monster damage
                    if (firstTarget instanceof Defendable) {
                        Defendable d = (Defendable) firstTarget;
                        monsterDmg   = monsterDmg - d.getDefenseRating();
                        if (monsterDmg < 1) monsterDmg = 1;
                    }

                    int newHp = firstTarget.getHp() - monsterDmg;
                    if (newHp < 0) newHp = 0;
                    firstTarget.setHp(newHp);

                    System.out.println(" " + RED + "Monster attacks..." + RESET);
                    System.out.println("  >> " + RED + monsterName + RESET
                            + " lunges at " + YELLOW + firstTarget.getName() + RESET
                            + " for " + monsterDmg + " damage!");
                    System.out.println("  >> [" + firstTarget.getName() + " HP: "
                            + firstTarget.getHp() + "]");

                    if (firstTarget.getHp() == 0) {
                        System.out.println("  " + RED
                                + firstTarget.getName() + " has been knocked out!" + RESET);
                    }
                    printBlank();
                }

                // --- Reg attacks (Warrior / Defendable - instanceof + downcast) ---
                if (party.get(1).isAlive() && monsterHp > 0) {
                    Warrior    reg    = (Warrior) party.get(1);
                    Defendable shield = (Defendable) reg;   // downcast to Defendable

                    int dmg   = reg.getAttackPower();
                    monsterHp = monsterHp - dmg;
                    if (monsterHp < 0) monsterHp = 0;

                    System.out.println(" " + YELLOW + "Reg" + RESET
                            + " takes the lead, raises shield, and charges at the monster!");
                    System.out.println("  >> Blocked with defense rating "
                            + shield.getDefenseRating());
                    System.out.println("  >> Strikes for " + dmg + " damage!");
                    System.out.println("  >> [Monster HP: " + monsterHp + "]");
                    printBlank();
                }

                // --- Riko attacks (Mage / CasterAbility - instanceof + downcast) ---
                if (party.get(0).isAlive() && monsterHp > 0) {
                    Mage          riko   = (Mage) party.get(0);
                    CasterAbility caster = (CasterAbility) riko;   // downcast to CasterAbility

                    int dmg   = riko.getAttackPower();
                    monsterHp = monsterHp - dmg;
                    if (monsterHp < 0) monsterHp = 0;

                    // castSpell deducts 20 mana internally inside Mage
                    caster.castSpell("Fireball");
                    System.out.println(" " + YELLOW + "Riko" + RESET
                            + " casts Fireball! (20 mana used)");
                    System.out.println("  >> Blazing flames scorch the monster for "
                            + dmg + " damage!");
                    System.out.println("  >> Mana remaining: " + caster.getMana());
                    System.out.println("  >> [Monster HP: " + monsterHp + "]");
                    printBlank();
                }

                // --- Nanachi attacks (Archer) ---
                if (party.get(2).isAlive() && monsterHp > 0) {
                    Archer nanachi = (Archer) party.get(2);

                    int dmg   = nanachi.getAttackPower();
                    monsterHp = monsterHp - dmg;
                    if (monsterHp < 0) monsterHp = 0;

                    // Deduct one arrow
                    nanachi.setArrowCount(nanachi.getArrowCount() - 1);

                    System.out.println(" " + YELLOW + "Nanachi" + RESET
                            + " lines up the perfect shot and releases!");
                    System.out.println("  >> Arrow pierces the monster for "
                            + dmg + " damage!");
                    System.out.println("  >> Arrows left: " + nanachi.getArrowCount());
                    System.out.println("  >> [Monster HP: " + monsterHp + "]");
                    printBlank();
                }

                // --- Monster attacks AGAIN at the end of each round ---
                if (monsterHp > 0) {
                    GameCharacter secondTarget = getRandomTarget(party);
                    if (secondTarget != null) {
                        int monsterDmg = monsterAtk;

                        // Warrior armor reduces damage
                        if (secondTarget instanceof Defendable) {
                            Defendable d = (Defendable) secondTarget;
                            monsterDmg   = monsterDmg - d.getDefenseRating();
                            if (monsterDmg < 1) monsterDmg = 1;
                        }

                        int newHp = secondTarget.getHp() - monsterDmg;
                        if (newHp < 0) newHp = 0;
                        secondTarget.setHp(newHp);

                        System.out.println(" " + RED + "Monster attacks..." + RESET);
                        System.out.println("  >> " + RED + monsterName + RESET
                                + " lunges at " + YELLOW + secondTarget.getName() + RESET
                                + " for " + monsterDmg + " damage!");
                        System.out.println("  >> [" + secondTarget.getName() + " HP: "
                                + secondTarget.getHp() + "]");

                        if (secondTarget.getHp() == 0) {
                            System.out.println("  " + RED
                                    + secondTarget.getName() + " has been knocked out!"
                                    + RESET);
                        }
                        printBlank();
                    }
                }

                // --- HP STATUS (top line only, no bottom line per expected output) ---
                printLine();
                printCentered("HP STATUS", BOLD);
                printBlank();

                for (GameCharacter c : party) {
                    if (c.isAlive()) {
                        System.out.printf("  %-10s | HP: %d%n",
                                c.getName(), c.getHp());
                    } else {
                        System.out.printf("  " + RED + "%-10s" + RESET
                                + RED + " | KNOCKED OUT!" + RESET + "%n",
                                c.getName());
                    }
                }
                System.out.printf("  " + RED + "%-10s" + RESET + " | HP: %d%n",
                        monsterName, monsterHp);
                printBlank();

                if (countAlive(party) == 0) {
                    partyWiped = true;
                    break;
                }

                System.out.print("  Press Enter to continue...");
                scanner.nextLine();
                printBlank();
            }

            // If monster still alive after 3 rounds and no party alive
            if (countAlive(party) == 0) {
                partyWiped = true;
            }

            // Battle outcome - two lines in one box
            printLine();
            if (partyWiped) {
                printCentered("THE MONSTER WIPED OUT THE PARTY!", BOLD + RED);
                printCentered("PARTY WIPED OUT!", BOLD + RED);
            } else {
                printCentered("YIPPIE! YOU DEFEATED THE MONSTER!", BOLD + GREEN);
                printCentered("PARTY WON!", BOLD + GREEN);
            }
            printLine();
            printBlank();

            // -----------------------------------------------
            // Party Summary - shows real current values
            // -----------------------------------------------
            printBlank();
            printLine();
            printCentered("PARTY SUMMARY", BOLD);
            printLine();
            printBlank();

            // Casters section (instanceof + downcast)
            System.out.println("  !! CASTERS !!");
            for (GameCharacter c : party) {
                if (c instanceof CasterAbility) {
                    CasterAbility caster = (CasterAbility) c;
                    System.out.println("  | " + YELLOW + c.getName() + RESET);
                    System.out.println("  | HP: " + c.getHp());
                    System.out.println("  | Attack Power Stat: " + c.getAttackPower());
                    System.out.println("  | Mana: " + caster.getMana());
                }
            }
            printBlank();

            // Defenders section (instanceof + downcast)
            System.out.println("  !! DEFENDERS !!");
            for (GameCharacter c : party) {
                if (c instanceof Defendable) {
                    Defendable defender = (Defendable) c;
                    System.out.println("  | " + YELLOW + c.getName() + RESET);
                    System.out.println("  | HP: " + c.getHp());
                    System.out.println("  | Attack Power Stat: " + c.getAttackPower());
                    System.out.println("  | Armor: " + defender.getDefenseRating());
                }
            }
            printBlank();

            // Ranged section
            System.out.println("  !! RANGED !!");
            for (GameCharacter c : party) {
                if (!(c instanceof CasterAbility) && !(c instanceof Defendable)) {
                    Archer a = (Archer) c;
                    System.out.println("  | " + YELLOW + c.getName() + RESET + " | Archer");
                    System.out.println("  | HP: " + c.getHp());
                    System.out.println("  | Attack Power Stat: " + c.getAttackPower());
                    System.out.println("  | Arrows: " + a.getArrowCount());
                }
            }
            printBlank();
            printLine();
            printBlank();

            // Totals block - uses actual current HP values
            int totalHp       = 0;
            int casterCount   = 0;
            int defenderCount = 0;
            int rangedCount    = 0;

            for (GameCharacter c : party) {
                totalHp = totalHp + c.getHp();
                if (c instanceof CasterAbility) {
                    casterCount = casterCount + 1;
                } else if (c instanceof Defendable) {
                    defenderCount = defenderCount + 1;
                } else {
                    rangedCount = rangedCount + 1;
                }
            }

            double avgHp = (double) totalHp / party.size();

            System.out.println("  Total Members   | " + party.size());
            System.out.println("  Total HP        | " + totalHp);
            System.out.println("  Casters         | " + casterCount);
            System.out.println("  Defenders       | " + defenderCount);
            System.out.println("  Ranged          | " + rangedCount);
            System.out.printf( "  Average HP      | %.1f%n", avgHp);

            String status;
            if (partyWiped) {
                status = RED + "DEFEATED" + RESET;
            } else if (avgHp >= 10) {
                status = GREEN + "READY FOR MORE" + RESET;
            } else {
                status = YELLOW + "NEEDS REST" + RESET;
            }
            System.out.println("  Party Status    | " + status);

            printBlank();
            printLine();
            printBlank();

            // Play again?
            System.out.print("  Play another? (y/n): ");
            String again = scanner.nextLine().trim().toLowerCase();

            if (again.equals("y")) {
                playAgain = true;
                // Party carries over with current HP/mana/arrows - no reset
            } else {
                playAgain = false;
                printBlank();
                System.out.println("  Thanks for playing! Exiting...");
                printBlank();
                printLine();
            }
        }

        scanner.close();
    }

    // --------------------------------------------------
    // Title screen
    // --------------------------------------------------
    static void showTitle() {
        printBlank();
        printLine();
        printRow("                                                                 ");
        printRow("         ____  ____   ____    ____    _    __  __ _____          ");
        printRow("        |  _ \\|  _ \\ / ___|  / ___|  / \\  |  \\/  | ____|         ");
        printRow("        | |_) | |_) | |  _  | |  _  / _ \\ | |\\/| |  _|           ");
        printRow("        |  _ <|  __/| |_| | | |_| |/ ___ \\| |  | | |___          ");
        printRow("        |_| \\_\\_|    \\____|  \\____/_/   \\_\\_|  |_|_____|         ");
        printRow("                                                                 ");
        printCentered("* F A N T A S Y *", BOLD + YELLOW);
        printRow("                                                                 ");
        printLine();
    }

    // --------------------------------------------------
    // Show all characters - PARTY ROSTER
    // Entire "(1) Riko | Mage" line is yellow
    // --------------------------------------------------
    static void showCharacters(ArrayList<GameCharacter> party) {
        printBlank();
        printLine();
        printCentered("PARTY ROSTER", BOLD);
        printLine();
        printBlank();

        for (int i = 0; i < party.size(); i++) {
            GameCharacter c = party.get(i);
            // Entire label line is yellow
            System.out.println("  " + YELLOW + "| (" + (i + 1) + ") "
                    + c.getName() + " | " + getClassName(c) + RESET);
            System.out.println("  | HP: " + c.getHp());
            System.out.println("  | Attack Power Stat: " + c.getAttackPower());
            printExtraStats(c);
            printBlank();
        }

        printLine();
        printBlank();
    }

    // Print class-specific stat
    static void printExtraStats(GameCharacter c) {
        if (c instanceof Mage) {
            Mage m = (Mage) c;
            System.out.println("  | Mana: " + m.getMana());
        } else if (c instanceof Warrior) {
            Warrior w = (Warrior) c;
            System.out.println("  | Armor: " + w.getArmorRating());
        } else if (c instanceof Archer) {
            Archer a = (Archer) c;
            System.out.println("  | Arrows: " + a.getArrowCount());
        }
    }

    // Return class name string
    static String getClassName(GameCharacter c) {
        if (c instanceof Mage)    return "Mage";
        if (c instanceof Warrior) return "Warrior";
        if (c instanceof Archer)  return "Archer";
        return "Unknown";
    }

    // --------------------------------------------------
    // Proceed to combat prompt
    // --------------------------------------------------
    static boolean askProceed() {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                System.out.print("  Go Back? (y/n): ");
                String back = scanner.nextLine().trim().toLowerCase();
                if (back.equals("y")) {
                    return false;
                } else if (back.equals("n")) {
                    System.out.print("  Proceed to Combat (y/n): ");
                    continue;
                } else {
                    System.out.println(RED + "  Please enter y or n." + RESET);
                    System.out.print("  Proceed to Combat (y/n): ");
                }
            } else {
                System.out.println(RED + "  Please enter y or n." + RESET);
                System.out.print("  Proceed to Combat (y/n): ");
            }
        }
    }

    // --------------------------------------------------
    // Pick a random living party member for monster to attack
    // --------------------------------------------------
    static GameCharacter getRandomTarget(ArrayList<GameCharacter> party) {
        ArrayList<GameCharacter> alive = new ArrayList<>();
        for (GameCharacter c : party) {
            if (c.isAlive()) alive.add(c);
        }
        if (alive.size() == 0) return null;
        return alive.get(random.nextInt(alive.size()));
    }

    // --------------------------------------------------
    // Count living party members
    // --------------------------------------------------
    static int countAlive(ArrayList<GameCharacter> party) {
        int count = 0;
        for (GameCharacter c : party) {
            if (c.isAlive()) count = count + 1;
        }
        return count;
    }

    // --------------------------------------------------
    // Display helpers
    // --------------------------------------------------
    static void printLine() {
        System.out.println(BORDER + LINE + RESET);
    }

    static void printBlank() {
        System.out.println();
    }

    static void printCentered(String text, String color) {
        int total = INNER - text.length();
        int left  = total / 2;
        int right = total - left;

        String ls = "";
        String rs = "";
        for (int i = 0; i < left;  i++) ls = ls + " ";
        for (int i = 0; i < right; i++) rs = rs + " ";

        System.out.println(
            BORDER + "|" + RESET
            + color + ls + text + rs + RESET
            + BORDER + "|" + RESET
        );
    }

    static void printRow(String text) {
        System.out.println(BORDER + "|" + RESET + text + BORDER + "|" + RESET);
    }
}
