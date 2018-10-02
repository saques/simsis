package tp5;

import common.Particle;
import common.Vector2D;
import java.util.List;

public class GranularParticle extends Particle {
    public static final double G = 9.8;
    double fx0, fy0, U;

    public GranularParticle(double x, double y, double vx, double vy, double radius, double mass) {
        super(x, y, vx, vy, radius, mass);
        fx0 = fy0 = U = 0;
    }

    public void interact(GranularParticle o){

        Vector2D rel = new Vector2D(o.getX(), o.getY()).sub(new Vector2D(getX(), getY()));

        double mp = G*getMass()*o.getMass();

        double f = mp/(rel.mod2());
        Vector2D xyforces = new Vector2D(rel).nor().scl(f);
        fx0 += xyforces.x;
        fy0 += xyforces.y;

        o.fx0 -= xyforces.x;
        o.fy0 -= xyforces.y;

        double U = (-1)*mp/rel.mod();
        this.U += U;
        o.U += U;
    }

    public double kineticEnergy(){
        return getMass()*Math.pow(new Vector2D(getVx(), getVy()).mod(), 2)/2.0;
    }

    void resetForces(){
        fx0 = fy0 = U = 0;
    }

//    abstract void saveState(int i) throws IOException;
//
//    public abstract void rDelta(double delta);
//
//    public abstract void vDelta(double delta);

    public static void interact(List<GranularParticle> particles){
        for(int i = 0; i < particles.size(); i++) {
            GranularParticle p = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++){
                p.interact(particles.get(j));
            }
        }
    }
}

