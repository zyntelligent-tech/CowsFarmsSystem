package com.jozzz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunDB {

    private static Connection connection;

    public RunDB(){}

    public static ArrayList<String[]> getAllCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM cow")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getCowParent(String cowCode){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "WITH RECURSIVE cow_family_tree(id, momId, dadId) AS ("+
                            " SELECT id, momId, dadId"+
                            " FROM cow"+
                            " WHERE id = '"+cowCode+"'"+
                            " UNION ALL"+
                            " SELECT cow.id, cow.momId, cow.dadId"+
                            " FROM cow"+
                            " JOIN cow_family_tree ON cow_family_tree.momId = cow.id"+
                            " WHERE cow_family_tree.momId IS NOT NULL)"+
                    " SELECT id, momId, dadId"+
                    " FROM cow_family_tree")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static String[] getCow(String table, String cowCode){
        String[] data = new String[0];
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM "+table+" WHERE id = '"+cowCode+"'")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    data = new String[column];
                    for (int i = 1; i <= column; i++) {
                        data[i - 1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getFarmer(String farmerCode){
        String[] data = new String[0];
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM farmer WHERE id = '"+farmerCode+"'")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    data = new String[column];
                    for (int i = 1; i <= column; i++) {
                        data[i - 1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getCenter(String centerCode){
        String[] data = new String[0];
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM center WHERE id = '"+centerCode+"'")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    data = new String[column];
                    for (int i = 1; i <= column; i++) {
                        data[i - 1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getSector(String sectorCode){
        String[] data = new String[0];
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM sector WHERE id = '"+sectorCode+"'")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    data = new String[column];
                    for (int i = 1; i <= column; i++) {
                        data[i - 1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getBreeds(String cowCode){
        String[] data = new String[0];
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId," +
                            " GROUP_CONCAT(CONCAT(breedId, ' : ', perInt+num_up/num_low) SEPARATOR ' , ') AS Breed_Perint,\n" +
                            " SUM(perInt+num_up/num_low) AS total_perint\n" +
                            " FROM cowbreed" +
                            " WHERE cowId = '" + cowCode + "'" +
                            " GROUP BY cowId")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    data = new String[column];
                    for (int i = 1; i <= column; i++) {
                        data[i - 1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static ArrayList<String[]> getAllCorrectBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId, GROUP_CONCAT(CONCAT(breedId, ' : ', num_up ,'/', num_low, '=', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed," +
                            " CAST(SUM(perInt + num_up / num_low) AS DECIMAL) AS total_perInt"+
                            " FROM cowbreed"+
                            " GROUP BY cowId"+
                            " HAVING total_perInt = 100")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllErrorBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId, GROUP_CONCAT(CONCAT(breedId, ' : ', num_up ,'/', num_low, '=', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed," +
                            " CAST(SUM(perInt + num_up / num_low) AS DECIMAL) AS total_perInt"+
                            " FROM cowbreed"+
                            " GROUP BY cowId"+
                            " HAVING total_perInt != 100")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllPerIntBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId," +
                            " GROUP_CONCAT(CONCAT(breedId, ' : ', num_up ,'/', num_low, '=', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed_PerInt," +
                            " SUM(perInt + num_up / num_low) AS total_perInt" +
                            " FROM cowbreed" +
                            " GROUP BY cowId")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllCorrectParent(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM cow" +
                    " WHERE (momId = '' AND dadId = '')" +
                    " OR (momId != '' AND dadId != '')")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllErrorParent(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDPIDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM cow" +
                    " WHERE (momId = '' AND dadId != '')" +
                    " OR (momId != '' AND dadId = '')")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllDairyBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM tbd_breed")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
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

    public static ArrayList<String[]> getAllDairyBreedPattern(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT breed_name, COUNT(*) AS pattern_count" +
                            " FROM tbd_breed" +
                            " GROUP BY breed_name")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getDairyFormat(String formatType){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            String sqlStr = switch (formatType) {
                case "PERCENT" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE breed_name REGEXP '^[0-9]+(\\.[0-9]+)? ?%$'";
                case "100%HF" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE BREED_NAME REGEXP '^[0-9]+(\\.[0-9]+)?+%[A-Za-z, ]+$'";
                case "HF100%" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE BREED_NAME REGEXP '^[A-Za-z]+[0-9]+(\\.[0-9]+)?+[%]?%$'";
                case "THAI100%" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE BREED_NAME REGEXP '^[ก-๙]+ ?+([ก-๙]+)?+[[:space:]]*[0-9]+(\\.[0-9]+)?+%?$'";
                case "HAS+" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE breed_name LIKE '%+%'";
                case "HAS," ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE breed_name LIKE '%,%'";
                case "THAI" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE breed_name REGEXP '^[ก-๙]+\\([^0-9%\\-]+\\)$'";
                case "ENG" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE breed_name REGEXP '^[a-z]+ ?+([a-z]+)?$'";
                case "OnlyNum" ->
                        "SELECT breed_uuid, breed_code, breed_name FROM tbd_breed WHERE BREED_NAME REGEXP '^[0-9]+(\\.[0-9]+)?$'";
                case "Other" -> """
                        SELECT breed_uuid, breed_code, breed_name
                        FROM tbd_breed
                        WHERE NOT (
                          BREED_NAME REGEXP '^[0-9]+(\\.[0-9]+)? ?%$'
                          OR BREED_NAME REGEXP '^[0-9]+(\\.[0-9]+)?+%[A-Za-z, ]+$'
                          OR BREED_NAME REGEXP '^[A-Za-z]+ ?+[0-9]+(\\.[0-9]+)?+[%]?%$'
                          OR BREED_NAME REGEXP '^[ก-๙]+ ?+([ก-๙]+)?+[[:space:]]*[0-9]+(\\.[0-9]+)?+%?$'
                          OR BREED_NAME LIKE '%+%'
                          OR BREED_NAME LIKE '%,%'
                          OR BREED_NAME REGEXP '^[ก-๙]+\\([^0-9%\\-]+\\)$'
                          OR BREED_NAME REGEXP '^[0-9]+(\\.[0-9]+)?$'
                          OR BREED_NAME REGEXP '^[a-z]+ ?+([a-z]+)?$'
                          AND BREED_NAME REGEXP '^[a-z]+ ?+([a-z]+)?$'
                        )""";
                default -> "";
            };

            try(PreparedStatement statement = connection.prepareStatement(sqlStr)){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    String[] data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                    dataList.add(data);
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    //Connect to DPI Database
    private static void openDPIDatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mariadb://ec2-54-251-168-197.ap-southeast-1.compute.amazonaws.com:6667/farmdb",
                "summer2023",
                "Summ3r!@MISL$$23"
        );
    }

    //Connect to Dairy Database
    private static void openDairyDatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mariadb://54.251.168.197:6667/zyanwoadev_test",
                "summer2023",
                "Summ3r!@MISL$$23"
        );
    }

//        private static void openDPIDatabaseConnection() throws SQLException {
//        connection = DriverManager.getConnection(
//                "jdbc:mariadb://localhost:3306/farmdb",
//                "root",
//                ""
//        );
//    }

    private static void closeDatabaseConnection() throws SQLException{
        connection.close();
    }
}
