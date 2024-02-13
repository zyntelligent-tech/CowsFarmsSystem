package com.jozzz.gui.component;

import com.jozzz.constant.DisplayState;
import com.jozzz.gui.dip.component.CowDetail;
import com.jozzz.util.CardPage;
import com.jozzz.util.CustomTable;
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
    private final JTable table;
    private  final DefaultTableModel tableModel;

    private final String[] dipSameColumn = { "cow_dpo_id", "cow_dpo_name", "dpo_mem_name", "dpo_mem_surname", "dpo_center"};
    private final String[] dairySameColumn = { "cow_zyan_id", "cow_zyan_name", "zyan_mem_name", "zyan_mem_surname", "zyan_center"};
    private final String[] dipColumn = { "dpo_mem_id"};
    private final String[] dairyColumn = { "zyan_code", "zyan_mem_id", "zyan_center", "zyan_farm_id", "zyan_farm_name"};

    private final String[] dairyDisplayBISColumn = { "breed_id_string"  };
    private final String[] dairyDisplayNewBISColumn = { "new_breed_id_string"};

    public CowsTable(ArrayList<String[]> allData, String[] columnNames, boolean isViewDetail){
        this.allData = allData;
        this.columnNames = columnNames;
        this.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(allData.toArray(new Object[0][0]), columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        for (int i = 0; i < table.getColumnCount(); i++) {
            String columnName = table.getColumnName(i);

            if (contains(dipSameColumn, columnName)) {
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(Color.CYAN));
            } else if (contains(dairySameColumn, columnName)) {
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(Color.PINK));
            } else if (contains(dipColumn, columnName)) {
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(Color.BLUE));
            } else if (contains(dairyColumn, columnName)) {
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(Color.MAGENTA));
            } else if (contains(dairyDisplayBISColumn , columnName)){
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(new Color(255,179,0)));
            } else if (contains(dairyDisplayNewBISColumn , columnName)){
                table.getColumnModel().getColumn(i).setCellRenderer(new CustomTable(new Color(255,65,65)));
            }
        }
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
                        CardPage.addPage(new CowDetail(cowCode), DisplayState.COW_DETAIL);
                        CardPage.showPage(DisplayState.COW_DETAIL);
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
        scrollPane.getHorizontalScrollBar().setUnitIncrement(50);

        this.add(scrollPane);
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public ArrayList<String[]> getAllData() {
        return allData;
    }

    public String[] getColumnNames(){
        return columnNames;
    }

    private boolean contains(String[] array, String value) {
        for (String element : array) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
