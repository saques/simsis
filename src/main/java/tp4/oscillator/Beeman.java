package tp4.oscillator;

public class Beeman {

    static class BeemanState {
        double vCurrent, vPast, xCurrent, xPast, aCurrent, aPast;
    }

    public static  BeemanState initialConditions(
            double position, double initialVelocity, double deltaT, double k, double gamma, double mass
    ) {
        BeemanState state = new BeemanState();
        state.xPast = position - initialVelocity * deltaT;
        state.vPast = initialVelocity;
        state.aPast = (- k * state.xPast - gamma * state.vPast) / mass;
        state.xCurrent = position;
        state.vCurrent = initialVelocity;
        state.aCurrent = (-k * state.xCurrent - gamma * state.vCurrent) / mass;
        return state;
    }

    public static BeemanState nextStep(
            BeemanState current, double deltaT, Accelerator accelerator
    ) {
        double squaredDeltaT = Math.pow(deltaT, 2);

        double pastAccel = accelerator.accelerate(current.xPast, current.vPast);
        double currentAccel = accelerator.accelerate(current.xCurrent, current.vCurrent);

        current.xPast = current.xCurrent;
        current.vPast = current.vCurrent;
        current.xCurrent = current.xCurrent + current.vCurrent * deltaT + (2.0 / 3.0) * currentAccel * squaredDeltaT - (1.0 / 6.0) * pastAccel * squaredDeltaT;

        double vPredicted = current.vCurrent + 1.5 * currentAccel * deltaT - .5 * pastAccel * deltaT;

        double futureAccel = accelerator.accelerate(current.xCurrent, vPredicted);
        current.vCurrent = current.vCurrent + (1.0 / 3.0) * futureAccel * deltaT + (5.0 / 6.0) * currentAccel * deltaT  - (1.0 / 6.0) * pastAccel * deltaT;

        return current;
    }
}
