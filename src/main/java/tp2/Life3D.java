package tp2;

import utils.PointDumper;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class Life3D {


    @FunctionalInterface
    public interface LifeRule {

        boolean compute(BitSet[][] arr, int i, int j, int k, int M);
    }

    public static final Life3D.LifeRule defaultRule = (arr, i, j, k, M) -> {
        int alive = countAlive(arr, i, j,k, M, false);
        if (arr[i][j].get(k) && (alive == 2 || alive == 3)) {
            return true;
        } else return alive == 3;
    };

    public interface LifeRuleFactory {
        public Life3D.LifeRule fabricate();
    }

    public static final  LifeRuleFactory b4s9Rule = () -> {
        Set<Integer> validAlive = new HashSet<>();
        validAlive.add(9);
        Set<Integer> validToBreed = new HashSet<>();
        validToBreed.add(4);
        return wxyzRuleFactory(validAlive, validToBreed);
    };

    public static final  LifeRuleFactory b5s56Rule = () -> {
        Set<Integer> validAlive = new HashSet<>();
        validAlive.add(5);
        validAlive.add(6);
        Set<Integer> validToBreed = new HashSet<>();
        validToBreed.add(5);
        return wxyzRuleFactory(validAlive, validToBreed);
    };

    /**
     *
     * @param w Minimum alive neighbours to keep living
     * @param x Maximum alive neighbours to keep living
     * @param y Minimum alive neighbours to be born
     * @param z Maximum alive neighbours to be born
     * @return
     */
    public static final Life3D.LifeRule wxyzRuleFactory(Set<Integer> validAlive, Set<Integer> validToBreed) {
        return (arr, i, j, k, M) -> {
            int alive = countAlive(arr, i, j, k, M, false);
            if (arr[i][j].get(k)) {
                // cell is alive
                return validAlive.contains(alive);
            } else {
                // cell is dead
                return validToBreed.contains(alive);
            }
        };
    }


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
        pointDumper.dumpStats();
    }

    private void singleGeneration(LifeRule rule, int gen) throws IOException {
        BitSet ans[][] = init(M);
        int alive = 0;
        int minX = M - 1 , minY = M - 1, minZ = M - 1,  maxX = 0, maxY = 0, maxZ = 0;
        double sumX = 0, sumY = 0, sumZ = 0;
        for(int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
                for(int k = 0; k < M; k++){
                    boolean status = rule.compute(arr, i, j, k, M);
                    ans[i][j].set(k, status);
                    if(status) {
                        alive++;
                        minX = Math.min(minX, i); maxX = Math.max(maxX, i);
                        minY = Math.min(minY, j); maxY = Math.max(maxY, j);
                        minZ = Math.min(minZ, k); maxZ = Math.max(maxZ, k);
                        sumX += i;
                        sumY += j;
                        sumZ += k;
                        pointDumper.print3D(gen, i, j, k);
                    }
                }

        double radius;
        if (alive == 0 || alive == 1) {
            radius = 0;
        } else {
            radius = Math.sqrt(Math.pow(maxX - minX, 2) + Math.pow(maxY - minY, 2) + Math.pow(maxZ - minZ, 2)) / 2.0;
        }
        pointDumper.pushStats(new Statistics(radius, alive, new double[]{sumX/alive, sumY/alive, sumZ/alive}));
        pointDumper.dump(gen);
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

    private static int countAlive(BitSet set[][], int i, int j, int k, int M, boolean periodic){
        int ans = 0;
        for(int x = i-1; x <= i+1; x++)
            for(int y = j-1; y <= j+1; y++)
                for(int z = k-1; z <= k+1; z++) {
                    if (x == i && y == j && z == k) {
                        continue;
                    }
                    if (periodic)
                        ans += set[Math.floorMod(x, M)][Math.floorMod(y, M)].get(Math.floorMod(z, M)) ? 1 : 0;
                    else
                        ans += !isOutOfBounds(x, y, z, M) && set[x][y].get(z) ? 1 : 0;
                }
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
        pointDumper.dump(generation);
    }

}
