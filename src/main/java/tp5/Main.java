package tp5;

import common.ParticleGenerators;
import utils.PointDumper;

import java.util.Random;

public class Main {
    static double frameRate = 60;
    static int seed = 4;
    static int M = 8;
    static int N = 500;
    static float L = 5, W = 2, minRadius = 0.02f, maxRadius = 0.03f, mass = 0.01f, radius = 0.03f;
    static double MaxTime = 2, DeltaTime = 5E-5;
    static double k = 1E5, gamma = 100, tgMult = 0.7;
    static double D = 0;
    static int dumpEach = (int) ((1.0/frameRate) / DeltaTime);


    public static void main(String [] args) throws Exception {
        runBeeman();
    }


    private static void runGear() throws Exception {
        Random r = new Random(seed);
        DynamicGrid grid = new DynamicGrid(L, W, M, D, r);
        ParticleGenerators.generateGranularParticles(L, W, N, radius, mass, k, gamma, tgMult, r).forEach(grid::add);

        //grid.add(new GranularParticle(1, 1,  0, 0, radius, mass, k, gamma, tgMult));
        //grid.add(new GranularParticle(1, (1 + .5),  0, 0, radius, mass, k, gamma, tgMult));

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
        ParticleGenerators.generateBeemanGranularParticles(L, W, N, minRadius, maxRadius, mass, k, gamma, tgMult, r).forEach(grid::add);

        PointDumper dumper = new PointDumper("./tp5/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GranularParticleStats stats = new GranularParticleStats();
        int frame = 0;
        for (int i = 0; i < MaxTime/DeltaTime; i ++) {
            boolean dump = i % dumpEach == 0;
            grid.update(frame, DeltaTime, dumper, dump, stats);
            if(dump)
                frame++;
        }

        dumper.dumpGranularStats(stats);
    }
}
