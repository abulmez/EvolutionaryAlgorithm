package repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRepo {

    String fileName;
    ArrayList<Double> vector;


    public FileRepo(String fileName){
        this.vector = new ArrayList<Double>();
        this.fileName = fileName;
        readFromFile();
    }

    private void readFromFile(){
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File file =
                    new File(classLoader.getResource(fileName).getFile());
            Scanner sc = new Scanner(file);
            int vectorSize = Integer.parseInt(sc.nextLine());
            String[] vectorAsString = sc.nextLine().split(" ");
            for(int i=0;i<vectorSize;i++) {
                vector.add(Double.parseDouble(vectorAsString[i]));
            }
        }
        catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Double> getVector() {
        return vector;
    }

    public double getValue(int position){
        return vector.get(position);
    }
}
