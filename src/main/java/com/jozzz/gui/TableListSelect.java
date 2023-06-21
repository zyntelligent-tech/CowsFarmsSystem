package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.constant.DisplayState;
import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class TableListSelect extends JPanel {
    private ArrayList<String[]> allDairyBreedPattern;
    private boolean isPageLoading = true;

    public TableListSelect() {
        Dialog dialog = new Dialog();
        new Thread(() -> {
            try {
                allDairyBreedPattern = RunDB.getAllDairyBreedPatternOnly();
                setPageLoading(false);
                SwingUtilities.invokeLater(() -> {
                    init();
                    dialog.getDialog().setVisible(false);
                });
            }catch (Exception e){
                setPageLoading(true);
                dialog.getDialog().setVisible(false);
                SwingUtilities.invokeLater(() -> Element.getCardLayout().show(Main.display, DisplayState.MAIN_MENU));
            }
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void init(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());
        String[] columnList = {"List"};
        String[] columnSelectedList = {"Selected List"};

        CowsTable table1 = new CowsTable(allDairyBreedPattern, columnList, false);
        CowsTable table2 = new CowsTable(new ArrayList<>(), columnSelectedList, false);

        JButton moveToTable2Button = new JButton("ย้ายไป Selected List >>");
        moveToTable2Button.setFont(Element.getFont(15));
        JButton moveToTable1Button = new JButton("<< ย้ายไป List");
        moveToTable1Button.setFont(Element.getFont(15));

        moveToTable2Button.addActionListener(event -> moveSelectedRows(table1.getTable(), table1.getTableModel(), table2.getTableModel()));
        moveToTable1Button.addActionListener(event -> moveSelectedRows(table2.getTable(), table2.getTableModel(), table1.getTableModel()));

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(table1);
        tablePanel.add(table2);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setPreferredSize(new Dimension(0,50));
        buttonPanel.add(moveToTable2Button);
        buttonPanel.add(moveToTable1Button);

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(15));
        backButton.addActionListener(event -> Element.getCardLayout().show(Main.display, DisplayState.MAIN_MENU));

        menuBarPanel.add(backButton);

        this.add(menuBarPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void moveSelectedRows(JTable sourceTable, DefaultTableModel sourceModel, DefaultTableModel targetModel) {
        int[] selectedRows = sourceTable.getSelectedRows();
        Vector<Vector<Object>> rowsToMove = new Vector<>();

        for (int row : selectedRows) {
            Vector<Object> rowData = new Vector<>();
            for (int col = 0; col < sourceModel.getColumnCount(); col++) {
                rowData.add(sourceModel.getValueAt(row, col));
            }
            rowsToMove.add(rowData);
        }

        for (Vector<Object> rowData : rowsToMove) {
            targetModel.addRow(rowData);
        }

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            sourceModel.removeRow(selectedRows[i]);
        }
    }

    public boolean isPageLoading() {
        return isPageLoading;
    }

    public void setPageLoading(boolean pageLoading) {
        isPageLoading = pageLoading;
    }
}
