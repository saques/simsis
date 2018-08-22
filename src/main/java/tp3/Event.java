package tp3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import tp1.Particle;

@EqualsAndHashCode
public class Event implements Comparable<Event>{


    enum EventType {
        PARTICLE, WALL
    }

    enum WallType{
        V, H
    }

    public Event(Particle p1, Particle p2, double time){
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.type = EventType.PARTICLE;
    }

    public Event(Particle p, double time, WallType wallType){
        this.p1 = p;
        this.time = time;
        this.type = EventType.WALL;
        this.wallType = wallType;
    }

    @Getter
    private Particle p1, p2;

    @Getter
    private double time;

    @Getter
    private EventType type;

    @Getter
    private WallType wallType;

    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.time);
    }
}
