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
            Matcher matcher = letterPattern.matcher(value[6]);

            while (matcher.find()) {
                String letter = matcher.group();
                if (!findBreedId(letter, listAllBreedMain).equals("")) {
                    breedIdList.add(findBreedId(letter, listAllBreedMain));
                }
            }
            if (breedIdList.isEmpty()) {
                matcher = letterPattern.matcher(value[5]);
                while (matcher.find()) {
                    String letter = matcher.group();
                    if (!findBreedId(letter, listAllBreedMain).equals("")) {
                        breedIdList.add(findBreedId(letter, listAllBreedMain));
                        if (findBreedId(letter, listAllBreedMain).equalsIgnoreCase("NA")) {
                            hasNA = true;
                        }
                    }
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
}
