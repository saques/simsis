package tp5;

import common.Grid;
import common.Particle;
import common.Vector2D;
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


    double fallingParticles = 0;
    double timeAcum = 0;
    public void update(int frame, double deltaTime, PointDumper dumper, boolean dump, GranularParticleStats stats) throws IOException {

        double kinetic = 0;
        for (BeemanGranularParticle particle : particles) {
            if(dump)
                kinetic += particle.kineticEnergy();
            particle.updateForces();
        }
        if(dump)
            stats.totalKineticEnergy.add(kinetic);

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
            checkWallCollisions(particle, getD(),deltaTime);
            alreadyInteracted.add(particle);
            particle.vDelta(deltaTime);
            fallingParticles += checkFallingOff(particle) ? 1: 0;
            if(dump)
                dumper.print2DForce(new Vector2D(particle.nx, particle.ny).mod(), particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
        }
        timeAcum += deltaTime;
        // Flow data is stored 30 times per second
        if (timeAcum > 0.033) {
            stats.flow.add(fallingParticles );
            fallingParticles = 0;
            timeAcum = 0;
        }
        updateParticles();

        if(dump)
            dumper.dump(frame);
    }

    private boolean checkFallingOff(BeemanGranularParticle particle) {
        if (particle.getY() < -(getL() / 10)) {
            Particle dummy = new Particle(0, getL(), particle.getRadius());
            do {
                dummy.setY(r.nextDouble() * 0.2 * getL() + getL() * (1 - 0.05 ) );
                dummy.setX(Math.max(0.1, Math.min(r.nextDouble(), .9)) * getW());
            }
            while(particles.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(dummy, 0)));
            particle.setY(dummy.getY());
            particle.setX(dummy.getX());
            particle.setVy(0);
            particle.setVx(0);
            particle.resetAllForces();
            return true;
        }
        return false;
    }

    public void checkWallCollisions(BeemanGranularParticle p, double D, double deltaTime) {
        // checking left vertical
        auxParticle.setY(p.getY());
        auxParticle.setX(0.0);

        if (p.isWithinRadiusBoundingBox(auxParticle, 0)) {

            p.interact(auxParticle, deltaTime);
        }


        // checking right vertical
        auxParticle.setY(p.getY());
        auxParticle.setX(getW());

        if (p.isWithinRadiusBoundingBox(auxParticle, 0)) {
            p.interact(auxParticle, deltaTime);
        }
        // checking bottom horizontal with hole
        auxParticle.setY(0);
        auxParticle.setX(p.getX());

//        if (p.isWithinRadiusBoundingBox(auxParticle, 0) && ( p.getX() < (L/2.0 - D/2.0) || p.getX() >= (L/2.0 + D/2.0)) ) {
        if (p.isWithinRadiusBoundingBox(auxParticle, 0) && ( p.getX() < (getW()/2.0 - D/2.0) || p.getX() >= (getW() /2.0 + D/2.0)) ) {
            p.interact(auxParticle, deltaTime);
        }
    }
}