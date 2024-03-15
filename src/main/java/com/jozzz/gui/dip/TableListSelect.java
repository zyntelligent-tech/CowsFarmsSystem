package com.jozzz.gui.dip;

import com.jozzz.constant.DisplayState;
import com.jozzz.cow_format.RegexPattern;
import com.jozzz.gui.component.CowsTable;
import com.jozzz.util.CardPage;
import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;
import com.jozzz.util.WriteXlsxFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

public class TableListSelect extends JPanel {
    private ArrayList<String[]> allDairyBreedPattern;
    private boolean isPageLoading = true;
    private ArrayList<String[]> mapedRows = new ArrayList<>();
    private String[] excelHeader;
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
            } catch (Exception e) {
                setPageLoading(true);
                dialog.getDialog().setVisible(false);
                SwingUtilities.invokeLater(() -> CardPage.showPage(DisplayState.MAIN_MENU));
            }
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void init() {
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new BorderLayout());
        String[] columnList = { "List" };
        String[] columnSelectedList = { "Selected List" };
        List<String[]> regex = RegexPattern.loadRegexProperties();
        ArrayList<String[]> tempDairyBreed = new ArrayList<>(allDairyBreedPattern);

        ArrayList<String[]> filter = RegexPattern.filterData(tempDairyBreed, regex.get(15)[1], 0);
        CowsTable table1 = new CowsTable(filter, columnList, false);
        CowsTable table2 = new CowsTable(new ArrayList<>(), columnSelectedList, false);

        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel keyLabel = new JLabel("Key Breed:");
        JTextField keyTextField = new JTextField(10);

        keyPanel.add(keyLabel);
        keyPanel.add(keyTextField);

        JButton moveToTable2Button = new JButton("ย้ายไป Selected List >>");
        moveToTable2Button.setFont(Element.getFont(15));
        JButton moveToTable1Button = new JButton("<< ย้ายไป List");
        moveToTable1Button.setFont(Element.getFont(15));

        moveToTable2Button.addActionListener(
                event -> moveSelectedRows(table1.getTable(), table1.getTableModel(), table2.getTableModel()));
        moveToTable1Button.addActionListener(
                event -> moveSelectedRows(table2.getTable(), table2.getTableModel(), table1.getTableModel()));

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(table1);
        tablePanel.add(table2);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setPreferredSize(new Dimension(0, 50));
        buttonPanel.add(moveToTable2Button);
        buttonPanel.add(moveToTable1Button);

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0, 50));
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(15));
        backButton.addActionListener(event -> CardPage.showPage(DisplayState.MAIN_MENU));

        JButton exportButton = new JButton("ส่งออกเป็นไฟล์ Excel (.xlsx)");
        exportButton.setFont(Element.getFont(15));
        exportButton.addActionListener(event -> WriteXlsxFile.exportToExcel(excelHeader , mapedRows));

        JButton mapButton = new JButton("ยืนยัน");
        mapButton.setFont(Element.getFont(15));
        mapButton.addActionListener(
                event -> mapKey(table2.getTable(), keyTextField, (DefaultTableModel) table2.getTable().getModel()));

        menuBarPanel.add(backButton);
        menuBarPanel.add(exportButton);
        menuBarPanel.add(mapButton);
        menuBarPanel.add(keyPanel);

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

    private void mapKey(JTable rightTable, JTextField keyTextField, DefaultTableModel rightTableModel) {
        String key = keyTextField.getText().trim();

        int rightRowCount = rightTableModel.getRowCount();
        if (key.equals("")) {
            System.out.println("Dont have key");
            return;
        }

        // เตรียมหัวของไฟล์ Excel
        excelHeader = new String[rightTableModel.getColumnCount() + 1];
        excelHeader[0] = "Key";
        for (int i = 1; i < rightTableModel.getColumnCount() + 1; i++) {
            excelHeader[i] = "String " + i;
        }

        for (int i = rightRowCount - 1; i >= 0; i--) {
            String[] rowData = new String[rightTableModel.getColumnCount() + 1]; // เก็บข้อมูลของแถวที่ถูกลบ
            rowData[0] = key; // เพิ่ม Key ในแถวแรก
            for (int j = 1; j < rightTableModel.getColumnCount() + 1; j++) {
                rowData[j] = rightTableModel.getValueAt(i, j - 1).toString();
            }
            mapedRows.add(rowData); // เพิ่มข้อมูลที่ถูกลบลงในรายการ
            rightTableModel.removeRow(i);
        }
        System.out.println("MAP SUCCESS " + key);
        keyTextField.setText("");
    }

    
    public boolean isPageLoading() {
        return isPageLoading;
    }

    public void setPageLoading(boolean pageLoading) {
        isPageLoading = pageLoading;
    }
}
