package tp3;


import lombok.Getter;
import tp1.Particle;

import java.util.List;
import java.util.PriorityQueue;

public class Board {

    @Getter
    private double width, height;

    @Getter
    private List<Particle> particles;

    public PriorityQueue<Event> events = new PriorityQueue<>();

    public Board(double width, double height, List<Particle> particles){
        this.width = width;
        this.height = height;
        this.particles = particles;
    }

    public void computeEvents(){
        events.clear();
        for(Particle x : particles){
            for(Particle y : particles){
                //check if x and y collide
                //add to PQ

                //check if two particles involved in an event
                //are already added in the PQ
            }
        }
    }

    public void processEvent(){
        Event event = events.poll();
        //process event

        //recompute events or evict invalid events
        //or check if valid before processing
    }
}
