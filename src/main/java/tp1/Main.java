package tp1;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static int N = 100;
    private static int M = 10;
    private static double L = 20;
    private static double rc = 2;
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
        Grid g = new Grid(L, M);

        LOGGER.info("Generating particles...");
        generateEntities(L, N, particleRadius, r, checkOverlapping).forEach(g::add);
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

    private static List<Particle> generateEntities(double L, int N, double radius, Random r, boolean checkOverlapping) throws Exception{

        List<Particle> ans = new ArrayList<>(N);
        PrintWriter sta = new PrintWriter(new FileWriter("static.txt"));
        PrintWriter din = new PrintWriter(new FileWriter("dynamic.txt"));

        sta.println(N);
        sta.println(L);

        //TODO: t0 when time is relevant, irrelevant in this case
        din.println(0);

        while(N > 0) {
            double x = r.nextDouble() * L, y = r.nextDouble() * L;

            Particle p = new Particle(x, y, radius);

            if(checkOverlapping && ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0))) {
                Particle.decreaseIDs();
                continue;
            }

            //TODO: set property of the particle, irrelevant in this case
            sta.printf("%f %d\n", radius, 0);
            //TODO: set speed of the particle, irrelevant in this case
            din.printf("%f %f %f %f\n", x, y, 0.0, 0.0);
            ans.add(p);
            N--;
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
