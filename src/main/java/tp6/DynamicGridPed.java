package tp6;

import common.Grid;
import common.Vector2D;
import tp5.BeemanGranularParticle;
import tp5.GranularParticleStats;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class DynamicGridPed extends Grid<Pedestrian> {
    Random r = new Random();

    private double D;
    private BeemanGranularParticle auxParticle = new BeemanGranularParticle(0, 0,  0, 0, 0, 1000, 0, 0,0, 0);
    private List<Vector2D> path;
    private double pathRadius;

    public DynamicGridPed(double L, double W, int M, Random r, double D, List<Vector2D> path, double pathRadius) {
        super(L, W, M, r);
        this.D = D;
        this.path = path;
        this.pathRadius  = pathRadius;
    }

    public double getD() {
        return D;
    }


    double timeAcum = 0;
    public void update(int frame, double deltaTime, PointDumper dumper, boolean dump, GranularParticleStats stats) throws IOException {

        double kinetic = 0;
        particles.parallelStream().forEach(particle -> {
            particle.updateForces();
            particle.drivingForce(path, pathRadius);
        });

        for (int i =  0 ; i < particles.size() ; i++) {
            Pedestrian particle = particles.get(i);
            IntStream.range(i+1, particles.size()).parallel().forEach(j -> {
                Pedestrian other = particles.get(j);
                particle.socialForce(other);
            });
        }
        if(dump)
            stats.totalKineticEnergy.add(kinetic);

        Map<Pedestrian, Set<Pedestrian>> particleMap = evalNeighbours(0, Mode.BOX);
        Set<BeemanGranularParticle> alreadyInteracted = new HashSet<>();
        // Update particle position based on cumulative force

        particles.parallelStream().forEach(particle -> {
            particle.rDelta(deltaTime);
            if(dump){
                double speed = new Vector2D(particle.getVx(), particle.getVy()).mod();
                dumper.updateMaxForce(speed);
            }
        });
        List<Pedestrian> toErase = new ArrayList<>();
        particles.stream().forEach(particle -> {
            for (Pedestrian other : particleMap.get(particle)) {
                if (!alreadyInteracted.contains(other) && particle.isWithinRadiusBoundingBox(other, 0)) {
                    particle.interact(other, deltaTime);
                }
            }
            checkWallCollisions(particle, getD(),deltaTime);
            alreadyInteracted.add(particle);
            if( checkFallingOff(particle, toErase)) {
                dumper.printFalling(timeAcum);
            }
            if(dump) {
                double speed = new Vector2D(particle.getVx(), particle.getVy()).mod();
                dumper.print2DForce(speed, particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
            }
        });
        particles.removeAll(toErase);
        particles.parallelStream().forEach( particle -> {
            particle.vDelta(deltaTime);
        });
        timeAcum += deltaTime;
        updateParticles();

        if(dump) {
            dumper.dump(frame);
            dumper.resetMaxForce();
        }
    }

    private boolean checkFallingOff(Pedestrian particle, List<Pedestrian> toErase) {
        if (particle.getY() < -(getL() / 10)) {
            toErase.add(particle);
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