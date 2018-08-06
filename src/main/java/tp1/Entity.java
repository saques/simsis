package tp1;

import java.util.List;

public interface Entity {

    List<Point2D> mbr();

    double getX();

    double getY();

    double getRadius();

    int getId();

    boolean isWithinRadiusBoundingBox(Entity t, double evalDistance);

    boolean isWithinRadiusPeriodic(Entity t, double evalDistance, double boxLength);

}
