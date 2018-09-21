package tp4.ship;

import common.Vector2D;
import utils.PointDumper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CraftMain {

    static final double maxTime = 31558118.4*4;
    static final double delta = 60*60*24*2;
    static final double maxSpeed = 20;
    static final double maxAltitude = 10000;
    static final double karmanLine = 100;

    static final double heightStep = 50;
    static final double speedStep = 0.5;

    static final double eMass = 5.97237E24, eRadius = 6371;
    static final double ex0 = 1.443040359985483E+8, ey0 = -4.566821691926755E+7;
    static final double evx0 = 8.429276455862507E+0, evy0 = 2.831601955976786E+1;


    static final double craftMass = 721.9;

    public static void main(String[] args) throws IOException, CloneNotSupportedException{

        beeman();

    }

    public static List<MDParticle> cloneSystem(List<MDParticle> system){
        List<MDParticle> ans = new LinkedList<>();

        system.forEach(x-> {
            try{
               ans.add((MDParticle)x.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });

        return ans;
    }


    private static void beeman() throws IOException {

        List<MDParticle> system = new LinkedList<>();


        PointDumper beemanDumper = new PointDumper(".\\tp4\\ovito\\beeman\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        BeemanMDParticle sun = new BeemanMDParticle(1.9885E30, 695700 , 0, 0, 0, 0, beemanDumper);

        BeemanMDParticle earth = new BeemanMDParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                beemanDumper);

        BeemanMDParticle saturn = new BeemanMDParticle(
                5.68319E+26, 58232,
                -1.075238877886715E+09, 8.538222924091074E+08,
                -6.527515746018062E+00, -7.590526046562251E+00,
                beemanDumper
        );

        BeemanMDParticle jupiter = new BeemanMDParticle(
                1.89813E+27, 69911,
                1.061950341671551E+08, 7.544955348409320E+08,
                -1.309157032053854E+01, 2.424744678419164E+00,
                beemanDumper
        );


        system.add(sun);
        system.add(earth);
        system.add(saturn);
        system.add(jupiter);
        CraftStats stats = runBeeman(delta, maxTime, system, beemanDumper, saturn, jupiter, earth, sun);

        beemanDumper.dumpList(stats.getDump());
    }

    private static void gpc() throws IOException{
        PointDumper gpcDumper = new PointDumper(".\\tp4\\ovito\\gpc\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GearPredictorCorrectorParticle sun2 = new GearPredictorCorrectorParticle(1.9885E30, 695700 , 0, 0, 0, 0, gpcDumper);

        List<MDParticle> system2 = new LinkedList<>();
        GearPredictorCorrectorParticle earth2 = new GearPredictorCorrectorParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                gpcDumper);

        GearPredictorCorrectorParticle saturn2 = new GearPredictorCorrectorParticle(
                5.68319E+26, 58232,
                -1.075238877886715E+09, 8.538222924091074E+08,
                -6.527515746018062E+00, -7.590526046562251E+00,
                gpcDumper
        );

        GearPredictorCorrectorParticle jupiter2 = new GearPredictorCorrectorParticle(
                1.89813E+27, 69911,
                1.061950341671551E+08, 7.544955348409320E+08,
                -1.309157032053854E+01, 2.424744678419164E+00,
                gpcDumper
        );

        system2.add(sun2);
        system2.add(earth2);
        system2.add(saturn2);
        system2.add(jupiter2);

        runGearPredictorCorrector(delta, maxTime, system2, gpcDumper);
    }



    private static CraftStats runBeeman(double delta, double maxTime, List<MDParticle> initialSystem, PointDumper dumper,
                                  BeemanMDParticle saturn, BeemanMDParticle jupiter, BeemanMDParticle earth, BeemanMDParticle sun) throws IOException {


        CraftStats stats = new CraftStats();

        Vector2D earthInit = new Vector2D(ex0, ey0);
        Vector2D earthNor = new Vector2D(earthInit).nor();
        double earthDistance = earthInit.mod();

        Vector2D earthVInit = new Vector2D(evx0, evy0);
        double earthVelocity = earthVInit.mod();
        Vector2D earthVNor = new Vector2D(earthVInit).nor();

        for(double h = karmanLine; h < maxAltitude; h += heightStep){

            //Calculate orbital speed at given height
            double minV = Math.sqrt((MDParticle.G*eMass)/(eRadius + h));

            System.out.println(minV);

            for(double v = minV; v < maxSpeed; v += speedStep){

                System.out.printf("Running for height %f, speed %f\n", h, v);

                List<MDParticle> system = cloneSystem(initialSystem);

                Vector2D craftPosition = new Vector2D(earthNor).scl(earthDistance + eRadius + h);

                Vector2D craftV = new Vector2D(earthVInit).add(new Vector2D(earthVNor).scl(v));

                BeemanMDParticle voyager = new BeemanMDParticle(
                        craftMass, 0.01,
                        craftPosition.x, craftPosition.y,
                        craftV.x, craftV.y,
                        dumper
                );

                system.add(voyager);

                system.forEach(x -> system.forEach(y -> {
                    if(x != y) {
                        x.interact(y);
                        ((BeemanMDParticle)x).initializeF();
                    }
                }));

                double minJupiter = Double.MAX_VALUE, minSaturn = Double.MAX_VALUE;

                for(int i = 0; i<maxTime/delta; i++){
                    system.forEach(x -> x.rDelta(delta));

                    double saturnDist = new Vector2D(saturn.x0, saturn.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    double jupiterDist = new Vector2D(jupiter.x0, jupiter.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    double earthDist = new Vector2D(earth.x0, earth.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    double sunDist = new Vector2D(sun.x0, sun.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    if(saturnDist <= saturn.radius || jupiterDist <= jupiter.radius || earthDist <= earth.radius || sunDist <= sun.radius){
                        dumper.getList();
                        break;
                    }

                    minJupiter = Math.min(minJupiter, jupiterDist);
                    minSaturn = Math.min(minSaturn, saturnDist);

                    for (MDParticle mdParticle : system) {
                        mdParticle.saveState(i);
                    }
                    system.forEach(x -> system.forEach(y -> {
                        if(x != y)
                            x.interact(y);
                    }));
                    system.forEach(x -> x.vDelta(delta));
                    dumper.dumpToList();
                }

                List<String> dump = dumper.getList();

                if(stats.isBetterApproach(minJupiter, minSaturn, craftPosition, craftV)){
                    stats.setDump(dump);
                }
            }
        }

        return stats;
    }

    private static void runGearPredictorCorrector(double delta, double maxtime, List<MDParticle> system, PointDumper dumper) throws  IOException  {
        system.forEach(x -> system.forEach(y -> {
            if(x != y) {
                x.interact(y);
            }
        }));

        for(int i = 0; i<maxTime/delta; i++){
            system.forEach(x -> x.rDelta(delta));
            system.forEach(x-> {
                x.resetForces();
            });
            system.forEach(x -> system.forEach(y -> {
                if(x != y)
                    x.interact(y);
            }));
            system.forEach(x -> x.vDelta(delta));
            dumper.dump(i);
        }
    }
}
