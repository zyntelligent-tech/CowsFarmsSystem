package com.jozzz.cow_format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DairyBreedFilter {
    private static Connection connection;
    public static Workbook workbook;
    private static List<String> allBreed2;
    public static void main(String[] args) {
        workbook = new XSSFWorkbook();
        List<String> allBreed = getAllDairyBreedPatternOnly();
        allBreed2 = new ArrayList<>(allBreed);
        String excelFile = "filterData.xlsx";

        makeSheet(allBreed2, "allPattern");
        makeSheetMultiCol(spiltPatternAll(allBreed2), "CorrectSpilt");
        makeSheet(allBreed2, "ErrorSpilt");

        String pattern1 = "\\d+\\.*\\d*\\s*%$";
        List<String> filter1 = filterData(allBreed, pattern1);
        makeSheet(filter1, "PercentData");
        makeSheetMultiCol(spiltPattern(filter1), "PercentSpilt");
        allBreed.removeAll(filter1);

        String pattern2 = "\\d+\\.*\\d*\\s*%*\\s*([a-zA-Zก-๙]+\\s*)+";
        List<String> filter2 = filterData(allBreed, pattern2);
        makeSheet(filter2, "NumAndLetter");
        makeSheetMultiCol(spiltPattern(filter2), "NumAndLetterSpilt");
        allBreed.removeAll(filter2);

        String pattern3 = "([ก-๙]+\\s*)+\\d+\\.*\\d*\\s*%*";
        List<String> filter3 = filterData(allBreed, pattern3);
        makeSheet(filter3, "LetterAndNum");
        makeSheetMultiCol(spiltPattern(filter3), "LetterAndNumSpilt");
        allBreed.removeAll(filter3);

        String pattern4 = ".+\\,.+";
        List<String> filter4 = filterData(allBreed, pattern4);
        makeSheet(filter4, "Comma");
        makeSheetMultiCol(spiltPattern(filter4), "CommaSpilt");
        allBreed.removeAll(filter4);

        String pattern5 = "(\\d+\\.*\\d*\\s*%\\s*[a-zA-Zก-๙]*\\s*)+";
        List<String> filter5 = filterData(allBreed, pattern5);
        makeSheet(filter5, "MultiBreed");
        allBreed.removeAll(filter5);

        String pattern6 = ".+\\+.+";
        List<String> filter6 = filterData(allBreed, pattern6);
        makeSheet(filter6, "Plus");
        allBreed.removeAll(filter6);

        String pattern7 = "([a-zA-Zก-๙]*\\s*\\d+\\.*\\d*\\s*%\\s*\\s*)+";
        List<String> filter7 = filterData(allBreed, pattern7);
        makeSheet(filter7, "Let&NamMulti");
        allBreed.removeAll(filter7);

        String pattern8 = "([^0-9]+\\s*)+";
        List<String> filter8 = filterData(allBreed, pattern8);
        makeSheet(filter8, "Letter");
        allBreed.removeAll(filter8);

        String pattern9 = "\\d+\\.*\\d*\\s*";
        List<String> filter9 = filterData(allBreed, pattern9);
        makeSheet(filter9, "Number");
        allBreed.removeAll(filter9);

        makeSheet(allBreed, "OtherPattern");

        try {
            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("Excel file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static List<String[]> spiltPattern(List<String> data){
        List<String[]> spiltData = new ArrayList<>();

        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern letterPattern = Pattern.compile("[ก-๙a-zA-Z]+(\\s*[ก-๙a-zA-Z]+)*");

        for (String value : data) {
            String[] dataArr = new String[2];
            List<String> dataList = new ArrayList<>();
            Matcher matcher = numberPattern.matcher(value);
            while (matcher.find()) {
                String number = matcher.group();
                dataList.add(number);
            }
            dataArr[0] = String.join(",", dataList);
            dataList = new ArrayList<>();
            matcher = letterPattern.matcher(value);
            while (matcher.find()) {
                String letter = matcher.group();
                dataList.add(letter);
            }
            dataArr[1] = String.join(",", dataList);

            spiltData.add(dataArr);
        }

        return spiltData;
    }

    public static List<String[]> spiltPatternAll(List<String> data){
        List<String[]> spiltData = new ArrayList<>();
        HashSet<String> selectedData = new HashSet<>();

        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern letterPattern = Pattern.compile("[ก-๙a-zA-Z]+(\\s*[ก-๙a-zA-Z]+)*");

        for (String value : data) {
            Matcher matcher = numberPattern.matcher(value);
            String[] dataArr = new String[2];
            List<String> dataList = new ArrayList<>();
            while (matcher.find()) {
                selectedData.add(value);
                String number = matcher.group();
                dataList.add(number);
            }
            if (selectedData.contains(value)){
                dataArr[0] = String.join(",", dataList);
                dataList = new ArrayList<>();
                matcher = letterPattern.matcher(value);
                while (matcher.find()) {
                    String letter = matcher.group();
                    dataList.add(letter);
                }
                dataArr[1] = String.join(",", dataList);
                spiltData.add(dataArr);
            }

        }
        data.removeAll(selectedData);

        return spiltData;
    }

    public static  void makeSheet(List<String> filter, String sheetName){
        Sheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        for (String row : filter) {
            Row excelRow = sheet.createRow(rowNum++);
            Cell cell = excelRow.createCell(0);
            cell.setCellValue(row);
        }
    }

    public static  void makeSheetMultiCol(List<String[]> filter, String sheetName){
        Sheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        for (String[] row : filter) {
            Row excelRow = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String cellData : row) {
                Cell cell = excelRow.createCell(colNum++);
                cell.setCellValue(cellData);
            }
        }
    }

    private static List<String> filterData(List<String> inputData, String pattern) {
        List<String> filteredData = new ArrayList<>();

        Pattern regexPattern = Pattern.compile(pattern);

        for (String value : inputData) {
            Matcher matcher = regexPattern.matcher(value.trim());
            if (matcher.matches()) {
                filteredData.add(value);
            }
        }

        return filteredData;
    }

    public static ArrayList<String> getAllDairyBreedPatternOnly(){
        ArrayList<String> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT breed_name" +
                            " FROM tbd_breed" +
                            " GROUP BY breed_name")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    dataList.add(resultSet.getString(1));
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    private static void openDairyDatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mariadb://54.251.168.197:6667/zyanwoadev_test",
                "summer2023",
                "Summ3r!@MISL$$23"
        );
    }

    private static void closeDatabaseConnection() throws SQLException{
        connection.close();
    }
}
