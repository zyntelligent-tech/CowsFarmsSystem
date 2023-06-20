package com.jozzz.models;

import java.util.ArrayList;

public class Cow {
    //Old variable
    private String momCode;
    private String dadCode;
    private String cowCode;
    
    //New variable
    private String zyanCode;
    private Cow mom;
    private Cow dad;
    private Cow child;

    public ArrayList<Breed> breeds;

    public Cow(){}

    public Cow(String zyanCode){
        this.zyanCode = zyanCode;
    }

    public Cow(String zyanCode,Cow mom, Cow dad) {
        this.zyanCode = zyanCode;
        this.mom = mom;
        this.dad = dad;
    }


    public String getZyanCode() {
        return zyanCode;
    }


    public void setZyanCode(String zyanCode) {
        this.zyanCode = zyanCode;
    }

    public Cow getMom() {
        return mom;
    }

    public void setMom(Cow mom) {
        this.mom = mom;
    }

    public Cow getDad() {
        return dad;
    }

    public void setDad(Cow dad) {
        this.dad = dad;
    }

    public Cow getChild() {
        return child;
    }

    public void setChild(Cow child) {
        this.child = child;
    }

    public ArrayList<Breed> getBreeds() {
        return breeds;
    }

    public void setBreeds(ArrayList<Breed> breeds) {
        this.breeds = breeds;
    }



    public String getCowCode() {
        return cowCode;
    }

    public void setCowCode(String cowCode) {
        this.cowCode = cowCode;
    }

    public String getMomCode() {
        return momCode;
    }

    public void setMomCode(String momCode) {
        this.momCode = momCode;
    }

    public String getDadCode() {
        return dadCode;
    }

    public void setDadCode(String dadCode) {
        this.dadCode = dadCode;
    }

    public Cow(String zyanCode , String momCode , String dadCode){
        this.zyanCode = zyanCode;
        this.momCode = momCode;
        this.dadCode = dadCode;
    }
    @Override
    public String toString() {
        return "Cow [zyanCode = "+zyanCode+"]";
    }

    
}