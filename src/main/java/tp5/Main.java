package tp5;

import common.ParticleGenerators;
import utils.PointDumper;

import java.util.Random;

public class Main {
    static int seed = 1;
    static int M = 10;
    static int N = 100;
    static float L = 1, W = 1, radius = 0.01f, mass = 0.01f;
    static double MaxTime = 10, DeltaTime = 0.01;
    static double k = 10E5, gamma = 100, mu = 0.7;
    static double D = 0.15;

    public static void main(String [] args) throws Exception {
        Random r = new Random(seed);
        DynamicGrid grid = new DynamicGrid(L, W, M, D);
        ParticleGenerators.generateGranularParticles(L, W, N, radius, mass, k, gamma, mu, r).forEach(grid::add);
        PointDumper dumper = new PointDumper("./tp5/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        int frame = 0;
        for (double t = 0; t < MaxTime; t += DeltaTime) {
            grid.update(frame, DeltaTime, dumper);
            frame++;
        }
    }
}
