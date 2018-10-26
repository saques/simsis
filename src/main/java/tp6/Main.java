package tp6;

import common.ParticleGenerators;
import tp5.DynamicGridBeeman;
import tp5.GranularParticleStats;
import utils.PointDumper;

import java.util.Random;

public class Main {
    static double frameRate = 60;
    static int seed = 4;
    static int M = 8;
    static int N = 500;
    static float L = 20, W = 20, minRadius = 0.25f, maxRadius = 0.29f, mass = 65.0f, radius = 0.03f,minV = 0.8f,maxV=6;
    static double MaxTime = 5, DeltaTime = 3E-5;
    static double k = 1E5, gamma = 100, mu = 0.07;
    static double D = 1.2;
    static double A = 2000;
    static double B = 0.08;
    static double tau = 0.5;
    static int dumpEach = (int) ((1.0/frameRate) / DeltaTime);

    public static void main(String[] args) throws Exception {
        Random r = new Random(seed);
        DynamicGridBeeman grid = new DynamicGridBeeman(L, W, M, D, r);
        ParticleGenerators.generatePedestrians(L, W, N, minRadius, maxRadius,minV,maxV, mass, k, gamma, mu, A,B,tau,r).forEach(grid::add);

        PointDumper dumper = new PointDumper("./tp6/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GranularParticleStats stats = new GranularParticleStats();
        stats.particles = N;
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
