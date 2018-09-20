package tp4.ship;

import utils.PointDumper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CraftMain {

    static final double maxTime = 31558118.4;
    static final double delta = 60*60*24;

    public static void main(String[] args) throws IOException{


        List<MDParticle> system = new LinkedList<>();


        PointDumper beemanDumper = new PointDumper(".\\tp4\\ovito\\beeman\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        BeemanMDParticle sun = new BeemanMDParticle(1.9885E30, 695700 , 0, 0, 0, 0, beemanDumper);

        BeemanMDParticle earth = new BeemanMDParticle(
                5.97237E24, 6371,
                  1.443040359985483E+8, -4.566821691926755E+7,
                 8.429276455862507E+0, 2.831601955976786E+1,
                beemanDumper);

        system.add(sun);
        system.add(earth);
        runBeeman(delta, maxTime, system, beemanDumper);


        PointDumper gpcDumper = new PointDumper(".\\tp4\\ovito\\gpc\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GearPredictorCorrectorParticle sun2 = new GearPredictorCorrectorParticle(1.9885E30, 695700 , 0, 0, 0, 0, gpcDumper);

        List<MDParticle> system2 = new LinkedList<>();
        GearPredictorCorrectorParticle earth2 = new GearPredictorCorrectorParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                gpcDumper);

        system2.add(sun2);
        system2.add(earth2);
        runGearPredictorCorrector(delta, maxTime, system2, gpcDumper);


    }

    private static void runBeeman(double delta, double maxTime, List<MDParticle> system, PointDumper dumper) throws IOException {


        system.forEach(x -> system.forEach(y -> {
            if(x != y) {
                x.interact(y);
                ((BeemanMDParticle)x).initializeF();
            }
        }));

        for(int i = 0; i<maxTime/delta; i++){
            system.forEach(x -> x.rDelta(delta));
            for (MDParticle mdParticle : system) {
                mdParticle.saveState(i);
            }
            system.forEach(x -> system.forEach(y -> {
                if(x != y)
                    x.interact(y);
            }));
            system.forEach(x -> x.vDelta(delta));
            dumper.dump(i);
        }

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
