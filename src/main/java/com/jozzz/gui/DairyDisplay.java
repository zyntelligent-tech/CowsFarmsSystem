package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;
import com.jozzz.util.WriteXlsxFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DairyDisplay extends JPanel {

    private ArrayList<String[]> listPercentFormat;
    private ArrayList<String[]> list100perFormat;
    private ArrayList<String[]> listPer100Format;
    private ArrayList<String[]> listThai100PerFormat;
    private ArrayList<String[]> listHasPlusFormat;
    private ArrayList<String[]> listHasCommaFormat;
    private ArrayList<String[]> listThaiFormat;
    private ArrayList<String[]> listEngFormat;
    private ArrayList<String[]> listOnlyNumFormat;
    private ArrayList<String[]> listOtherFormat;
    private JTabbedPane tabbedPane;
    private final String[] columnAlLBreed = {"breed_uuid", "breed_code", "breed_name"};
    public DairyDisplay(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());

        Dialog dialog = new Dialog();
        new Thread(() -> {
            try {
                listPercentFormat = RunDB.getDairyFormat("PERCENT");
                list100perFormat = RunDB.getDairyFormat("100%HF");
                listPer100Format = RunDB.getDairyFormat("HF100%");
                listThai100PerFormat = RunDB.getDairyFormat("THAI100%");
                listHasPlusFormat = RunDB.getDairyFormat("HAS+");
                listHasCommaFormat = RunDB.getDairyFormat("HAS,");
                listThaiFormat = RunDB.getDairyFormat("THAI");
                listEngFormat = RunDB.getDairyFormat("ENG");
                listOnlyNumFormat = RunDB.getDairyFormat("OnlyNum");
                listOtherFormat = RunDB.getDairyFormat("Other");
                createTable();
            }catch (Exception ignored){}
            SwingUtilities.invokeLater(() -> dialog.getDialog().setVisible(false));
        }).start();
        dialog.getDialog().setVisible(true);

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(15));
        backButton.addActionListener(event -> Element.getCardLayout().show(Main.display, "MAIN_MENU"));

        JButton exportButton = new JButton("ส่งออกเป็นไฟล์ Excel (.xlsx)");
        exportButton.setFont(Element.getFont(15));
        exportButton.addActionListener(event -> WriteXlsxFile.selectFileDialog(tabbedPane));

        menuBarPanel.add(backButton);
        menuBarPanel.add(exportButton);
        this.add(menuBarPanel, BorderLayout.NORTH);
    }

    private void createTable(){
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Element.getFont(15));

        tabbedPane.add("Percent Format ("+decimalFormat(listPercentFormat.size())+" รายการ)",
                new CowsTable(listPercentFormat, columnAlLBreed, false));
        tabbedPane.add("100%HF Format ("+decimalFormat(list100perFormat.size())+" รายการ)",
                new CowsTable(list100perFormat, columnAlLBreed, false));
        tabbedPane.add("HF100% Format ("+decimalFormat(listPer100Format.size())+" รายการ)",
                new CowsTable(listPer100Format, columnAlLBreed, false));
        tabbedPane.add("Thai100% Format ("+decimalFormat(listThai100PerFormat.size())+" รายการ)",
                new CowsTable(listThai100PerFormat, columnAlLBreed, false));
        tabbedPane.add("HAS + Format ("+decimalFormat(listHasPlusFormat.size())+" รายการ)",
                new CowsTable(listHasPlusFormat, columnAlLBreed, false));
        tabbedPane.add("HAS , Format ("+decimalFormat(listHasCommaFormat.size())+" รายการ)",
                new CowsTable(listHasCommaFormat, columnAlLBreed, false));
        tabbedPane.add("Thai Format ("+decimalFormat(listThaiFormat.size())+" รายการ)",
                new CowsTable(listThaiFormat, columnAlLBreed, false));
        tabbedPane.add("Eng Format ("+decimalFormat(listEngFormat.size())+" รายการ)",
                new CowsTable(listEngFormat, columnAlLBreed, false));
        tabbedPane.add("Only Number Format ("+decimalFormat(listOnlyNumFormat.size())+" รายการ)",
                new CowsTable(listOnlyNumFormat, columnAlLBreed, false));
        tabbedPane.add("Other Format ("+decimalFormat(listOtherFormat.size())+" รายการ)",
                new CowsTable(listOtherFormat, columnAlLBreed, false));

        this.add(tabbedPane);
//        this.validate();
    }



    public String decimalFormat (int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number);
    }

}
