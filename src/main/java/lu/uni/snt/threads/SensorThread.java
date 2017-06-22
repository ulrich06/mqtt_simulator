package lu.uni.snt.threads;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import lu.uni.snt.actors.Sensor;

/**
 * Created by Cyril Cecchinel - I3S Laboratory on 22/06/2017.
 */
public class SensorThread implements Runnable {

    private final int period;
    private final int id;
    private final ActorSystem system;
    private final ActorRef sensorActor;
    private final double max;
    private final double min;
    private final double failureRate;

    public SensorThread(ActorSystem system, int id, int period, double max, double min, double failureRate) {
        this.system = system;
        this.id = id;
        this.period = period;
        this.max = max;
        this.min = min;
        this.failureRate = failureRate;
        sensorActor = system.actorOf(Sensor.props(id, system.actorFor("akka://fake-sensors/user/mqttActor")), "sensorActor" + id);
    }

    @Override
    public void run() {
        while (true) {
            sensorActor.tell(new Sensor.Sense(min, max, failureRate), ActorRef.noSender());
            sensorActor.tell(new Sensor.Send(), ActorRef.noSender());

            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
