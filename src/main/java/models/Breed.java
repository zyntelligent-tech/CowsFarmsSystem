package models;

public class Breed {

    private String breed;
    private double percent;

    public Breed(String breed, double percent) {
        this.breed = breed;
        this.percent = percent;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return getBreed()+" "+getPercent();
    }
}
