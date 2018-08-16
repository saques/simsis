package tp2;

import utils.LifParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        test2D();
//        test3D();
    }

    private static void test3D() throws IOException{
        Life3D life3D = new Life3D(120, "./out/glider3D/");

        /**
         * 3D glider, 5655.
         * https://pdfs.semanticscholar.org/8eda/1c703dc143269c1613fcff63fee15f15f899.pdf
         */
        life3D.set(1, 0, 10);
        life3D.set(2, 0, 10);
        life3D.set(1, 1, 10);
        life3D.set(2, 1, 10);
        life3D.set(1, 3, 10);
        life3D.set(2, 3, 10);
        life3D.set(1, 4, 10);
        life3D.set(2, 4, 10);

        life3D.set(0, 2, 10);
        life3D.set(3, 2, 10);

        life3D.set(0, 1, 9);
        life3D.set(0, 3, 9);

        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);

        life3D.set(1, 0, 9);
        life3D.set(2, 0, 9);
        life3D.set(1, 4, 9);
        life3D.set(2, 4, 9);

        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);
        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);
        life3D.run(100, Life3D.b5s56Rule.fabricate());
    }


    private static void test2D() throws IOException {
        Life2D life = LifParser.newLifeGame("./lif/replicator.lif", 1000, "./out/replicator2D/" );
        life.run(1000, Life2D.highLifeFactory.fabricate() );
    }
}
