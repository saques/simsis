package tp4.ship;

import common.Vector2D;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class CraftStats {

    private List<Vector2D> speeds;

    private List<Vector2D> bestSpeeds;

    private double minToJupiter, minToSaturn;
    private double v, h;

    private List<String> dump;


    public CraftStats(){
        speeds = new LinkedList<>();
        minToJupiter = minToSaturn = Double.MAX_VALUE;
    }

    public List<String> getDump() {
        return dump;
    }

    public void setDump(List<String> dump) {
        this.dump = dump;
    }

    public double getMinToSaturn() {
        return minToSaturn;
    }

    public double getMinToJupiter() {
        return minToJupiter;
    }

    public boolean isBetterApproach(double jupiterDist, double saturnDist, double v, double h){
        if((jupiterDist+saturnDist) < (minToJupiter + minToSaturn)){
            minToJupiter = jupiterDist;
            minToSaturn = saturnDist;
            this.v = v;
            this.h = h;
            bestSpeeds = new LinkedList<>(speeds);
            return true;
        }
        return false;
    }

    public void printBestSpeeds(String file) throws IOException{
        PrintWriter writer = new PrintWriter(new FileWriter(file + ".speeds"));
        bestSpeeds.forEach(x-> {
            writer.printf("%f %f\n", x.x, x.y);
        });
        writer.flush();
        writer.close();
    }

    public void resetSpeeds(){
        speeds = new LinkedList<>();
    }

    public void logSpeed(double t, double v){
        speeds.add(new Vector2D(t, v));
    }

    public double getV() {
        return v;
    }

    public double getH() {
        return h;
    }
}
