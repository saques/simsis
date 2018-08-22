package tp3;


import common.Vector2D;
import lombok.Getter;
import tp1.Particle;
import utils.PointDumper;

import java.io.IOException;
import java.util.*;

public class Board {

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
        events.clear();

        //Compute wall collision events
        particles.forEach(x -> events.addAll(wallCollisions(x)));

        List<Particle> remaining = new LinkedList<>(particles);
        for(Particle x : particles){
            remaining.remove(0);
            events.addAll(particleCollisions(x, remaining));
        }

    }


    private List<Event> particleCollisions(Particle p, List<Particle> particles){
        List<Event> events = new LinkedList<>();

        for(Particle o : particles){
            Vector2D deltaV = p.velocity().dif(o.velocity());
            Vector2D deltaR = p.position().dif(o.position());
            double VR = deltaV.dot(deltaR);
            double VV = deltaV.dot(deltaV);
            double RR = deltaR.dot(deltaR);
            double sigma = p.getRadius()+o.getRadius();
            double d = Math.pow(VR,2) - VV*(RR-Math.pow(sigma, 2));
            if(VR >= 0 || d < 0)
                continue;
            double t = (-1)*(VR + Math.sqrt(d))/VV;
            events.add(new Event(p, o, t));
        }

        return events;
    }


    private List<Event> wallCollisions(Particle p){

        List<Event> ans = new LinkedList<>();

        double t, oPos;
        if(p.getVx() > 0){
            t = (L-p.getRadius()-p.getX())/p.getVx();

            oPos = t*p.getVy() + p.getY();
            if(oPos >= 0 && oPos < L)
                ans.add(new Event(p, t, Event.WallType.V));

        } else if(p.getVx() < 0){
            t = (p.getRadius()-p.getX())/p.getVx();

            oPos = t*p.getVy() + p.getY();
            if(oPos >= 0 && oPos < L)
                ans.add(new Event(p, t, Event.WallType.V));
        }

        if(p.getVy() > 0){
            t = (L-p.getRadius()-p.getY())/p.getVy();

            oPos = t*p.getVx() + p.getX();
            if(oPos >= 0 && oPos < L)
                ans.add(new Event(p, t, Event.WallType.H));

        } else if(p.getVy() < 0){
            t = (p.getRadius()-p.getY())/p.getVy();

            oPos = t*p.getVx() + p.getX();
            if(oPos >= 0 && oPos < L)
                ans.add(new Event(p, t, Event.WallType.H));
        }

        return ans;

    }

    public void processEvent(){
        if(events.isEmpty())
            throw new IllegalStateException();

        Event event = events.remove();
        double delta = event.getTime() - t;
        t = event.getTime();

        Particle p1 = event.getP1(), p2 = event.getP2();


        particles.forEach(p -> {
            p.setX(p.getVx()*delta + p.getX());
            p.setY(p.getVy()*delta + p.getY());
        });


        switch (event.getType()){

            case PARTICLE:
                Vector2D deltaV = p1.velocity().dif(p2.velocity());
                Vector2D deltaR = p1.position().dif(p2.position());
                double VR = deltaV.dot(deltaR);
                double sigma = p1.getRadius() + p2.getRadius();
                double J = (2*p1.getMass()*p2.getMass()*VR)/(sigma*(p1.getMass()+p2.getMass()));
                double Jx = J*(p1.getX()-p2.getX())/sigma;
                double Jy = J*(p1.getY()-p2.getY())/sigma;

                p1.setVx(p1.getVx() + Jx/p1.getMass());
                p1.setVy(p1.getVy() + Jy/p1.getMass());

                p2.setVx(p2.getVx() + Jx/p2.getMass());
                p2.setVy(p2.getVy() + Jy/p2.getMass());

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

                break;
        }

    }

    public void dumpParticles() throws IOException {
        particles.forEach(x -> dumper.print2D(x.getX(), x.getY(), x.getVx(), x.getVy(), x.getMass(), x.getRadius()));
        dumper.dump(t);
    }
}
