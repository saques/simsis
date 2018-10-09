package tp5;

import common.Grid;
import tp1.Point2D;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class DynamicGrid extends Grid<GranularParticle> {
    Random r = new Random();

    double D;

    public DynamicGrid(double L, double W, int M, double D) {
        super(L, W, M);
        this.D = D;
    }

    public double getD() {
        return D;
    }

    public void update(int frame, double deltaTime, PointDumper dumper) throws IOException {
        for (GranularParticle particle : particles) {
            particle.resetForces();
            if (particle.getId() == 1) {
                particle.applyGravity();
            }
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
            checkWallCollisions(particle, getL(), getD());
            alreadyInteracted.add(particle);
        }

        // Update particle position based on cumulative force
        for (GranularParticle particle : particles) {
            double oldX = particle.getX(), oldY = particle.getY();
            List<Point2D> oldMbr = particle.mbr();
            particle.rDelta(deltaTime);
            particle.vDelta(deltaTime);
            updateCell(particle, oldMbr, oldX, oldY, particle.getX(), particle.getY());
            dumper.print2D(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
        }

        dumper.dump(frame);
    }

    public void checkWallCollisions(GranularParticle p, double L, double D) {
        if (p.getRadius() + p.getX() == L || p.getX() - p.getRadius() == 0.0) {
            System.out.println("Wall collision");
            p.setVx(p.getVx() * (-1));
        } else if (p.getY() + p.getRadius() == L && ((p.getX() + p.getRadius() > L / 2.0 + D / 2.0) || (p.getX() - p.getRadius() < L / 2.0 - D / 2.0))) {
            System.out.println("Wall collision");
            p.setVy(p.getVy() * (-1));
        }
    }
}
