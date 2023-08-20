package com.jozzz;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MergeToDairy {

    private static Connection connection;
    String dairyApiRegisterUrl = "http://192.168.182.244:8083/register/add";
    String [] dipAndDairyDataBase = {"jdbc:mariadb://localhost:3306/dip_dairy", "root",""};
    String [] dairyDataBase = {"jdbc:mariadb://localhost:3306/zyanwoadev_test","root",""};
    public static void main(String[]args) throws SQLException {
        new MergeToDairy();
    }

    public MergeToDairy() throws SQLException {
        ArrayList<String[]> allDIPMember = getAllDIPMember();
        ArrayList<String[]> allDIPSameEmail = getAllDIPDairySameEmail();
        ArrayList<String[]> allDIPSameDairy  = new ArrayList<>();

        for(String [] sameFarmer : allDIPSameEmail){
            for (String[] farmer : allDIPMember){
                if (Arrays.equals(sameFarmer, farmer)){
                    allDIPSameDairy.add(farmer);
                    break;
                }
            }
        }
        //Remove same data Dip and dairy from allDipMember
        allDIPMember.removeAll(allDIPSameDairy);
        int count = 1;
        //insert dip member to dairy
        for (String[] farmer : allDIPMember){
            System.out.print(count+" ");
            regMember(farmer);
            count++;
        }
        System.out.println("Insert Successful");
    }

    private void regMember(String [] payload){
        try {
            URL obj = new URL(dairyApiRegisterUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonPayload = getRegMemberPayload(payload);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("POST request successful");
                System.out.println("Insert : "+ Arrays.toString(payload));
            } else {
                System.out.println("POST request failed with status code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRegMemberPayload(String [] payload) {
        RegisterPayload regPayload = new RegisterPayload();
        regPayload.setMemberName(payload[3]);
        regPayload.setMemberSurname(payload[4]);
        regPayload.setMemberTel("");
        regPayload.setEmail("dpoTest"+ payload[1] +"@zyanwoa.com");
        regPayload.setPassword("123456");

        String addr = !payload[6].isEmpty() ? payload[6]+" " : "";
        addr += !payload[7].isEmpty() ? "ต." + payload[7]+" " : "";
        addr += !payload[8].isEmpty() ? "อ." + payload[8]+" " : "";
        addr += !payload[9].isEmpty() ? "จ." + payload[9]: "";
        regPayload.setFarmAddress(addr);
        regPayload.setFarmProvince(payload[9]);
        regPayload.setFarmDistrict(payload[8]);
        regPayload.setFarmSubDistrict(payload[7]);
        regPayload.setPostCode("");
        regPayload.setFarmType(1);
        regPayload.setZwIdCard("");
        regPayload.setZwKycTempUuid("");

        return regPayload.toString();
    }

    public ArrayList<String[]> getAllDIPMember(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDIPDairyConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT farmer.* FROM farmer")){
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

    public ArrayList<String[]> getAllDIPDairySameEmail(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDIPDairyConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT farmer.*" +
                            "FROM tb_member, farmer\n" +
                            "WHERE tb_member.member_email = CONCAT('dpo', farmer.id, '@zyanwoa.com')")){
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

    public ArrayList<String[]> getAllDairyMember(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDIPDairyConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT tb_member.* FROM tb_member")){
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

    public ArrayList<String[]> getAllDairySameEmail(){
        ArrayList<String[]> dataList = new ArrayList<>();
        try {
            openDIPDairyConnection();
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT tb_member.*\n" +
                            "FROM tb_member, farmer\n" +
                            "WHERE tb_member.member_email = CONCAT('dpo', farmer.id, '@zyanwoa.com')")){
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



    private void openDIPDairyConnection() throws SQLException {
        connection = DriverManager.getConnection(
                dipAndDairyDataBase[0],
                dipAndDairyDataBase[1],
                dipAndDairyDataBase[2]
        );
    }

    private void openDairyConnection() throws SQLException {
        connection = DriverManager.getConnection(
                dairyDataBase[0],
                dairyDataBase[1],
                dairyDataBase[2]
        );
    }

    private static void closeDatabaseConnection() throws SQLException{
        System.out.println("Closing database connection...");
        connection.close();
        System.out.println("Connection valid : "+connection.isValid(5));
    }

}

class RegisterPayload {

    //member
    private String memberName;
    private String memberSurname;
    private String memberTel;
    private String email;
    private String password;

    //farm
    private String farmAddress;
    private String farmProvince;
    private String farmDistrict;
    private String farmSubDistrict;
    private String postCode;
    private int farmType;

    //zyanwoa app
    private String zwIdCard;
    private String zwKycTempUuid;

    @Override
    public String toString() {
        return "{" +
                "\"member_name\":\""+getMemberName()+"\"," +
                "\"member_surname\":\""+getMemberSurname()+"\"," +
                "\"member_tel\":\""+getMemberTel()+"\"," +
                "\"email\":\""+getEmail()+"\"," +
                "\"password\":\""+getPassword()+"\"," +
                "\"farm_address\":\""+getFarmAddress()+"\"," +
                "\"farm_province\":\""+getFarmProvince()+"\"," +
                "\"farm_district\":\""+getFarmDistrict()+"\"," +
                "\"farm_sub_district\":\""+getFarmSubDistrict()+"\"," +
                "\"post_code\":\""+getPostCode()+"\"," +
                "\"farm_type\":"+getFarmType()+"," +
                "\"zw_id_card\":\""+getZwIdCard()+"\"," +
                "\"zw_kyc_temp_uuid\":\""+getZwKycTempUuid()+"\"" +
                "}";
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFarmAddress() {
        return farmAddress;
    }

    public void setFarmAddress(String farmAddress) {
        this.farmAddress = farmAddress;
    }

    public String getFarmProvince() {
        return farmProvince;
    }

    public void setFarmProvince(String farmProvince) {
        this.farmProvince = farmProvince;
    }

    public String getFarmDistrict() {
        return farmDistrict;
    }

    public void setFarmDistrict(String farmDistrict) {
        this.farmDistrict = farmDistrict;
    }

    public String getFarmSubDistrict() {
        return farmSubDistrict;
    }

    public void setFarmSubDistrict(String farmSubDistrict) {
        this.farmSubDistrict = farmSubDistrict;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public int getFarmType() {
        return farmType;
    }

    public void setFarmType(int farmType) {
        this.farmType = farmType;
    }

    public String getZwIdCard() {
        return zwIdCard;
    }

    public void setZwIdCard(String zwIdCard) {
        this.zwIdCard = zwIdCard;
    }

    public String getZwKycTempUuid() {
        return zwKycTempUuid;
    }

    public void setZwKycTempUuid(String zwKycTempUuid) {
        this.zwKycTempUuid = zwKycTempUuid;
    }
}
