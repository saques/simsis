package tp4.ship;

import common.Vector2D;

import java.io.IOException;
import java.util.List;

public abstract class MDParticle implements Cloneable{

    private static int IDs = 0;

    int id;

    public static final double G = 6.67408E-20;
    public static final double AU = 149597870.7;

    double mass;
    double radius;

    double x0;
    double y0;

    double vx0;
    double vy0;

    double fx0;
    double fy0;

    double U;


    public MDParticle(double mass, double radius, double x0, double y0, double vx0, double vy0){
        id = IDs++;
        this.mass = mass;
        this.radius = radius;
        this.x0 = x0;
        this.y0 = y0;
        this.vx0 = vx0;
        this.vy0 = vy0;
        fx0 = fy0 = U = 0;
    }

    public void interact(MDParticle o){

        Vector2D rel = new Vector2D(o.x0, o.y0).sub(new Vector2D(x0, y0));

        double mp = G*mass*o.mass;

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
        return mass*Math.pow(new Vector2D(vx0, vy0).mod(), 2)/2.0;
    }

    void resetForces(){
        fx0 = fy0 = U = 0;
    }

    abstract void saveState(int i) throws IOException;

    public abstract void rDelta(double delta);

    public abstract void vDelta(double delta);

    public static void interact(List<MDParticle> particles){
        for(int i = 0; i < particles.size(); i++) {
            MDParticle p = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++){
                p.interact(particles.get(j));
            }
        }
    }

}
