package com.jozzz.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jozzz.gui.component.CowsTable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WriteXlsxFile {

    public static void selectFileDialog(JTabbedPane tabbedPane){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ArrayList<JCheckBox> checkBoxesList = new ArrayList<>();
        ArrayList<CowsTable> cowsTables = new ArrayList<>();

        for (int i=0;i < tabbedPane.getTabCount();i++){
            checkBoxesList.add(new JCheckBox(tabbedPane.getTitleAt(i)));
            cowsTables.add((CowsTable) tabbedPane.getComponentAt(i));
        }

        for (JCheckBox checkBox : checkBoxesList){
            checkBox.setSelected(true);
            checkBox.setFont(Element.getFont(20));
            panel.add(checkBox);
        }

        JButton exportButton = new JButton("ส่งออก");
        exportButton.setFont(Element.getFont(20));
        exportButton.addActionListener(event -> {
            boolean isSelected = false;
            for (JCheckBox checkBox : checkBoxesList){
                if (checkBox.isSelected()){
                    isSelected = true;
                    break;
                }
            }
            if (isSelected){
                JOptionPane.getRootFrame().dispose();
                showExportFileChooser(checkBoxesList, cowsTables, tabbedPane);
            }
            else{
                JLabel label = new JLabel("กรุณาเลือกตารางที่จะส่งออก");
                label.setFont(Element.getFont(20));
                JOptionPane.showMessageDialog(null,label,"กรุณาเลือกตารางที่จะส่งออก",JOptionPane.WARNING_MESSAGE,null);
            }
        });

        JOptionPane.showOptionDialog(null, panel, "กรุณาเลือกตารางที่จะส่งออก",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {exportButton}, null);
    }

    public static void showExportFileChooser(ArrayList<JCheckBox> checkBoxesList , ArrayList<CowsTable> cowsTables, JTabbedPane tabbedPane) {
        setFileChooserUI();
        JFileChooser fileChooser = new JFileChooser();
        setFileChooserFont(fileChooser ,Element.getFont(15));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("."));
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            exportFile(filePath, checkBoxesList, cowsTables, tabbedPane);
        }
    }

    private static void exportFile(String filePath, ArrayList<JCheckBox> checkBoxesList, ArrayList<CowsTable> cowsTables, JTabbedPane tabbedPane){
        String excelFile = filePath + ".xlsx";
        new Thread(() -> {
            Workbook workbook = new XSSFWorkbook();
            int totalProgress = 0;
            int currentProgress = 0;
            for (int i=0;i < checkBoxesList.size();i++) {
                if (checkBoxesList.get(i).isSelected()){
                    totalProgress += cowsTables.get(i).getAllData().size();
                }
            }
            ProgressBar progressBar = new ProgressBar(totalProgress);
            for (int i=0;i < checkBoxesList.size();i++){
                if (checkBoxesList.get(i).isSelected()) {
                    Sheet sheet = workbook.createSheet(tabbedPane.getTitleAt(i));
                    Row headerRow = sheet.createRow(0);
                    int headerColNum = 0;
                    for (String columnHeader : cowsTables.get(i).getColumnNames()) {
                        Cell cell = headerRow.createCell(headerColNum++);
                        cell.setCellValue(columnHeader);
                    }
                    int rowNum = 1;
                    for (String[] row : cowsTables.get(i).getAllData()) {
                        Row excelRow = sheet.createRow(rowNum++);
                        int colNum = 0;
                        for (String cellData : row) {
                            Cell cell = excelRow.createCell(colNum++);
                            cell.setCellValue(cellData);
                        }
                        progressBar.setProgress(currentProgress++);
                    }
                }
            }

            try {
                progressBar.getProgressLabel().setText("กำลังเขียนลงไฟล์...");
                FileOutputStream outputStream = new FileOutputStream(excelFile);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                progressBar.getProgressBarFrame().dispose();
                JLabel label = new JLabel("ส่งออกไฟล์สำเร็จ");
                label.setFont(Element.getFont(20));
                JOptionPane.showMessageDialog(null,label,"ส่งออกไฟล์สำเร็จ",JOptionPane.INFORMATION_MESSAGE,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void setFileChooserUI(){
        UIManager.put("FileChooser.openDialogTitleText", "เปิดไฟล์");
        UIManager.put("FileChooser.saveDialogTitleText", "บันทึกไฟล์");
        UIManager.put("FileChooser.lookInLabelText", "ค้นหาใน");
        UIManager.put("FileChooser.saveInLabelText", "บันทึกใน");
        UIManager.put("FileChooser.openButtonText", "เปิด");
        UIManager.put("FileChooser.saveButtonText", "บันทึก");
        UIManager.put("FileChooser.cancelButtonText", "ยกเลิก");
        UIManager.put("FileChooser.fileNameLabelText", "ชื่อไฟล์");
        UIManager.put("FileChooser.filesOfTypeLabelText", "ประเภทไฟล์");
        UIManager.put("FileChooser.folderNameLabelText", "ชื่อโฟลเดอร์");
    }

    private static void setFileChooserFont(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setFileChooserFont(child, font);
            }
        }
    }

   
    public static void exportToExcel(String[] excelHeader ,ArrayList<String[]> mapedRows) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String excelFilePath = fileChooser.getSelectedFile().getPath() + ".xlsx";
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data");

                // เขียนหัวตาราง Excel
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < excelHeader.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(excelHeader[i]);
                }

                // เขียนข้อมูลจาก deletedRows ลงใน Excel
                int rowCount = 1;
                for (String[] rowData : mapedRows) {
                    Row row = sheet.createRow(rowCount++);
                    for (int j = 0; j < rowData.length; j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(rowData[j]);
                    }
                }

                // บันทึกไฟล์ Excel
                try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                    workbook.write(outputStream);
                }
                System.out.println("Excel written successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

