package lu.uni.snt;

import akka.actor.ActorSystem;
import lu.uni.snt.actors.MQTTSender;
import lu.uni.snt.language.SensorBuilder;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("fake-sensors");
        system.actorOf(MQTTSender.props(), "mqttActor");

        SensorBuilder.newBuilder()
                .onSystem(system)
                .setId(15)
                .withPeriod(250)
                .setMin(0)
                .setMax(5)
                .withFailureRate(0.5)
                .start();

        /*SensorPool.newPool()
                .amount(500)
                .onSystem(system)
                .setMax(15)
                .setMin(0)
                .withPeriod(1000)
                .withFailureRate(0.1)
                .start();*/
    }
}
