package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/** Shared styles (colors, fonts, helpers) for a neat, modern Swing look. */
public final class Styles {

    // -------- Theme palette --------
    public static final Color BG = new Color(250, 251, 254);      // app background
    public static final Color CARD = Color.WHITE;                  // panels/cards
    public static final Color TEXT = new Color(30, 41, 59);        // dark slate
    public static final Color MUTED = new Color(100, 116, 139);    // slate-500
    public static final Color ACCENT = new Color(79, 70, 229);     // indigo-600
    public static final Color ACCENT_DARK = new Color(67, 56, 202);// indigo-700
    public static final Color BORDER = new Color(226, 232, 240);   // light border
    public static final Color ROW_ALT = new Color(248, 250, 252);  // zebra rows
    public static final Font  BASE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font  H6 = BASE_FONT.deriveFont(Font.BOLD, 14f);

    private Styles(){}

    // -------- Global Look & Feel --------
    public static void applyGlobalNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ignored) {}
        UIManager.put("control", CARD);
        UIManager.put("nimbusBase", ACCENT);
        UIManager.put("text", TEXT);
        UIManager.put("nimbusBlueGrey", new Color(200, 210, 225));
        UIManager.put("Table.background", CARD);
        UIManager.put("Table.alternateRowColor", ROW_ALT);
        UIManager.put("Table.showGrid", Boolean.FALSE);
        UIManager.put("Table.font", BASE_FONT);
        UIManager.put("Table.focusCellHighlightBorder", new EmptyBorder(0,0,0,0));
        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.messageFont", BASE_FONT);
        UIManager.put("OptionPane.buttonFont", BASE_FONT);
        UIManager.put("Button.font", BASE_FONT);
        UIManager.put("Label.font", BASE_FONT);
        UIManager.put("TextField.font", BASE_FONT);
    }

    // -------- Components --------
    /** Create a clean card panel with padding and slight border. */
    public static JPanel cardPanel(String title, JComponent content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16,16,16,16)
        ));
        if (title != null && !title.isEmpty()) {
            JLabel t = new JLabel(title);
            t.setFont(H6);
            t.setForeground(TEXT);
            t.setBorder(new EmptyBorder(0,0,12,0));
            card.add(t, BorderLayout.NORTH);
        }
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /** Style a primary button (accent background, white text). */
    public static void stylePrimary(JButton b) {
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8,14,8,14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
    }

    /** Style a subtle button (light border). */
    public static void styleSubtle(JButton b) {
        b.setBackground(CARD);
        b.setForeground(TEXT);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8,14,8,14)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
    }

    /** Style a text field with padding and light border. */
    public static void styleField(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8,10,8,10)
        ));
        f.setFont(BASE_FONT);
    }

    /** Apply neat table defaults: zebra rows, header styling, row height. */
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(TEXT);

        JTableHeader header = table.getTableHeader();
        header.setFont(H6);
        header.setForeground(TEXT);
        header.setBackground(new Color(241, 245, 249));
        header.setBorder(BorderFactory.createLineBorder(BORDER));

        // zebra rows (also works with Nimbus)
        DefaultTableCellRenderer z = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) c.setBackground((row % 2 == 0) ? CARD : ROW_ALT);
                setBorder(new EmptyBorder(0,12,0,12));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(z);
        }
    }
}