package ui.validation;

import javax.swing.*;
import javax.swing.text.*;

public class FieldValidators {

    // --------- Filters ----------
    public static class NumericFilter extends DocumentFilter {
        private final boolean allowDecimal;
        public NumericFilter(boolean allowDecimal) { this.allowDecimal = allowDecimal; }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (isValid(fb, offset, 0, string)) super.insertString(fb, offset, string, attr);
            else UIManager.getLookAndFeel().provideErrorFeedback(null);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (isValid(fb, offset, length, text)) super.replace(fb, offset, length, text, attrs);
            else UIManager.getLookAndFeel().provideErrorFeedback(null);
        }

        private boolean isValid(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
            if (text == null) return true;

            String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder sb = new StringBuilder(cur);
            sb.replace(offset, offset + length, text);
            String next = sb.toString();

            if (next.isEmpty()) return true;

            String regex = allowDecimal ? "^[+-]?\\d*(?:\\.\\d*)?$" : "^[+-]?\\d*$";
            return next.matches(regex);
        }
    }

    public static class AlphaFilter extends DocumentFilter {
        @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null || string.matches("[a-zA-Z\\s-]+")) super.insertString(fb, offset, string, attr);
            else UIManager.getLookAndFeel().provideErrorFeedback(null);
        }
        @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null || text.matches("[a-zA-Z\\s-]+")) super.replace(fb, offset, length, text, attrs);
            else UIManager.getLookAndFeel().provideErrorFeedback(null);
        }
    }

    // --------- Appliers ----------
    public static void applyNumeric(JTextField field, boolean allowDecimal) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericFilter(allowDecimal));
    }
    public static void applyAlpha(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new AlphaFilter());
    }

    // --------- Dialog helpers ----------
    public static boolean requireAlpha(JComponent parent, JTextField field, String label) {
        String v = field.getText().trim();
        if (!v.matches("[A-Za-z\\s-]+")) {
            showError(parent, "Please input alphabets only for " + label + ".");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean requireNumericRange(JComponent parent, JTextField field, String label, double min, double max) {
        String t = field.getText().trim();
        try {
            double val = Double.parseDouble(t);
            if (val < min || val > max) {
                showError(parent, label + " must be between " + min + " and " + max + ".");
                field.requestFocus();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError(parent, "Please input numbers for " + label + ".");
            field.requestFocus();
            return false;
        }
    }

    public static boolean requireNotEmpty(JComponent parent, JTextField field, String label) {
        if (field.getText().trim().isEmpty()) {
            showError(parent, label + " cannot be empty.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static void showError(JComponent parent, String message) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(parent), message, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }
}