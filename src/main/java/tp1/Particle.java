package tp1;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
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
        double distance = Math.sqrt(Math.pow(Math.abs(this.x - o.getX()),2) + Math.pow(Math.abs(this.y - o.getY()) ,2));
        return distance - radius - o.getRadius() <= evalDistance;
    }

    public boolean isWithinRadiusPeriodic(Entity o, double evalDistance, double boxLength){
        return true;
    }

    public List<Point2D> mbr() {
        List<Point2D> ans = new ArrayList<Point2D>(2);
        ans.add(new Point2D(x-radius, y-radius));
        ans.add(new Point2D(x+radius, y+radius));
        return ans;
    }
}
