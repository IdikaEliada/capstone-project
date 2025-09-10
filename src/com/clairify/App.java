package com.clairify;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        // Set system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Create and display the application
        new EnhancedSmartStudentPlatform();
      } catch (Exception e) {
        e.printStackTrace();
        // Fallback to default look and feel
        new EnhancedSmartStudentPlatform();
      }
    });
  }
}
