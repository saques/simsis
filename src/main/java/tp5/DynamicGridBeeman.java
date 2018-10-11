package tp5;

import common.Grid;
import common.Particle;
import tp1.Point2D;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class DynamicGridBeeman extends Grid<BeemanGranularParticle> {

    double D;
    BeemanGranularParticle auxParticle = new BeemanGranularParticle(0, 0,  0, 0, 0, 1000, 0, 0, 0);


    public DynamicGridBeeman(double L, double W, int M, double D, Random r) {
        super(L, W, M, r);
        this.D = D;
    }

    public double getD() {
        return D;
    }

    public void update(int frame, double deltaTime, PointDumper dumper, boolean dump) throws IOException {
        for (BeemanGranularParticle particle : particles) {
            particle.updateForces();
        }

        Map<BeemanGranularParticle, Set<BeemanGranularParticle>> particleMap = evalNeighbours(0, Mode.BOX);
        Set<BeemanGranularParticle> alreadyInteracted = new HashSet<>();

        // Update particle position based on cumulative force
        for (BeemanGranularParticle particle : particles) {
            particle.rDelta(deltaTime);
            particle.applyGravity();
            for (BeemanGranularParticle other : particleMap.get(particle)) {
                if (!alreadyInteracted.contains(other) && particle.isWithinRadiusBoundingBox(other, 0)) {
                    particle.interact(other, deltaTime);
                }
            }
            checkWallCollisions(particle, getL(), getD(),deltaTime);
            alreadyInteracted.add(particle);
            particle.vDelta(deltaTime);
            checkFallingOff(particle);
            if(dump)
                dumper.print2D(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
        }

        updateParticles();

        if(dump)
            dumper.dump(frame);
    }

    private void checkFallingOff(BeemanGranularParticle particle) {
        if (particle.getY() < -(getL() / 10)) {
            Particle dummy = new Particle(0, getL(), particle.getRadius());
            do {
                dummy.setX(Math.max(0.1, Math.min(r.nextDouble(), .9)) * getW());
            }
            while(particles.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(dummy, 0)));
            particle.setY(dummy.getY());
            particle.setX(dummy.getX());
            particle.setVy(0);
            particle.setVx(0);
            particle.resetAllForces();

        }
    }

    public void checkWallCollisions(BeemanGranularParticle p, double L, double D, double deltaTime) {
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