package tp4.ship;

import java.util.LinkedList;
import java.util.List;

public class CraftMain {

    static final double maxTime = 100;
    static final double delta = 0.01;

    public static void main(String[] args) {


        List<MDParticle> system = new LinkedList<>();

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
