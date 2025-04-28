import javax.swing.*;

public class Racer implements Runnable {

    private String name;
    private int speed;
    private int recovery;
    private int luck;
    private int position = 0;

    private JProgressBar progressBar; // the progress bar this racer updates

    public Racer(String name, int speed, int recovery, int luck) {
        this.name = name;
        this.speed = speed;
        this.recovery = recovery;
        this.luck = luck;
    }

    public String getName() {
        return name;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void run() {
        System.out.println(name + " started racing!");

        while (position < 100) {
            try {
                int move = speed;

                int luckChance = (int)(Math.random() * 100);
                if (luckChance < luck * 10) {
                    move += 2; // extra move if lucky
                    System.out.println(name + " got lucky! Boosted ahead.");
                }

                position += move;

                if (position > 100) {
                    position = 100;
                }

                System.out.println(name + " is at position: " + position);

                SwingUtilities.invokeLater(() -> {
                    if (progressBar != null) {
                        progressBar.setValue(position);
                    }
                });
                

                Thread.sleep(300 - recovery * 20); // Faster updates if better recovery

            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted!");
            }
        }

        System.out.println(name + " finished the race!");

        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setValue(100);
                progressBar.setString("Finished!");
            }
        });
        
    }

    @Override
    public String toString() {
        return name + " (Speed: " + speed + ", Recovery: " + recovery + ", Luck: " + luck + ")";
    }
}
