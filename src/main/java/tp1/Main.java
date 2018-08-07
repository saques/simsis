package tp1;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static int N = 1000;
    private static int M = 5;
    private static double L = 20;
    private static double rc = 1;
    private static double particleRadius = 0.25;
    private static Grid.Mode mode = Grid.Mode.BOX;
    private static Long seed = null;

    public static void main(String[] args) throws Exception{
        BasicConfigurator.configure();

        if(seed == null)
            seed = System.currentTimeMillis();
        LOGGER.info("Seed: {}", seed);

        Random r = new Random(seed);
        Grid g = new Grid(L, M);

        LOGGER.info("Generating particles...");
        generateEntities(L, N, particleRadius, r).forEach(g::add);
        LOGGER.info("Success");

        Map<Entity, Set<Entity>> adjacencies = null;
        LOGGER.info("Running algorithms on {} mode", mode.name());

        LOGGER.info("Running brute force method");
        long t0 = System.currentTimeMillis();
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

    private static List<Particle> generateEntities(double L, int N, double radius, Random r) throws Exception{

        List<Particle> ans = new ArrayList<>(N);
        PrintWriter sta = new PrintWriter(new FileWriter("static.txt"));
        PrintWriter din = new PrintWriter(new FileWriter("dynamic.txt"));

        sta.println(N);
        sta.println(L);

        //TODO: t0 when time is relevant, irrelevant in this case
        din.println(0);

        for(int i = 0; i < N; i++) {
            double x = r.nextDouble() * L, y = r.nextDouble() * L;
            //TODO: set property of the particle, irrelevant in this case
            sta.printf("%f %d\n", radius, 0);
            //TODO: set speed of the particle, irrelevant in this case
            din.printf("%f %f %f %f\n", x, y, 0.0, 0.0);
            ans.add(new Particle(x, y, radius));
        }

        sta.flush(); sta.close();
        din.flush(); din.close();
        return ans;
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
