package tp4.ship;

import java.util.LinkedList;
import java.util.List;

public class CraftMain {

    static final double maxTime = 31558118.4*2;
    static final double delta = 60*60*24;

    public static void main(String[] args) {


        List<MDParticle> system = new LinkedList<>();


        BeemanMDParticle sun = new BeemanMDParticle(1.9885E30, 695700 , 0, 0, 0, 0);
        BeemanMDParticle earth = new BeemanMDParticle(
                5.97237E24, 6371,
                  1.443040359985483E+8, -4.566821691926755E+7,
                 8.429276455862507E+0, 2.831601955976786E+1);

        system.add(sun);
        system.add(earth);

        runBeeman(delta, maxTime, system);


    }

    private static void runBeeman(double delta, double maxTime, List<MDParticle> system){


        system.forEach(x -> system.forEach(y -> {
            if(x != y) {
                x.interact(y);
                ((BeemanMDParticle)x).initializeF();
            }
        }));

        for(int i = 0; i<maxTime/delta; i++){
            System.out.println(i);
            system.forEach(x -> x.rDelta(delta));
            system.forEach(MDParticle::saveState);
            system.forEach(x -> system.forEach(y -> {
                if(x != y)
                    x.interact(y);
            }));
            system.forEach(x -> x.vDelta(delta));
        }

    }


}
