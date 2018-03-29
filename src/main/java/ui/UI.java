package ui;

import com.google.common.base.Stopwatch;
import repository.FileRepo;
import service.Service;

import java.util.ArrayList;
import java.util.Scanner;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class UI {

    Service service;
    Scanner sc;
    public UI(){
        sc = new Scanner(System.in);
    }

    /**
     * Displays the main menu of the application
     */
    public void displayMainMenu() {
        System.out.println("Dati numele fisierului de intrare:");
        String fileName = sc.nextLine();
        service = new Service(new FileRepo(fileName));
        System.out.println("Dati numarul de generatii:");
        int generationNumber = sc.nextInt();
        System.out.println("Dati marimea populatiei:");
        int populationSize = sc.nextInt();

        Stopwatch stopwatch = Stopwatch.createStarted();

        Double min = service.getMinFromCrossing();
        stopwatch.stop();
        System.out.println("Minimul determinat prin parcurgere: "+min);
        System.out.println("Cautarea a durat: "+stopwatch.elapsed(MILLISECONDS)+ " milisecunde");

        stopwatch.reset();
        stopwatch.start();
        System.out.println("Minimul determinat prin algoritm evolutiv: "+service.solve(generationNumber,populationSize));
        stopwatch.stop();
        System.out.println("Cautarea a durat: "+stopwatch.elapsed(MILLISECONDS)+ " milisecunde");
    }
}
