package tp4.particle;

public class BeemanMDParticle extends  MDParticle{


    private double fx_1, fy_1;

    public BeemanMDParticle(double mass, double radius, double x0, double y0, double vx0, double vy0){
        super(mass, radius, x0, y0, vx0, vy0);
        fx_1 = fy_1 = 0;
    }


    @Override
    void interact(MDParticle mdParticle) {
        fx_1 = fx0;
        fy_1 = fy0;
        interact(mdParticle);
    }

    @Override
    void processDelta(double delta) {

        double x1 = r(delta, x0, vx0, fx0/mass, fx_1/mass);
        double y1 = r(delta, y0, vy0, fy0/mass, fy_1/mass);

        

    }



    private double r(double t, double r0, double v0, double a0, double a_1){
        double t2 = Math.pow(t, 2);
        return r0 + v0*t + (2.0/3.0)*a0*t2 - (1.0/6.0)*a_1*t2;
    }


}
