package com.jozzz.mock_corrector;

import java.util.Random;

public class Corrector {
    private static Random rand = new Random();
    static int numberOfRand;
    public Corrector(){
        
    }

    public static String corrector(){
        String breedIdString = "";

        numberOfRand = rand.nextInt(3);
        if(numberOfRand == 0){   
            breedIdString = "HP87.5 + NA12.5";
        }else if(numberOfRand == 1){  
            breedIdString = "HP93.75 + NA6.25";
        }else{
            breedIdString="ERROR";
        }

        return breedIdString;
    }
    

}
