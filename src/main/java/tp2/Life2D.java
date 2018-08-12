package tp2;

import utils.PointDumper;

import java.io.IOException;
import java.util.BitSet;

public class Life2D {

    @FunctionalInterface
    public interface LifeRule {
        boolean compute(BitSet[] arr, int i, int j, int M);
    }


    public static final LifeRule defaultRule = (arr, i, j, M) -> {
        int alive = countAlive(arr, i, j, M);
        if (arr[i].get(j) && (alive == 2 || alive == 3)) {
            return true;
        } else return alive == 3;
    };


    private BitSet arr[];
    private int M;
    private PointDumper pointDumper;

    public Life2D(int M, String basePath) {
        this.M = M;
        arr = init(M);
        pointDumper = new PointDumper(basePath, PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
    }

    public void run(int generations, LifeRule rule) throws IOException{
        printAlive(0);
        for(int i = 1; i < generations+1; i++){
            singleGeneration(rule, i);
        }
    }

    private void singleGeneration(LifeRule rule, int gen) throws IOException {
        BitSet ans[] = init(M);
        for(int i = 0; i < M; i++){
            for (int j = 0; j < M; j++){
                boolean status = rule.compute(arr, i, j, M);
                ans[i].set(j, status);
                if (status){
                    pointDumper.print2D(gen, i, j);
                }
            }
        }
        pointDumper.dump(gen);
        arr = ans;
    }

    public void set(int i, int j){
        checkConstraints(i, j);
        arr[i].set(j);
    }

    private void checkConstraints(int i, int j){
        if(isOutOfBounds(i, j, M))
            throw new IndexOutOfBoundsException("M was: " + M + ", i:" + i +", j: "+ j);
    }

    private static boolean isOutOfBounds(int i, int j, int M){
        return i < 0 || i >= M || j < 0 || j >= M;
    }

    private static int countAlive(BitSet set[], int i, int j, int M){
        int ans = 0;
        for(int x = i-1; x <= i+1; x++)
            for(int y = j-1; y <= j+1; y++)
                if((!(i == x && j == y)) && !isOutOfBounds(x, y, M) && set[x].get(y))
                    ans ++;

        return ans;
    }

    private static BitSet[] init(int M){
        BitSet ans[] = new BitSet[M];
        for(int i = 0; i < M; i++)
            ans[i] = new BitSet(M);
        return ans;
    }

    public void printAlive(int generation) throws IOException{
        for(int i = 0; i < M; i++){
            for (int j = 0; j < M; j++){
                boolean status = arr[i].get(j);
                if(status){
                    pointDumper.print2D(generation, i, j);
                }

            }
        }
        pointDumper.dump(generation);
    }

}
