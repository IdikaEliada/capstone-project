package com.capstone;
//import com.capstone.Course;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
  public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        System.err.println("Could not set look and feel: " + e.getMessage());
      }
      
      // Create and run the application on the EDT
      SwingUtilities.invokeLater(() -> {
        new SmartStudentPlatform();
      });
  }
}
