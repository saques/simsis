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

    long collisionA;
    long collisionB;

    public Event(Particle p1, Particle p2, double time){
        p1.collisionTime = time;
        p2.collisionTime = time;
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.type = EventType.PARTICLE;
    }

    public Event(Particle p, double time, WallType wallType){
        p.collisionTime = time;
        this.p1 = p;
        this.time = time;
        this.type = EventType.WALL;
        this.wallType = wallType;
    }

    public boolean isEventStillValid() {
        if (type == EventType.PARTICLE) {
            return time == p1.collisionTime && time == p2.collisionTime;
        } else if (type == EventType.WALL) {
            return time == p1.collisionTime;
        } else {
            throw new RuntimeException("Event was invalid");
        }
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

    @Override
    public String toString() {
        if (type == EventType.PARTICLE) {
            return "particleA" + p1.toString() + ", particleB: " + p2.toString() + ", time:" + time;
        } else {
            return "particle" + p1.toString() + ", wall: " + wallType.toString() + ", time:" + time;
        }
    }
}
