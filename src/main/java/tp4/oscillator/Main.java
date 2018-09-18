package tp4.oscillator;

import utils.PointDumper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<Double> numericSolutions = new ArrayList<>();
        double step = 0.000001;
        double mass = 70, gamma = 100, k = 10000, amplitude = 1;
        for (double i = step; i < 5; i+=step) {
            double yPos = analyticSolution(gamma, mass, k, i, amplitude);
           // dumper.print2D(0, yPos);
            //dumper.dump(i);
            numericSolutions.add(yPos);
        }
        dumper.dumpOscilatorAproximation(numericSolutions);
        numericSolutions.clear();
        dumper = new PointDumper("./tp4/gearPred_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        GearPredictorCorrector.FiveCoefficients coefficients = GearPredictorCorrector.initialConditionsGearPredictor(amplitude, gamma, mass, k);

        for ( double t = step; t < 5; t += step) {
            coefficients = GearPredictorCorrector.nextStep(coefficients, step, (pos, vel) -> (- k * pos - gamma * vel) / mass);
//            dumper.print2D(0, coefficients.r0);
  //          dumper.dump(t);
            numericSolutions.add(coefficients.r0);
        }
        dumper.dumpOscilatorAproximation(numericSolutions);
        numericSolutions.clear();
        dumper = new PointDumper("./tp4/beeman_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        Beeman.BeemanState state = Beeman.initialConditions(amplitude, - gamma / (2.0 * mass), step, k, gamma, mass);
        for (double t = step; t < 5; t += step) {
            state = Beeman.nextStep(state, step, (pos, vel) -> (- k * pos - gamma * vel) / mass);
            //dumper.print2D(0, state.xCurrent);
            //dumper.dump(t);
            numericSolutions.add(state.xCurrent);

        }
        dumper.dumpOscilatorAproximation(numericSolutions);
        numericSolutions.clear();

        dumper = new PointDumper("./tp4/verlet_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        VelocityVerlet.VerletState state2 = VelocityVerlet.initialState(amplitude, - gamma / (2.0 * mass));
        for (double t = step; t < 5; t += step) {
            state2 = VelocityVerlet.nextStep(state2, step, (pos, vel) -> (- k * pos - gamma * vel) / mass);
            //dumper.print2D(0, state2.x);
            //dumper.dump(t);
            numericSolutions.add(state2.x);

        }
        dumper.dumpOscilatorAproximation(numericSolutions);
        numericSolutions.clear();
    }
}
