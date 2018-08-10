package tp2;

public class Main {

    public static void main(String[] args) {
        Life2D life2D = new Life2D(5);
        life2D.set(1,2);
        life2D.set(2,2);
        life2D.set(3,2);
        life2D.run(5);
    }
}
