package tp5;

import common.ParticleGenerators;
import common.Grid;
import java.util.Random;

public class Main {
    static int seed = 1;
    static int M = 10;
    static int N = 10;
    static float L, W, radius, mass;
    static double MaxTime = 10, DeltaTime = 0.1;

    public static void main(String [] args) throws Exception {
        Random r = new Random(seed);
        Grid grid = new Grid(L, W, M);
        ParticleGenerators.generateGranularParticles(L, W, N, radius, mass, r).forEach(grid::add);

        for (double t = 0; t < MaxTime; t += DeltaTime) {

        }
    }
}
