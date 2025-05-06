import javax.swing.*;

public class Racer implements Runnable {

    // Racer attributes
    private String name;
    private int speed = 1;
    private int recovery = 1;
    private int luck = 1;
    private int position = 0;

    private JProgressBar progressBar; // the progress bar this racer updates

    // Constructor for Racer
    public Racer(String name, int speed, int recovery, int luck) {
        this.name = name;
        this.speed = speed;
        this.recovery = recovery;
        this.luck = luck;
    }

    //returns the name of the racer (used for the label)
    public String getName() {
        return name;
    }
    
    //methiod to set the progress bar
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    //Reset the racer's position and progress bar
    public void reset() {
        position = 0;
        if (progressBar != null) {
            progressBar.setValue(0);
        }
    }
    
    //run method for the thread
    public void run() {

        //print the name of the racer
        System.out.println(name + " started racing!");

        // Loop until the racer reaches the finish line (100)
        while (position < 100) {
            try {

                //the amount the racer moves is based on the speed
                int move = 1 + (int)(0.25 * speed);
                
                // Random chance to get an extra move based on luck
                int luckChance = (int)(Math.random() * 100); // Random number between 0 and 100
                if (luckChance < luck * 10) { //if the random number is less than the luck * 10 (more luck = more chance)
                    move += 2; // extra move if lucky
                    System.out.println(name + " got lucky! Boosted ahead.");
                }

                position += move; //update the position 

                //makes sure the racer doesnt go over 100
                if (position > 100) {
                    position = 100;
                }

                //Print the current position of the racer
                System.out.println(name + " is at position: " + position);

                // Update the progress bar
                SwingUtilities.invokeLater(() -> {
                    if (progressBar != null) {
                        progressBar.setValue(position); //sets the value of the progress bar to the position
                    }
                });
                
                // Sleep for a time based on the recovery attribute
                int sleepTime = Math.max(100, 300 - recovery * 25); // Never less than 100ms
                Thread.sleep(sleepTime);

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

    //toString method to print the racer in the output area
    public String toString() {
        return name + " (Speed: " + speed + ", Recovery: " + recovery + ", Luck: " + luck + ")";
    }
}
