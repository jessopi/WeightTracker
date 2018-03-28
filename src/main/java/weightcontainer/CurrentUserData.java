package main.java.weightcontainer;

import java.util.ArrayList;

import java.util.List;

/*
    Holds all data received from previously executed sql search query.
    Contains a list of WeightData objects and performs operations on them.
*/
public class CurrentUserData {
    private List<WeightData> weightDataArrayList;

    public CurrentUserData(){
        weightDataArrayList = new ArrayList<>();
    }

    public void addData(WeightData data){
        this.weightDataArrayList.add(data);
    }

    public void clear(){
        this.weightDataArrayList.clear();
    }

    public List<WeightData> getWeightDataArrayList() {
        return weightDataArrayList;
    }

    public int numberOfEntries(){
        return this.weightDataArrayList.size();
    }

    public String getSearchRange(){
        if(this.weightDataArrayList.isEmpty()){
            return "N/A";
        }
        String output;
        output = weightDataArrayList.get(0).getDate() + " - " + weightDataArrayList.get(weightDataArrayList.size() - 1).getDate();
        return output;
    }
    public double getMax(){
        double imax = 0;
        for(WeightData item : this.weightDataArrayList){
            imax = Math.max(imax,item.getWeight());
        }
        return imax;
    }

    public double getMin(){
        if(this.weightDataArrayList.isEmpty()){
            return 0.0;
        }
        double imin = Integer.MAX_VALUE;
        for(WeightData item : this.weightDataArrayList){
            imin = Math.min(imin,item.getWeight());
        }
        return imin;
    }

    public double getAverage(){
        double sum = 0;
        if(this.weightDataArrayList.isEmpty()){
            return sum;
        }
        for(WeightData item : weightDataArrayList){
            sum+=item.getWeight();
        }
        return sum/this.numberOfEntries();
    }

    //returns largest change in weight
    public double getLargestFluctuation(){
        double fluct = 0;
        for(int i = 0; i < this.weightDataArrayList.size() - 1;i++){
            fluct = Math.max(Math.abs(weightDataArrayList.get(i).getWeight() - weightDataArrayList.get(i + 1).getWeight()),fluct);
        }
        return fluct;
    }
}
