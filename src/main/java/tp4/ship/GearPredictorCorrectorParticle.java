package tp4.ship;

import utils.PointDumper;

import java.io.IOException;

public class GearPredictorCorrectorParticle extends MDParticle {

    double x2, x3, x4, x5;
    double y2, y3, y4, y5;

    PointDumper dumper;

    GearPredictorCorrectorParticle(double mass, double radius, double x0, double y0, double vx0, double vy0, PointDumper dumper) {
        super(mass, radius, x0, y0, vx0, vy0);
        this.dumper = dumper;
        
    }

    @Override
    void saveState(int i) throws IOException {

    }

    /**
     * Primero se predice en rDelta, despues se llama a interact y despues se corrige en vDelta, despues se imprime al archivo
     * los valores corregidos.
     * @param delta
     */
    @Override
    public void rDelta(double delta) {
        x0 = taylorEval(x0, vx0, x2, x3, x4, x5, delta);
        y0 = taylorEval(y0, vy0, y2, y3, y4, y5, delta);
        vx0 = taylorEval(vx0, x2, x3, x4, x5, 0, delta);
        vy0 = taylorEval(vy0, y2, y3, y4, y5, 0, delta);
        x2 = taylorEval(x2, x3, x4, x5, 0, 0, delta);
        y2 = taylorEval(y2, y3, y4, y5, 0, 0, delta);
        x3 = taylorEval(x3, x4, x5, 0, 0, 0, delta);
        y3 = taylorEval(y3, y4, y5, 0, 0, 0, delta);
        x4 = taylorEval(x4, x5, 0, 0, 0, 0, delta);
        y4 = taylorEval(y4, y5, 0, 0, 0, 0, delta);
        x5 = taylorEval(x5, 0, 0, 0, 0, 0, delta);
        y5 = taylorEval(y5, 0 ,0 ,0, 0, 0, delta);
    }

    @Override
    public void vDelta(double delta) {
        double accelX = fx0 / mass, accelY = fy0 / mass;
        double deltaAX = accelX - x2, deltaAY = accelY - y2;

        double deltaSquared = delta * delta;
        double deltaR2X = deltaAX * deltaSquared / 2, deltaR2Y = deltaAY * deltaSquared / 2;

        x0 = correct(x0,3.0 / 16.0,  deltaR2X, delta, 0, 1);
        y0 = correct(y0,3.0 / 16.0, deltaR2Y, delta, 0, 1);
        vx0 = correct(vx0,251.0 / 360.0, deltaR2X, delta, 1, 1);
        vy0 = correct(vy0,251.0 / 360.0, deltaR2Y, delta, 1, 1);
        x2 = correct(x2,1.0, deltaR2X, delta, 2, 2);
        y2 = correct(y2,1.0, deltaR2Y, delta, 2, 2);
        x3 = correct(x3,11.0 / 18.0, deltaR2X, delta, 3, 6);
        y3 = correct(y3, 11.0 / 18.0, deltaR2Y, delta, 3, 6);
        x4 = correct(x4, 1.0 / 6.0, deltaR2X, delta, 4, 24);
        y4 = correct(y4, 1.0 / 6.0, deltaR2Y, delta, 4, 24);
        x5 = correct(x5, 1.0 / 60.0, deltaR2X, delta, 5, 120);
        y5 = correct(y5, 1.0 / 60.0, deltaR2Y, delta, 5, 120);


    }

    private static double taylorEval(double a, double b, double c, double d, double e, double f, double deltaT) {
        double ret = 0;
        if (a != 0) {
            ret += a;
        }
        if (b != 0) {
            ret += b * deltaT;
        }
        if (c != 0) {
            ret += c * Math.pow(deltaT, 2) / 2.0;
        }
        if (d != 0) {
            ret += d * Math.pow(deltaT, 3) / 6.0;
        }
        if (e != 0) {
            ret += e * Math.pow(deltaT, 4) / 24.0;
        }
        if (f != 0) {
            ret += f * Math.pow(deltaT, 5) / 120.0;
        }
        return ret;
    }

    public static double correct(
            double pred, double alfa, double deltaR2, double deltaT, double q, double qFact
    ) {
        return pred + alfa * deltaR2 * qFact / Math.pow(deltaT, q);
    }
}
