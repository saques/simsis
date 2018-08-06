package tp1;

import java.util.*;

public class Main {

    private static int N = 100000;
    private static int M = 5;
    private static double L = 20;
    private static double rc = 1;

    public static void main(String[] args) {
        Random r = new Random(System.currentTimeMillis());
        Grid g = new Grid(L, M);
        generateEntities(L, N, 0.25, r).forEach(g::add);

        Map<Entity, Set<Entity>> adjacencies = null;

        long t0 = System.currentTimeMillis();
        adjacencies = g.evalNeighbours(rc, Grid.Mode.BOX);
        System.out.println("Adjacencies count: " + adjacencies.size());
        System.out.println("Time: " + (System.currentTimeMillis() - t0));

        t0 = System.currentTimeMillis();
        adjacencies = g.evalNeighboursBruteForce(rc, Grid.Mode.BOX);
        System.out.println("Adjacencies count: " + adjacencies.size());
        System.out.println("Time: " + (System.currentTimeMillis() - t0));

    }

    private static List<Particle> generateEntities(double range, int N, double radius, Random r){
        List<Particle> ans = new ArrayList<>(N);
        for(int i = 0; i < N; i++)
            ans.add(new Particle(r.nextDouble()*range, r.nextDouble()*range, radius));
        return ans;
    }

}
