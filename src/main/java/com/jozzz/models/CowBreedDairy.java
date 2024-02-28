package com.jozzz.models;

public class CowBreedDairy {
    private int cow_id;
    private String cow_name;
    private String cow_fa_zyan_code;
    private String cow_ma_zyan_code;
    private int farm_id;
    private int breed_id;
    private String breed_code;
    private String breed_name;
    private String breed_id_string;

    public int getBreed_id(){
        return breed_id;
    }
    public void setBreed_id(int breed_id){
        this.breed_id = breed_id;
    }
    public int getCow_id() {
        return cow_id;
    }
    public void setCow_id(int cow_id) {
        this.cow_id = cow_id;
    }
    public String getCow_name() {
        return cow_name;
    }
    public void setCow_name(String cow_name) {
        this.cow_name = cow_name;
    }
    public String getCow_fa_zyan_code() {
        return cow_fa_zyan_code;
    }
    public void setCow_fa_zyan_code(String cow_fa_zyan_code) {
        this.cow_fa_zyan_code = cow_fa_zyan_code;
    }
    public String getCow_ma_zyan_code() {
        return cow_ma_zyan_code;
    }
    public void setCow_ma_zyan_code(String cow_ma_zyan_code) {
        this.cow_ma_zyan_code = cow_ma_zyan_code;
    }
    public int getFarm_id() {
        return farm_id;
    }
    public void setFarm_id(int farm_id) {
        this.farm_id = farm_id;
    }
    public String getBreed_code() {
        return breed_code;
    }
    public void setBreed_code(String breed_code) {
        this.breed_code = breed_code;
    }
    public String getBreed_name() {
        return breed_name;
    }
    public void setBreed_name(String breed_name) {
        this.breed_name = breed_name;
    }
    public String getBreed_id_string() {
        return breed_id_string;
    }
    public void setBreed_id_string(String breed_id_string) {
        this.breed_id_string = breed_id_string;
    }
    
}
