package spendingtracker;
import spendingtracker.ui.SpendingTrackerUI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SpendingTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Spending Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setContentPane(new SpendingTrackerUI());

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}