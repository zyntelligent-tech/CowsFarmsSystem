package com.jozzz.algor;

import java.util.ArrayList;
import java.util.List;

import com.jozzz.models.Cow;
import com.jozzz.util.CompareCow;
import com.jozzz.util.RunDB;

public class BFS_Parent {
    
    String[] sourceCow;
    ArrayList<String[]> cowListData;
    ArrayList<String[]> breederListData;

    Cow cow;
    ArrayList<Cow> cowList;
    ArrayList<Cow> breederList;
    ArrayList<Cow> cowTree;

    ArrayList<String[]> cowString;

    public void findParent(String cowCode){
        //Z00096C54EC1C48F64A24D82E5DF3CF8CE96 สมบูรณ์
        //Z00047E0639EF136991F6384998BD1E26BD3 ไม่สมบุรณ์
        //Z000B1837F7D245386DB523121EE42485797 โครตสมบุรณ์
        
        sourceCow = RunDB.getDairyCow(cowCode);
        cow = CompareCow.toCow(sourceCow);
        System.out.println(cow);
        
        cowTree = new ArrayList<>();
        cowTree.add(cow);
        
        
        //set Parent
        bfsSearch(cowTree);
        cowString = CompareCow.toArrayList(cow);

    }
    
    public ArrayList<String[]> getCowString() {
        return cowString;
    }

    public void setCowString(ArrayList<String[]> cowString) {
        this.cowString = cowString;
    }

    public void setList(){

        cowListData = RunDB.getAllDairyCows();
        breederListData = RunDB.getAllDairyBreeders();

        cowList = new ArrayList<>();
        breederList = new ArrayList<>();

        for(String[] cowData : cowListData){
            Cow cow = CompareCow.toCow(cowData);
            cowList.add(cow);
        }
        for(String[] breederData : breederListData){
            Cow breeder = CompareCow.toBreeder(breederData);
            breederList.add(breeder);
        }
    }
    
    public void bfsSearch(List<Cow> sourceCowList){
        if(sourceCowList.isEmpty()){
            return;
        }

        ArrayList<Cow> levelList = new ArrayList<>();
        for(Cow sourceCow : sourceCowList){
            if(sourceCow == null ){
                System.out.println(levelList.toString());
                System.out.println("sourceCow == null");
                return ;
            }
            for(Cow cow : cowList){
                if(sourceCow.getDadCode().contains(cow.getZyanCode())){
                    sourceCow.setDad(cow);
                    levelList.add(cow);
                }

                if(sourceCow.getMomCode().contains(cow.getZyanCode())){
                    sourceCow.setMom(cow);
                    levelList.add(cow);
                }
            }

            for(Cow breeder : breederList){
                if(sourceCow.getDadCode().contains(breeder.getZyanCode())){
                    sourceCow.setDad(breeder);
                    levelList.add(breeder);
                }
            }
        }
        System.out.println(levelList.toString());
        bfsSearch(levelList);

    }
    public static void main(String[]args){
        BFS_Parent bfs = new BFS_Parent();
        bfs.setList();
        bfs.findParent("Z000B1837F7D245386DB523121EE42485797");
        
        for(String[] row : bfs.getCowString()){
            System.out.println(row[0] + " " + row[1] + " " + row[2]);
        }

    }
}
