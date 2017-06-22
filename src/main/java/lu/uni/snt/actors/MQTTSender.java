package lu.uni.snt.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Cyril Cecchinel - I3S Laboratory on 22/06/2017.
 */
public class MQTTSender extends AbstractActor {

    public MQTTSender() {
    }

    static public Props props() {
        return Props.create(MQTTSender.class, MQTTSender::new);
    }

    public void send(Sensor.SensorValue value) {
        try {
            MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage(value.format().getBytes());
            if (value.failure)
                client.publish("admin", message);
            else
                client.publish("info", message);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(Sensor.SensorValue.class, value -> {
                    System.out.println("Received: " + value);
                    send(value);
                })
                .build();
    }


}
