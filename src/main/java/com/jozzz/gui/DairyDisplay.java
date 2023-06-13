package com.jozzz.gui;

import com.jozzz.Main;
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

        String percentRegx = "\\d+\\.*\\d*\\s*%$";
        String numberRegx = "\\d+\\.*\\d*\\s*";
        String percentAndEngRegx = "\\d+\\.*\\d*\\s*%\\s*([a-zA-Z]+\\s*)+";
        String percentAndThaiRegx = "\\d+\\.*\\d*\\s*%\\s*([ก-๙]+\\s*)+";
        String engAndPercentRegx = "([a-zA-Z]+\\s*)+\\d+\\.*\\d*\\s*%";
        String thaiAndPercentRegx = "([ก-๙]+\\s*)+\\d+\\.*\\d*\\s*%";
        String numAndEngRegx = "\\d+\\.*\\d*\\s*([a-zA-Z]+\\s*)+";
        String numAndThaiRegx = "\\d+\\.*\\d*\\s*([ก-๙]+\\s*)+";
        String engAndNumRegx = "([a-zA-Z]+\\s*)+\\d+\\.*\\d*";
        String thaiAndNumRegx = "([ก-๙]+\\s*)+\\d+\\.*\\d*";
        String engMultiRegx = "(\\d+\\.*\\d*\\s*%\\s*[a-zA-Z]*\\s*)+";
        String thaiMultiRegx = "(\\d+\\.*\\d*\\s*%\\s*[ก-๙]*\\s*)+";
        String commaRegx = ".+\\,.+";
        String plusRegx = ".+\\+.+";
        String letterAndNumMultiRegx = "([a-zA-Zก-๙]*\\s*\\d+\\.*\\d*\\s*%\\s*\\s*)+";
        String letterRegx = "([^0-9]+\\s*)+";
        String thaiPercentEng = "([ก-๙]+\\s*)+\\d+\\.*\\d*\\s*%\\s*([a-zA-Z]+\\s*)+";

        allDataTabs = new ArrayList<>();

        new Thread(() -> {
            try {
                listAllPattern = RunDB.getAllDairyBreedPattern();
                allDataTabs.add(new DataTab("Percent",
                        filterData(listAllPattern, percentRegx)));
                allDataTabs.add(new DataTab("Number",
                        filterData(listAllPattern, numberRegx)));
                allDataTabs.add(new DataTab("Percent & Eng",
                        filterData(listAllPattern, percentAndEngRegx)));
                allDataTabs.add(new DataTab("Percent & Thai",
                        filterData(listAllPattern, percentAndThaiRegx)));
                allDataTabs.add(new DataTab("Eng & Percent",
                        filterData(listAllPattern, engAndPercentRegx)));
                allDataTabs.add(new DataTab("Thai & Percent",
                        filterData(listAllPattern, thaiAndPercentRegx)));
                allDataTabs.add(new DataTab("Num & Eng",
                        filterData(listAllPattern, numAndEngRegx)));
                allDataTabs.add(new DataTab("Num & Thai",
                        filterData(listAllPattern, numAndThaiRegx)));
                allDataTabs.add(new DataTab("Eng & Num",
                        filterData(listAllPattern, engAndNumRegx)));
                allDataTabs.add(new DataTab("Thai & Num",
                        filterData(listAllPattern, thaiAndNumRegx)));
                allDataTabs.add(new DataTab("Eng Multi",
                        filterData(listAllPattern, engMultiRegx)));
                allDataTabs.add(new DataTab("Thai Multi",
                        filterData(listAllPattern, thaiMultiRegx)));
                allDataTabs.add(new DataTab("Comma",
                        filterData(listAllPattern, commaRegx)));
                allDataTabs.add(new DataTab("Plus",
                        filterData(listAllPattern, plusRegx)));
                allDataTabs.add(new DataTab("Letter & Num Multi",
                        filterData(listAllPattern, letterAndNumMultiRegx)));
                allDataTabs.add(new DataTab("Letter",
                        filterData(listAllPattern, letterRegx)));
                allDataTabs.add(new DataTab("Thai Per Eng",
                        filterData(listAllPattern, thaiPercentEng)));
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
