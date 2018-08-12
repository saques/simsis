package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public final class PointDumper {

    private final FileMode mode;
    private final Dimensions dimensions;
    private final Queue<String> queue = new LinkedList<>();
    private final String basePath;
    private int id = 0;
    private double currentTimestamp = -1;

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
    }

    public void print2D(double timestamp, double x, double y) throws IOException{
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
//        checkTimestamp(timestamp);
        queue.add(String.format("%f %f\n", x, y));
    }

    public void print3D(double timestamp, double x, double y, double z) throws IOException{
        checkConstraints(FileMode.DYNAMIC, Dimensions._3D);
//        checkTimestamp(timestamp);
        queue.add(String.format("%f %f %f\n", x, y, z));
    }

//    private void checkTimestamp(double timestamp) throws IOException{
//        if(timestamp > currentTimestamp) {
//            if(!queue.isEmpty()) {
//                flush();
//                currentTimestamp = timestamp;
//                id++;
//            } else {
//                currentTimestamp = timestamp;
//            }
//        }
//    }

    public  void dump(double timestamp) throws IOException{
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

    private void checkConstraints(FileMode mode, Dimensions dimensions) {
        if (!this.mode.equals(mode) || !this.dimensions.equals(dimensions))
            throw new IllegalStateException("Should be " + mode + " with " + dimensions + " dimensions");
    }

}
