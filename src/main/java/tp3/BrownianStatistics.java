package tp3;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
public class BrownianStatistics {

    @Getter
    private List<Double> collisionTimes;

    @Getter
    private List<Double> velocitiesAtInit;

    @Getter
    private List<Double> velocities;

    @Getter
    private List<Point2D> bigParticleTrajectory;



}
