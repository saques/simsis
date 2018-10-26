package common;

import tp5.BeemanGranularParticle;
import tp5.GranularParticle;
import tp6.Pedestrian;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ParticleGenerators {

    private ParticleGenerators(){}

    public static List<Particle> generateEntities(double L, double W, int N, double radius, Random r, boolean checkOverlapping) throws Exception{

        List<Particle> ans = new ArrayList<>(N);
        PrintWriter sta = new PrintWriter(new FileWriter("static.txt"));
        PrintWriter din = new PrintWriter(new FileWriter("dynamic.txt"));

        sta.println(N);
        sta.println(L);

        //TODO: t0 when time is relevant, irrelevant in this case
        din.println(0);

        while(N > 0) {
            double x = r.nextDouble() * L, y = r.nextDouble() * W;

            Particle p = new Particle(x, y, radius);

            if(checkOverlapping && ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0))) {
                Particle.decreaseIDs();
                continue;
            }

            //TODO: set property of the ship, irrelevant in this case
            sta.printf("%f %d\n", radius, 0);
            //TODO: set speed of the ship, irrelevant in this case
            din.printf("%f %f %f %f\n", x, y, 0.0, 0.0);
            ans.add(p);
            N--;
        }

        sta.flush(); sta.close();
        din.flush(); din.close();
        return ans;
    }

    public static List<GranularParticle> generateGranularParticles(double L, double W, int N, double radius, double mass, double k, double gamma, double mu, Random r) throws Exception {
        List<GranularParticle> ans = new ArrayList<>(N);

        while (N > 0) {
            double x = Math.max(0.1, Math.min(r.nextDouble(), .9)) * W , y = Math.max(0.1, Math.min(r.nextDouble(), .9)) * L;
            GranularParticle p = new GranularParticle(x, y,0, 0, radius, mass, k, gamma, mu);
            if (ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0))) {
                GranularParticle.decreaseIDs();
            } else {
                ans.add(p);
                N--;
            }
        }
        return ans;
    }

    public static List<BeemanGranularParticle> generateBeemanGranularParticles(double L, double W, int N, double minRadius, double maxRadius, double mass, double k, double gamma, double mu, Random r) throws Exception {
        List<BeemanGranularParticle> ans = new ArrayList<>(N);

        while (N > 0) {
            double x = Math.max(0.1, Math.min(r.nextDouble(), .9)) * W , y = Math.max(0.1, Math.min(r.nextDouble(), .9)) * L;
            double radius = r.nextDouble() * (maxRadius - minRadius) + minRadius;
            BeemanGranularParticle p = new BeemanGranularParticle(x, y,0, 0, radius, mass, k, gamma, mu);
            if (ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0))) {
                GranularParticle.decreaseIDs();
            } else {
                ans.add(p);
                N--;
            }
        }
        return ans;
    }

    public static List<Pedestrian> generatePedestrians(double L, double W, int N, double minRadius, double maxRadius, double minV,double maxV,double mass, double k, double gamma, double mu, double A,double B,double tau,Random r) throws Exception {
        List<Pedestrian> ans = new ArrayList<>(N);

        while (N > 0) {
            double x = Math.max(0.1, Math.min(r.nextDouble(), .9)) * W , y = Math.max(0.1, Math.min(r.nextDouble(), .9)) * L;
            double radius = r.nextDouble() * (maxRadius - minRadius) + minRadius;
            double v =  r.nextDouble() * (maxV - minV) + minV;
            Pedestrian p = new Pedestrian(x, y,0, 0, radius, mass, k, gamma, mu,A,B,tau,v);
            if (ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0))) {
                GranularParticle.decreaseIDs();
            } else {
                ans.add(p);
                N--;
            }
        }
        return ans;
    }

    public static List<Particle> generateBrownianParticles(double bigParticleMass, double bigParticleRadius, double L, int N, double radius, double mass, double maxSpeed, Random r, boolean checkOverlapping) throws Exception{

        List<Particle> ans = new ArrayList<>(N+1);
        PrintWriter sta = new PrintWriter(new FileWriter("static.txt"));
        PrintWriter din = new PrintWriter(new FileWriter("dynamic.txt"));

        sta.println(N+1);
        sta.println(L);

        //TODO: t0 when time is relevant, irrelevant in this case
        din.println(0);

        //Big ship data
        sta.printf("%f %f\n", bigParticleRadius, bigParticleMass);
        din.printf("%f %f %f %f\n", L/2, L/2, 0.0, 0.0);
        ans.add(new Particle( L/2,  L/2,  0,  0,  bigParticleRadius,  bigParticleMass));

        while(N > 0) {
            double x = r.nextDouble() * L, y = r.nextDouble() * L;
            double v = r.nextDouble()*maxSpeed;
            double vx = r.nextDouble()*v*(r.nextDouble() > 0.5 ? 1 : -1);
            double vy = Math.sqrt((Math.pow(v, 2) - Math.pow(vx, 2)))*(r.nextDouble() > 0.5 ? 1 : -1);

            Particle p = new Particle(x, y,vx, vy, radius, mass);

            if(checkOverlapping && (ans.stream().anyMatch(t -> t.isWithinRadiusBoundingBox(p, 0)) ||
                                    p.overlapsBoundaries(L))) {
                Particle.decreaseIDs();
                continue;
            }

            //TODO: set property of the ship, irrelevant in this case
            sta.printf("%f %f\n", radius, mass);
            //TODO: set speed of the ship, irrelevant in this case
            din.printf("%f %f %f %f\n", x, y, vx, vy);
            ans.add(p);
            N--;
        }

        sta.flush(); sta.close();
        din.flush(); din.close();
        return ans;
    }



}
