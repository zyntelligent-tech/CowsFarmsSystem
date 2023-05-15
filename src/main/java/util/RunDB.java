package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RunDB {

    private static Connection connection;

    public RunDB(){}

    public static ArrayList<String[]> getAllCows(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getCowParent(String cowCode){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
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
                    System.out.println(Arrays.toString(data));
                    dataList.add(data);
                }
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static String[] getCow(String table, String cowCode){
        String[] data = new String[0];
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getFarmer(String farmerCode){
        String[] data = new String[0];
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getCenter(String centerCode){
        String[] data = new String[0];
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static String[] getSector(String sectorCode){
        String[] data = new String[0];
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static ArrayList<String[]> getAllCorrectBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId, GROUP_CONCAT(CONCAT(breedId, ' : ', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed," +
                            " SUM(perInt + num_up / num_low) AS total_perInt"+
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllErrorBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId, GROUP_CONCAT(CONCAT(breedId, ' : ', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed," +
                            " SUM(perInt + num_up / num_low) AS total_perInt"+
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllPerIntBreed(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT cowId," +
                            " GROUP_CONCAT(CONCAT(breedId, ' : ', perInt + num_up / num_low) SEPARATOR ' , ') AS Breed_PerInt," +
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllCorrectParent(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    public static ArrayList<String[]> getAllErrorParent(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDatabaseConnection();
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
            };
            closeDatabaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    boolean isFirst = true;
    ArrayList<String[]> dataList;
    ArrayList<String[]> dataCow;
    ArrayList<String[]> dataBreeder;

    ArrayList<String> dataCowBreed;
    ArrayList<String> dataBreederBreed;
    
    ArrayList<String[]> dataBreederContain;
    ArrayList<String[]> dataBreederNotContain; 
    ArrayList<String[]> dataCowContain;
    ArrayList<String[]> dataCowNotContain; 
    

    ArrayList<String> breedContain;
    ArrayList<String> breedNotContain; 
    
    HashSet<String> breed;
    public ArrayList<String[]> getAllPerentBreed(String cowCode){
        dataList = new ArrayList<>();
        dataCow = new ArrayList<>();
        dataBreeder = new ArrayList<>();
       
        dataCowBreed = new ArrayList<>();
        dataBreederBreed = new ArrayList<>();   
       
        breedContain = new ArrayList<>();
        breedNotContain = new ArrayList<>();
        dataCowContain = new ArrayList<>();
        dataCowNotContain = new ArrayList<>();
        dataBreederContain = new ArrayList<>();
        dataBreederNotContain = new ArrayList<>();
        
        try {
            openDatabaseConnection();
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
                    System.out.println(Arrays.toString(data));
                    dataList.add(data);
                }
            };

            for(int i = dataList.size() - 2 ; i >= 0 ; i--){
                String[] data = dataList.get(i);

                breedContain.clear();
                breedNotContain.clear();
                dataBreederContain.clear();
                dataCowContain.clear();
                dataBreeder.clear();
                dataCow.clear();
                dataCowBreed.clear();
                dataBreederBreed.clear();

                System.out.println("=========");
                if(isFirst){
                    try(PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM cowBreed where cowId = '"+data[1]+"'")
                    ){
                        ResultSet resultSet = statement.executeQuery();
                        int column = statement.getMetaData().getColumnCount();
                        while (resultSet.next()){
                            String[] cow = new String[column];
                            for (int c = 1 ; c <= column;c++){
                                cow[c-1] = resultSet.getString(c);
                            }
                            System.out.println(Arrays.toString(cow)); 
                            dataCow.add(cow);
                            dataCowBreed.add(cow[1]);
                        }
                    }
                }else{
                    
                }
                
                try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM breederBreed where breederId = '"+data[2]+"'")
                ){
                    ResultSet resultSet = statement.executeQuery();
                        int column = statement.getMetaData().getColumnCount();
                        while (resultSet.next()){
                            String[] breeder = new String[column];
                            for (int c = 1 ; c <= column;c++){
                                breeder[c-1] = resultSet.getString(c);
                            }
                            System.out.println(Arrays.toString(breeder)); 
                            dataBreeder.add(breeder);
                            dataBreederBreed.add(breeder[1]);
                        }
                }

                for(String breed : dataCowBreed){
                    if(dataBreederBreed.stream().anyMatch(breed::equals)){
                        breedContain.add(breed);
                    }else{
                        breedNotContain.add(breed);
                    }
                }

                for(String breed : dataBreederBreed){
                    if(!dataCowBreed.stream().anyMatch(breed::equals)){
                        breedNotContain.add(breed);
                    }
                }

                
                for(String breed : breedContain){
                    for(String[] cow : dataCow){
                        if(breed.equals(cow[1])){
                            dataCowContain.add(cow);
                        }
                    }
                    for(String[] breeder : dataBreeder){
                        if(breed.equals(breeder[1])){
                            dataBreederContain.add(breeder);
                        }
                    }
                }   
                
                for(String breed : breedNotContain){
                    for(String[] cow : dataCow){
                        if(breed.equals(cow[1])){
                            dataCowNotContain.add(cow);
                        }
                    }
                    for(String[] breeder : dataBreeder){
                        if(breed.equals(breeder[1])){
                            dataBreederNotContain.add(breeder);
                        }
                    }
                }   
               
                String[] str = {dataList.get(i)};
                isFirst = false;
            }
            closeDatabaseConnection();


            return dataList;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void openDatabaseConnection() throws SQLException {
        System.out.println("Connecting to the database....");
        connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/farmdb",
                "root",
                ""
        );
        System.out.println("Connection valid : " + connection.isValid(5)) ;
    }

    private static void closeDatabaseConnection() throws SQLException{
        System.out.println("Closing database connection...");
        connection.close();
        System.out.println("Connection valid : "+connection.isValid(5));
    }

    public static void main(String[]args){
        new RunDB().getAllPerentBreed("?ศ560134");
    }
}
