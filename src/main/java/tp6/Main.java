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
    private static double frameRate = 30;
    private static Long seed = 4l;
    private static int M = 8;
    private static int N = 200;
    private static float L = 20, W = 20, minRadius = 0.25f, maxRadius = 0.29f, mass = 65.0f, v = 2f;
    private static double DeltaTime = 5E-4;
    private static double k = 1.2E5, gamma = 100, mu = 0.1, kt = 2.4E5;
    private static double D = 1.2;
    private static double A = 2000;
    private static double B = 0.08;
    private static double tau = 0.5;
    private static int dumpEach = (int) ((1.0/frameRate) / DeltaTime);
    private static double pathRadius = 0.5;

    public static void main(String[] args) throws Exception {

        if(seed == null)
            seed = System.currentTimeMillis();
        Random r = new Random(seed);

        List<Vector2D> list = new ArrayList<>();
        list.add(new Vector2D(10, -0));
        list.add(new Vector2D(10, -100));
        DynamicGridPed grid = new DynamicGridPed(L, W, M, r, D, list, pathRadius);
        ParticleGenerators.generatePedestrians(L, W, N, minRadius, maxRadius,v, mass, k, kt, gamma, mu, A,B,tau,r).forEach(grid::add);

        PointDumper dumper = new PointDumper("./tp6/ovito/", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GranularParticleStats stats = new GranularParticleStats();
        stats.particles = N;
        int frame = 0;
        for (int i = 0; grid.size() > 0; i ++) {
            boolean dump = i % dumpEach == 0;
            grid.update(frame, DeltaTime, dumper, dump, stats);
            if(dump) {
                frame++;
                System.out.println(grid.size());
            }
        }

        dumper.dumpGranularStats(stats);
    }
}
