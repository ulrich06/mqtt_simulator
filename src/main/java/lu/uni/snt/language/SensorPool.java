package lu.uni.snt.language;

import akka.actor.ActorSystem;

/**
 * Created by Cyril Cecchinel - I3S Laboratory on 22/06/2017.
 */
public class SensorPool {

    private int amount;
    private double max;
    private double min;
    private double failureRate;
    private ActorSystem system;
    private int period;

    public static SensorPool newPool() {
        return new SensorPool();
    }

    public SensorPool withPeriod(int period) {
        this.period = period;
        return this;
    }


    public SensorPool amount(int amount) {
        this.amount = amount;
        return this;
    }

    public SensorPool setMax(double max) {
        this.max = max;
        return this;
    }

    public SensorPool setMin(double min) {
        this.min = min;
        return this;
    }

    public SensorPool withFailureRate(double failureRate) {
        this.failureRate = failureRate;
        return this;
    }


    public SensorPool onSystem(ActorSystem system) {
        this.system = system;
        return this;
    }

    public void start() {
        for (int i = 0; i < amount; i++) {
            new SensorBuilder()
                    .setId(i)
                    .setMax(max)
                    .setMin(min)
                    .withFailureRate(failureRate)
                    .onSystem(system)
                    .withPeriod(period).start();
        }
    }

}
