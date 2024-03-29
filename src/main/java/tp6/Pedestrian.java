package tp6;

import common.Vector2D;
import tp5.BeemanGranularParticle;
import tp5.GranularParticle;

import java.util.List;
import java.util.Vector;

public class Pedestrian extends BeemanGranularParticle {
    private double A, B, tau, dSpeed;
    private int pathIndex = 0;

    public Pedestrian(
            double x,
            double y,
            double vx,
            double vy,
            double radius,
            double mass,
            double k,
            double kt,
            double gamma,
            double mu,
            double A,
            double B,
            double tau,
            double dSpeed
    ) {
        super(x, y, vx, vy, radius, mass, k, kt, gamma, mu);
        this.A = A;
        this.B = B;
        this.tau = tau;
        this.dSpeed = dSpeed;
    }


    public void socialForce(Pedestrian other)
    {
        Vector2D pos = new Vector2D(x, y);
        Vector2D opos = new Vector2D(other.x, other.y);
        Vector2D normalVector = pos.sub(opos);
        double dist = normalVector.mod();
        Vector2D e = normalVector.scl(1 / dist);
        double overlap = dist -  (radius + other.radius);
        Vector2D force = e.scl(A * Math.exp(-overlap/B));

        fx0 += force.x;
        fy0 += force.y;
        other.fx0 -= force.x;
        other.fy0 -= force.y;
    }

    public void drivingForce(List<Vector2D> path, double pathRadius) {
        Vector2D destiny = null;


        if(pathIndex < path.size())
            destiny = path.get(pathIndex);

        if (destiny == null) {
            System.out.println("No driving force pedestrian: " + getId());
            return;
        }

        if(position().sub(destiny).mod() <= pathRadius || pathIndex != (path.size()-1) && position().y < 0)
            pathIndex++;

        // Calculate driving force.
        Vector2D pos = new Vector2D(x, y);
        Vector2D normalVector = destiny.sub(pos);
        Vector2D e = normalVector.scl(1 / normalVector.mod());
        Vector2D v = new Vector2D(vx, vy);
        Vector2D desiredForce = e.scl(dSpeed).sub(v).scl(mass / tau);
        fx0 += desiredForce.x;
        fy0 += desiredForce.y;
    }
}
