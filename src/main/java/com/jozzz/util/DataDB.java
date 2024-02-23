package com.jozzz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.dvcs.Data;

public class DataDB {
    // Connect to spring framwork or backend dpo-api
    static String url = "http://localhost:8083";

    public static ArrayList<String[]> getAllDairyCowBreedForPattern() throws IOException {
        System.out.println("connection to dpo_api");
        String endpoint = url + "/cow/all_list";
        URL obj = new URL(endpoint);

        // Open a connection using HttpURLConnection
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set the request method (GET, POST, PUT, DELETE, etc.)
        con.setRequestMethod("GET");

        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("closing to dpo_api");
        // Parse the response and return as ArrayList<String[]>
        return parseResponse(response.toString());
    }

    public static ArrayList<String[]> getCowById() throws IOException {
        System.out.println("connection to dpo_api");
        String endpoint = url + "/cow/135678";
        URL obj = new URL(endpoint);

        // Open a connection using HttpURLConnection
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set the request method (GET, POST, PUT, DELETE, etc.)
        con.setRequestMethod("GET");

        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("closing to dpo_api");
        // Parse the response and return as ArrayList<String[]>
        return parseResponse(response.toString());
    }

    private static ArrayList<String[]> parseResponse(String response) throws JsonMappingException, JsonProcessingException {
        ArrayList<String[]> dataList = new ArrayList<>();
        // You need to implement your own parsing logic here
        // Example: Split response by newline and then split each line by comma
        // Initialize ObjectMapper from Jackson library
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Parse JSON response
        JsonNode rootNode = objectMapper.readTree(response);
        
        // Iterate through JSON array
        for (JsonNode node : rootNode) {
            // Extract values from JSON object
            String cow_id = node.get("cow_id").asText();
            String cow_name = node.get("cow_name").asText();
            String cow_fa_zyan_code = node.get("cow_fa_zyan_code").asText();
            String cow_ma_zyan_code = node.get("cow_ma_zyan_code").asText();
            String farm_id = node.get("farm_id").asText();
            String breed_code = node.get("breed_code").asText();
            String breed_name = node.get("breed_name").asText();
            String breed_id_string = node.get("breed_id_string").asText();
            
            // Create a String array to hold the values
            String[] values = { cow_id, cow_name, cow_fa_zyan_code, cow_ma_zyan_code ,farm_id,breed_code,breed_name,breed_id_string,"",""};
            
            // Add the String array to the dataList
            dataList.add(values);
        }
        return dataList;
    }
    private static void printParsedResponse(ArrayList<String[]> parsedResponse) {
        for (String[] row : parsedResponse) {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) throws IOException {
        ArrayList data = DataDB.getAllDairyCowBreedForPattern();
        printParsedResponse(data);
    }
}
