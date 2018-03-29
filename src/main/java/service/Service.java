package service;

import model.Chromosome;
import model.Matrix;
import model.Population;
import repository.FileRepo;

import java.util.*;

public class Service {

    private FileRepo repo;
    private Matrix<Double> normalizationMatrix;
    private ArrayList<Double> mutationVector;
    public static ArrayList<Double> minHistory;

    public Service(FileRepo repo){
        this.repo = repo;
        normalizationMatrix = new Matrix<>(repo.getNumberOfRows(),repo.getNumberOfColumns(),0.0);
        minHistory = new ArrayList<>();
    }

    /**
     *  The function initializes the mutation vector with less/higher chance of mutation depending on the mutatianRate parameter
        mutatiaRate - Integer between 0 and 10
     **/
    private void initMutationVector(Integer mutatianRate){
        mutationVector = new ArrayList<>();
        mutationVector.add(0.1*mutatianRate);
        Double remainingMutatianRate = (Double)(1-0.1*mutatianRate);
        mutationVector.add(0.3*remainingMutatianRate);
        mutationVector.add(0.2*remainingMutatianRate);
        mutationVector.add(0.2*remainingMutatianRate);
        mutationVector.add(0.1*remainingMutatianRate);
        mutationVector.add(0.1*remainingMutatianRate);
        mutationVector.add(0.05*remainingMutatianRate);
        mutationVector.add(0.05*remainingMutatianRate);
        cumsum(mutationVector);
    }


    /**
     *  The function normalises the values of a population
     * @param p - The population of which values will be normalized
     * @return  Returns the sum of the values of every chromosome after normalisation
     */
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
                normalizationMatrix.set(normalizationFactor,c.getPosition().toArray(new Integer[0]));
            }
        }
        else{
            for(Chromosome c:p.getChromosomesList()){
                sum = sum + fitness(c);
            }
        }
        return sum;
    }


    /**
     *  The function determines the value of a given probability using a provided simple random variable
     * @param list  The distribution of the random variable
     * @param random Double (probability between 0 and 1)
     * @return  Returns the corresponding value for the given probability
     */
    private Integer vasInv(List<Double> list,Double random){
        for(int i=0;i<list.size();i++){
            if(random<list.get(i)){
                return i;
            }
        }
        return -1;
    }

    /**
     * The function determines the value associated with a chromosome
     * @param c Chromosome
     * @return  Returns the values of the provided Chromosome
     */
    private double fitness(Chromosome c){
        return repo.getValue(c.getPosition());
    }

    /**
     * The function determines the value associated with a chromosome after normalisation
     * @param c Chromosome
     * @return Returns the values of the provided Chromosome with normalisation added
     */
    private double fitnessWithNormalization(Chromosome c){
        return  fitness(c) + normalizationMatrix.get(c.getPosition().toArray(new Integer[0]));
    }

    /**
     * The function determines the cumulative sum of a list of values
     * @param list List of Double values
     */
    private void cumsum(List<Double> list){
        for(int j=1;j<list.size();j++){
            list.set(j,list.get(j)+list.get(j-1));
        }
    }

    /**
     * The function determines the minimum value in a population
     * @param p Population
     * @return Returns the minimum found value
     */
    private Double evaluatePopulation(Population p){
        Double min = Double.MAX_VALUE;
        for(Chromosome c:p.getChromosomesList()){
            if(fitness(c)<min){
                min = fitness(c);
            }
        }
        minHistory.add(min);
        return min;
    }

    /**
     * The function resets the normalisation for a given population
     * @param p Population
     */
    private void resetNormalization(Population p){
        for(Chromosome c:p.getChromosomesList()) {
            normalizationMatrix.set(0.0,c.getPosition().toArray(new Integer[0]));
        }
    }


    /**
     * The function mutates a Chromosome
     * @param offspring The Chromosome to be mutated
     */
    private  void mutate(Chromosome offspring){
        for(int i=0;i<offspring.getPosition().size();i++){
            Integer mutationOffset = vasInv(mutationVector,Math.random());
            Boolean mutationDirection = Math.random()<0.5;
            Integer mutatedPosition = 0;
            if(mutationDirection)
                mutatedPosition =  offspring.getPosition().get(i)+mutationOffset;
            else
                mutatedPosition = offspring.getPosition().get(i)-mutationOffset;
            if(mutatedPosition>=0 && mutatedPosition<repo.getNumberOfColumns()){
                offspring.getPosition().set(i,mutatedPosition);
            }
        }
    }

    /**
     * The function creates a new Chromosome according to two other Chromosomes
     * @param dad  One of the parent Chromosome
     * @param mom  The other parent Chromosome
     * @return  Return the offspring of the 2 provided chromosomes
     */
    private Chromosome offspring(Chromosome dad,Chromosome mom){
        Vector<Integer> newCoords = new Vector<>();
        for(int i=0;i<dad.getPosition().size();i++){
            newCoords.add((dad.getPosition().get(i)+mom.getPosition().get(i))/2);
        }
        return new Chromosome(newCoords.toArray(new Integer[0]));
    }


    /**
     * The function determines the minimum value in an 1 or 2 dimensional array using an evolutionary algorithm
     * @param numberOfGenerations
     * @param populationSize    The size of every population in each generation
     * @param mutatianRate     The mutation rate of the population
     * @return  Return the found minimum
     */
    public double solve(Integer numberOfGenerations, Integer populationSize,Integer mutatianRate){
        initMutationVector(10-mutatianRate);
        Random r = new Random();
        Population p = new Population();
        for(int i=0;i<populationSize;i++) {
            if(repo.getNumberOfRows()==1){
                p.addChromosome(new Chromosome(r.nextInt(repo.getNumberOfColumns())));
            }
            else p.addChromosome(new Chromosome(r.nextInt(repo.getNumberOfRows()),r.nextInt(repo.getNumberOfColumns())));
        }
        Double min = evaluatePopulation(p);
        for(int i=0;i<numberOfGenerations;i++){
            Population offsprings = new Population();

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
                Chromosome offspring = offspring(dad,mom);
                mutate(offspring);
                offsprings.addChromosome(offspring);


            }
            Double newMin = evaluatePopulation(offsprings);
            //System.out.println("Min value of population "+i+" is: "+newMin);
            if(newMin<min)
                min = newMin;
            p = offsprings;
        }
        return min;
    }

    /**
     * The function determines the minimum value in an 1 or 2 dimensional array by browsing the array
     * @return Return the minimum
     */
    public Double getMinFromCrossing(){
        return repo.getMin();
    }

}
