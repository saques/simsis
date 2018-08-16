package tp2;

import lombok.Getter;

public class Statistics {

    @Getter private double radius;
    @Getter private int alive;
    @Getter private double[] centerOfMass;

    Statistics(double radius, int alive, double[] centerOfMass) {
        this.radius = radius;
        this.alive = alive;
        this.centerOfMass = new double[3];
        for (int i = 0; i < centerOfMass.length; i++) {
            this.centerOfMass[i] = centerOfMass[i];
        }
    }
}
