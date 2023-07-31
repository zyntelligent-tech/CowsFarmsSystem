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
            List<String> breedIdList = new ArrayList<>();
            List<String> breedPercentList = new ArrayList<>();
            List<String> newFormatList = new ArrayList<>();
            boolean hasNA = false;
            Matcher matcher = letterPattern.matcher(value[1]);

            while (matcher.find()) {
                String letter = matcher.group();
                if (!findBreedId(letter).equals("")){
                    breedIdList.add(findBreedId(letter));
                }
            }
            if (breedIdList.isEmpty()){
                matcher = letterPattern.matcher(value[0]);
                while (matcher.find()) {
                    String letter = matcher.group();
                    if (!findBreedId(letter).equals("")){
                        breedIdList.add(findBreedId(letter));
                        if (findBreedId(letter).equalsIgnoreCase("NA")){
                            hasNA = true;
                        }
                    }
                }
            }
            double divisorBreed = Math.pow(2,breedIdList.size());
            BigDecimal sumBreed = BigDecimal.ZERO;
            double perLeftover = 0;
            matcher = numberPattern.matcher(value[1]).find()
                    ? numberPattern.matcher(value[1])
                    : numberPattern.matcher(value[0]);
            while (matcher.find()) {
                String number = matcher.group();
                // if (Double.parseDouble(number) <= 100){
                    sumBreed = sumBreed.add(new BigDecimal(number));
                    breedPercentList.add(number);
                // }
            }

            dataArr[0] = String.join(",", breedIdList);
            dataArr[1] = String.join(",", breedPercentList);

            value[2] = isCorrectBreed(dataArr[0], dataArr[1])
                    ? dataArr[0] + ":" + dataArr[1] : "";

            int comparisonResult = sumBreed.compareTo(new BigDecimal("100"));
            int count = 0;
            if (breedPercentList.size() > 1){
                double perBreed = 0; 
                for(int i = 0 ; i < breedPercentList.size() ; i++){
                    perBreed = perBreed + decimalBreed(breedPercentList.get(i));
                }
                if (comparisonResult < 0){ // less than 100
                    for(String percent : breedPercentList){{
                        if(percent.equals("0")){
                            count ++;
                        }
                    }}
                    if(count == 0){
                        int i = 0;
                        perLeftover = 100.0 - perBreed;
                        for(String id : breedIdList){
                            if(id.equals("20")){
                                break;
                            }   
                            i++;
                        } 
                        if(i == breedIdList.size()){
                            breedIdList.add("20");
                            breedPercentList.add(Double.toString(perLeftover));
                        }else{
                            breedPercentList.set(i, Double.toString(perLeftover));
                        }
                    }else{
                        perLeftover = 100.0 - perBreed;
                        double resultDiv = perLeftover / count;
                        int i = 0;
                        if(Double.toString(resultDiv).contains(".")){
                            if(Double.toString(resultDiv).split("\\.")[1].length() > 3){
                                resultDiv = perLeftover / (count + 1);
                                if(Double.toString(resultDiv).split("\\.")[1].length() > 3){   
                                    for(String id : breedIdList){
                                        if(id.equals("20")){
                                            break;
                                        }   
                                        i++;
                                    }     
                                    if(i == breedIdList.size()){
                                        double mod = perLeftover % 6;
                                        if(perLeftover > 3){
                                            mod = perLeftover % 3;
                                            perLeftover = perLeftover - mod;
                                            breedIdList.add("20");
                                            breedPercentList.add(Double.toString(mod));
                                        }else{
                                            mod = 3 - perLeftover;
                                            perLeftover = perLeftover + mod;
                                            breedPercentList.set(0, Double.toString(Double.parseDouble(breedPercentList.get(0)) - mod));
                                        }
                                        resultDiv = perLeftover / count ;
                                        for(String percent : breedPercentList){
                                            if(percent.equals("0")){
                                                breedPercentList.set(i, Double.toString(resultDiv));
                                            }
                                            i++;
                                        }     
                                    }else{
                                        breedPercentList.set(i,Integer.toString(Integer.parseInt(breedPercentList.get(i)) + 1));
                                        resultDiv = (perLeftover - 1) / (count - 1);
                                    }
                                    i = 0;
                                    for(String percent : breedPercentList){
                                        if(percent.equals("0")){
                                            breedPercentList.set(i, Double.toString(resultDiv));
                                        }
                                        i++;
                                    }                
                                }else{
                                    for(String percent : breedPercentList){
                                        if(i == 0){
                                            breedPercentList.set(i ,Double.toString(Double.parseDouble(breedPercentList.get(i)) + resultDiv));
                                        }
                                        if(percent.equals("0")){
                                            breedPercentList.set(i, Double.toString(resultDiv));
                                        }
                                        i++;
                                    }  
                                }
                            }else{
                                for(String percent : breedPercentList){
                                    if(percent.equals("0")){
                                        breedPercentList.set(i, Double.toString(resultDiv));
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                    for(String percent : breedPercentList){
                        newFormatList.add(newBreedFormat(Double.parseDouble(percent)));
                    }
                }
                else if (comparisonResult > 0){ // greater than 100
                   
                }
            }
            else if (breedPercentList.size() == 1){
                double perBreed = decimalBreed(breedPercentList.get(0));
                if (comparisonResult < 0){ // less than 100
                    breedIdList.add(findBreedId("NA"));
                    perLeftover = 100.0 - perBreed;
                    sumBreed = BigDecimal.valueOf(perBreed);
                    newFormatList.add(newBreedFormat(perBreed));
                    newFormatList.add(newBreedFormat(perLeftover));
                    breedPercentList.add(String.valueOf(perLeftover));
                }
                else if (comparisonResult > 0){ // greater than 100
                    System.out.println(breedIdList);
                    System.out.println(breedPercentList);
                    
                }
                else {
                    newFormatList.add(newBreedFormat(perBreed));
                }
            }

            dataArr[0] = String.join(",", breedIdList);
            dataArr[2] = String.join(",", newFormatList);

//            if (breedIdList.size() > 1 && comparisonResult > 0){
//                breedIdList = new ArrayList<>();
//                sumBreed = BigDecimal.ZERO;
//                for (String percentBreed : dataArr[1].split(",")){
//                    double result = Double.parseDouble(percentBreed);
//                    if (percentBreed.contains(".")){
//                        result = Double.parseDouble(percentBreed.split("\\.")[0])
//                                + Math.round(Double.parseDouble("0."+percentBreed.split("\\.")[1])
//                                * divisorBreed)
//                                / divisorBreed;
//                    }
//                    sumBreed = sumBreed.add(new BigDecimal(result));
//                    breedIdList.add(String.valueOf(result));
//                }
//                dataArr[1] = String.join(",", breedIdList);
//            }

            value[3] = perLeftover == 0
                    ? String.valueOf(sumBreed)
                    : sumBreed + "+" + perLeftover;

            value[4] = isCorrectBreed(dataArr[0], dataArr[2])
                    ? dataArr[0] + ":" + dataArr[2] : "";

            if (!value[2].equals("")){
                selectedBreed.add(value);
            }
        }
        return data;
    }

    private String newBreedFormat(Double percent){
        if (String.valueOf(percent).contains(".")){
            if (String.valueOf(percent).split("\\.")[1].trim().equals("0")){
                return String.valueOf(percent).split("\\.")[0] +"+0/1";
            }
            double r = Math.round(percent * 512f) / 512f;
            long d = (long) Math.pow(10, String.valueOf(r).split("\\.")[1].length());
            long x = Integer.parseInt(String.valueOf(r).split("\\.")[1]);
            long gcd = gcd(x, d);

            long re = x / gcd;
            long di = d / gcd;

            return String.valueOf(r).split("\\.")[0] +"+"+ re + "/" + di;
        }
        return percent +"+0/1";
    }

    private double decimalBreed(String percent){
        if (percent.contains(".")){
            if (percent.split("\\.")[1].trim().equals("0")){
                return Double.parseDouble(percent);
            }
            double r = Math.round(Double.parseDouble(percent) * 512f) / 512f;
            long d = (long) Math.pow(10, String.valueOf(r).split("\\.")[1].length());
            long num = Integer.parseInt(String.valueOf(r).split("\\.")[0]);
            long x = Integer.parseInt(String.valueOf(r).split("\\.")[1]);
            long gcd = gcd(x, d);

            double re = (double) x / gcd;
            double di = (double) d / gcd;

            return num + re / di;
        }
        return Double.parseDouble(percent);
    }

    private long gcd(long a, long b){
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
