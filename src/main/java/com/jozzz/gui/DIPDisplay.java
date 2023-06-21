package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.constant.DisplayState;
import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;
import com.jozzz.util.WriteXlsxFile;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DIPDisplay extends JPanel {

    private ArrayList<String[]> allCow;
    private ArrayList<String[]> allCorrectBreeds;
    private ArrayList<String[]> allErrorBreeds;
    private ArrayList<String[]> allCorrectParent;
    private ArrayList<String[]> allErrorParent;
    private JTabbedPane tabbedPane;
    private final String[] columnAlLCows = {"เลขเกษตรกร", "หมายเลขโค", "สถานะโค","วันที่", "ชื่อโค", "c_oth", "วันเกิด"
            , "หมายเลขแม่", "หมายเลขพ่อ", "เพศ", "outfg", "milk", "eurbrd", "eurper"};
    private final String[] columnAlLBreed = {"หมายเลขโค", "สายพันธุ์", "เปอร์เซ็นต์รวม"};
    private boolean isPageLoading = true;
    public DIPDisplay(){
        Dialog dialog = new Dialog();
        new Thread(() -> {
            try {
                allCow = RunDB.getAllCows();
                allCorrectBreeds = RunDB.getAllCorrectBreed();
                allErrorBreeds = RunDB.getAllErrorBreed();
                allCorrectParent = RunDB.getAllCorrectParent();
                allErrorParent = RunDB.getAllErrorParent();
                setPageLoading(false);
                dialog.getDialog().setVisible(false);
                SwingUtilities.invokeLater(this::init);
            }catch (Exception e){
                setPageLoading(true);
                dialog.getDialog().setVisible(false);
                SwingUtilities.invokeLater(() -> Element.getCardLayout().show(Main.display, DisplayState.MAIN_MENU));
            }
            SwingUtilities.invokeLater(() -> dialog.getDialog().setVisible(false));
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void init(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());

        createTable();

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(15));
        backButton.addActionListener(event -> Element.getCardLayout().show(Main.display, DisplayState.MAIN_MENU));

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

        tabbedPane.add("All Cows วัวทั้งหมด ("+decimalFormat(allCow.size())+" รายการ)",
                new CowsTable(allCow, columnAlLCows, true));

        tabbedPane.add("Cows Correct ข้อมูลวัว 100 % ("+decimalFormat(allCorrectBreeds.size())+" รายการ)",
                new CowsTable(allCorrectBreeds, columnAlLBreed, true));
        tabbedPane.add("Cows Error ข้อมูลวัวไม่ 100 % ("+decimalFormat(allErrorBreeds.size())+" รายการ)",
                new CowsTable(allErrorBreeds, columnAlLBreed, true));

        tabbedPane.add("Correct Parent พ่อแม่ถูกต้อง ("+decimalFormat(allCorrectParent.size())+" รายการ)",
                new CowsTable(allCorrectParent, columnAlLCows, true));
        tabbedPane.add("Error Parent พ่อแม่ไม่ถูกต้อง ("+decimalFormat(allErrorParent.size())+" รายการ)",
                new CowsTable(allErrorParent, columnAlLCows, true));

        this.add(tabbedPane);
    }

    public String decimalFormat (int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number);
    }

    public boolean isPageLoading() {
        return isPageLoading;
    }

    public void setPageLoading(boolean pageLoading) {
        isPageLoading = pageLoading;
    }
}
