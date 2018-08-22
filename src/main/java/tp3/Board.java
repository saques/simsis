package tp3;


import lombok.Getter;
import tp1.Particle;

import java.util.List;
import java.util.PriorityQueue;

public class Board {

    @Getter
    private double L;

    private double t = 0;

    @Getter
    private List<Particle> particles;

    public PriorityQueue<Event> events = new PriorityQueue<>();

    public Board(double L, List<Particle> particles){
        this.L = L;
        this.particles = particles;
    }

    public void computeEvents(){
        events.clear();

        //Compute collisions against walls
        for(Particle x : particles){
            double t = 0, oPos;
            if(x.getVx() > 0){
                t = (L-x.getRadius()-x.getX())/x.getVx();

                oPos = t*x.getVy() + x.getY();
            } else if(x.getVx() < 0){
                t = (x.getRadius()-x.getX())/x.getVx();

            }

            if(x.getVy() > 0){
                t = (L-x.getRadius()-x.getY())/x.getVy();

            } else if(x.getVy() < 0){
                t = (x.getRadius()-x.getY())/x.getVy();

            }

        }

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
