package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.constant.DisplayState;
import com.jozzz.cow_format.RegexPattern;
import com.jozzz.records.DataTab;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DairyDisplay extends JPanel {
    private ArrayList<String[]> listAllPattern;
    private JTabbedPane tabbedPane;
    private final ArrayList<DataTab> allDataTabs;
    private final String[] columnAlLBreed = {"breed_uuid", "breed_code", "breed_name"};
    private final String[] columnAlLPattern = {"breed_code","breed_name"};
    public DairyDisplay(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());

        Dialog dialog = new Dialog();
        RegexPattern.setRegexProperties();

        List<String[]> regexList = RegexPattern.loadRegexProperties();


        allDataTabs = new ArrayList<>();

        new Thread(() -> {
            try {
                listAllPattern = RunDB.getAllDairyBreedPattern();
                for (String[] regex : regexList) {
                    allDataTabs.add(new DataTab(regex[0],
                            filterData(listAllPattern, regex[1])));
                }
                allDataTabs.add(new DataTab("Other",
                        listAllPattern));
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

        for (DataTab tab : allDataTabs){
            tabbedPane.add(tab.title(),
                    new CowsTable(tab.data(), columnAlLPattern, false));
        }

        this.add(tabbedPane);
    }

    private ArrayList<String[]> filterData(ArrayList<String[]> inputData, String pattern) {
        ArrayList<String[]> filteredData = new ArrayList<>();

        Pattern regexPattern = Pattern.compile(pattern);

        for (String[] value : inputData) {
            Matcher matcher = regexPattern.matcher(value[1].trim());
            if (matcher.matches()) {
                filteredData.add(value);
            }
        }
        inputData.removeAll(filteredData);
        return filteredData;
    }




}
