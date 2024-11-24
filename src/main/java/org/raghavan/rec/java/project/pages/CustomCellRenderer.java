package org.raghavan.rec.java.project.pages;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class CustomCellRenderer extends JTextArea implements TableCellRenderer {

    public CustomCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText(value != null ? value.toString() : "");

        // Adjust row height to fit the text
        int rowHeight = 70;
        if (table.getRowHeight(row) != rowHeight) {
            table.setRowHeight(row, rowHeight);
        }

        // Handle selection and focus styling
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this;
    }
}
