package tp3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import tp1.Particle;

@EqualsAndHashCode
public class Event implements Comparable<Event>{

    enum EventType {
        PARTICLE, WALL;
    }

    public Event(Particle p1, Particle p2, double time){
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.type = EventType.PARTICLE;
    }

    public Event(Particle p, double time){
        this.p1 = p;
        this.time = time;
        this.type = EventType.WALL;
    }

    @Getter
    private Particle p1, p2;

    @Getter
    private double time;

    @Getter
    private EventType type;

    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.time);
    }
}
