package tp4;

public class GearPredictorCorrector {

    static class FiveCoefficients {
        double r0;
        double r1;
        double r2;
        double r3;
        double r4;
        double r5;
    }

    public static double evalTaylor5(
            double a, double b, double c, double d, double e, double f, double deltaT
    ) {
        return a + b * deltaT + c * Math.pow(deltaT, 2) / 2.0 + d * Math.pow(deltaT, 3) / 6.0 + e * Math.pow(deltaT, 4) / 24.0 + f * Math.pow(deltaT, 5) / 120.0;
    }

    public static double correct(
            double pred, double alfa, double deltaR2, double deltaT, double q, double qFact
    ) {
        return pred + alfa * deltaR2 * qFact / Math.pow(deltaT, q);
    }

    public static FiveCoefficients nextStep(
            FiveCoefficients q, double deltaT, double k, double gamma, double mass
    ) {
        double r0Pred = evalTaylor5(q.r0, q.r1, q.r2, q.r3, q.r4, q.r5, deltaT);
        double r1Pred = evalTaylor5(q.r1, q.r2, q.r3, q.r4, q.r5, 0, deltaT);
        double r2Pred = evalTaylor5(q.r2, q.r3, q.r4, q.r5, 0, 0, deltaT);
        double r3Pred = evalTaylor5(q.r3, q.r4, q.r5, 0, 0, 0, deltaT);
        double r4Pred = evalTaylor5(q.r4, q.r5, 0, 0, 0, 0, deltaT);
        double r5Pred = evalTaylor5(q.r5, 0, 0, 0, 0, 0, deltaT);

        double a = (- k * r0Pred - gamma * r1Pred) / mass;
        double deltaR2 = (a - r2Pred) * Math.pow(deltaT, 2) / 2;

        FiveCoefficients rCorrected = new FiveCoefficients();

        rCorrected.r0 = correct(r0Pred, 3.0 / 16.0, deltaR2, deltaT, 0, 1);
        rCorrected.r1 = correct(r1Pred, 251.0 / 360.0, deltaR2, deltaT, 1, 1);
        rCorrected.r2 = correct(r2Pred, 1.0, deltaR2, deltaT, 2, 2);
        rCorrected.r3 = correct(r3Pred, 11.0 / 18.0, deltaR2, deltaT, 3, 6);
        rCorrected.r4 = correct(r4Pred, 1.0 / 6.0, deltaR2, deltaT, 4, 24);
        rCorrected.r5 = correct(r5Pred, 1.0 / 60.0, deltaR2, deltaT, 5, 120);
        return rCorrected;
    }

    public static FiveCoefficients initialConditionsGearPredictor(
            double amplitude, double gamma, double mass, double k
    ) {
        FiveCoefficients coefficients = new FiveCoefficients();
        coefficients.r0 = amplitude;
        coefficients.r1 = - gamma / (2.0 * mass);
        coefficients.r2 = (-k / mass) * coefficients.r0;
        coefficients.r3 = (-k / mass) * coefficients.r1;
        coefficients.r4 = (-k / mass) * coefficients.r2;
        coefficients.r5 = (-k / mass) * coefficients.r3;

        return coefficients;
    }
}
