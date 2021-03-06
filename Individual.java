
/**
 *  GATOS   to evolve a set of weights (one for each trading signal) to determine an optimal trading action       .
 *
 * @author (Dan Bright)
 * @version (12/2019)
 */
import java.util.Random;
 

public class Individual implements Comparable<Individual> {
    public static final int weight = 1;
    public boolean[] tradingsignal;
    final Random rand = new Random();
 
    public Individual() {
        tradingsignal = new boolean[weight];
    }
 
    void random() {
        for (int i = 0; i < tradingsignal.length; i++) {
            tradingsignal[i] = 0.5 > rand.nextFloat();
        }
    }
 
    private String gen() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tradingsignal.length; i++) {
            sb.append(tradingsignal[i] == true ? 1 : 0);
        }
            return sb.toString();
        }
 
    int fitness() {
        int sum = 0;
        for (int i = 0; i < tradingsignal.length; i++) {
            if (tradingsignal[i])
                sum++;
        }
        return sum;
    }
 
    public int compareTo(Individual o) {
        int f1 = this.fitness();
        int f2 = o.fitness();
 
        if (f1 < f2)
            return 1;
        else if (f1 > f2)
            return -1;
        else
            return 0;
    }
 
    @Override
    public String toString() {
        return "gen=" + gen() + " fit=" + fitness();
    }
}