package tp3;


import common.Vector2D;
import lombok.Getter;
import tp1.Particle;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class Board {

    private static final double EPSILON = 0.0001;

    @Getter
    private double L;

    private double t = 0;

    private PointDumper dumper;

    @Getter
    private List<Particle> particles;

    public PriorityQueue<Event> events = new PriorityQueue<>();

    public Board(double L, List<Particle> particles, String basePath){
        this.L = L;
        this.particles = particles;
        this.dumper = new PointDumper(basePath, PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
    }

    public void computeEvents(){
//        events.clear();

        //Compute wall collision events
        particles.forEach(x -> {
            Event e = wallCollisions(x, 0);
            if (e != null) {
                events.add(e);
            }
        });

        List<Particle> remaining = new LinkedList<>(particles);
        for(Particle x : particles){
            remaining.remove(0);
            events.addAll(particleCollisions(x, remaining, 0));
        }
    }

    public void computeEvents(Particle p, double acumTime) {
        PriorityQueue<Event> particleEvents = new PriorityQueue<>();
        particleEvents.add(wallCollisions(p, acumTime));
        List<Particle> remaining = new LinkedList<>(particles);
        remaining.remove(p);
        particleEvents.addAll(particleCollisions(p, remaining, acumTime));
        events.add(particleEvents.remove());
    }


    private List<Event> particleCollisions(Particle p, List<Particle> particles, double acumTime){
        List<Event> events = new LinkedList<>();

        for(Particle o : particles){
            Vector2D deltaV = o.velocity().dif(p.velocity());
            Vector2D deltaR = o.position().dif(p.position());
            double VR = deltaV.dot(deltaR);
            double VV = deltaV.dot(deltaV);
            double RR = deltaR.dot(deltaR);
            double sigma = p.getRadius()+o.getRadius();
            double d = Math.pow(VR,2) - VV*(RR-Math.pow(sigma, 2));
            if(VR >= 0 || d < 0)
                continue;
            double t = (-1)*(VR + Math.sqrt(d))/VV;
            double eventTime = acumTime + t;
            if(t < EPSILON || eventTime > p.collisionTime || eventTime > o.collisionTime)
                continue;
            events.add(new Event(p, o, eventTime));
        }

        return events;
    }


    private Event wallCollisions(Particle p, double acumTime){

        double t, oPos;
        if(p.getVx() > 0){
            t = (L-p.getRadius()-p.getX())/p.getVx();

            oPos = t*p.getVy() + p.getY();
            double eventTime = acumTime + t;
            if(oPos >= 0 && oPos < L && eventTime < p.collisionTime)
                return new Event(p, eventTime, Event.WallType.V);

        } else if(p.getVx() < 0){
            t = (p.getRadius()-p.getX())/p.getVx();

            oPos = t*p.getVy() + p.getY();
            double eventTime = acumTime + t;
            if(oPos >= 0 && oPos < L && eventTime < p.collisionTime)
                return new Event(p, eventTime, Event.WallType.V);
        }

        if(p.getVy() > 0){
            t = (L-p.getRadius()-p.getY())/p.getVy();

            oPos = t*p.getVx() + p.getX();
            double eventTime = acumTime + t;
            if(oPos >= 0 && oPos < L && eventTime < p.collisionTime)
                return new Event(p, eventTime, Event.WallType.H);

        } else if(p.getVy() < 0){
            t = (p.getRadius()-p.getY())/p.getVy();

            oPos = t*p.getVx() + p.getX();
            double eventTime = acumTime + t;
            if(oPos >= 0 && oPos < L && eventTime < p.collisionTime)
                return new Event(p, eventTime, Event.WallType.H);
        }
        return null;
    }

    public void processEvent() throws IOException{
        if(events.isEmpty())
            throw new IllegalStateException();

        Event event = events.remove();
        double delta = event.getTime() - t;
        t = event.getTime();

        Particle p1 = event.getP1(), p2 = event.getP2();
        if (event.getTime() == p1.collisionTime && (p2 == null || event.getTime() == p2.collisionTime)) {
            p1.collisionTime = Double.MAX_VALUE;
            if (p2 != null) {
                p2.collisionTime = Double.MAX_VALUE;
            }
        } else {

            return;
        }

        particles.forEach(p -> {
            p.setX(p.getVx()*delta + p.getX());
            p.setY(p.getVy()*delta + p.getY());
        });

        switch (event.getType()){

            case PARTICLE:
                Vector2D deltaV = p2.velocity().dif(p1.velocity());
                Vector2D deltaR = p2.position().dif(p1.position());
                double VR = deltaV.dot(deltaR);
                double sigma = p1.getRadius() + p2.getRadius();
                double J = (2*p1.getMass()*p2.getMass()*VR)/(sigma*(p1.getMass()+p2.getMass()));
                double Jx = J*(p2.getX()-p1.getX())/sigma;
                double Jy = J*(p2.getY()-p1.getY())/sigma;

                p1.setVx(p1.getVx() + Jx/p1.getMass());
                p1.setVy(p1.getVy() + Jy/p1.getMass());

                p2.setVx(p2.getVx() - Jx/p2.getMass());
                p2.setVy(p2.getVy() - Jy/p2.getMass());

                computeEvents(p1, event.getTime());
                computeEvents(p2, event.getTime());
                break;
            case WALL:

                switch (event.getWallType()){
                    case V:
                        p1.setVx(p1.getVx()*(-1));
                        break;
                    case H:
                        p1.setVy(p1.getVy()*(-1));
                        break;
                }
                computeEvents(p1, event.getTime());
                break;
        }
        dumpParticles();
    }

    public void dumpParticles() throws IOException {
        particles.forEach(x -> dumper.print2D(x.getX(), x.getY(), x.getVx(), x.getVy(), x.getMass(), x.getRadius(), x.getId()));
        dumper.dump(t, L);
    }
}
