package tp4.particle;

import common.Vector2D;

public abstract class MDParticle {

    public static final double G = 6.693E-11;

    double mass;
    double radius;

    double x0;
    double y0;

    double vx0;
    double vy0;

    double fx0;
    double fy0;


    public MDParticle(double mass, double radius, double x0, double y0, double vx0, double vy0){
        this.mass = mass;
        this.radius = radius;
        this.x0 = x0;
        this.y0 = y0;
        this.vx0 = vx0;
        this.vy0 = vy0;
        fx0 = fy0 = 0;
    }

    void interact(MDParticle o){

        double f = G*mass*o.mass/(Math.pow(x0, 2) + Math.pow(y0, 2));

        Vector2D xyforces = new Vector2D(o.x0, o.y0).sub(new Vector2D(x0, y0)).nor().scl(f);

        fx0 += xyforces.x;
        fy0 += xyforces.y;
    }

    void resetForces(){
        fx0 = fy0 = 0;
    }

    abstract void processDelta(double delta);
    
}
