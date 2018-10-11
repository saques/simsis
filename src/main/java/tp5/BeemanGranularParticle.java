package tp5;

import common.Particle;
import common.Vector2D;
import java.util.HashMap;
import java.util.Map;

public class BeemanGranularParticle extends Particle {
    double fx0, fy0, U;
    double k;
    double gamma, mu;

    private double fx_1, fy_1;
    double fx1, fy1;

    double nx, ny;

    Map<BeemanGranularParticle, Double> pastOverlaps = new HashMap<>();

    public BeemanGranularParticle(double x, double y, double vx, double vy, double radius, double mass, double k, double gamma, double mu) {
        super(x, y, vx, vy, radius, mass);
        fx0 = fy0 = U = 0;
        this.k = k;
        this.gamma = gamma;
        this.mu = mu;
    }

    public void interact(BeemanGranularParticle o, double deltaT){
        Vector2D pos = new Vector2D(x, y);
        Vector2D otherPos = new Vector2D(o.x, o.y);
        double dist = pos.sub(otherPos).mod();
        double exn = (o.x - x) / dist;
        double eyn = (o.y - y) / dist;

        Vector2D normVers = new Vector2D(exn, eyn);
        Vector2D tanVers = new Vector2D((-1)*eyn, exn);

        double overlap = radius + o.radius - dist;

        double pastOverlap = pastOverlaps.getOrDefault(o, 0.0);
        pastOverlaps.put(o, overlap);
        o.pastOverlaps.put(this, overlap);
        double overlapDeriv  = (overlap - pastOverlap) / deltaT;
        double normalForceMag = (-1) * k * overlap - gamma * overlapDeriv;


        Vector2D vel = new Vector2D(vx, vy);
        Vector2D otherVel = new Vector2D(o.vx, o.vy);
        Vector2D velRel = new Vector2D(vel).sub(otherVel);

        Vector2D normalForce = normVers.scl(normalForceMag);

        Vector2D tangForce = tanVers.scl(-mu * normalForce.mod() * Math.signum(velRel.dot(tanVers)));

        nx += normalForce.x;
        ny += normalForce.y;

        fx1 += normalForce.x;
        fy1 += normalForce.y;
        fx1 += tangForce.x;
        fy1 += tangForce.y;

        o.fx1 -= normalForce.x;
        o.fy1 -= normalForce.y;
        o.fx1 -= tangForce.x;
        o.fy1 -= tangForce.y;

    }

    public void updateForces(){
        fx_1 = fx0;
        fy_1 = fy0;

        fx0 = fx1;
        fy0 = fy1;
        resetForces();
    }


    public double kineticEnergy(){
        return getMass()*Math.pow(new Vector2D(getVx(), getVy()).mod(), 2)/2.0;
    }

    void resetForces(){
        nx = ny = fx1 = fy1 = U = 0;
    }

    void resetAllForces() {
        resetForces();
        fx_1 = fy_1 = fx1 = fy1 = 0;
    }
    void applyGravity() {
        fy1 -= 9.807 * mass;
    }
    /**
     * Primero se predice en rDelta, despues se llama a interact y despues se corrige en vDelta, despues se imprime al archivo
     * los valores corregidos.
     * @param delta
     */
    public void rDelta(double delta) {
        x = r(delta, x, vx, fx0/mass, fx_1/mass);
        y = r(delta, y, vy, fy0/mass, fy_1/mass);
    }

    public void vDelta(double delta) {
        vx = v(delta, vx, fx1/mass, fx0/mass, fx_1/mass);
        vy = v(delta, vy, fy1/mass, fy0/mass, fy_1/mass);
    }

    private double r(double t, double r0, double v0, double a0, double a_1){
        double t2 = Math.pow(t, 2);
        return r0 + v0*t + (2.0/3.0)*a0*t2 - (1.0/6.0)*a_1*t2;
    }

    private double v(double t, double v0, double a1, double a0, double a_1){
        return v0 + (1.0/3.0)*a1*t + (5.0/6.0)*a0*t - (1.0/6.0)*a_1*t;
    }

    public double circumference(){
        return 2.0*Math.PI*radius;
    }

}
