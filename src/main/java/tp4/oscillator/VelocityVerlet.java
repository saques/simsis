package tp4.oscillator;

import lombok.AllArgsConstructor;

public class VelocityVerlet {

    @AllArgsConstructor
    static class VerletState {
        double x, v;
    }

    static VerletState initialState(double x0, double v0) {
        return new VerletState(x0, v0);
    }

    static VerletState nextStep(VerletState state, double deltaTime, Accelerator accelerator) {

        double accelCurrent = accelerator.accelerate(state.x, state.v);
        double velocityHalfStep = state.v + 0.5 * accelCurrent * deltaTime;
        state.x = state.x + velocityHalfStep * deltaTime;

        double accelFuture = accelerator.accelerate(state.x, velocityHalfStep);
        state.v = velocityHalfStep + 0.5 * accelFuture * deltaTime;

        return state;

    }
}
