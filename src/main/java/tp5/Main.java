package tp5;

import common.ParticleGenerators;
import utils.PointDumper;

import java.io.IOException;
import java.util.Random;

public class Main {
    static int seed = 2;
    static int M = 7;
    static int N = 500;
    static float L = 4, W = 4, radius = 0.03f, mass = 0.01f;
    static double MaxTime = 5, DeltaTime = 4E-5;
    static double k = 1E5, gamma = 100, mu = 0.1;
    static double D = L * 0.1;
    static int dumpEach = (int) (0.016 / DeltaTime);


    public static void main(String [] args) throws Exception {
        runBeeman();
    }


    private static void runGear() throws Exception {
        Random r = new Random(seed);
        DynamicGrid grid = new DynamicGrid(L, W, M, D, r);
        ParticleGenerators.generateGranularParticles(L, W, N, radius, mass, k, gamma, mu, r).forEach(grid::add);

        //grid.add(new GranularParticle(1, 1,  0, 0, radius, mass, k, gamma, mu));
        //grid.add(new GranularParticle(1, (1 + .5),  0, 0, radius, mass, k, gamma, mu));

        PointDumper dumper = new PointDumper("./tp5/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        int frame = 0;
        for (int i = 0; i < MaxTime/DeltaTime; i ++) {
            boolean dump = i % dumpEach == 0;
            grid.update(frame, DeltaTime, dumper, dump);
            if(dump)
                frame++;
        }
    }

    private static void runBeeman() throws Exception{
        Random r = new Random(seed);
        DynamicGridBeeman grid = new DynamicGridBeeman(L, W, M, D, r);
        ParticleGenerators.generateBeemanGranularParticles(L, W, N, radius, mass, k, gamma, mu, r).forEach(grid::add);

//        grid.add(new GranularParticle(1, 1,  0, 0, radius, mass, k, gamma, mu));
//        grid.add(new GranularParticle(1, (1 + .5),  0, 0, radius, mass, k, gamma, mu));

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
