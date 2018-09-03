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

    public Vector2D dif(Vector2D o){
        return new Vector2D(x-o.x, y-o.y);
    }


}
