package com.jozzz.gui;

import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Display extends JPanel {

    private static CardLayout cardLayout;
    private ArrayList<String[]> allCow;
    private ArrayList<String[]> allCorrectBreeds;
    private ArrayList<String[]> allErrorBreeds;
    private ArrayList<String[]> allCorrectParent;
    private ArrayList<String[]> allErrorParent;
    private ArrayList<String[]> allPerIntBreed;
    public Display(){

        cardLayout = new CardLayout();
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(cardLayout);

        Dialog dialog = new Dialog();
        new Thread(() -> {
            allCow = RunDB.getAllCows();
            allCorrectBreeds = RunDB.getAllCorrectBreed();
            allErrorBreeds = RunDB.getAllErrorBreed();
            allCorrectParent = RunDB.getAllCorrectParent();
            allErrorParent = RunDB.getAllErrorParent();
            allPerIntBreed = RunDB.getAllPerIntBreed();
            createTable();
            SwingUtilities.invokeLater(() -> dialog.getDialog().setVisible(false));
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void createTable(){
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Element.getFont(15));

        String[] columnAlLCows = {"เลขเกษตรกร", "หมายเลขโค", "สถานะโค","วันที่", "ชื่อโค", "c_oth", "วันเกิด"
                , "หมายเลขแม่", "หมายเลขพ่อ", "เพศ", "outfg", "milk", "eurbrd", "eurper"};
        tabbedPane.add("All Cows วัวทั้งหมด ("+decimalFormat(allCow.size())+" รายการ)",
                new CowsTable(allCow, columnAlLCows, this));

        String[] columnAlLBreed = {"หมายเลขโค", "สายพันธุ์", "เปอร์เซ็นต์รวม"};
        tabbedPane.add("Cows Correct ข้อมูลวัว 100 % ("+decimalFormat(allCorrectBreeds.size())+" รายการ)",
                new CowsTable(allCorrectBreeds, columnAlLBreed, this));
        tabbedPane.add("Cows Error ข้อมูลวัวไม่ 100 % ("+decimalFormat(allErrorBreeds.size())+" รายการ)",
                new CowsTable(allErrorBreeds, columnAlLBreed, this));

        tabbedPane.add("Correct Parent พ่อแม่ถูกต้อง ("+decimalFormat(allCorrectParent.size())+" รายการ)",
                new CowsTable(allCorrectParent, columnAlLCows, this));
        tabbedPane.add("Error Parent พ่อแม่ไม่ถูกต้อง ("+decimalFormat(allErrorParent.size())+" รายการ)",
                new CowsTable(allErrorParent, columnAlLCows, this));

        this.add(tabbedPane, "COWS_TABLE");
        this.validate();
    }

    public static CardLayout getCardLayout() {
        return cardLayout;
    }

    public String decimalFormat (int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number);
    }
}
