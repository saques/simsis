package tp5;

import common.Grid;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class DynamicGrid extends Grid<GranularParticle> {
    Random r = new Random();

    public DynamicGrid(double L, double W, int M) {
        super(L, W, M);

    }

    public void update(int frame, double deltaTime, PointDumper dumper) throws IOException {
        for (GranularParticle particle: particles) {
            particle.resetForces();
            if (r.nextDouble() < 0.01) {
                particle.applyGravity();
            }

        }

        Map<GranularParticle, Set<GranularParticle>> particleMap = evalNeighbours(0, Mode.BOX);
        Set<GranularParticle> alreadyInteracted = new HashSet<>();
        for (Map.Entry<GranularParticle, Set<GranularParticle>> entry: particleMap.entrySet() ) {
            // Interact with every particle, by checking overlapping and applying force
            GranularParticle particle = entry.getKey();
            for (GranularParticle other: entry.getValue()) {
                if (!alreadyInteracted.contains(other) && particle.isWithinRadiusBoundingBox(other, 0)) {
                    particle.interact(other, deltaTime);
                }
            }
            alreadyInteracted.add(particle);
        }

        // Update particle position based on cumulative force
        for (GranularParticle particle: particles) {
            particle.rDelta(deltaTime);
            particle.vDelta(deltaTime);
            // updateCell(particle)
            dumper.print2D(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.getMass(), particle.getRadius(), particle.getId());
        }

        dumper.dump(frame);
    }
}
