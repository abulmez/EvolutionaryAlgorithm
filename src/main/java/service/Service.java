package service;

import model.Chromosome;
import model.Population;
import repository.FileRepo;

import java.util.*;

public class Service {

    FileRepo repo;
    ArrayList<Double> normalizationVector;
    ArrayList<Double> mutationVector;

    public Service(FileRepo repo){
        this.repo = repo;
        this.normalizationVector = new ArrayList<>();
        for(int i=0;i<repo.getVector().size();i++){
            normalizationVector.add(0.0);
        }
        initMutationVector();
    }

    private void initMutationVector(){
        mutationVector = new ArrayList<>();
        mutationVector.add(0.3);
        mutationVector.add(0.2);
        mutationVector.add(0.2);
        mutationVector.add(0.1);
        mutationVector.add(0.1);
        mutationVector.add(0.05);
        mutationVector.add(0.05);
        cumsum(mutationVector);
    }

    private double normalizePopulation(Population p){
        Double min = Double.MAX_VALUE;
        for(Chromosome c:p.getChromosomesList()){
            if(fitness(c)<min){
                min = fitness(c);
            }
        }
        Double normalizationFactor = 0.0;
        Double sum = 0.0;
        if(min<0){
            normalizationFactor = Math.abs(min) + 1;
            for(Chromosome c:p.getChromosomesList()){
                sum = sum + normalizationFactor + fitness(c);
                normalizationVector.set(c.getPosition(),normalizationFactor);
            }
        }
        else{
            for(Chromosome c:p.getChromosomesList()){
                sum = sum + fitness(c);
            }
        }
        return sum;
    }

    private Integer vasInv(List<Double> list,Double random){
        for(int i=0;i<list.size();i++){
            if(random<list.get(i)){
                return i;
            }
        }
        return -1;
    }

    private double fitness(Chromosome c){
        return repo.getValue(c.getPosition());
    }

    private double fitnessWithNormalization(Chromosome c){
        Double value = repo.getValue(c.getPosition()) + normalizationVector.get(c.getPosition());
        return value;
    }

    private void cumsum(List<Double> list){
        for(int j=1;j<list.size();j++){
            list.set(j,list.get(j)+list.get(j-1));
        }
    }

    private Double evaluatePopulation(Population p){
        Double min = Double.MAX_VALUE;
        for(Chromosome c:p.getChromosomesList()){
            if(fitness(c)<min){
                min = fitness(c);
            }
        }
        return min;
    }

    private void resetNormalization(Population p){
        for(Chromosome c:p.getChromosomesList()) {
            normalizationVector.set(c.getPosition(), 0.0);
        }
    }

    public Integer mutate(Integer toMutateValue,Integer mutationValue,Boolean mutationDirection){
        if(mutationDirection)
            return toMutateValue-mutationValue;
        else return toMutateValue+mutationValue;
    }

    public double solve(Integer numberOfGenerations, Integer populationSize){
        Population p = new Population();
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i=0;i<repo.getVector().size();i++){
            positions.add(i);
        }
        Collections.shuffle(positions);
        for(int i=0;i<populationSize;i++){
            p.addChromosome(new Chromosome(positions.get(i)));
        }
        Double min = evaluatePopulation(p);
        for(int i=0;i<numberOfGenerations;i++){
            Population offspring = new Population();

            Double sum = normalizePopulation(p);
            LinkedList<Double> chance = new LinkedList<>();
            for(Chromosome c:p.getChromosomesList()){
                chance.addFirst(fitnessWithNormalization(c)/sum);
            }
            resetNormalization(p);
            cumsum(chance);
            for(int j=0;j<populationSize;j++) {
                Chromosome dad = p.getChromosome(vasInv(chance, Math.random()));
                Chromosome mom = p.getChromosome(vasInv(chance, Math.random()));
                Integer offstringPosition = (dad.getPosition() + mom.getPosition()) / 2;
                Integer mutationOffset = vasInv(mutationVector,Math.random());
                Boolean mutationDirection = Math.random()<0.5;
                Integer mutatedOffstringPosition = mutate(offstringPosition,mutationOffset,mutationDirection);
                if(mutatedOffstringPosition>=0 && mutatedOffstringPosition< repo.getVector().size())
                    offspring.addChromosome(new Chromosome(mutatedOffstringPosition));
                else offspring.addChromosome(new Chromosome(offstringPosition));


            }
            Double newMin = evaluatePopulation(offspring);
            //System.out.println("Min value of population "+i+" is: "+newMin);
            if(newMin<min)
                min = newMin;
            p = offspring;
        }
        return min;
    }
}
