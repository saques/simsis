package tp5;

import common.ParticleGenerators;
import utils.PointDumper;

import java.util.Random;

public class Main {
    static int seed = 1;
    static int M = 10;
    static int N = 100;
    static float L = 1, W = 1, radius = 0.01f, mass = 0.01f;
    static double MaxTime = 0.1, DeltaTime = 1E-4;
    static double k = 10E5, gamma = 100, mu = 0.1;
    static double D = 0.15;
    static int dumpEach = 10;


    public static void main(String [] args) throws Exception {
        Random r = new Random(seed);
        DynamicGrid grid = new DynamicGrid(L, W, M, D);
        //ParticleGenerators.generateGranularParticles(L, W, N, radius, mass, k, gamma, mu, r).forEach(grid::add);

        grid.add(new GranularParticle(1, 1,  0, 0, radius, mass, k, gamma, mu));
        grid.add(new GranularParticle(1, 1 + 3*radius,  0, 0, radius, mass, k, gamma, mu));

        PointDumper dumper = new PointDumper("./tp5/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        int frame = 0;
        for (int i = 0; i < MaxTime/DeltaTime; i ++) {
            boolean dump = i % dumpEach == 0;
            grid.update(frame, DeltaTime, dumper, dump);
            if(dump)
                frame++;
        }
    }
}
