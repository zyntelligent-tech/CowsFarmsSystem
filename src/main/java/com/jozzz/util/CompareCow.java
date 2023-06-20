package com.jozzz.util;

import java.util.ArrayList;

import com.jozzz.models.Cow;

public class CompareCow {
    
    public static Cow toCow(String[] cowData){
        Cow cow = new Cow(cowData[3] , cowData[15] , cowData[10]);
        return cow;
    }

    public static Cow toBreeder(String[] cowData){
        Cow cow = new Cow(cowData[2] , cowData[4] , cowData[6]);
        return cow;
    }

    public static ArrayList<String[]> toArrayList(Cow cow){
        String[] row;
        ArrayList<String[]> level = new ArrayList<>();

        int round = findLevel(cow);
        for(int i = 0 ; i < round; i++){
            row = new String[3];
            row[0] = cow.getZyanCode();
            if(cow.getMom() != null){
                row[1] = cow.getMom().getZyanCode();
            }else{
                row[1] = "";
            }

            if(cow.getDad() != null){
                row[2] = cow.getDad().getZyanCode();
            }else if(!cow.getDadCode().equals("")){
                row[2] = cow.getDadCode();
            }else{
                row[2] = "";
            }
            level.add(row);
            cow = cow.getMom();
        }

        return level;

    }

    public static int findLevel(Cow sourceCow){
        int count = 0;
        while(sourceCow != null){
            sourceCow = sourceCow.getMom();
            count++;
        }
        System.out.println(count);
        return count;
    }
}
