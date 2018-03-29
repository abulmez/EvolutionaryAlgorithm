import com.google.common.base.Stopwatch;
import repository.FileRepo;
import service.Service;

import java.util.AbstractQueue;
import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Main {
    public static void main(String[] args){
        FileRepo r = new FileRepo("02_date4.txt");
        Service s = new Service(r);
        Stopwatch stopwatch = Stopwatch.createStarted();

        Double min = Double.MAX_VALUE;
        for(int i=0;i<r.getNumberOfRows();i++){
            for(int j=0;j<r.getNumberOfColumns();j++)
                if(r.getValue(i,j)<min)
                    min = r.getValue(i,j);
        }

        stopwatch.stop();
        System.out.println("Minimul determinat prin parcurgere: "+min);
        System.out.println("Cautarea a durat: "+stopwatch.elapsed(MILLISECONDS)+ " milisecunde");

        stopwatch.reset();
        stopwatch.start();
        System.out.println("Minimul determinat prin algoritm evolutiv: "+s.solve(1000,4));
        stopwatch.stop();
        System.out.println("Cautarea a durat: "+stopwatch.elapsed(MILLISECONDS)+ " milisecunde");
    }
}
