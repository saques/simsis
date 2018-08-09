package tp1;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle implements Entity{

    private static int IDS = 0;

    @Getter
    private int id;

    @Getter @Setter
    private double x;
    @Getter @Setter
    private double y;
    @Getter @Setter
    private double radius;

    public Particle(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.id = IDS++;
    }

    public boolean isWithinRadiusBoundingBox(Entity o, double evalDistance){
        return pithagoras(x,o.getX(),y,o.getY()) - radius - o.getRadius() <= evalDistance;
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static void decreaseIDs(){
        IDS--;
    }
}
