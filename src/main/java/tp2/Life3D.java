package tp2;

import utils.PointDumper;

import java.io.IOException;
import java.util.BitSet;

public class Life3D {


    @FunctionalInterface
    public interface LifeRule {

        boolean compute(BitSet[][] arr, int i, int j, int k, int M);
    }

    public static final Life3D.LifeRule defaultRule = (arr, i, j, k, M) -> {
        int alive = countAlive(arr, i, j,k, M);
        if (arr[i][k].get(k) && (alive == 2 || alive == 3)) {
            return true;
        } else return alive == 3;
    };


    private final PointDumper pointDumper;
    private BitSet arr[][];
    private int M;

    public Life3D(int M, String basePath){
        this.M = M;
        arr = init(M);
        pointDumper = new PointDumper(basePath, PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._3D);
    }

    public void run(int generations, LifeRule rule) throws IOException{
        printAlive(0);
        for(int i = 1; i < generations+1; i++) {
            singleGeneration(rule, i);
        }
    }

    private void singleGeneration(LifeRule rule, int gen) throws IOException {
        BitSet ans[][] = init(M);
        for(int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
                for(int k = 0; k < M; k++){
                    boolean status = rule.compute(arr, i, j, k, M);
                    ans[i][j].set(k, status);
                    if(status)
                        pointDumper.print3D(gen, i, j, k);
                }
        arr = ans;
    }

    public void set(int i, int j, int k){
        checkConstraints(i, j, k);
        arr[i][j].set(k);
    }

    private void checkConstraints(int i, int j, int k){
        if(isOutOfBounds(i, j, k, M))
            throw new IndexOutOfBoundsException();
    }

    private static boolean isOutOfBounds(int i, int j, int k, int M){
        return i < 0 || i >= M || j < 0 || j >= M || k < 0 || k >= M;
    }

    private static int countAlive(BitSet set[][], int i, int j, int k, int M){
        int ans = 0;
        for(int x = i-1; x <= i+1; x++)
            for(int y = j-1; y <= j+1; y++)
                for(int z = k-1; z <= k+1; z++)
                    if((!(i == x && j == y && k == z)) && !isOutOfBounds(x, y, z, M) && set[x][y].get(z))
                        ans ++;
        return ans;
    }

    private static BitSet[][] init(int M){
        BitSet ans[][] = new BitSet[M][M];
        for(int i = 0; i < M; i++)
            for(int j = 0; j < M; j++)
                ans[i][j] = new BitSet(M);
        return ans;
    }

    public void printAlive(int generation) throws IOException {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < M; k++) {
                    boolean status = arr[i][j].get(k);
                    if (status) {
                        pointDumper.print3D(generation, i, j, k);
                    }
                }
            }
        }
    }

}
