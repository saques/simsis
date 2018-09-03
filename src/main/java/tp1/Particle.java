package tp1;

import common.Vector2D;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class Particle implements Entity{

    private static int IDS = 0;

    @Getter
    private int id;

    @Getter @Setter
    private double x, vx, y, vy;
    @Getter
    private double radius, mass;

    public double collisionTime = Double.MAX_VALUE;

    public Particle(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.id = IDS++;
    }

    public Particle(double x, double y, double vx, double vy, double radius, double mass){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.id = IDS++;
    }

    public boolean isWithinRadiusBoundingBox(Entity o, double evalDistance){
        return pithagoras(x,o.getX(),y,o.getY()) - radius - o.getRadius() <= evalDistance;
    }

    public boolean overlapsBoundaries(double L){
        return (L-x) < radius || x < radius || (L-y) < radius || y < radius;
    }

    public boolean isWithinRadiusPeriodic(Entity o, double evalDistance, double boxLength){
        if(isWithinRadiusBoundingBox(o, evalDistance))
            return true;


        double tx=x, ty=y, ox=o.getX(), oy=o.getY();
        if(Math.abs(x-o.getX()) >= boxLength/2){
            if(x > o.getX()) {
                tx = x - boxLength;
            } else {
                ox = o.getX() - boxLength;
            }
        }
        if(Math.abs(y-o.getY()) >= boxLength/2){
            if(y > o.getY()) {
                ty = x - boxLength;
            } else {
                oy = o.getY() - boxLength;
            }
        }

        return pithagoras(tx, ox, ty, oy) - radius - o.getRadius() <= evalDistance;
    }

    private static double pithagoras(double tx, double ox, double ty, double oy){
        return Math.sqrt(Math.pow(Math.abs(tx - ox), 2) + Math.pow(Math.abs(ty - oy), 2));
    }

    public List<Point2D> mbr() {
        List<Point2D> ans = new ArrayList<Point2D>(2);
        ans.add(new Point2D(x-radius, y-radius));
        ans.add(new Point2D(x+radius, y+radius));
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    public Vector2D position(){
        return new Vector2D(x, y);
    }

    public Vector2D velocity(){
        return new Vector2D(vx, vy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static void decreaseIDs(){
        IDS--;
    }

    public static void resetIDs(){
        IDS = 0;
    }
}
