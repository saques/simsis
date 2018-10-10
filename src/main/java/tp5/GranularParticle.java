package tp5;

import common.Particle;
import common.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GranularParticle extends Particle {
    public static final double G = 9.8;
    double fx0, fy0, U;
    double x2, x3, x4, x5;
    double y2, y3, y4, y5;
    double k;
    double gamma, mu;

    private double fx_1, fy_1;
    private double fx_2, fy_2;

    Map<GranularParticle, Double> pastOverlaps = new HashMap<>();

    public GranularParticle(double x, double y, double vx, double vy, double radius, double mass, double k, double gamma, double mu) {
        super(x, y, vx, vy, radius, mass);
        fx0 = fy0 = U = 0;
        this.k = k;
        this.gamma = gamma;
        this.mu = mu;
    }

    public void interact(GranularParticle o, double deltaT){
        Vector2D pos = new Vector2D(x, y);
        Vector2D otherPos = new Vector2D(o.x, o.y);
        double dist = pos.sub(otherPos).mod();
        double exn = (o.x - x) / dist;
        double eyn = (o.y - y) / dist;

        Vector2D normVers = new Vector2D(exn, eyn);
        Vector2D tanVers = new Vector2D(-eyn, exn);

        double overlap = radius + o.radius - dist;
        assert (overlap > 0 && overlap < radius);

        double pastOverlap = pastOverlaps.getOrDefault(o, 0.0);
        pastOverlaps.put(o, overlap);
        o.pastOverlaps.put(this, overlap);
        double overlapDeriv  = (overlap - pastOverlap) / deltaT;
        double normalForceMag = (-1) * k * overlap - gamma * overlapDeriv;


        Vector2D vel = new Vector2D(vx, vy);
        Vector2D otherVel = new Vector2D(o.vx, o.vy);
        double velRel = vel.sub(otherVel).mod();

        Vector2D normalForce = normVers.scl(normalForceMag);

        Vector2D tangForce = tanVers.scl((-1) * mu * normalForce.mod() * Math.signum(velRel));

        fx0 += normalForce.x;
        fy0 += normalForce.y;
        fx0 += tangForce.x;
        fy0 += tangForce.y;

        o.fx0 -= normalForce.x;
        o.fy0 -= normalForce.y;
        o.fx0 -= tangForce.x;
        o.fy0 -= tangForce.y;

    }

    public void updateForces(){
        fx_2 = fx_1;
        fy_2 = fy_1;

        fx_1 = fx0;
        fy_1 = fy0;
        resetForces();
    }


    public double kineticEnergy(){
        return getMass()*Math.pow(new Vector2D(getVx(), getVy()).mod(), 2)/2.0;
    }

    void resetForces(){
        fx0 = fy0 = U = 0;
    }
    void applyGravity() {
        fy0 -= 9.807 * mass;
    }
    /**
     * Primero se predice en rDelta, despues se llama a interact y despues se corrige en vDelta, despues se imprime al archivo
     * los valores corregidos.
     * @param delta
     */
    public void rDelta(double delta) {
//        x = taylorEval(x, vx, x2, x3, x4, x5, delta);
//        y = taylorEval(y, vy, y2, y3, y4, y5, delta);
//        vx = taylorEval(vx, x2, x3, x4, x5, 0, delta);
//        vy = taylorEval(vy, y2, y3, y4, y5, 0, delta);
//        x2 = taylorEval(x2, x3, x4, x5, 0, 0, delta);
//        y2 = taylorEval(y2, y3, y4, y5, 0, 0, delta);
//        x3 = taylorEval(x3, x4, x5, 0, 0, 0, delta);
//        y3 = taylorEval(y3, y4, y5, 0, 0, 0, delta);
//        x4 = taylorEval(x4, x5, 0, 0, 0, 0, delta);
//        y4 = taylorEval(y4, y5, 0, 0, 0, 0, delta);
//        x5 = taylorEval(x5, 0, 0, 0, 0, 0, delta);
//        y5 = taylorEval(y5, 0 ,0 ,0, 0, 0, delta);

        x = r(delta, x, vx, fx0/mass, fx_1/mass);
        y = r(delta, y, vy, fy0/mass, fy_1/mass);
    }

    public void vDelta(double delta) {
//        double accelX = fx0 / getMass(), accelY = fy0 / getMass();
//        double deltaAX = accelX - x2, deltaAY = accelY - y2;
//
//        double deltaSquared = delta * delta;
//        double deltaR2X = deltaAX * deltaSquared / 2, deltaR2Y = deltaAY * deltaSquared / 2;
//
//        x = correct(x,3.0 / 16.0,  deltaR2X, delta, 0, 1);
//        y = correct(y,3.0 / 16.0, deltaR2Y, delta, 0, 1);
//        vx = correct(vx,251.0 / 360.0, deltaR2X, delta, 1, 1);
//        vy = correct(vy,251.0 / 360.0, deltaR2Y, delta, 1, 1);
//        x2 = correct(x2,1.0, deltaR2X, delta, 2, 2);
//        y2 = correct(y2,1.0, deltaR2Y, delta, 2, 2);
//        x3 = correct(x3,11.0 / 18.0, deltaR2X, delta, 3, 6);
//        y3 = correct(y3, 11.0 / 18.0, deltaR2Y, delta, 3, 6);
//        x4 = correct(x4, 1.0 / 6.0, deltaR2X, delta, 4, 24);
//        y4 = correct(y4, 1.0 / 6.0, deltaR2Y, delta, 4, 24);
//        x5 = correct(x5, 1.0 / 60.0, deltaR2X, delta, 5, 120);
//        y5 = correct(y5, 1.0 / 60.0, deltaR2Y, delta, 5, 120);

        vx = v(delta, vx, fx0/mass, fx_1/mass, fx_2/mass);
        vy = v(delta, vy, fy0/mass, fy_1/mass, fy_2/mass);
    }

    private double r(double t, double r0, double v0, double a0, double a_1){
        double t2 = Math.pow(t, 2);
        return r0 + v0*t + (2.0/3.0)*a0*t2 - (1.0/6.0)*a_1*t2;
    }

    private double v(double t, double v0, double a1, double a0, double a_1){
        return v0 + (1.0/3.0)*a1*t + (5.0/6.0)*a0*t - (1.0/6.0)*a_1*t;
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

