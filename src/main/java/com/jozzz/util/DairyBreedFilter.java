package com.jozzz.util;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DairyBreedFilter {
    private static Connection connection;
    public static void main(String[] args) {
        ArrayList<String> allBreed = getAllDairyBreedPatternOnly();
        String excelFile = "filterData.xlsx";
        Workbook workbook = new XSSFWorkbook();

        String pattern1 = "\\d+\\.*\\d*\\s*%$";
        List<String> filter1 = filterData(allBreed, pattern1);
        Sheet sheet1 = workbook.createSheet("PercentData");
        createSheet(filter1, sheet1);
        allBreed.removeAll(filter1);

        String pattern2 = "\\d+\\.*\\d*\\s*%*\\s*([a-zA-Zก-๙]+\\s*)+";
        List<String> filter2 = filterData(allBreed, pattern2);
        Sheet sheet2 = workbook.createSheet("NumAndLetter");
        createSheet(filter2, sheet2);
        allBreed.removeAll(filter2);

        String pattern3 = "[a-zA-Zก-๙]+\\s*\\d+\\.*\\d*\\s*%*";
        List<String> filter3 = filterData(allBreed, pattern3);
        Sheet sheet3 = workbook.createSheet("LetterAndNum");
        createSheet(filter3, sheet3);
        allBreed.removeAll(filter3);

        String pattern4 = ".+\\,.+";
        List<String> filter4 = filterData(allBreed, pattern4);
        Sheet sheet4 = workbook.createSheet("Comma");
        createSheet(filter4, sheet4);
        allBreed.removeAll(filter4);

        String pattern5 = "(\\d+\\.*\\d*\\s*%\\s*[a-zA-Zก-๙]*\\s*)+";
        List<String> filter5 = filterData(allBreed, pattern5);
        Sheet sheet5 = workbook.createSheet("ManyBreed");
        createSheet(filter5, sheet5);
        allBreed.removeAll(filter5);

        String pattern6 = ".+\\+.+";
        List<String> filter6 = filterData(allBreed, pattern6);
        Sheet sheet6 = workbook.createSheet("Plus");
        createSheet(filter6, sheet6);
        allBreed.removeAll(filter6);

        String pattern7 = "([a-zA-Zก-๙]*\\s*\\d+\\.*\\d*\\s*%\\s*\\s*)+";
        List<String> filter7 = filterData(allBreed, pattern7);
        Sheet sheet7 = workbook.createSheet("Let&NamMulti");
        createSheet(filter7, sheet7);
        allBreed.removeAll(filter7);

        String pattern8 = "([^0-9]+\\s*)+";
        List<String> filter8 = filterData(allBreed, pattern8);
        Sheet sheet8 = workbook.createSheet("Letter");
        createSheet(filter8, sheet8);
        allBreed.removeAll(filter8);

        String pattern9 = "\\d+\\.*\\d*\\s*";
        List<String> filter9 = filterData(allBreed, pattern9);
        Sheet sheet9 = workbook.createSheet("Number");
        createSheet(filter9, sheet9);
        allBreed.removeAll(filter9);

        Sheet sheetOther = workbook.createSheet("OtherPattern");
        createSheet(allBreed, sheetOther);

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

    public static  void createSheet(List<String> filter, Sheet sheet){
        int rowNum = 0;
        for (String row : filter) {
            Row excelRow = sheet.createRow(rowNum++);
            Cell cell = excelRow.createCell(0);
            cell.setCellValue(row);
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
