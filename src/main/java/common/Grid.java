package common;

import com.google.common.collect.Iterators;
import lombok.Getter;
import tp1.Cell;
import tp1.Point2D;

import java.util.*;


public class Grid<E extends Entity>{

    @FunctionalInterface
    interface DistanceCondition {
        boolean check(Grid grid, Entity x, Entity y, double evalDistance);
    }

    @FunctionalInterface
    interface EntityIterator {
        Iterator<Entity> compute(Grid grid, int x, int y);
    }

    private static DistanceCondition boxCondition = (g, x, y, d)  ->
            !x.equals(y) && x.isWithinRadiusBoundingBox(y, d);

    private static DistanceCondition periodicCondition = (g, x, y, d) ->
            !x.equals(y) && x.isWithinRadiusPeriodic(y, d, g.L);

    private static EntityIterator boxIterator = (g, x, y) -> {
        if(!g.isWithinBounds(x, y))
            throw new IllegalArgumentException();
        Iterator<Entity> ans = g.grid[x][y].iterator();
        if(g.isWithinBounds(x-1, y))
            ans = Iterators.concat(ans, g.grid[x-1][y].iterator());
        if(g.isWithinBounds(x-1, y+1))
            ans = Iterators.concat(ans , g.grid[x-1][y+1].iterator());
        if(g.isWithinBounds(x, y+1))
            ans =  Iterators.concat(ans, g.grid[x][y+1].iterator());
        if(g.isWithinBounds(x+1, y+1))
            ans = Iterators.concat(ans, g.grid[x+1][y+1].iterator());
        return ans;
    };

    private static EntityIterator periodicIterator = (g, x, y) -> {
        if(!g.isWithinBounds(x, y))
            throw new IllegalArgumentException();

        int M = g.M;

        Iterator<Entity> ans = g.grid[x][y].iterator();
        ans = Iterators.concat(ans, g.grid[x][Math.floorMod(y-1, M)].iterator());
        if(!(x == (M-1) && y == 0))
            ans = Iterators.concat(ans, g.grid[(x+1) % M][Math.floorMod(y-1, M)].iterator());
        ans = Iterators.concat(ans, g.grid[(x+1) % M][y].iterator());
        if(!(x == (M-1) && y == (M-1)))
            ans = Iterators.concat(ans, g.grid[(x+1) % M][(y+1) % M].iterator());

        return ans;
    };

    public enum Mode {
        BOX(boxCondition, boxIterator), PERIODIC(periodicCondition, periodicIterator);

         private DistanceCondition condition;
         private EntityIterator entityIterator;

         Mode(DistanceCondition condition, EntityIterator entityIterator){
            this.condition = condition;
            this.entityIterator = entityIterator;
        }

    }


    @Getter
    private double L, W, heightSegment, widthSegment;

    public Random r;

    @Getter
    private int M;

    private Cell<E>[][] grid;
    protected List<E> particles = new LinkedList<>();

    @SuppressWarnings("unchecked")
    public Grid(double L, double W, int M, Random r){
        this.L = L;
        this.W = W;
        this.M = M;
        this.r = r;
        heightSegment = L/M;
        widthSegment = W/M;
        this.grid = new Cell[M][M];
        initialize();
    }

    private void initialize(){
        for(int i = 0; i < M; i++)
            for(int j = 0; j < M; j++)
                grid[i][j] = new Cell<>();
    }

    public Iterator<E> getIterator() {
        return particles.iterator();
    }

    public void add(E t) {
        add(t, true);
    }

    public void add(E t, boolean addParticle){
        List<Point2D> mbr = t.mbr();
        Point2D a = mbr.get(0);
        Point2D b = mbr.get(1);

        int cellAx = (int) (Math.max(0, a.getX()) / widthSegment), cellAy = (int) (Math.min(L, a.getY()) / heightSegment);
        int cellBx = (int) (Math.max(0, b.getX()) / widthSegment), cellBy = (int) (Math.min(L, b.getY()) / heightSegment);

        int minx = Math.max(0, Math.min(Math.min(cellAx, cellBx), M-1)), miny = Math.max(0, Math.min(Math.min(cellAy, cellBy), M-1));
        int maxx = Math.max(0, Math.min(Math.max(cellAx, cellBx), M-1)), maxy = Math.max(0, Math.min(Math.max(cellAy, cellBy), M-1));
        for (int i = minx; i <= maxx; i++) {
            for (int j = miny; j <= maxy; j++) {
                grid[i][j].add(t);
            }
        }
        if (addParticle) {
            particles.add(t);
        }
    }

    public Map<E, Set<E>> evalNeighboursBruteForce(double evalDistance, Mode mode){
        Map<E, Set<E>> adjacencyMap = new HashMap<>();
        particles.forEach(x-> adjacencyMap.put(x, new HashSet<>()));
        particles.forEach(y -> {
            particles.forEach(x -> {
                if(mode.condition.check(this, x, y, evalDistance)){
                    addToMap(adjacencyMap, x, y);
                }
            });
        });

        return adjacencyMap;
    }

    public Map<E, Set<E>> evalNeighbours(double evalDistance, Mode mode) {
        Map<E, Set<E>> adjacencyMap = new HashMap<>();
        particles.forEach(x-> adjacencyMap.put(x, new HashSet<>()));
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                Cell<E> c = grid[i][j];

                mode.entityIterator.compute(this, i, j).forEachRemaining(y -> {
                    c.iterator().forEachRemaining(x -> {
                        if(mode.condition.check(this, x, y, evalDistance)){
                            addToMap(adjacencyMap, x, (E) y);
                        }
                    });
                });

            }
        }
        return adjacencyMap;
    }



    public void updateParticles() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                grid[i][j].units = new HashSet<>();
            }
        }

        for (E particle: particles) {
            add(particle, false);
        }
    }

    private void addToMap(Map<E, Set<E>> map, E x, E y){
        Set<E> set = map.get(x);
        set.add(y);
        set = map.get(y);
        set.add(x);
    }

    private boolean isWithinBounds(int x, int y){
        return x >= 0 && x < M && y >= 0 && y < M;
    }

    public void resetCells() {

    }

    public void remove(E particle, List<Point2D> oldMbr){
        List<Point2D> mbr = oldMbr;
        Point2D a = mbr.get(0);
        Point2D b = mbr.get(1);

        int cellAx = (int) (Math.max(0, a.getX()) / widthSegment), cellAy = (int) (Math.min(L, a.getY()) / heightSegment);
        int cellBx = (int) (Math.max(0, b.getX()) / widthSegment), cellBy = (int) (Math.min(L, b.getY()) / heightSegment);

        int minx = Math.max(0, Math.min(Math.min(cellAx, cellBx), M-1)), miny = Math.max(0, Math.min(Math.min(cellAy, cellBy), M-1));
        int maxx = Math.max(0, Math.min(Math.max(cellAx, cellBx), M-1)), maxy = Math.max(0, Math.min(Math.max(cellAy, cellBy), M-1));

        for (int i = minx; i <= maxx; i++) {
            for (int j = miny; j <= maxy; j++) {
                grid[i][j].units.remove(particle);
            }
        }
//        particles.remove(particle);
    }


}
