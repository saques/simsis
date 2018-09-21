package common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Vector2D {

    @Getter
    public double x, y;

    public Vector2D(Vector2D vector2D){
        this.x = vector2D.x;
        this.y = vector2D.y;
    }


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
        double mod = mod();
        return scl(1.0/mod);
    }

    public Vector2D scl(double scl){
        x *= scl;
        y *= scl;
        return this;
    }

    public double mod(){
        return Math.sqrt(mod2());
    }

    public double mod2(){
        return (Math.pow(x, 2) + Math.pow(y, 2));
    }

}
