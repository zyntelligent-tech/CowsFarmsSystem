package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WriteXlsxFile {
    public static void main(String[] args) {
        String excelFile = "ErrorCows.xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ErrorCowsNot100");
        Sheet sheet2 = workbook.createSheet("ErrorParent");
        ArrayList<String[]> data1 = RunDB.getAllErrorBreed();
        ArrayList<String[]> data2 = RunDB.getAllErrorParent();

        String[] columnAlLCows = new String[]{"เลขเกษตรกร", "หมายเลขโค", "สถานะโค","วันที่", "ชื่อโค", "c_oth", "วันเกิด"
                , "หมายเลขแม่", "หมายเลขพ่อ", "เพศ", "outfg", "milk", "eurbrd", "eurper"};
        String[] columnAlLBreed = new String[]{"หมายเลขโค", "สายพันธุ์", "เปอร์เซ็นต์รวม"};

        Row headerRow = sheet.createRow(0);
        int headerColNum = 0;
        for (String columnHeader : columnAlLBreed) {
            Cell cell = headerRow.createCell(headerColNum++);
            cell.setCellValue(columnHeader);
        }
        int rowNum = 1;
        for (String[] row : data1) {
            Row excelRow = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String cellData : row) {
                Cell cell = excelRow.createCell(colNum++);
                cell.setCellValue(cellData);
            }
        }

        headerRow = sheet2.createRow(0);
        headerColNum = 0;
        for (String columnHeader : columnAlLCows) {
            Cell cell = headerRow.createCell(headerColNum++);
            cell.setCellValue(columnHeader);
        }
        rowNum = 1;
        for (String[] row : data2) {
            Row excelRow = sheet2.createRow(rowNum++);
            int colNum = 0;
            for (String cellData : row) {
                Cell cell = excelRow.createCell(colNum++);
                cell.setCellValue(cellData);
            }
        }

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
}

