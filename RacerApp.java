import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RacerApp extends JFrame implements ActionListener {

    // UI components
    private JTextField nameField, speedField, recoveryField, luckField;
    private JButton addButton;
    private JTextArea outputArea;
    private JLabel pointsLeftLabel;
    private JButton startRaceButton;

    // List to store racers
    public ArrayList<Racer> racers = new ArrayList<>();

    public RacerApp() {

        // Frame settings
        setTitle("Racer Creator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        //Name label and field
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        //Speed label and field
        add(new JLabel("Speed: (Increases how far the racer moves each turn)"));
        speedField = new JTextField();
        add(speedField);

        //Recovery label and field
        add(new JLabel("Recovery: (Lowers the time between turns)"));
        recoveryField = new JTextField();
        add(recoveryField);

        //Luck label and field
        add(new JLabel("Luck: (Increases the chance to get a boost each turn)"));
        luckField = new JTextField();
        add(luckField);

        //Button to add racer
        addButton = new JButton("Add Racer");
        addButton.addActionListener(this);
        add(addButton);

        // Points left label and label with listener to display points left
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pointsPanel.add(new JLabel("Points Left (out of 10):"));
        pointsLeftLabel = new JLabel("10");
        pointsPanel.add(pointsLeftLabel);
        add(pointsPanel);

        //Shows final racer stats
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);

        //Button to start race
        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(this);
        add(startRaceButton);

        setVisible(true);

        setupPointListeners();
    }

    // Method to set up listeners for the point fields
    private void setupPointListeners() {
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePointsLeft(); } // Called when text is inserted
            public void removeUpdate(DocumentEvent e) { updatePointsLeft(); } // Called when text is removed
            public void changedUpdate(DocumentEvent e) { updatePointsLeft(); } // Called when text is changed
        };

        speedField.getDocument().addDocumentListener(listener);//update points left when text is changed
        recoveryField.getDocument().addDocumentListener(listener); //update points left when text is changed
        luckField.getDocument().addDocumentListener(listener); //update points left when text is changed
    }

    // Method to update the points left label
    private void updatePointsLeft() {
        
        int total = 0;// total points between speed, recovery, and luck

        try { total += Integer.parseInt(speedField.getText()); } catch (NumberFormatException e) {} //update total points for speed box
        try { total += Integer.parseInt(recoveryField.getText()); } catch (NumberFormatException e) {} //update total points for recovery box
        try { total += Integer.parseInt(luckField.getText()); } catch (NumberFormatException e) {} //update total points for luck box

        int remaining = 10 - total; //calculate remaining points
        pointsLeftLabel.setText(String.valueOf(Math.max(remaining, 0))); //label for points left 
    }

    // Action listener for button clicks
    @Override
    public void actionPerformed(ActionEvent e) {

        // Check which button was clicked
        if (e.getSource() == addButton) {

            // Validate input fields
            String name = nameField.getText();
            if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for the racer.", "Missing Name", JOptionPane.WARNING_MESSAGE); //check if there is a name
                return;
            }

            //Stats for the racer
            int speed = Integer.parseInt(speedField.getText());
            int recovery = Integer.parseInt(recoveryField.getText());
            int luck = Integer.parseInt(luckField.getText());

            //set the total to the sum of the stats
            int total = speed + recovery + luck;

            // Check if the total points are valid
            if (total < 10) {
                JOptionPane.showMessageDialog(this, "You must allocate all 10 points before adding a racer!", "Not enough points", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (total > 10) {
                JOptionPane.showMessageDialog(this, "You can only allocate up to 10 points total!", "Too many points", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //create a new racer and add it to the list based on its stats
            Racer newRacer = new Racer(name, speed, recovery, luck);
            racers.add(newRacer);

            // Update the racer values area with the new racer's stats
            outputArea.setText(""); // clear previous
            for (Racer r : racers) {
                outputArea.append(r + "\n");
            }

            // Clear input fields
            nameField.setText("");
            speedField.setText("");
            recoveryField.setText("");
            luckField.setText("");

        } else if (e.getSource() == startRaceButton) { //if the start button is clicked

            // Check if there are enough racers
            if (racers.size() < 2 || racers.size() > 6) {
                JOptionPane.showMessageDialog(this, "You must have between 2 and 6 racers to start the race.", "Invalid Racer Count", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //resest the race
            for (Racer r : racers) {
                r.reset(); //reset the racer
            }
            
            openRaceWindow(); //start the race

        }
    }

    //opens and runs the race frame
    private void openRaceWindow() {

        //make the frame
        JFrame raceFrame = new JFrame("Race in Progress");
        raceFrame.setSize(600, 400);
        raceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        raceFrame.setLayout(new BorderLayout());

        // Create a panel to hold the racers
        JPanel racePanel = new JPanel();
        racePanel.setLayout(new GridLayout(racers.size(), 2)); //2 columns: first for name, second for progress bar

        //cycle through racers
        for (Racer r : racers) {
            JLabel nameLabel = new JLabel(r.getName()); //set label with the racer's name
            JProgressBar progressBar = new JProgressBar(0, 100); //make a progress bar to represent the racing
            progressBar.setStringPainted(false);

            racePanel.add(nameLabel);// Name on the left
            racePanel.add(progressBar);// Bar on the right

            r.setProgressBar(progressBar); //set the progress bar to the postion of the racer
            
            //pass into the thread
            Thread t = new Thread(r);
            t.start(); //start the thread
        }

        raceFrame.add(new JScrollPane(racePanel), BorderLayout.CENTER);

        raceFrame.setVisible(true);
    } 
}
