import com.google.common.base.Stopwatch;
import repository.FileRepo;
import service.Service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Main {
    public static void main(String[] args){
        FileRepo r = new FileRepo("02_date3.txt");
        Service s = new Service(r);
        Stopwatch stopwatch = Stopwatch.createStarted();

        Double min = Double.MAX_VALUE;
        for(int i=0;i<r.getVector().size();i++){
            if(r.getValue(i)<min)
                min = r.getValue(i);
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
