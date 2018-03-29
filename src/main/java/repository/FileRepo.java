package repository;

import model.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRepo {

    String fileName;
    Matrix<Double> matrix;


    public FileRepo(String fileName){
        this.fileName = fileName;
        readFromFile();
    }

    private void readFromFile(){
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File file =
                    new File(classLoader.getResource(fileName).getFile());
            Scanner sc = new Scanner(file);
            int rows = Integer.parseInt(sc.nextLine());
            String[] vectorAsString = sc.nextLine().split(" ");
            if(vectorAsString.length == 1 && rows!=1){
                int columns = Integer.parseInt(vectorAsString[0]);
                matrix = new Matrix<>(rows,columns,0.0);
                for(int i=0;i<rows;i++){
                    vectorAsString = sc.nextLine().split(" ");
                    for(int j=0;j<columns;j++) {
                        matrix.set(Double.parseDouble(vectorAsString[i]),i,j);
                    }
                }
            }
            else {
                matrix = new Matrix<>(rows,0.0);
                for (int i = 0; i < rows; i++) {

                    matrix.set(Double.parseDouble(vectorAsString[i]),i);
                }
            }
        }
        catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public double getValue(ArrayList<Integer> coords){
        return matrix.get(coords.toArray(new Integer[0]));
    }

    public double getValue(Integer row,Integer column){
        return matrix.get(row,column);
    }

    public int getNumberOfRows(){
        return matrix.getRows();
    }

    public int getNumberOfColumns(){
        return matrix.getColumns();
    }

    public Double getMin(){
        Double min = Double.MAX_VALUE;
        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<getNumberOfColumns();j++)
                if(getValue(i,j)<min)
                    min = getValue(i,j);
        }
        return min;
    }
}
