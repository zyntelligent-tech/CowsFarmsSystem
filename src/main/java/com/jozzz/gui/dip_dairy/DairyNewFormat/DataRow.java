package com.jozzz.gui.dip_dairy.DairyNewFormat;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.math.BigDecimal;

import com.jozzz.mock_corrector.Corrector;
import com.jozzz.util.RunDB;

public class DataRow {

    String[] data;

    public static ArrayList<String[]> formatDairyNewFormatDisplay(ArrayList<String[]> data,
            ArrayList<String[]> selectedBreed) {
        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern letterPattern = Pattern.compile("[ก-๙a-zA-Z]+(\\s*[ก-๙a-zA-Z]+)*");

        // BreeedMain
        ArrayList<String[]> listAllBreedMain = RunDB.getAllDairyBreedMain();

        for (String[] value : data) {
            String[] dataArr = new String[3];
            List<String> breedIdList = new ArrayList<>();
            List<String> breedPercentList = new ArrayList<>();
            List<String> newFormatList = new ArrayList<>();
            boolean hasNA = false;
            Matcher matcher = letterPattern.matcher(value[5]);

            while (matcher.find()) {
                String letter = matcher.group();
                if (!findBreedId(letter, listAllBreedMain).equals("")) {
                    breedIdList.add(findBreedId(letter, listAllBreedMain));
                }
            }
            
            double divisorBreed = Math.pow(2, breedIdList.size());
            BigDecimal sumBreed = BigDecimal.ZERO;
            double perLeftover = 0;
            matcher = numberPattern.matcher(value[6]).find()
                    ? numberPattern.matcher(value[6])
                    : numberPattern.matcher(value[5]);
            while (matcher.find()) {
                String number = matcher.group();
                // if (Double.parseDouble(number) <= 100){
                sumBreed = sumBreed.add(new BigDecimal(number));
                breedPercentList.add(number);
                // }
            }

            dataArr[0] = String.join(",", breedIdList);
            dataArr[1] = String.join(",", breedPercentList);

            value[7] = isCorrectBreed(dataArr[0], dataArr[1])
                    ? dataArr[0] + ":" + dataArr[1]
                    : "";
            int comparisonResult = sumBreed.compareTo(new BigDecimal("100"));
            int count = 0;
            boolean is96 = false;
            if (breedPercentList.size() > 1){
                double perBreed = 0; 
                for(int i = 0 ; i < breedPercentList.size() ; i++){
                    perBreed = perBreed + decimalBreed(breedPercentList.get(i));
                }
                if (comparisonResult < 0){ // less than 100
                    for(String percent : breedPercentList){{
                        if(percent.equals("0")){
                            count ++;
                        }
                    }}
                    if(count == 0){
                        int i = 0;
                        perLeftover = 100.0 - perBreed;
                        for(String id : breedIdList){
                            if(id.equals("20")){
                                break;
                            }   
                            i++;
                        } 
                        if(i == breedIdList.size()){
                            breedIdList.add("20");
                            breedPercentList.add(Double.toString(perLeftover));
                        }else{
                            breedPercentList.set(i, Double.toString(perLeftover + Double.parseDouble(breedPercentList.get(i))));
//                            System.out.println(breedPercentList);
                        }
                    }else{
                        perLeftover = 100.0 - perBreed;
                        double resultDiv = perLeftover / count;
                        int i = 0;
                        if(Double.toString(resultDiv).contains(".")){
                            if(Double.toString(resultDiv).split("\\.")[1].length() > 3){
                                resultDiv = perLeftover / (count + 1);
                                if(Double.toString(resultDiv).split("\\.")[1].length() > 3){   
                                    for(String id : breedIdList){
                                        if(id.equals("20")){
                                            break;
                                        }   
                                        i++;
                                    }     
                                    if(i == breedIdList.size()){
                                        double mod = perLeftover % 6;
                                        if(perLeftover > 3){
                                            mod = perLeftover % 3;
                                            perLeftover = perLeftover - mod;
                                            breedIdList.add("20");
                                            breedPercentList.add(Double.toString(mod));
                                        }else{
                                            mod = 3 - perLeftover;
                                            perLeftover = perLeftover + mod;
                                            breedPercentList.set(0, Double.toString(Double.parseDouble(breedPercentList.get(0)) - mod));
                                        }
                                        resultDiv = perLeftover / count ;
                                        for(String percent : breedPercentList){
                                            if(percent.equals("0")){
                                                breedPercentList.set(i, Double.toString(resultDiv));
                                            }
                                            i++;
                                        }     
                                    }else{
                                        breedPercentList.set(i,Integer.toString(Integer.parseInt(breedPercentList.get(i)) + 1));
                                        resultDiv = (perLeftover - 1) / (count - 1);

                                    }
                                    i = 0;
                                    for(String percent : breedPercentList){
                                        if(percent.equals("0")){
                                            breedPercentList.set(i, Double.toString(resultDiv));
                                        }
                                        i++;
                                    }
                                    is96 = true;
                                }else{
                                    for(String percent : breedPercentList){
                                        if(i == 0){
                                            breedPercentList.set(i ,Double.toString(Double.parseDouble(breedPercentList.get(i)) + resultDiv));
                                        }
                                        if(percent.equals("0")){
                                            breedPercentList.set(i, Double.toString(resultDiv));
                                        }
                                        i++;
                                    }  
                                }
                            }else{
                                for(String percent : breedPercentList){
                                    if(percent.equals("0")){
                                        breedPercentList.set(i, Double.toString(resultDiv));
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                    sumBreed = BigDecimal.ZERO;
                    for(String percent : breedPercentList){

                        newFormatList.add(newBreedFormat(Double.parseDouble(percent)));
                        sumBreed = sumBreed.add(new BigDecimal(percent));
                    }
//                    if(is96){
////                        System.out.println(breedIdList);
////                        System.out.println(breedPercentList);
//                        System.out.println(newFormatList);
//                    }
                }
                else if (comparisonResult > 0){ // greater than 100
                    for(String percent : breedPercentList){
                        newFormatList.add(newBreedFormat(Double.parseDouble(percent)));
                    }
                }
                else{
                    for(String percent : breedPercentList){
                        newFormatList.add(newBreedFormat(Double.parseDouble(percent)));
                    }
                }
            }
            else if (breedPercentList.size() == 1){
                double perBreed = decimalBreed(breedPercentList.get(0));
                if (comparisonResult < 0){ // less than 100
                    breedIdList.add(findBreedId("NA" , listAllBreedMain));
                    perLeftover = 100.0 - perBreed;
                    sumBreed = BigDecimal.valueOf(perBreed);
                    newFormatList.add(newBreedFormat(perBreed));
                    newFormatList.add(newBreedFormat(perLeftover));
                    breedPercentList.add(String.valueOf(perLeftover));
                    sumBreed = sumBreed.add(new BigDecimal(perLeftover));
                }
                else if (comparisonResult > 0){ // greater than 100
                    newFormatList.add(newBreedFormat(perBreed - (perBreed - 100)));
                    sumBreed = new BigDecimal("100");
                }
                else {
                    newFormatList.add(newBreedFormat(perBreed));
                }
            }
 
            ////////////
            dataArr[0] = String.join(",", breedIdList);
            dataArr[2] = String.join(",", newFormatList);
            ////////////
            
            if (!value[7].equals("")) {
                selectedBreed.add(value);
            }
            
            //mock corrector for waiting
            value[8] = Corrector.corrector();
            if(value[8] != "ERROR"){
                value[9] = "100";
            }else{
                value[9] = "ERROR"; 
            }

        }
        return data;
    }

    private static boolean isCorrectBreed(String str1, String str2) {
        return (!str1.equals("") && !str2.equals(""))
                && str1.split(",").length == str2.split(",").length;
    }

    private static String findBreedId(String breedStr, ArrayList<String[]> listAllBreedMain) {
        for (String[] breed : listAllBreedMain) {
            if (similarStr(breed[1], (breedStr))
                    || similarStr(breed[2], (breedStr))
                    || similarStr(breed[3], (breedStr))) {
                return breed[0];
            }
        }
        return "";
    }

    private static boolean similarStr(String str1, String str2) {
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();

        for (char c : str1.trim().toLowerCase().toCharArray()) {
            set1.add(c);
        }

        for (char c : str2.trim().toLowerCase().toCharArray()) {
            set2.add(c);
        }

        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        double similarity = (double) intersection.size() / union.size();
        return similarity > 0.7;
    }

    private static long gcd(long a, long b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }
    
    private static double decimalBreed(String percent){
        if (percent.contains(".")){
            if (percent.split("\\.")[1].trim().equals("0")){
                return Double.parseDouble(percent);
            }
            double r = Math.round(Double.parseDouble(percent) * 512f) / 512f;
            long d = (long) Math.pow(10, String.valueOf(r).split("\\.")[1].length());
            long num = Integer.parseInt(String.valueOf(r).split("\\.")[0]);
            long x = Integer.parseInt(String.valueOf(r).split("\\.")[1]);
            long gcd = gcd(x, d);

            double re = (double) x / gcd;
            double di = (double) d / gcd;

            return num + re / di;
        }
        return Double.parseDouble(percent);
    }

    private static String newBreedFormat(Double percent){
        if (String.valueOf(percent).contains(".")){
            if (String.valueOf(percent).split("\\.")[1].trim().equals("0")){
                return String.valueOf(percent).split("\\.")[0] +"+0/1";
            }
            double r = Math.round(percent * 512f) / 512f;
            long d = (long) Math.pow(10, String.valueOf(r).split("\\.")[1].length());
            long x = Integer.parseInt(String.valueOf(r).split("\\.")[1]);
            long gcd = gcd(x, d);

            long re = x / gcd;
            long di = d / gcd;

            return String.valueOf(r).split("\\.")[0] +"+"+ re + "/" + di;
        }
        return percent +"+0/1";
    }
}
