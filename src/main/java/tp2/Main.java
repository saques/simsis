package tp2;

import utils.LifParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        test2D();
//        test3D();
    }

    private static void test3D() throws IOException{
        Life3D life3D = new Life3D(100, "./other/");

        /**
         * Beacon
         */
//        life3D.set(21,22, 0);
//        life3D.set(22,22, 0);
//        life3D.set(21,23, 0);
//        life3D.set(22,23, 0);
//        life3D.set(23,24, 0);
//        life3D.set(24,24, 0);
//        life3D.set(23,25, 0);
//        life3D.set(24,25, 0);
//        life3D.set(21,22, 1);
//        life3D.set(22,22, 1);
//        life3D.set(21,23, 1);
//        life3D.set(22,23, 1);
//        life3D.set(23,24, 1);
//        life3D.set(24,24, 1);
//        life3D.set(23,25, 1);
//        life3D.set(24,25, 1);

        /**
         * Glider
         */
        life3D.set(21,21, 0);
        life3D.set(22,20, 0);
        life3D.set(20,19, 0);
        life3D.set(21,19, 0);
        life3D.set(22,19, 0);
        life3D.set(21,21, 1);
        life3D.set(22,20, 1);
        life3D.set(20,19, 1);
        life3D.set(21,19, 1);
        life3D.set(22,19, 1);




        // Rule 4555 is stable, according to http://web.stanford.edu/~cdebs/GameOfLife/
        life3D.run(42, Life3D.wxyzRuleFactory(5, 7, 6, 6));
    }


    private static void test2D() throws IOException {
//        long start = System.currentTimeMillis();
//        Life2D life2D = new Life2D(1000, "./block/");
//        life2D.set(201,202);
//        life2D.set(202,202);
//        life2D.set(201,203);
//        life2D.set(202,203);
//        life2D.set(203,204);
//        life2D.set(204,204);
//        life2D.set(203,205);
//        life2D.set(204,205);
//        life2D.run(1000, Life2D.defaultRule);
//        long end = System.currentTimeMillis();
//        System.out.println("time: " + (end - start));
        Life2D life = LifParser.newLifeGame("./lif/explosion.lif", 500, "./out/");
        life.run(200, Life2D.defaultRule);
    }
}
