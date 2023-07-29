package com.jozzz.gui;

import com.jozzz.constant.DisplayState;
import com.jozzz.cow_format.RegexPattern;
import com.jozzz.records.DataTab;
import com.jozzz.util.Dialog;
import com.jozzz.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DairyDisplay extends JPanel {
    private ArrayList<String[]> listAllPattern;
    private ArrayList<String[]> listAllBreedMain;
    private ArrayList<String[]> selectedBreed;
    private JTabbedPane tabbedPane;
    private ArrayList<DataTab> allDataTabs;
    private final String[] columnAlLBreed = {"breed_uuid", "breed_code", "breed_name"};
    private final String[] columnAlLPattern = {"breed_code","breed_name", "breed_id_string", "sumBreed", "new_format"};
    private boolean isPageLoading = true;
    public DairyDisplay(){
        Dialog dialog = new Dialog();
        new Thread(() -> {
            try {
                listAllPattern = RunDB.getAllDairyBreedPattern();
                listAllBreedMain = RunDB.getAllDairyBreedMain();
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
        }).start();
        dialog.getDialog().setVisible(true);
    }

    private void init(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());

        RegexPattern.setRegexProperties();
        List<String[]> regexList = RegexPattern.loadRegexProperties();
        allDataTabs = new ArrayList<>();
        selectedBreed = new ArrayList<>();
        ArrayList<String[]> notHaveStringBreed = new ArrayList<>(listAllPattern);

        for (String[] regex : regexList) {
            ArrayList<String[]> filteredData = filterData(listAllPattern, regex[1]);

            allDataTabs.add(new DataTab(regex[0], spiltPattern(filteredData)));
            listAllPattern.removeAll(filteredData);
        }
        allDataTabs.add(new DataTab("Other",
                listAllPattern));

        notHaveStringBreed.removeAll(selectedBreed);

        allDataTabs.add(new DataTab("BreedIdString", selectedBreed));
        allDataTabs.add(new DataTab("NotHaveBreedIdString", notHaveStringBreed));
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
        return filteredData;
    }

    public ArrayList<String[]> spiltPattern(ArrayList<String[]> data){

        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern letterPattern = Pattern.compile("[ก-๙a-zA-Z]+(\\s*[ก-๙a-zA-Z]+)*");

        for (String[] value : data) {
            String[] dataArr = new String[3];
            List<String> dataList = new ArrayList<>();
            List<String> newFormatList = new ArrayList<>();
            boolean hasNA = false;
            Matcher matcher = letterPattern.matcher(value[1]);

            while (matcher.find()) {
                String letter = matcher.group();
                if (!findBreedId(letter).equals("")){
                    dataList.add(findBreedId(letter));
                }
            }
            if (dataList.isEmpty()){
                matcher = letterPattern.matcher(value[0]);
                while (matcher.find()) {
                    String letter = matcher.group();
                    if (!findBreedId(letter).equals("")){
                        dataList.add(findBreedId(letter));
                        if (findBreedId(letter).equalsIgnoreCase("NA")){
                            hasNA = true;
                        }
                    }
                }
            }
            dataArr[0] = String.join(",", dataList);
            double divisorBreed = Math.pow(2,dataList.size());
            dataList = new ArrayList<>();
            BigDecimal sumBreed = BigDecimal.ZERO;
            matcher = numberPattern.matcher(value[1]).find()
                    ? numberPattern.matcher(value[1])
                    : numberPattern.matcher(value[0]);
            while (matcher.find()) {
                String number = matcher.group();
                if (Double.parseDouble(number) <= 100){
                    sumBreed = sumBreed.add(new BigDecimal(number));
                    dataList.add(number);
                    newFormatList.add(newBreedFormat(Double.valueOf(number)));
                }
            }
            dataArr[1] = String.join(",", dataList);
            dataArr[2] = String.join(",", newFormatList);

            int comparisonResult = sumBreed.compareTo(new BigDecimal("100"));

            if (dataList.size() > 1 && comparisonResult > 0){
                dataList = new ArrayList<>();
                sumBreed = BigDecimal.ZERO;
                for (String percentBreed : dataArr[1].split(",")){
                    double result = Double.parseDouble(percentBreed);
                    if (percentBreed.contains(".")){
                        result = Double.parseDouble(percentBreed.split("\\.")[0])
                                + Math.round(Double.parseDouble("0."+percentBreed.split("\\.")[1])
                                * divisorBreed)
                                / divisorBreed;
                    }
                    sumBreed = sumBreed.add(new BigDecimal(result));
                    dataList.add(String.valueOf(result));
                }
                dataArr[1] = String.join(",", dataList);
            }
            value[2] = isCorrectBreed(dataArr[0], dataArr[1])
                    ? dataArr[0] + ":" + dataArr[1] : "";

            value[3] = String.valueOf(sumBreed);

            value[4] = isCorrectBreed(dataArr[0], dataArr[2])
                    ? dataArr[0] + ":" + dataArr[2] : "";

            if (!value[2].equals("")){
                selectedBreed.add(value);
            }
        }
        return data;
    }

    private static String newBreedFormat(Double percent){
        if (String.valueOf(percent).contains(".")){
            if (String.valueOf(percent).split("\\.")[1].trim().equals("0")){
                return String.valueOf(percent).split("\\.")[0] +"+0/1";
            }
            double r = Math.round(percent * 512f) / 512f;
            int d = (int) Math.pow(10, String.valueOf(r).split("\\.")[1].length());
            int x = Integer.parseInt(String.valueOf(r).split("\\.")[1]);
            int gcd = gcd(x, d);

            int re = x / gcd;
            int di = d / gcd;

            return String.valueOf(r).split("\\.")[0] +"+"+ re + "/" + di;
        }
        return percent +"+0/1";
    }

    private static int gcd(int a, int b){
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    private String findBreedId(String breedStr){
        for(String[] breed : listAllBreedMain){
            if (similarStr(breed[1], (breedStr))
            || similarStr(breed[2], (breedStr))
            || similarStr(breed[3], (breedStr))){
                return breed[0];
            }
        }
        return "";
    }

    private ArrayList<String[]> findBreedIdList(String breedStr){
        ArrayList<String[]> selectedBreed = new ArrayList<>();
        for(String[] breed : listAllBreedMain){
            if (breed[1].trim().equalsIgnoreCase(breedStr.trim())
                    || breed[2].trim().equalsIgnoreCase(breedStr.trim())
                    || breed[3].trim().equalsIgnoreCase(breedStr.trim())){
                selectedBreed.add(breed);
            }
        }
        return selectedBreed;
    }

    private boolean similarStr(String str1, String str2) {
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();

        for (char c : str1.trim().toLowerCase().toCharArray()) {
            set1.add(c);
        }

        for (char c : str2.trim().toLowerCase().toCharArray()) {
            set2.add(c);
        }

        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        double similarity = (double) intersection.size() / union.size();
        return similarity > 0.7;
    }

    private boolean isCorrectBreed(String str1, String str2) {
        return (!str1.equals("") && !str2.equals(""))
                && str1.split(",").length == str2.split(",").length;
    }

    public boolean isPageLoading() {
        return isPageLoading;
    }

    public void setPageLoading(boolean pageLoading) {
        isPageLoading = pageLoading;
    }
}
