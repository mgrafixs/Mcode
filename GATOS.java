/**
 *  GATOS   to evolve a set of weights (one for each trading signal) to determine an optimal trading action       .
 *
 * @author (Dan Bright)
 * @version (12/2019)
 */
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

class Individual implements Comparable<Individual> {
    public static final int SIZE = 4;
    public boolean[] tradingsignal;
    final Random rand = new Random();
 
    public Individual() {
        tradingsignal = new boolean[SIZE];
    }
 
    void random() {
        for (int i = 0; i < tradingsignal.length; i++) {
            tradingsignal[i] = 0.5 > rand.nextFloat();
        }
    }
 
    private String TDOutput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tradingsignal.length; i++) {
            sb.append(tradingsignal[i] == true ? 1 : 0);
        }
        return sb.toString();
    }
 
    int fitness() {
        int stock = 0;
        for (int i = 0; i < tradingsignal.length; i++) {
            if (tradingsignal[i])
                stock++;
        }
        return stock; 
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
        return "TDOutput=" + TDOutput() + " fit=" + fitness();
    }
}
 class GATOS {
 
    static long STARTTRADING;
    static final boolean _DEBUG = true;
    LinkedList<Individual> candidate = new LinkedList<Individual>();
    final Random rand = new Random();
 
    final int candidateSize = 4;
    final double parentUsePercent = 0.8;
 
    public GATOS() {
        for (int i = 0; i < candidateSize; i++) {
            Individual c = new Individual();
            c.random();
            candidate.add(c);
        }
 
        Collections.sort(candidate); // sorting method
        System.out.println("Init candidate sorted");
        print();
    }
 
    void print() {
        System.out.println("-- print");
        for (Individual c : candidate) {
            System.out.println(c);
        }
    }
 
    /**
     * Selection method: Tournament method strategy: elitism 0.8 */
    
     void produceNextOutput() {
        LinkedList<Individual> newcandidate = new LinkedList<Individual>();
 
        while (newcandidate.size() < candidateSize
                * (parentUsePercent)) {
 
            int size = candidate.size();
            int i = rand.nextInt(size);
            int j, k, l, m;
 
            j = k = l = m = i;
 
            while (j == i)
                j = rand.nextInt(size);
            while (k == i || k == j)
                k = rand.nextInt(size);
            while (l == i || l == j || k == l)
                l = rand.nextInt(size);
            while (m == i || m == j || k == m)
                m = rand.nextInt(size);
 
            Individual c1 = candidate.get(i);
            Individual c2 = candidate.get(j);
            Individual c3 = candidate.get(k);
            Individual c4 = candidate.get(l);
            Individual c5 = candidate.get(m);
 
            double f1 = c1.fitness();
            double f2 = c2.fitness();
            double f3 = c3.fitness();
            double f4 = c4.fitness();
            double f5 = c5.fitness();
 
            Individual w1, w2, w3;
 
            if (f1 > f2)
                w1 = c1;
            else
                w1 = c2;
 
            if (f3 > f4)
                w2 = c3;
            else
                w2 = c4;
            
            if (f4 > f5)
                w3 = c4;
            else
                w3 = c5;
            
 
            Individual TS1, TS2;
 
            // Method one-point crossover random pivot
            // int pivot = rand.nextInt(Individual.SIZE-2) + 1; // cut interval
            // is 1 .. size-1
            // TS1 = newTS(w1,w2,pivot);
            // TS2 = newTS(w2,w1,pivot);
 
            // Method uniform crossover
            Individual[] TSs = newTSs(w1, w2);
            TS1 = TSs[0];
            TS2 = TSs[1];
 
            double mutatePercent = 0.01;
            boolean m1 = rand.nextFloat() <= mutatePercent;
            boolean m2 = rand.nextFloat() <= mutatePercent;
 
            if (m1)
                mutate(TS1);
            if (m2)
                mutate(TS2);
 
            boolean isTS1Good = TS1.fitness() >= w1.fitness();
            boolean isTS2Good = TS2.fitness() >= w2.fitness();
 
            newcandidate.add(isTS1Good ? TS1 : w1);
            newcandidate.add(isTS2Good ? TS2 : w2);
        }
 
        // add top percent parent
        int j = (int) (candidateSize * parentUsePercent / 100.0);
        for (int i = 0; i < j; i++) {
            newcandidate.add(candidate.get(i));
        }
 
        candidate = newcandidate;
        Collections.sort(candidate);
    }
 
    // one-point crossover random pivot
    Individual newTS(Individual c1, Individual c2, int pivot) {
        Individual TS = new Individual();
 
        for (int i = 0; i < pivot; i++) {
            TS.tradingsignal[i] = c1.tradingsignal[i];
        }
        for (int j = pivot; j < Individual.SIZE; j++) {
            TS.tradingsignal[j] = c2.tradingsignal[j];
        }
 
        return TS;
    }
 
    // Uniform crossover
    Individual[] newTSs(Individual c1, Individual c2) {
        Individual TS1 = new Individual();
        Individual TS2 = new Individual();
 
        for (int i = 0; i < Individual.SIZE; i++) {
            boolean b = rand.nextFloat() >= 0.5;
            if (b) {
                TS1.tradingsignal[i] = c1.tradingsignal[i];
                TS2.tradingsignal[i] = c2.tradingsignal[i];
            } else {
                TS1.tradingsignal[i] = c2.tradingsignal[i];
                TS2.tradingsignal[i] = c1.tradingsignal[i];
            }
        }
 
        return new Individual[] { TS1, TS2 };
    }
 
    void mutate(Individual c) {
        int i = rand.nextInt(Individual.SIZE);
        c.tradingsignal[i] = !c.tradingsignal[i]; // flip
    }
 
    public static void main(String[] args) {
        
        GATOS GATOS = new GATOS();
        GATOS.run();
 
           }
 /**Termination Criteria**
  */
    void run() {
        final int maxGen = 1000;
        int count = 0;
 
        while (count < maxGen) {
            count++;
            produceNextOutput();
        }
 
        System.out.println("\nResult");
        print();
    }
}

