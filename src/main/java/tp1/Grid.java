package tp1;

import lombok.Getter;

import java.util.List;


public class Grid <T extends Entity> {

    @Getter
    private double L, segmentLength;

    private int M;

    private Cell<T> [][] grid;

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
                grid[i][j] = new Cell<T>();
    }

    public void add(T t){
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

    public void evalNeighbours(double evalDistance) {
        int cellEvalDistance = (int) Math.ceil(evalDistance / segmentLength);

        // Bounding box method, definitely not the best, considers more cells than necessary
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                Cell c = grid[i][j];

                // Iterate over every T of the Cell and return 
            }
        }


    }
}
