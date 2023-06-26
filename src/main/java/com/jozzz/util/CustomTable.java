package com.jozzz.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTable extends DefaultTableCellRenderer {

    private final Color backgroundColor;
    public CustomTable(Color backgroundColor){
        this.backgroundColor = backgroundColor;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        component.setBackground(backgroundColor);
        return component;
    }

}
