package main.java.weightcontainer;


/*
    Class that contains a date and weight pair.
*/
public class WeightData  {

    private String date;
    private Double weight;

    public WeightData(String inputDate, Double inputWeight){
        this.date = inputDate;
        this.weight = inputWeight;
    }

    public Double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    //just for testing values
    @Override
    public String toString() {
        return date + " - " + weight + "lbs.";
    }


}
