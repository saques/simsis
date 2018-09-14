package tp4.ship;

import java.util.LinkedList;
import java.util.List;

public class CraftMain {

    static final double maxTime = 1000;
    static final double delta = 1;

    public static void main(String[] args) {


        List<MDParticle> system = new LinkedList<>();


        BeemanMDParticle sun = new BeemanMDParticle(1.9885E30, 695700000 , 0, 0, 0, 0);
        BeemanMDParticle earth = new BeemanMDParticle(5.97237E24, 6371000, 1.443040359985483E11, -4.566821691926755E10, 8.429276455862507E3, 2.831601955976786E4);

        system.forEach(x -> system.forEach(y -> {
            if(x != y) {
                x.interact(y);
                ((BeemanMDParticle)x).initializeF();
            }
        }));

        for(int i = 0; i<maxTime/delta; i++)
            runBeeman(delta, system);



    }

    private static void runBeeman(double delta, List<MDParticle> system){
        system.forEach(x -> x.rDelta(delta));
        system.forEach(x -> system.forEach(y -> {
            if(x != y)
                x.interact(y);
        }));
        system.forEach(x -> x.vDelta(delta));
    }


}
