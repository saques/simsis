package tp4.ship;

import utils.PointDumper;

import java.io.IOException;

public class BeemanMDParticle extends  MDParticle{


    private double fx_1, fy_1;
    private double fx_2, fy_2;
    private PointDumper dumper;

    public BeemanMDParticle(double mass, double radius, double x0, double y0, double vx0, double vy0, PointDumper dumper){
        super(mass, radius, x0, y0, vx0, vy0);
        fx_1 = fy_1 = 0;
        this.dumper = dumper;
    }

    @Override
    public void saveState(int i) throws IOException {
        fx_2 = fx_1;
        fy_2 = fy_1;

        fx_1 = fx0;
        fy_1 = fy0;
        resetForces();
    }

    @Override
    public void rDelta(double delta) {
        x0 = r(delta, x0, vx0, fx0/mass, fx_1/mass);
        y0 = r(delta, y0, vy0, fy0/mass, fy_1/mass);

        dumper.print2D(x0/AU, y0/AU, vx0/AU, vy0/AU, mass, radius, id);
    }

    /**
     * Run interact(MDParticle o) before this method
     * @param delta
     */
    @Override
    public void vDelta(double delta){
        //Passing fx0/mass as a1 because interact() was called, so
        //we are using the prediction obtained from the new position
        vx0 = v(delta, vx0, fx0/mass, fx_1/mass, fx_2/mass);
        vy0 = v(delta, vy0, fy0/mass, fy_1/mass, fy_2/mass);
    }

    private double r(double t, double r0, double v0, double a0, double a_1){
        double t2 = Math.pow(t, 2);
        return r0 + v0*t + (2.0/3.0)*a0*t2 - (1.0/6.0)*a_1*t2;
    }

    private double v(double t, double v0, double a1, double a0, double a_1){
        return v0 + (1.0/3.0)*a1*t + (5.0/6.0)*a0*t - (1.0/6.0)*a_1*t;
    }

    public void initializeF(){
        fx_2 = fx_1 = fx0;
        fy_2 = fy_1 = fy0;
    }
}
