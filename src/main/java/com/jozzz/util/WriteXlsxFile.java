package com.jozzz.util;

import com.jozzz.gui.CowsTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
                JLabel label = new JLabel("กรุณาเลือกไฟล์");
                label.setFont(Element.getFont(20));
                JOptionPane.showMessageDialog(null,label,"กรุณาเลือกไฟล์",JOptionPane.WARNING_MESSAGE,null);
            }
        });

        JOptionPane.showOptionDialog(null, panel, "เลือกไฟล์ที่จะส่งออก",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {exportButton}, null);
    }

    public static void showExportFileChooser(ArrayList<JCheckBox> checkBoxesList , ArrayList<CowsTable> cowsTables, JTabbedPane tabbedPane) {
        JFileChooser fileChooser = new JFileChooser();
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


}

