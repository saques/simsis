package tp1;

import com.google.common.collect.Iterators;
import lombok.Getter;

import java.util.*;


public class Grid {

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
            !x.equals(y) && x.isWithinRadiusPeriodic(y, d, g.L*g.M);

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
        ans = Iterators.concat(ans, g.grid[(x-1) % M][y % M].iterator());
        ans = Iterators.concat(ans , g.grid[(x-1) % M][(y+1) % M].iterator());
        ans =  Iterators.concat(ans, g.grid[x % M][(y+1) % M].iterator());
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
    private double L, segmentLength;

    @Getter
    private int M;

    private Cell<Entity> [][] grid;

    @SuppressWarnings("unchecked")
    public Grid(double L, int M){
        this.L = L;
        this.M = M;
        segmentLength = L/M;
        this.grid = new Cell[M][M];
        initialize();
    }

    private void initialize(){
        for(int i = 0; i < M; i++)
            for(int j = 0; j < M; j++)
                grid[i][j] = new Cell<>();
    }

    public void add(Entity t){
        List<Point2D> mbr = t.mbr();
        Point2D a = mbr.get(0);
        Point2D b = mbr.get(1);

        int cellAx = (int) (a.getX() / segmentLength), cellAy = (int) (a.getY() / segmentLength);
        int cellBx = (int) (b.getX() / segmentLength), cellBy = (int) (b.getY() / segmentLength);

        int minx = Math.min(cellAx, cellBx), miny = Math.min(cellAy, cellBy);
        int maxx = Math.max(cellAx, cellBx), maxy = Math.max(cellAy, cellBy);
        for (int i = minx; i < maxx; i++) {
            for (int j = miny; j < maxy; j++) {
                grid[i][j].add(t);
            }
        }
    }

    public Map<Entity, Set<Entity>> evalNeighbours(double evalDistance, Mode mode) {
        int cellEvalDistance = (int) Math.ceil(evalDistance / segmentLength);
        Map<Entity, Set<Entity>> adjacencyMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                Cell<Entity> c = grid[i][j];
                mode.entityIterator.compute(this, i, j).forEachRemaining(y -> {
                    c.iterator().forEachRemaining(x -> {
                        if(mode.condition.check(this, x, y, evalDistance)){
                            if(!adjacencyMap.containsKey(x))
                                adjacencyMap.put(x, new HashSet<>());
                            Set<Entity> set = adjacencyMap.get(x);
                            set.add(y);
                        }
                    });
                });
            }
        }
        return adjacencyMap;
    }

    private boolean isWithinBounds(int x, int y){
        return x >= 0 && x < M && y >= 0 && y < M;
    }
}