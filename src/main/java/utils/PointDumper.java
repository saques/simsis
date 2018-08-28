package utils;

import tp2.Statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public final class PointDumper {

    private final FileMode mode;
    private final Dimensions dimensions;
    private final Queue<String> queue = new LinkedList<>();
    private final Queue<String> statsQueue = new LinkedList<>();
    private final String basePath;
    private int id = 0;
    private double currentTimestamp = -1;
    private int randomRed;
    private int randomGreen;
    private int randomBlue;

    public enum FileMode {
        STATIC, DYNAMIC
    }

    public enum Dimensions {
        _2D, _3D
    }

    public static PrintWriter getPrintWriter(String path, int id) throws IOException {
        return new PrintWriter(new FileWriter(path + id + ".dump"));
    }

    public PointDumper(String basePath, FileMode mode, Dimensions dimensions){
        this.basePath = basePath;
        this.mode = mode;
        this.dimensions = dimensions;
        Random random = new Random();
        randomBlue = random.nextInt();
        randomGreen = random.nextInt();
        randomRed = random.nextInt();
    }

    public void print2D(double x, double y){
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
        queue.add(String.format("%f %f\n", x, y));
    }

    public void print2D(double x, double y, double vx, double vy, double mass, double radius, int id){
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
        int red = id * randomRed % 1000;
        int blue = id * randomBlue % 1000;
        int green = id * randomGreen % 1000;
        queue.add(String.format("%f %f %f %f %f %f %d %d %d\n", x, y, vx, vy, mass, radius, red, green, blue));
    }

    public void print3D(double x, double y, double z){
        checkConstraints(FileMode.DYNAMIC, Dimensions._3D);
        queue.add(String.format("%f %f %f\n", x, y, z));
    }

    public void dump (double timestamp) throws IOException {
        PrintWriter printWriter = getPrintWriter(basePath, id);
        printWriter.println(queue.size());
        printWriter.println();
        queue.forEach(printWriter::print);
        queue.clear();
        printWriter.flush();
        printWriter.close();
        currentTimestamp = timestamp;
        id++;
    }

    public  void dump(double timestamp, double L) throws IOException{
        print2D(0, 0, 0, 0, 1, 0.001,0);
        print2D(0, L, 0, 0, 1, 0.001, 0);
        print2D(100, L, 0, 0, 1, 0.001, 0);
        print2D(100, L, 0, 0, 1, 0.001, 0);
        dump(timestamp);
    }

    public void pushStats(Statistics statistics) {
        statsQueue.add(String.format("%f %d %f %f %f \n",
                statistics.getRadius(),
                statistics.getAlive(),
                statistics.getCenterOfMass()[0],
                statistics.getCenterOfMass()[1],
                statistics.getCenterOfMass()[2]));
    }

    public void dumpStats() throws IOException{
        PrintWriter printWriter = new PrintWriter(new FileWriter(basePath + ".statistics"));
        statsQueue.forEach(printWriter::print);
        printWriter.flush();
        printWriter.close();
    }



    private void checkConstraints(FileMode mode, Dimensions dimensions) {
        if (!this.mode.equals(mode) || !this.dimensions.equals(dimensions))
            throw new IllegalStateException("Should be " + mode + " with " + dimensions + " dimensions");
    }

}
