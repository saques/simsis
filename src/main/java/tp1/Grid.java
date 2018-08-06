package tp1;

import com.google.common.collect.Iterators;
import lombok.Getter;

import java.util.*;


public class Grid {

    @Getter
    private double L, segmentLength;

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

    public Map<Entity, Set<Entity>> evalNeigboursBox(double evalDistance) {
        int cellEvalDistance = (int) Math.ceil(evalDistance / segmentLength);

        Map<Entity, Set<Entity>> adjacencyMap = new HashMap<>();

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                Cell<Entity> c = grid[i][j];
                boundingBoxIterator(i, j).forEachRemaining(x -> {
                    c.iterator().forEachRemaining(y -> {
                        if(!y.equals(x) && y.isWithinRadiusBoundingBox(x, evalDistance)){
                            if(!adjacencyMap.containsKey(y))
                                adjacencyMap.put(y, new HashSet<>());
                            Set<Entity> set = adjacencyMap.get(y);
                            set.add(x);
                        }
                    });
                });
            }
        }
        return adjacencyMap;
    }

    public Map<Entity, Set<Entity>> evalNeigbboursPeriodic(double evalDistance) {
        return null;
    }

    private Iterator<Entity> boundingBoxIterator(int x, int y){
        if(!isWithinBounds(x, y))
            throw new IllegalArgumentException();
        Iterator<Entity> ans = grid[x][y].iterator();
        if(isWithinBounds(x-1, y))
            ans = Iterators.concat(ans, grid[x-1][y].iterator());
        if(isWithinBounds(x-1, y+1))
            ans = Iterators.concat(ans , grid[x-1][y+1].iterator());
        if(isWithinBounds(x, y+1))
            ans =  Iterators.concat(ans, grid[x][y+1].iterator());
        if(isWithinBounds(x+1, y+1))
            ans = Iterators.concat(ans, grid[x+1][y+1].iterator());
        return ans;
    }

    private boolean isWithinBounds(int x, int y){
        return x >= 0 && x < M && y >= 0 && y < M;
    }
}
