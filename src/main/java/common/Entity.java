package common;

import tp1.Point2D;

import java.util.List;

public interface Entity {

    List<Point2D> mbr();

    double getX();

    double getY();

    void setX(double x);

    void setY(double y);

    void setVx(double vx);

    void setVy(double vy);

    double getRadius();

    int getId();

    boolean isWithinRadiusBoundingBox(Entity t, double evalDistance);

    boolean isWithinRadiusPeriodic(Entity t, double evalDistance, double boxLength);

}
