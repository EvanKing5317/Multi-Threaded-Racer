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
    private ArrayList<JProgressBar> racerBars = new ArrayList<>();
    private ArrayList<JLabel> racerLabels = new ArrayList<>();


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

    private void setupPointListeners() {
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePointsLeft(); }
            public void removeUpdate(DocumentEvent e) { updatePointsLeft(); }
            public void changedUpdate(DocumentEvent e) { updatePointsLeft(); }
        };

        speedField.getDocument().addDocumentListener(listener);
        recoveryField.getDocument().addDocumentListener(listener);
        luckField.getDocument().addDocumentListener(listener);
    }

    private void updatePointsLeft() {
        int total = 0;

        try { total += Integer.parseInt(speedField.getText()); } catch (NumberFormatException e) {}
        try { total += Integer.parseInt(recoveryField.getText()); } catch (NumberFormatException e) {}
        try { total += Integer.parseInt(luckField.getText()); } catch (NumberFormatException e) {}

        int remaining = 10 - total;
        pointsLeftLabel.setText(String.valueOf(Math.max(remaining, 0)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {

            String name = nameField.getText();
            if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for the racer.", "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int speed = Integer.parseInt(speedField.getText());
            int recovery = Integer.parseInt(recoveryField.getText());
            int luck = Integer.parseInt(luckField.getText());

            int total = speed + recovery + luck;

            if (total < 10) {
                JOptionPane.showMessageDialog(this, "You must allocate all 10 points before adding a racer!", "Not enough points", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (total > 10) {
                JOptionPane.showMessageDialog(this, "You can only allocate up to 10 points total!", "Too many points", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Racer newRacer = new Racer(name, speed, recovery, luck);
            racers.add(newRacer);

            outputArea.setText(""); // clear previous
            for (Racer r : racers) {
                outputArea.append(r + "\n");
            }

            // Clear input fields
            nameField.setText("");
            speedField.setText("");
            recoveryField.setText("");
            luckField.setText("");

        } else if (e.getSource() == startRaceButton) {
            openRaceWindow();
        }
    }

    private void openRaceWindow() {
        JFrame raceFrame = new JFrame("Race in Progress");
        raceFrame.setSize(600, 400);
        raceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        raceFrame.setLayout(new BorderLayout());

        if (racers.isEmpty()) {
            JTextArea raceText = new JTextArea("No racers added yet!");
            raceText.setEditable(false);
            raceFrame.add(new JScrollPane(raceText), BorderLayout.CENTER);
        } else {
            JPanel racePanel = new JPanel();
            racePanel.setLayout(new GridLayout(racers.size(), 2)); // 2 columns
            // 2 columns: first for name, second for progress bar

            for (Racer r : racers) {
                JLabel nameLabel = new JLabel(r.getName());
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setStringPainted(true);

                racePanel.add(nameLabel);     // Name on the left
                racePanel.add(progressBar);   // Bar on the right

                r.setProgressBar(progressBar);

                Thread t = new Thread(r);
                t.start();
            }

            raceFrame.add(new JScrollPane(racePanel), BorderLayout.CENTER);
        }

        raceFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RacerApp();
        });
    }
    
}
