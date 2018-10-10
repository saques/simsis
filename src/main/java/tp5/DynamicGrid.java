package tp5;

import common.Grid;
import tp1.Point2D;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class DynamicGrid extends Grid<GranularParticle> {
    Random r = new Random();

    double D;
    GranularParticle auxParticle = new GranularParticle(0, 0,  0, 0, 0, 1000, 0, 0, 0);


    public DynamicGrid(double L, double W, int M, double D) {
        super(L, W, M);
        this.D = D;
    }

    public double getD() {
        return D;
    }

    public void update(int frame, double deltaTime, PointDumper dumper, boolean dump) throws IOException {
        for (GranularParticle particle : particles) {
            particle.updateForces();
           // if (/*r.nextDouble() < 0.25*/ particle.getId() == 1) {
                particle.applyGravity();
           // }
        }

        Map<GranularParticle, Set<GranularParticle>> particleMap = evalNeighbours(0, Mode.BOX);
        Set<GranularParticle> alreadyInteracted = new HashSet<>();
        for (Map.Entry<GranularParticle, Set<GranularParticle>> entry : particleMap.entrySet()) {
            // Interact with every particle, by checking overlapping and applying force
            GranularParticle particle = entry.getKey();
            for (GranularParticle other : entry.getValue()) {
                if (!alreadyInteracted.contains(other) && particle.isWithinRadiusBoundingBox(other, 0)) {
                    particle.interact(other, deltaTime);
                }
            }
            checkWallCollisions(particle, getL(), getD(),deltaTime);
            alreadyInteracted.add(particle);
        }

        // Update particle position based on cumulative force
        for (GranularParticle particle : particles) {
            double oldX = particle.getX(), oldY = particle.getY();
            List<Point2D> oldMbr = particle.mbr();
            particle.rDelta(deltaTime);
            particle.vDelta(deltaTime);
            updateCell(particle, oldMbr, oldX, oldY, particle.getX(), particle.getY());
            if(dump)
                dumper.print2D(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
        }

        if(dump)
            dumper.dump(frame);
    }

    public void checkWallCollisions(GranularParticle p, double L, double D, double deltaTime) {
        // checking left vertical
        auxParticle.setY(p.getY());
        auxParticle.setX(0.0);

        if (p.isWithinRadiusBoundingBox(auxParticle, 0)) {

            p.interact(auxParticle, deltaTime);
        }


        // checking right vertical
        auxParticle.setY(p.getY());
        auxParticle.setX(L);

        if (p.isWithinRadiusBoundingBox(auxParticle, 0)) {
            p.interact(auxParticle, deltaTime);
        }



        // checking bottom horizontal with hole
        auxParticle.setY(0);
        auxParticle.setX(p.getX());

//        if (p.isWithinRadiusBoundingBox(auxParticle, 0) && ( p.getX() < (L/2.0 - D/2.0) || p.getX() >= (L/2.0 + D/2.0)) ) {
        if (p.isWithinRadiusBoundingBox(auxParticle, 0) && ( p.getX() < (L/2.0 - D/2.0) || p.getX() >= (L/2.0 + D/2.0)) ) {
            p.interact(auxParticle, deltaTime);
        }
    }
}
