package tp2;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        test3D();
    }

    private static void test3D() throws IOException{
        Life3D life3D = new Life3D(5, "D:\\git\\simsis\\ovito\\life3D_");
        life3D.set(1,2, 0);
        life3D.set(2,2, 0);
        life3D.set(3,2, 0);
        life3D.run(42, Life3D.defaultRule);
    }


    private static void test2D() throws IOException {
        Life2D life2D = new Life2D(5, "D:\\git\\simsis\\ovito\\life2D_");
        life2D.set(1,2);
        life2D.set(2,2);
        life2D.set(3,2);
        life2D.run(5, Life2D.defaultRule);
    }
}
