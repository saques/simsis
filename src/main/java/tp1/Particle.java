package tp1;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
public class Particle implements Entity{




    @Getter @Setter
    private double x, y, radius;

    public boolean isWithinRadius(Particle o, double evalDistance){
        double distance = Math.sqrt(Math.pow(Math.abs(this.x - o.x),2) + Math.pow(Math.abs(this.y - o.y) ,2));
        return distance - radius - o.radius <= evalDistance;
    }

    public List<Point2D> mbr() {
        List<Point2D> ans = new ArrayList<Point2D>(2);
        ans.add(new Point2D(x-radius, y-radius));
        ans.add(new Point2D(x+radius, y+radius));
        return ans;
    }
}
