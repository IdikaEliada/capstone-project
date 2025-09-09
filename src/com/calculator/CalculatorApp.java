package com.calculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CalculatorApp extends JFrame {
    private JLabel displayLabel;
    private JLabel historyLabel;
    private double currentValue = 0;
    private String currentOperation = "";
    private boolean newNumber = true;
    private boolean isRadianMode = true;
    private boolean isInverseMode = false;
    
    // Dark theme colors
    private final Color BACKGROUND = new Color(60, 67, 78);
    private final Color BUTTON_NORMAL = new Color(73, 82, 95);
    private final Color BUTTON_OPERATOR = new Color(255, 159, 10);
    private final Color BUTTON_CLEAR = new Color(255, 69, 58);
    private final Color BUTTON_FUNCTION = new Color(85, 95, 110);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color DISPLAY_BG = new Color(52, 58, 68);
    
    private JPanel buttonPanel;

    public CalculatorApp() {
        setTitle("Scientific Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 550));
        getContentPane().setBackground(BACKGROUND);
        
        initializeComponents();
        setupResponsiveLayout();
        pack();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Display panel
        JPanel displayPanel = createDisplayPanel();
        add(displayPanel, BorderLayout.NORTH);
        
        // Button panel
        buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(DISPLAY_BG);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        historyLabel = new JLabel("", SwingConstants.RIGHT);
        historyLabel.setForeground(Color.GRAY);
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setForeground(TEXT_COLOR);
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        
        displayPanel.add(historyLabel, BorderLayout.NORTH);
        displayPanel.add(displayLabel, BorderLayout.CENTER);
        
        return displayPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 4, 8, 8));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Row 1: Clear and basic operations
        panel.add(createButton("CE", BUTTON_CLEAR, e -> clear()));
        panel.add(createButton("C", BUTTON_CLEAR, e -> clearAll()));
        panel.add(createButton("⌫", BUTTON_FUNCTION, e -> backspace()));
        panel.add(createButton("÷", BUTTON_OPERATOR, e -> setOperation("÷")));
        
        // Row 2: Scientific mode toggles and functions
        panel.add(createToggleButton("Inv", BUTTON_FUNCTION, e -> toggleInverse()));
        panel.add(createToggleButton("Rad", BUTTON_FUNCTION, e -> toggleAngleMode()));
        panel.add(createButton("Sin", BUTTON_FUNCTION, e -> calculateTrigFunction("sin")));
        panel.add(createButton("×", BUTTON_OPERATOR, e -> setOperation("×")));
        
        // Row 3: Numbers and trig functions
        panel.add(createButton("7", BUTTON_NORMAL, e -> appendNumber("7")));
        panel.add(createButton("8", BUTTON_NORMAL, e -> appendNumber("8")));
        panel.add(createButton("9", BUTTON_NORMAL, e -> appendNumber("9")));
        panel.add(createButton("Cos", BUTTON_FUNCTION, e -> calculateTrigFunction("cos")));
        
        // Row 4: Numbers and trig functions
        panel.add(createButton("4", BUTTON_NORMAL, e -> appendNumber("4")));
        panel.add(createButton("5", BUTTON_NORMAL, e -> appendNumber("5")));
        panel.add(createButton("6", BUTTON_NORMAL, e -> appendNumber("6")));
        panel.add(createButton("Tan", BUTTON_FUNCTION, e -> calculateTrigFunction("tan")));
        
        // Row 5: Numbers and operations
        panel.add(createButton("1", BUTTON_NORMAL, e -> appendNumber("1")));
        panel.add(createButton("2", BUTTON_NORMAL, e -> appendNumber("2")));
        panel.add(createButton("3", BUTTON_NORMAL, e -> appendNumber("3")));
        panel.add(createButton("−", BUTTON_OPERATOR, e -> setOperation("−")));
        
        // Row 6: Special functions
        panel.add(createButton(".", BUTTON_NORMAL, e -> appendNumber(".")));
        panel.add(createButton("0", BUTTON_NORMAL, e -> appendNumber("0")));
        panel.add(createButton("=", BUTTON_OPERATOR, e -> calculate()));
        panel.add(createButton("+", BUTTON_OPERATOR, e -> setOperation("+")));
        
        return panel;
    }

    private void setupResponsiveLayout() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustFontSizes();
                revalidate();
                repaint();
            }
        });
    }

    private void adjustFontSizes() {
        int width = getWidth();
        //int height = getHeight();
        
        // Adjust display font based on window size
        int displaySize = Math.max(24, Math.min(48, width / 12));
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, displaySize));
        
        int historySize = Math.max(10, Math.min(16, width / 30));
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, historySize));
        
        // Adjust button fonts
        Component[] components = buttonPanel.getComponents();
        int buttonSize = Math.max(12, Math.min(18, width / 25));
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, buttonSize);
        
        for (Component comp : components) {
            if (comp instanceof JButton) {
                comp.setFont(buttonFont);
            }
        }
    }

    private JButton createButton(String text, Color bgColor, ActionListener action) {
        return createButtonWithHover(text, bgColor, action, false);
    }

    private JButton createToggleButton(String text, Color bgColor, ActionListener action) {
        return createButtonWithHover(text, bgColor, action, true);
    }

    private JButton createButtonWithHover(String text, Color bgColor, ActionListener action, boolean isToggle) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(action);
        
        // Enhanced hover effects with smooth transitions
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer hoverTimer;
            private Color originalColor = bgColor;
            private Color hoverColor = createHoverColor(bgColor);
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (hoverTimer != null && hoverTimer.isRunning()) {
                    hoverTimer.stop();
                }
                
                // Smooth color transition
                hoverTimer = new Timer(10, e -> {
                    Color current = button.getBackground();
                    Color target = isToggleActive(button) ? createActiveColor(originalColor) : hoverColor;
                    
                    Color newColor = blendColors(current, target, 0.2f);
                    button.setBackground(newColor);
                    button.repaint();
                    
                    if (colorsAreClose(newColor, target)) {
                        hoverTimer.stop();
                    }
                });
                hoverTimer.start();
                
                // Scale effect
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (hoverTimer != null && hoverTimer.isRunning()) {
                    hoverTimer.stop();
                }
                
                // Smooth return transition
                hoverTimer = new Timer(10, e -> {
                    Color current = button.getBackground();
                    Color target = isToggleActive(button) ? createActiveColor(originalColor) : originalColor;
                    
                    Color newColor = blendColors(current, target, 0.2f);
                    button.setBackground(newColor);
                    button.repaint();
                    
                    if (colorsAreClose(newColor, target)) {
                        hoverTimer.stop();
                    }
                });
                hoverTimer.start();
                
                button.setBorder(null);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(createPressedColor(originalColor));
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (button.contains(evt.getPoint())) {
                    button.setBackground(hoverColor);
                }
            }
        });
        
        return button;
    }

    private boolean isToggleActive(JButton button) {
        String text = button.getText();
        return (text.equals("Inv") && isInverseMode) || 
               (text.equals("Rad") && isRadianMode) ||
               (text.equals("Deg") && !isRadianMode);
    }

    private Color createHoverColor(Color original) {
        return new Color(
            Math.min(255, original.getRed() + 30),
            Math.min(255, original.getGreen() + 30),
            Math.min(255, original.getBlue() + 30)
        );
    }

    private Color createActiveColor(Color original) {
        return new Color(
            Math.min(255, original.getRed() + 50),
            Math.min(255, original.getGreen() + 50),
            Math.min(255, original.getBlue() + 50)
        );
    }

    private Color createPressedColor(Color original) {
        return new Color(
            Math.max(0, original.getRed() - 20),
            Math.max(0, original.getGreen() - 20),
            Math.max(0, original.getBlue() - 20)
        );
    }

    private Color blendColors(Color c1, Color c2, float ratio) {
        return new Color(
            (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * ratio),
            (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio),
            (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio)
        );
    }

    private boolean colorsAreClose(Color c1, Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) < 5 &&
               Math.abs(c1.getGreen() - c2.getGreen()) < 5 &&
               Math.abs(c1.getBlue() - c2.getBlue()) < 5;
    }

    private void toggleInverse() {
        isInverseMode = !isInverseMode;
        updateToggleButton("Inv");
    }

    private void toggleAngleMode() {
        isRadianMode = !isRadianMode;
        updateToggleButton(isRadianMode ? "Rad" : "Deg");
    }

    private void updateToggleButton(String buttonText) {
        Component[] components = buttonPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals("Rad")) {
                ((JButton) comp).setText(isRadianMode ? "Rad" : "Deg");
                comp.repaint();
                break;
            }
        }
    }

    private void calculateTrigFunction(String function) {
        try {
            double value = Double.parseDouble(displayLabel.getText());
            double result = 0;
            
            // Convert to radians if in degree mode
            double angleValue = isRadianMode ? value : Math.toRadians(value);
            
            switch (function.toLowerCase()) {
                case "sin":
                    result = isInverseMode ? Math.asin(value) : Math.sin(angleValue);
                    break;
                case "cos":
                    result = isInverseMode ? Math.acos(value) : Math.cos(angleValue);
                    break;
                case "tan":
                    result = isInverseMode ? Math.atan(value) : Math.tan(angleValue);
                    break;
            }
            
            // Convert result back to degrees if in degree mode and inverse
            if (isInverseMode && !isRadianMode) {
                result = Math.toDegrees(result);
            }
            
            displayLabel.setText(formatNumber(result));
            historyLabel.setText((isInverseMode ? "arc" : "") + function + "(" + formatNumber(value) + ") =");
            newNumber = true;
            
        } catch (NumberFormatException e) {
            displayLabel.setText("Error");
        } catch (ArithmeticException e) {
            displayLabel.setText("Math Error");
        }
    }

    private void appendNumber(String digit) {
        if (newNumber) {
            if (digit.equals(".")) {
                displayLabel.setText("0.");
            } else {
                displayLabel.setText(digit);
            }
            newNumber = false;
        } else {
            String current = displayLabel.getText();
            if (digit.equals(".") && current.contains(".")) {
                return;
            }
            displayLabel.setText(current + digit);
        }
    }

    private void setOperation(String operation) {
        if (!currentOperation.isEmpty() && !newNumber) {
            calculate();
        }
        
        currentValue = Double.parseDouble(displayLabel.getText());
        currentOperation = operation;
        historyLabel.setText(formatNumber(currentValue) + " " + operation);
        newNumber = true;
    }

    private void calculate() {
        if (currentOperation.isEmpty() || newNumber) {
            return;
        }

        try {
            double secondValue = Double.parseDouble(displayLabel.getText());
            double result = 0;

            switch (currentOperation) {
                case "+":
                    result = currentValue + secondValue;
                    break;
                case "−":
                    result = currentValue - secondValue;
                    break;
                case "×":
                    result = currentValue * secondValue;
                    break;
                case "÷":
                    if (secondValue == 0) {
                        displayLabel.setText("Cannot divide by zero");
                        return;
                    }
                    result = currentValue / secondValue;
                    break;
            }

            displayLabel.setText(formatNumber(result));
            historyLabel.setText(formatNumber(currentValue) + " " + currentOperation + " " + formatNumber(secondValue) + " =");
            currentValue = result;
            currentOperation = "";
            newNumber = true;

        } catch (NumberFormatException e) {
            displayLabel.setText("Error");
        }
    }

    private void clear() {
        displayLabel.setText("0");
        newNumber = true;
    }

    private void clearAll() {
        displayLabel.setText("0");
        historyLabel.setText("");
        currentValue = 0;
        currentOperation = "";
        newNumber = true;
        isInverseMode = false;
        updateToggleButton("Inv");
    }

    private void backspace() {
        String current = displayLabel.getText();
        if (current.length() > 1 && !current.equals("Error") && !current.startsWith("Cannot")) {
            displayLabel.setText(current.substring(0, current.length() - 1));
        } else {
            displayLabel.setText("0");
            newNumber = true;
        }
    }

    private String formatNumber(double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return "Error";
        }
        
        // Format very small numbers in scientific notation
        if (Math.abs(number) < 1e-10 && number != 0) {
            return String.format("%.2e", number);
        }
        
        // Format large numbers in scientific notation
        if (Math.abs(number) > 1e10) {
            return String.format("%.2e", number);
        }
        
        // Regular formatting
        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            // Limit decimal places for display
            return String.format("%.8f", number).replaceAll("0+$", "").replaceAll("\\.$", "");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Enable anti-aliasing for smoother text
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
            } catch (Exception e) {
                // Ignore if not supported
            }
            
            new CalculatorApp().setVisible(true);
        });
    }
}