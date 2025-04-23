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


    // List to store racers
    private ArrayList<Racer> racers = new ArrayList<>();

    public RacerApp() {
        // Frame settings
        setTitle("Racer Creator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        //Name label and field
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        //Speed label and field
        add(new JLabel("Speed:"));
        speedField = new JTextField();
        add(speedField);

        //Recovery label and field
        add(new JLabel("Recovery:"));
        recoveryField = new JTextField();
        add(recoveryField);

        //Luck label and field
        add(new JLabel("Luck:"));
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
        // Read values from text fields
        String name = nameField.getText();
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
        

        // Create new racer and add to list
        Racer newRacer = new Racer(name, speed, recovery, luck);
        racers.add(newRacer);

        // Show all racers
        outputArea.setText(""); // clear previous
        for (Racer r : racers) {
            outputArea.append(r + "\n");
        }

        // Clear input fields
        nameField.setText("");
        speedField.setText("");
        recoveryField.setText("");
        luckField.setText("");
    }

    public static void main(String[] args) {
        new RacerApp(); // run the app
    }
}

// Racer class
class Racer {
    private String name;
    private int speed, recovery, luck;

    public Racer(String name, int speed, int recovery, int luck) {
        this.name = name;
        this.speed = speed;
        this.recovery = recovery;
        this.luck = luck;
    }

    public String toString() {
        return name + " (Speed: " + speed + ", Recovery: " + recovery + ", Luck: " + luck + ")";
    }
}
