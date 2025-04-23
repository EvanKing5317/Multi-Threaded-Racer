import java.util.ArrayList;
import java.util.Scanner;

public class Racer {
    private String name;
    private int speed;
    private int recovery;
    private int luck;

    // Constructor
    public Racer(String name, int speed, int recovery, int luck) {
        this.name = name;
        this.speed = speed;
        this.recovery = recovery;
        this.luck = luck;
    }

    // Optional: to print racer details
    public String toString() {
        return name + " (Speed: " + speed + ", Recovery: " + recovery + ", Luck: " + luck + ")";
    }

    // Getters if you need them later
    public String getName() { return name; }
    public int getSpeed() { return speed; }
    public int getRecovery() { return recovery; }
    public int getLuck() { return luck; }

    // Main method to create racers
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Racer> racers = new ArrayList<>();

        System.out.print("Enter the number of racers: ");
        int racerAmnt = input.nextInt();
        input.nextLine(); // consume newline

        for (int i = 1; i <= racerAmnt; i++) {
            System.out.print("Enter racer's " + i + " name: ");
            String name = input.nextLine();

            System.out.print("Enter the speed of " + name + ": ");
            int speed = input.nextInt();

            System.out.print("Enter the recovery of " + name + ": ");
            int recovery = input.nextInt();

            System.out.print("Enter the luck of " + name + ": ");
            int luck = input.nextInt();
            input.nextLine(); // consume newline

            Racer r = new Racer(name, speed, recovery, luck);
            racers.add(r);
        }

        // Print all racers
        System.out.println("\nAll racers:");
        for (Racer r : racers) {
            System.out.println(r);3
        }

        input.close();
    }
}
