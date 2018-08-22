package tp3;

import common.ParticleGenerators;
import tp1.Particle;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Main {

    private static double L = 100;
    private static int N = 1000;
    private static int iterations = 100;
    private static double bigParticleRadius = 0.05;
    private static double bigParticleMass = 100;
    private static double particleRadius = 0.005;
    private static double particleMass = 0.1;
    private static double maxSpeed = 0.1;
    private static long seed = -1;


    public static void main(String[] args) throws Exception{
        seed = seed == -1 ? System.currentTimeMillis() : seed;
        Random r = new Random(seed);

        List<Particle> list =  ParticleGenerators.generateBrownianParticles(bigParticleMass, bigParticleRadius,
                                                                            L, N, particleRadius, particleMass,
                                                                            maxSpeed, r, true);

        Board board = new Board(L, list, "./tp3/");


        board.dumpParticles();
        board.computeEvents();
        for(int i = 0; i < iterations; i++){
            board.processEvent();
            board.dumpParticles();
        }

    }

}
