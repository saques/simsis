package tp3;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import tp1.Particle;

@AllArgsConstructor
@EqualsAndHashCode
public class Event implements Comparable<Event>{

    @Getter
    private Particle p1, p2;

    @Getter
    private double time;

    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.time);
    }
}
