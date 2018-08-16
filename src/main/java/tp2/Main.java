package tp2;

import utils.LifParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        test2D();
//        test3D();
    }

    private static void test3D() throws IOException{
        Life3D life3D = new Life3D(100, "./out/oscillator3D/");

        /**
         * Simple H Oscillator.
         * http://wpmedia.wolfram.com/uploads/sites/13/2018/02/06-5-3.pdf
         */
        life3D.set(1, 1, 10);
        life3D.set(1, 2, 10);
        life3D.set(1, 3, 10);
        life3D.set(2, 2, 10);
        life3D.set(3, 1, 10);
        life3D.set(3, 2, 10);
        life3D.set(3, 3, 10);

        life3D.run(100, Life3D.b5s56Rule.fabricate());
    }


    private static void test2D() throws IOException {
        Life2D life = LifParser.newLifeGame("./lif/acorn.lif", 1000, "./out/acorn2D/" );
        life.run(2000, Life2D.defaultRule );
    }
}
