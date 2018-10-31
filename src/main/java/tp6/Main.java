package tp6;

import common.ParticleGenerators;
import common.Vector2D;
import tp5.DynamicGridBeeman;
import tp5.GranularParticleStats;
import utils.PointDumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    static double frameRate = 30;
    static int seed = 4;
    static int M = 8;
    static int N = 10;
    static float L = 20, W = 20, minRadius = 0.25f, maxRadius = 0.29f, mass = 65.0f, radius = 0.03f,minV = 4f,maxV=4f;
    static double MaxTime = 120, DeltaTime = 1E-3;
    static double k = 1.2E5, gamma = 100, mu = 0.1, kt = 2.4E5;
    static double D = 1.2;
    static double A = 2000;
    static double B = 0.08;
    static double tau = 0.5;
    static int dumpEach = (int) ((1.0/frameRate) / DeltaTime);

    public static void main(String[] args) throws Exception {
        Random r = new Random(seed);
        List<Vector2D> list = new ArrayList<>();
        list.add(new Vector2D(10, (maxRadius + minRadius) / 2));
        list.add(new Vector2D(10, -100));
        DynamicGridPed grid = new DynamicGridPed(L, W, M, r, D, list, 2);
        ParticleGenerators.generatePedestrians(L, W, N, minRadius, maxRadius,minV,maxV, mass, k, kt, gamma, mu, A,B,tau,r).forEach(grid::add);

        PointDumper dumper = new PointDumper("./tp6/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GranularParticleStats stats = new GranularParticleStats();
        stats.particles = N;
        int frame = 0;
        for (int i = 0; grid.size() != 0; i ++) {
            boolean dump = i % dumpEach == 0;
            grid.update(frame, DeltaTime, dumper, dump, stats);
            if(dump)
                frame++;
        }

        dumper.dumpGranularStats(stats);
    }
}
