package com.jozzz.gui;

import com.jozzz.constant.DisplayState;
import com.jozzz.util.Dialog;
import com.jozzz.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DipDairyMergeDisplay extends JPanel{
    private boolean isPageLoading = true;
    private JTabbedPane tabbedPane;
    private ArrayList<String[]> allDipDairyCompare;
    private ArrayList<String[]> allDipSameCows;
    private ArrayList<String[]> allDairySameCows;
    private ArrayList<String[]> allDIPCow;
    private ArrayList<String[]> allDairyCow;
    private final String[] dipCowsColumn = {"farmerId", "id", "status", "date", "nickName", "c_oth", "birthDate", "momId", "dadId", "gender", "outfg", "milk", "eurbrd", "eurper"};
    private final String[] dairyCowsColumn = {"cow_id", "cow_uuid", "cow_code", "zyan_code", "farm_code", "nid_code", "rfid_code", "dpo_code", "cow_ear_code", "cow_fa_code", "cow_fa_zyan_code", "cow_fa_nid", "cow_fa_rfid", "cow_fa_dpo", "cow_ma_code", "cow_ma_zyan_code", "cow_ma_nid", "cow_ma_rfid", "cow_ma_dpo", "cow_name", "cow_birth"};
    private final String[] dipDairyCompareColumn = { "zyan_code", "cow_zyan_id", "cow_zyan_name", "cow_dpo_id", "cow_dpo_name", "zyan_mem_id", "zyan_mem_name", "zyan_mem_surname", "dpo_mem_id", "dpo_mem_name", "dpo_mem_surname", "zyan_center", "dpo_center", "zyan_farm_id", "zyan_farm_name"};

    public DipDairyMergeDisplay(){
        Dialog dialog = new Dialog();
        new Thread(() -> {
            try {
                allDipDairyCompare = RunDB.getAllDipDairyCompare();
//                allDipSameCows = RunDB.getAllDipSameCows();
//                allDairySameCows = RunDB.getAllDairySameCows();
//                allDairyCow = RunDB.getAllDairyCows();
//                allDIPCow = RunDB.getAllCows();
                setPageLoading(false);
                SwingUtilities.invokeLater(() -> {
                    init();
                    dialog.getDialog().setVisible(false);
                });
            }catch (Exception e){
                setPageLoading(true);
                dialog.getDialog().setVisible(false);
                SwingUtilities.invokeLater(() -> CardPage.showPage(DisplayState.MAIN_MENU));
            }
            SwingUtilities.invokeLater(() -> dialog.getDialog().setVisible(false));
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void init(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());

//        Iterator<String[]> dipIterator = allDIPCow.iterator();
//        while (dipIterator.hasNext()) {
//            String[] cow = dipIterator.next();
//            for (String[] dipCow : allDipSameCows) {
//                if (Arrays.equals(cow, dipCow)) {
//                    dipIterator.remove();
//                    break;
//                }
//            }
//        }
//
//        Iterator<String[]> dairyIterator = allDairyCow.iterator();
//        while (dairyIterator.hasNext()) {
//            String[] cow = dairyIterator.next();
//            for (String[] dairyCow : allDairySameCows) {
//                if (Arrays.equals(cow, dairyCow)) {
//                    dairyIterator.remove();
//                    break;
//                }
//            }
//        }


        createTable();

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(15));
        backButton.addActionListener(event -> CardPage.showPage(DisplayState.MAIN_MENU));

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

        tabbedPane.add("วัว DIP & Dairy ที่ซ้ำกัน ("+decimalFormat(allDipDairyCompare.size())+" รายการ)",
                new CowsTable(allDipDairyCompare, dipDairyCompareColumn, false));

//        tabbedPane.add("วัว DIP ที่ซ้ำ ("+decimalFormat(allDipSameCows.size())+" รายการ)",
//                new CowsTable(allDipSameCows, dipCowsColumn, false));
//        tabbedPane.add("วัว DIP ที่ไม่ซ้ำ ("+decimalFormat(allDIPCow.size())+" รายการ)",
//                new CowsTable(allDIPCow, dipCowsColumn, false));
//
//        tabbedPane.add("วัว Dairy ที่ซ้ำ ("+decimalFormat(allDairySameCows.size())+" รายการ)",
//                new CowsTable(allDairySameCows, dairyCowsColumn, false));
//        tabbedPane.add("วัว Dairy ที่ไม่ซ้ำ ("+decimalFormat(allDairyCow.size())+" รายการ)",
//                new CowsTable(allDairyCow, dairyCowsColumn, false));

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
