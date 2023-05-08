package gui;

import util.Element;
import util.RunDB;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Display extends JPanel {

    private static CardLayout cardLayout;

    public Display(){
        ArrayList<String[]> allCow = RunDB.getAllCows();
        ArrayList<String[]> allCorrectBreeds = RunDB.getAllCorrectBreed();
        ArrayList<String[]> allErrorBreeds = RunDB.getAllErrorBreed();
        ArrayList<String[]> allCorrectParent = RunDB.getAllCorrectParent();
        ArrayList<String[]> allErrorParent = RunDB.getAllErrorParent();
        ArrayList<String[]> allPerIntBreed = RunDB.getAllPerIntBreed();
        cardLayout = new CardLayout();
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(cardLayout);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Element.getFont(15));
        String[] columnAlLCows = {"เลขเกษตรกร", "หมายเลขโค", "สถานะโค","วันที่", "ชื่อโค", "c_oth", "วันเกิด"
                , "หมายเลขแม่", "หมายเลขพ่อ", "เพศ", "outfg", "milk", "eurbrd", "eurper"};
        tabbedPane.add("All Cows วัวทั้งหมด ("+decimalFormat(allCow.size())+" รายการ)",
                new CowsTable(allCow, columnAlLCows, this));

        String[] columnAlLBreed = {"หมายเลขโค", "สายพันธุ์", "เปอร์เซ็นต์รวม"};
        tabbedPane.add("Cows Correct ข้อมูลวัว 100 % ("+decimalFormat(allCorrectBreeds.size())+" รายการ)",
                new CowsTable(allCorrectBreeds, columnAlLBreed, this));
        tabbedPane.add("Cows Error ข้อมูลวัวไม่ 100 % ("+decimalFormat(allErrorBreeds.size())+" รายการ)",
                new CowsTable(allErrorBreeds, columnAlLBreed, this));

        tabbedPane.add("Cows Compare เปรียบเทียบ % ("+decimalFormat(allPerIntBreed.size())+" รายการ)",
                new CowsTable(allPerIntBreed, columnAlLBreed, this));

        tabbedPane.add("Correct Parent พ่อแม่ถูกต้อง ("+decimalFormat(allCorrectParent.size())+" รายการ)",
                new CowsTable(allCorrectParent, columnAlLCows, this));
        tabbedPane.add("Error Parent พ่อแม่ไม่ถูกต้อง ("+decimalFormat(allErrorParent.size())+" รายการ)",
                new CowsTable(allErrorParent, columnAlLCows, this));

        this.add(tabbedPane, "COWS_TABLE");
    }

    public static CardLayout getCardLayout() {
        return cardLayout;
    }

    public String decimalFormat (int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number);
    }
}
