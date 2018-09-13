package tp4;

import lombok.AllArgsConstructor;
import tp1.Particle;
import utils.PointDumper;

import java.io.IOException;

public class Main {



    public static double analyticSolution(
            double gama,
            double mass,
            double k,
            double t,
            double amplitude
    ) {

        double temp = Math.cos(
                Math.sqrt( k/mass - Math.pow(gama / (2 * mass), 2)) * t
        );
        return amplitude * Math.exp(-(gama / (2 * mass)) * t) * temp;
    }



    public static void main(String [] args) throws IOException {

        PointDumper dumper = new PointDumper("./tp4/analytic_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        // Parameters: mass = 70kg, k = 10^4 N/m, gama = 100 kg/s tf = 5 s
        // Initial conditions: r(t=0) = 1 m v(t=0) = -C gama / (2m) m/s
        double mass = 70, gamma = 100, k = 10000, amplitude = 20;
        for (double i = 0; i < 10; i+=0.1) {
            double yPos = analyticSolution(gamma, mass, k, i, amplitude);
            dumper.print2D(0, yPos);
            dumper.dump(i);
        }

        dumper = new PointDumper("./tp4/gearPred_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GearPredictorCorrector.FiveCoefficients coefficients = GearPredictorCorrector.initialConditionsGearPredictor(amplitude, gamma, mass, k);

        for (double step = 0.01, t = step; t < 10; t += step) {
            coefficients = GearPredictorCorrector.nextStep(coefficients, step, k, gamma, mass);

            dumper.print2D(0, coefficients.r0);

            dumper.dump(t);
        }
    }
}