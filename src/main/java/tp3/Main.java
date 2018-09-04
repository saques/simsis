package tp3;

import common.ParticleGenerators;
import tp1.Particle;
import utils.PointDumper;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static double L = .5;
    private static int N = 500;
    private static int iterations = 1000;
    private static double bigParticleRadius = 0.05;
    private static double bigParticleMass = 100;
    private static double particleRadius = .005;
    private static double particleMass = 0.1;
    private static double maxSpeed = 0.1;
    private static long seed = -1;


    public static void main(String[] args) throws Exception{
        Integer times;
        try{
             times = new Integer(args[0]);
        }catch(ArrayIndexOutOfBoundsException e){
            times = 1;
        }

        ArrayList<ArrayList<Double>> bigParticleSd = new ArrayList<>() ;
        ArrayList<ArrayList<Double>> smallParticleSd = new ArrayList<>() ;

        System.out.println(times);
        seed = seed == -1 ? System.currentTimeMillis() : seed;
        for (int time = 0 ; time <  times ; time++) {
            System.out.println(time);
            Random r = new Random(seed);
            Particle.resetIDs();
            List<Particle> list = ParticleGenerators.generateBrownianParticles(bigParticleMass, bigParticleRadius,
                    L, N, particleRadius, particleMass,
                    maxSpeed, r, true);
            Board board = new Board(L, list, ".\\src\\main\\java\\tp3\\ovito\\");
            board.dumpParticles();
            board.addTimestamps(iterations);
            board.computeEvents();
            int i = 0;
            while (i < iterations) {
                if (i > (2 * iterations) / 3) {
                    if (board.processEvent(true)) {
                        i++;
                    }
                } else {
                    if (board.processEvent(false)) {
                        i++;
                    }
                }
            }

            bigParticleSd.add(board.getBigParticleSD());
            smallParticleSd.add(board.getSmallParticleSD());

            board.dumpStatistics(".\\src\\main\\java\\tp3\\stats\\",time);
            seed = System.currentTimeMillis();

        }

        Integer finalTimes = times;
        int min = bigParticleSd.stream().map( x -> x.size()).min(Integer::compareTo).get();
        int minSmall = smallParticleSd.stream().map( x -> x.size()).min(Integer::compareTo).get();


        PointDumper dumper = new PointDumper(".\\src\\main\\java\\tp3\\ovito\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        dumper.dumpMSD(".\\src\\main\\java\\tp3\\","msd",bigParticleSd,min);
        dumper.dumpMSD(".\\src\\main\\java\\tp3\\","msdSmall",smallParticleSd,minSmall);

    }

}
