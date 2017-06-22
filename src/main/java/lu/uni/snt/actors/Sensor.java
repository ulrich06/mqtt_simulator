package lu.uni.snt.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Cyril Cecchinel - I3S Laboratory on 22/06/2017.
 */
public class Sensor extends AbstractActor {

    private final int id;
    private final ActorRef mqttSender;
    private double value = 0.0;
    private long date = 0L;
    private boolean failure = false;

    public Sensor(int id, ActorRef mqttSender) {
        this.id = id;
        this.mqttSender = mqttSender;
    }

    static public Props props(int id, ActorRef mqttSender) {
        return Props.create(Sensor.class, () -> new Sensor(id, mqttSender));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Sense.class, x -> {
                    this.value = ThreadLocalRandom.current().nextDouble(x.min, x.max + 1);
                    this.date = System.currentTimeMillis() / 1000;
                    if ((new Random().nextDouble()) < x.failureRate)
                        this.failure = true;
                })
                .match(Send.class, x -> mqttSender.tell(new SensorValue(id, value, date, failure), getSelf()))
                .build();
    }

    static public class SensorValue {

        public final int id;
        public final double value;
        public final long date;
        public boolean failure = false;

        public SensorValue(int id, double value, long date) {
            this.id = id;
            this.value = value;
            this.date = date;
        }

        public SensorValue(int id, double value, long date, boolean failure) {
            this(id, value, date);
            this.failure = failure;


        }

        @Override
        public String toString() {
            return id + " - " + value + " - " + date;
        }

        public String format() {
            return id + ";" + value + ";" + date;
        }
    }

    static public class Sense {
        private final double max;
        private final double failureRate;
        private final double min;

        public Sense(double min, double max, double failureRate) {
            this.max = max;
            this.min = min;
            this.failureRate = failureRate;

        }
    }

    static public class Send {
        public Send() {
        }
    }
}
