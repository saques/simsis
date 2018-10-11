package tp1;

import common.Entity;
import common.Grid;
import common.ParticleGenerators;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static int N = 1000;
    private static int M = 17;
    private static double L = 20;
    private static double rc = 8;
    private static double particleRadius = 0.25;
    private static Grid.Mode mode = Grid.Mode.PERIODIC;
    private static boolean checkOverlapping = false;
    private static Long seed = 1533609637906L;

    public static void main(String[] args) throws Exception{
        BasicConfigurator.configure();

        if(seed == null)
            seed = System.currentTimeMillis();
        LOGGER.info("Seed: {}", seed);

        Random r = new Random(seed);
        Grid g = new Grid(L, L,  M, r);

        LOGGER.info("Generating particles...");
        ParticleGenerators.generateEntities(L, L, N, particleRadius, r, checkOverlapping).forEach(g::add);
        LOGGER.info("Success");

        Map<Entity, Set<Entity>> adjacencies = null;
        long t0 = 0;
        LOGGER.info("Running algorithms on {} mode", mode.name());


        LOGGER.info("Running brute force method");
        t0 = System.currentTimeMillis();
        adjacencies = g.evalNeighboursBruteForce(rc, mode);
        LOGGER.info("Time: {}", System.currentTimeMillis() - t0);
        LOGGER.info("Nonempty adjacent count: {}", adjacencies.values().stream().map(Set::size).reduce(0, (x, y) -> y != 0 ? x+y : x));


        LOGGER.info("Running Cell Index Method");
        t0 = System.currentTimeMillis();
        adjacencies = g.evalNeighbours(rc, mode);
        LOGGER.info("Time: {}", System.currentTimeMillis() - t0);
        LOGGER.info("Nonempty adjacent count: {}", adjacencies.values().stream().map(Set::size).reduce(0, (x, y) -> y != 0 ? x+y : x));

        LOGGER.info("Flushing neighbours list...");
        printAdjacent(adjacencies);
        LOGGER.info("Success");

    }

    private static void printAdjacent(Map<Entity, Set<Entity>> adj) throws Exception{
        PrintWriter file = new PrintWriter(new FileWriter("adjacent.txt"));
        adj.forEach(((entity, entities) -> {
            file.printf("[%d", entity.getId());
            entities.forEach(x -> file.printf(" %d", x.getId()));
            file.println("]");
        }));
        file.flush(); file.close();
    }

}
