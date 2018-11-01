package utils;

import tp2.Statistics;
import tp3.BrownianStatistics;
import tp3.Point2D;
import tp5.GranularParticleStats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public final class PointDumper {

    private final FileMode mode;
    private final Dimensions dimensions;
    private final Queue<String> queue = new LinkedList<>();
    private final Queue<String> statsQueue = new LinkedList<>();
    private final String basePath;
    private int id = 0;
    private int randomRed;
    private int randomGreen;
    private int randomBlue;
    private List<String> list = new LinkedList<>();
    private double maxForce = Double.MIN_VALUE;
    private List<Double> fallingTimes;

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
        this.fallingTimes = new ArrayList<>();
    }

    public void updateMaxForce(double force){
        maxForce = Math.max(maxForce, force);
    }

    public void resetMaxForce(){
        maxForce = Double.MIN_VALUE;
    }

    public void print2D(double x, double y){
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
        queue.add(String.format("%f %f\n", x, y));
    }

    public void print2D(double x, double y, double vx, double vy, double mass, double radius, int id){
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
        int red, blue, green;
        if (id == 0) {
            red = 100;
            blue = 0;
            green = 0;
        } else {
            red = (randomRed * id);
            green = (randomGreen * id );
            blue = (randomBlue * id);
        }
        queue.add(String.format("%.20f %.20f %f %f %f %f %d %d %d", x, y, vx, vy, mass, radius, red, green, blue));
    }

    public void print2DForce(double force, double x, double y, double vx, double vy, double mass, double radius, int id){
        checkConstraints(FileMode.DYNAMIC, Dimensions._2D);
        double red, blue, green;
        if (id == 0) {
            red = 0.5;
            blue = 0;
            green = 0;
        } else {

            double val = force/maxForce;

            red = green = blue = 1;

            green = blue = 1 - val;
        }
        queue.add(String.format("%.20f %.20f %f %f %f %f %f %f %f", x, y, vx, vy, mass, radius, red, green, blue));
    }

    public void printFalling(double time) {
        fallingTimes.add(time);
    }

    public void print3D(double x, double y, double z){
        checkConstraints(FileMode.DYNAMIC, Dimensions._3D);
        queue.add(String.format("%f %f %f\n", x, y, z));
    }

    public void dump (double timestamp) throws IOException {
        PrintWriter printWriter = getPrintWriter(basePath, id);
        printWriter.println(queue.size());
        printWriter.println();
        queue.forEach(printWriter::println);
        queue.clear();
        printWriter.flush();
        printWriter.close();
        id++;
    }

    public void clear(){
        queue.clear();
    }

    public void dumpToList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%d\n", queue.size()));
        stringBuilder.append("\n");
        queue.forEach(x-> stringBuilder.append(String.format("%s\n", x)));
        queue.clear();
        list.add(stringBuilder.toString());
    }

    public void dumpList(List<String> strings) throws IOException {
        int id = 0;
        for (String s : strings) {
            PrintWriter printWriter = getPrintWriter(basePath, id);
            printWriter.print(s);
            printWriter.flush();
            printWriter.close();
            id++;
        }
    }

    public List<String> getList(){
        List<String> ans = list;
        list = new LinkedList<>();
        queue.clear();
        return ans;
    }

    public  void dump(double timestamp, double L) throws IOException{
        print2D(0, 0, 0, 0, 1, 0.001,0);
        print2D(0, L, 0, 0, 1, 0.001, 0);
        print2D(L, L, 0, 0, 1, 0.001, 0);
        print2D(L, 0, 0, 0, 1, 0.001, 0);
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


    public void dumpStatsBrownian(BrownianStatistics statistics , String path ) throws IOException{
        statsQueue.add(String.format("%d\n",statistics.getCollisionTimes().size()));
        for (double t : statistics.getCollisionTimes()){
            statsQueue.add(String.format("%.17f\n",t));
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(path + "statisticsTime.txt"));
        statsQueue.forEach(printWriter::println);
        printWriter.flush();
        printWriter.close();

        statsQueue.clear();
        statsQueue.add(String.format("%d\n",statistics.getVelocities().size()));
        for (double v : statistics.getVelocities()){
            statsQueue.add(String.format("%.17f\n",v));
        }
        printWriter = new PrintWriter(new FileWriter(path + "statisticsVelocity.txt"));
        statsQueue.forEach(printWriter::println);
        printWriter.flush();
        printWriter.close();

        statsQueue.clear();
        statsQueue.add(String.format("%d\n",statistics.getVelocitiesAtInit().size()));
        for (double v : statistics.getVelocitiesAtInit()){
            statsQueue.add(String.format("%.17f\n",v));
        }
        printWriter = new PrintWriter(new FileWriter(path + "statisticsVelocityInit.txt"));
        statsQueue.forEach(printWriter::println);
        printWriter.flush();
        printWriter.close();

        statsQueue.clear();
        statsQueue.add(String.format("%d\n",statistics.getBigParticleTrajectory().size()));
        for (Point2D p : statistics.getBigParticleTrajectory()){
            statsQueue.add(String.format("%.17f;%.17f\n",p.getX(),p.getY()));
        }
        printWriter = new PrintWriter(new FileWriter(path + "statisticsTrajectory.txt"));
        statsQueue.forEach(printWriter::println);
        printWriter.flush();
        printWriter.close();


    }

    public void dumpGranularStats(GranularParticleStats statistics) throws IOException {
        {

            PrintWriter printWriter = new PrintWriter(new FileWriter(basePath + "kinematic.txt"));
            printWriter.print(String.format("["));
            for (double energy : statistics.totalKineticEnergy){
                printWriter.print(String.format("%.17f,", energy));
            }
            printWriter.print(String.format("]"));
            printWriter.flush();
            printWriter.close();
        }
        {
            PrintWriter printWriter = new PrintWriter(new FileWriter(basePath + "flow.txt"));
            printWriter.print(String.format("%d\n", statistics.particles));
            printWriter.print(String.format("["));
            for (double flow : statistics.flow){
                printWriter.print(String.format("%.17f,", flow));
            }
            printWriter.print(String.format("]"));
            printWriter.flush();
            printWriter.close();
        }
    }


    public void dumpPedStats() throws IOException{
        {
            PrintWriter printWriter = new PrintWriter(new FileWriter(basePath + "evacuationTimes.txt"));
            printWriter.print(String.format("["));
            for (double time : fallingTimes){
                printWriter.print(String.format("%.17f,", time));
            }
            printWriter.print(String.format("]"));
            printWriter.flush();
            printWriter.close();
        }
    }

    public void dumpMSD(String path , String msdFolder,ArrayList<ArrayList<Double>> sd, int size ) throws IOException{
        int i = 0;
        for (List<Double> msd : sd) {
            statsQueue.clear();
            statsQueue.add(String.format("%d\n", size));
            for (Double z : msd) {
                statsQueue.add(String.format("%.17f\n", z));
            }
            PrintWriter printWriter = new PrintWriter(new FileWriter(path + "stats\\"+msdFolder+"\\statisticsMSD"+i+".txt"));
            statsQueue.forEach(printWriter::println);
            printWriter.flush();
            printWriter.close();
            i++;
        }


        PrintWriter printWriter = new PrintWriter(new FileWriter(path + "octave\\"+msdFolder+"Script.m"));
        printWriter.print("msdAvg(");
        for (int j = 0 ; j < sd.size() - 1 ; j++){
            printWriter.print("'..\\stats\\"+msdFolder+"\\statisticsMSD"+j+".txt',");
        }
        printWriter.print("'..\\stats\\"+msdFolder+"\\statisticsMSD"+(sd.size()-1)+".txt');");
        printWriter.flush();
        printWriter.close();
    }

    public void dumpOscilatorAproximation(List<Double> numericSolutions,Integer num,Double step) throws IOException {
        //statsQueue.add(String.format("%d",numericSolutions.size()));

        for (double t : numericSolutions){
            statsQueue.add(String.format(Locale.US,"%.17f",t));
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(basePath + "numericSolutions"+num+".txt"));
        printWriter.println(String.format(Locale.US,"%f",step));
        printWriter.print("[");
        statsQueue.forEach( x -> printWriter.print(x+";"));
        printWriter.print("]");
        printWriter.flush();
        printWriter.close();
    }

    private void checkConstraints(FileMode mode, Dimensions dimensions) {
        if (!this.mode.equals(mode) || !this.dimensions.equals(dimensions))
            throw new IllegalStateException("Should be " + mode + " with " + dimensions + " dimensions");
    }

}
