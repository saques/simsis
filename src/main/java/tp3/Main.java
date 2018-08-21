package tp3;

import tp1.Particle;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class Main {


    public static void main(String[] args) {
        double width = 1000, height = 1000, maxRadius = 10;
        ArrayList<Particle> list = new ArrayList<>();
        Random r = new Random(1L);


        for (int i = 0; i < 100; i++) {
            double x = r.nextDouble() * width, y = r.nextDouble() * height, radius = r.nextDouble() * maxRadius;
            list.add(new Particle(x, y, radius));
        }

        Board board = new Board(width, height, list);




    }
}
