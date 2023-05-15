package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteCSVFile {
    public static void main(String[] args) {
        String csvFile = "ErrorCowsNot100.xlsx";
        FileWriter writer = null;
        ArrayList<String[]> data = RunDB.getAllErrorBreed();

        try {
            writer = new FileWriter(csvFile);
            for (String[] row : data) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
