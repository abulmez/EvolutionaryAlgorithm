package model;

import java.util.ArrayList;


public class Chromosome {
    private ArrayList<Integer> position;

    public Chromosome(Integer ... args){
        position = new ArrayList<>();
        for(Integer arg:args){
            position.add(arg);
        }
    }

    public ArrayList<Integer> getPosition() {
        return position;
    }

    public void setPosition(ArrayList<Integer> position) {
        this.position = position;
    }
}
