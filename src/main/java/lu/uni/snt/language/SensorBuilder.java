package lu.uni.snt.language;

import akka.actor.ActorSystem;
import lu.uni.snt.threads.SensorThread;

/**
 * Created by Cyril Cecchinel - I3S Laboratory on 22/06/2017.
 */
public class SensorBuilder {

    private int period;
    private double max;
    private double min;
    private double failureRate = 0.0;
    private ActorSystem system;
    private int id;


    public static SensorBuilder newBuilder() {
        return new SensorBuilder();
    }

    public SensorBuilder withPeriod(int period) {
        this.period = period;
        return this;
    }

    public SensorBuilder setMax(double max) {
        this.max = max;
        return this;
    }

    public SensorBuilder setMin(double min) {
        this.min = min;
        return this;
    }

    public SensorBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public SensorBuilder withFailureRate(double failureRate) {
        this.failureRate = failureRate;
        return this;
    }

    public SensorBuilder onSystem(ActorSystem system) {
        this.system = system;
        return this;
    }

    public void start() {
        new Thread(new SensorThread(system, id, period, max, min, failureRate)).start();
    }
}
