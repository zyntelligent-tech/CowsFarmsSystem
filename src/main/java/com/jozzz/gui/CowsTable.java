package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.util.Element;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CowsTable extends JPanel {
    private final ArrayList<String[]> allData;
    private final String[] columnNames;

    public CowsTable(ArrayList<String[]> allData, String[] columnNames, boolean isViewDetail){
        this.allData = allData;
        this.columnNames = columnNames;
        this.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(allData.toArray(new Object[0][0]), columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(Element.getFont(15));
        table.setRowHeight(20);
        table.setRowSelectionAllowed(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        if(isViewDetail){
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.rowAtPoint(e.getPoint());
                        String cowCode = (String) table.getValueAt(row, table.getColumn("หมายเลขโค").getModelIndex());
                        Main.display.add(new CowDetail(cowCode), "COW_DETAIL");
                        Element.getCardLayout().show(Main.display, "COW_DETAIL");
                    }
                }
            });
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(Element.getFont(15));
        header.setReorderingAllowed(true);
        header.setResizingAllowed(true);
        header.setBackground(Color.lightGray);
        header.setForeground(Color.black);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1366, 768));
        scrollPane.getVerticalScrollBar().setUnitIncrement(100);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                header);

        this.add(scrollPane);
    }

    public ArrayList<String[]> getAllData() {
        return allData;
    }

    public String[] getColumnNames(){
        return columnNames;
    }

}
