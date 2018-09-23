package tp4.ship;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class CraftStats {

    private List<Double> speeds;

    private List<Double> bestSpeeds;

    double maxEnergy;
    private List<Double> energy;

    double maxBestEnergy;
    private List<Double> bestEnergy;

    private double minToJupiter, minToSaturn;
    private double v, h;

    private List<String> dump;


    public CraftStats(){
        speeds = new LinkedList<>();
        energy = new LinkedList<>();
        minToJupiter = minToSaturn = Double.MAX_VALUE;
        maxEnergy = Double.MIN_VALUE;
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
            bestEnergy = new LinkedList<>(energy);
            maxBestEnergy = maxEnergy;
            return true;
        }
        return false;
    }

    public void printBestSpeedsAndEnergy(String file) throws IOException{
        PrintWriter speeds = new PrintWriter(new FileWriter(file + ".speeds"));
        bestSpeeds.forEach(x-> speeds.printf("%f\n", x));
        speeds.flush();
        speeds.close();

        PrintWriter energy = new PrintWriter(new FileWriter(file + ".energy"));
        bestEnergy.forEach(x-> energy.printf("%f\n", x/maxBestEnergy));
        energy.flush();
        energy.close();
    }

    public void resetSpeedsAndEnergy(){
        speeds = new LinkedList<>();
        energy = new LinkedList<>();
        maxEnergy = Double.MIN_VALUE;
    }

    public void logSpeedAndEnergy(double v, double e){
        speeds.add(v);
        energy.add(e);
        maxEnergy = Math.max(maxEnergy, e);
    }

    public double getV() {
        return v;
    }

    public double getH() {
        return h;
    }
}
