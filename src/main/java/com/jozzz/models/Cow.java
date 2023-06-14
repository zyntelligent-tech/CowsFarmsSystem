package com.jozzz.models;

import java.util.ArrayList;

public class Cow {

    private String cowCode;
    private String momCode;
    private String dadCode;

    private Cow mom;
    private Cow dad;
    private Cow child;

    public ArrayList<Breed> breeds;

    public Cow(){}

    public Cow(String cowCode, String momCode, String dadCode) {
        this.cowCode = cowCode;
        this.momCode = momCode;
        this.dadCode = dadCode;
    }

    public Cow(String cowCode, String momCode, String dadCode, Cow mom, Cow dad) {
        this.cowCode = cowCode;
        this.momCode = momCode;
        this.dadCode = dadCode;
        this.mom = mom;
        this.dad = dad;
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

    @Override
    public String toString() {
        return getCowCode() + " " + getMomCode() + " " + getDadCode() + "\t";
    }
}