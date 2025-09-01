package ui;

import javax.swing.*;
import service.StudentService;

/** Launcher with two buttons to open either UI. */
public class LauncherApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentService svc = new StudentService();
            JFrame f = new JFrame("Smart Student Platform - Launcher");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(400,150);
            f.setLocationRelativeTo(null);

            JButton manual = new JButton("Manual CGPA Version");
            JButton original = new JButton("Original Computed CGPA Version");

            manual.addActionListener(e -> new StudentAppManual(svc).setVisible(true));
            JPanel p = new JPanel();
            p.add(manual);
            f.add(p);
            f.setVisible(true);
        });
    }
}