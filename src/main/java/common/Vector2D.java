package common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Vector2D {

    @Getter
    public double x, y;


    public double dot(Vector2D o){
        return x*o.x + y*o.y;
    }

    public Vector2D sub(Vector2D o){
        return new Vector2D(x-o.x, y-o.y);
    }

    public Vector2D add(Vector2D o) {
        return new Vector2D(x+o.x, y+o.y);
    }

    public Vector2D nor(){
        double mod = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return scl(1.0/mod);
    }

    public Vector2D scl(double scl){
        x *= scl;
        y *= scl;
        return this;
    }


}
