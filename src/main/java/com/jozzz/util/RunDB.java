package com.jozzz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.*;
import java.util.ArrayList;

public class RunDB {

    private static Connection connection;

    public RunDB(){}

    //DIP Query

    public static ArrayList<String[]> getAllCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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
            openDIPDatabaseConnection();
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

    //Dairy Query

    public static ArrayList<String[]> getAllDairyCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT *" +
                            " FROM tbd_cow")){
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

    public static ArrayList<String[]> getAllDairyBreeders(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT *" +
                            " FROM tbd_codad")){
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

    public static String[] getDairyCow(String zyanCode){
        String[] data = {};
        try {
            openDIPDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT *" +
                            " FROM tbd_cow"+
                            " WHERE zyan_code ='"+zyanCode+"'")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                while (resultSet.next()){
                    data = new String[column];
                    for (int i=1;i <= column;i++){
                        data[i-1] = resultSet.getString(i);
                    }
                }
            }
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
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
    public static ArrayList<String[]> getAllDairyBreedPatternOnly(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT breed_name" +
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

    public static ArrayList<String[]> getAllDairyBreedMain(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, breed_code, breed_code_eng, breed_code_initials FROM tbd_breed_main")){
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
    
    public static ArrayList<String[]> getAllDairyBreedPattern(){
        ArrayList<String[]> dataList = new ArrayList<>();
        int count = 0;
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cow_id , cow_name , cow_fa_zyan_code,cow_ma_zyan_code,tbd_cow.farm_id,\r\n"+
                    "tbd_breed.breed_id,breed_code, breed_name, breed_id_string \r\n" + //
                            "FROM tbd_breed , tbd_cow\r\n" + //
                            "WHERE tbd_breed.breed_id = tbd_cow.breed_id\r\n" + //
                            "")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                
                
                while (resultSet.next()){
                    //plus 2 for column new_breed_id_string and sum_breed 
                    //because we need to use string[] for one row it's mean if we want to add column in row we need to plus column here
                    String[] data = new String[column+3];
                    for (int i=2;i <= column;i++){
                        data[i-2] = resultSet.getString(i-1);
                    }
                    dataList.add(data);
                    // if(count == 50000){
                    //     break;
                    // }
                    count++;
                }
            }
            
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllDairyBreedByFarmId(int id){
        ArrayList<String[]> dataList = new ArrayList<>();
        int count = 0;
        try {
            openDairyDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cow_id , cow_name , cow_fa_zyan_code,cow_ma_zyan_code,tbd_cow.farm_id,breed_code, breed_name, breed_id_string \r\n" + //
                            "FROM tbd_breed , tbd_cow\r\n" + //
                            "WHERE tbd_breed.breed_id = tbd_cow.breed_id\r\n" + //
                            "")){
                ResultSet resultSet = statement.executeQuery();
                int column = statement.getMetaData().getColumnCount();
                
                
                while (resultSet.next()){
                    //plus 2 for column new_breed_id_string and sum_breed 
                    //because we need to use string[] for one row it's mean if we want to add column in row we need to plus column here
                    String[] data = new String[column+2];
                    for (int i=2;i <= column;i++){
                        data[i-2] = resultSet.getString(i-1);
                    }
                    dataList.add(data);
                    if(count == 50000){
                        break;
                    }
                    count++;
                }
            }
            
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }
    public static ArrayList<String[]> getAllDipDairyCompare(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            Connection dpidbConnection = openDIPConnection();
            Connection dairydbConnection = openDairyConnection();

            dataList = executeQueryDipDairyCompare(dpidbConnection, dairydbConnection);

            closeDatabaseConnection(dpidbConnection);
            closeDatabaseConnection(dairydbConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    private static ArrayList<String[]> executeQueryDipDairyCompare(Connection dpidbConnection, Connection dairydbConnection) throws SQLException {
        String query = """
                         SELECT tbd_cow.zyan_code,
                         tbd_cow.dpo_code AS cow_zyan_id,
                         tbd_cow.cow_name AS cow_zyan_name,
                         cow.id AS cow_dpo_id,
                         cow.nickName AS cow_dpo_name,
                         tb_member.member_id AS zyan_mem_id,
                         tb_member.member_name AS zyan_mem_name,
                         tb_member.member_surname AS zyan_mem_surname,
                         farmer.id AS dpo_mem_id,
                         farmer.fName AS dpo_mem_name,
                         farmer.sName AS dpo_mem_surname,
                         tbc_farm_selected.f_s_detail AS zyan_center,
                         center.name AS dpo_center,
                         tbd_farm.farm_id AS zyan_farm_id,
                         tbd_farm.farm_name AS zyan_farm_name
                          FROM zyanwoadev_test.tbd_cow,
                               farmdb.cow,
                               zyanwoadev_test.tbd_farm,
                               zyanwoadev_test.tb_member,
                               zyanwoadev_test.tbc_farm_selected,
                               farmdb.farmer,
                               farmdb.center
                          WHERE zyanwoadev_test.tbd_farm.member_id = zyanwoadev_test.tb_member.member_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbd_cow.farm_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbc_farm_selected.farm_id
                            AND farmdb.cow.id = zyanwoadev_test.tbd_cow.dpo_code
                            AND farmdb.cow.farmerId = farmdb.farmer.id
                            AND farmdb.farmer.centerId = farmdb.center.id
                            AND zyanwoadev_test.tbd_cow.dpo_code != ''
                            AND zyanwoadev_test.tb_member.member_name = farmdb.farmer.fName;
                          """;

        Statement statement = dairydbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String[]> resultData = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        while (resultSet.next()) {
            String[] rowData = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getString(i);
            }
            resultData.add(rowData);
        }
        return resultData;
    }

    public static ArrayList<String[]> getAllDipSameCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            Connection dpidbConnection = openDIPConnection();
            Connection dairydbConnection = openDairyConnection();

            dataList = executeQueryDipSameCows(dpidbConnection, dairydbConnection);

            closeDatabaseConnection(dpidbConnection);
            closeDatabaseConnection(dairydbConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }


    private static ArrayList<String[]> executeQueryDipSameCows(Connection dpidbConnection, Connection dairydbConnection) throws SQLException {
        String query = """
                         SELECT cow.*
                          FROM zyanwoadev_test.tbd_cow,
                               farmdb.cow,
                               zyanwoadev_test.tbd_farm,
                               zyanwoadev_test.tb_member,
                               zyanwoadev_test.tbc_farm_selected,
                               farmdb.farmer,
                               farmdb.center
                          WHERE zyanwoadev_test.tbd_farm.member_id = zyanwoadev_test.tb_member.member_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbd_cow.farm_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbc_farm_selected.farm_id
                            AND farmdb.cow.id = zyanwoadev_test.tbd_cow.dpo_code
                            AND farmdb.cow.farmerId = farmdb.farmer.id
                            AND farmdb.farmer.centerId = farmdb.center.id
                            AND zyanwoadev_test.tbd_cow.dpo_code != ''
                            AND zyanwoadev_test.tb_member.member_name = farmdb.farmer.fName;
                          """;

        Statement statement = dairydbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String[]> resultData = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            String[] rowData = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getString(i);
            }
            resultData.add(rowData);
        }
        return resultData;
    }

    public static ArrayList<String[]> getAllDairySameCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            Connection dpidbConnection = openDIPConnection();
            Connection dairydbConnection = openDairyConnection();

            dataList = executeQueryDairySameCows(dpidbConnection, dairydbConnection);

            closeDatabaseConnection(dpidbConnection);
            closeDatabaseConnection(dairydbConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }


    private static ArrayList<String[]> executeQueryDairySameCows(Connection dpidbConnection, Connection dairydbConnection) throws SQLException {
        String query = """
                         SELECT tbd_cow.*
                          FROM zyanwoadev_test.tbd_cow,
                               farmdb.cow,
                               zyanwoadev_test.tbd_farm,
                               zyanwoadev_test.tb_member,
                               zyanwoadev_test.tbc_farm_selected,
                               farmdb.farmer,
                               farmdb.center
                          WHERE zyanwoadev_test.tbd_farm.member_id = zyanwoadev_test.tb_member.member_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbd_cow.farm_id
                            AND zyanwoadev_test.tbd_farm.farm_id = zyanwoadev_test.tbc_farm_selected.farm_id
                            AND farmdb.cow.id = zyanwoadev_test.tbd_cow.dpo_code
                            AND farmdb.cow.farmerId = farmdb.farmer.id
                            AND farmdb.farmer.centerId = farmdb.center.id
                            AND zyanwoadev_test.tbd_cow.dpo_code != ''
                            AND zyanwoadev_test.tb_member.member_name = farmdb.farmer.fName;
                          """;

        Statement statement = dairydbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String[]> resultData = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            String[] rowData = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getString(i);
            }
            resultData.add(rowData);
        }
        return resultData;
    }

   
    //Connect to DPI Database
    private static void openDIPDatabaseConnection() throws SQLException {
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

    private static Connection openDIPConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mariadb://ec2-54-251-168-197.ap-southeast-1.compute.amazonaws.com:6667/farmdb",
                "summer2023",
                "Summ3r!@MISL$$23"
        );
    }

    private static Connection openDairyConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mariadb://54.251.168.197:6667/zyanwoadev_test",
                "summer2023",
                "Summ3r!@MISL$$23"
        );
    }

//    private static void openDIPDatabaseConnection() throws SQLException {
//        connection = DriverManager.getConnection(
//                "jdbc:mariadb://localhost:3306/farmdb",
//                "root",
//                ""
//        );
//    }

    private static void closeDatabaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    private static void closeDatabaseConnection() throws SQLException{
        if (connection != null) {
            connection.close();
        }
    }
}
