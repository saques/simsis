package tp4.ship;

import common.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class CraftStats {

    private List<Double> speeds;

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
            return true;
        }
        return false;
    }

    public double getV() {
        return v;
    }

    public double getH() {
        return h;
    }
}
