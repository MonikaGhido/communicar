package it.unimore.iot;

import it.unimore.dipi.iot.wldt.engine.WldtConfiguration;
import it.unimore.dipi.iot.wldt.engine.WldtEngine;
import it.unimore.dipi.iot.wldt.worker.mqtt.Mqtt2MqttConfiguration;
import it.unimore.dipi.iot.wldt.worker.mqtt.Mqtt2MqttWorker;
import it.unimore.dipi.iot.wldt.worker.mqtt.MqttTopicDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MqttDtProcess {
    private static final Logger logger = LoggerFactory.getLogger(MqttDtProcess.class);
    private static final String SOURCE_BROKER_ADDRESS = "127.0.0.1";
    private static final int SOURCE_BROKER_PORT = 1883;

    private static final String DESTINATION_BROKER_ADDRESS = "127.0.0.1";
    private static final int DESTINATION_BROKER_PORT = 1884;

    private static final String DEVICE_ID = "vehicle001";

    private static Mqtt2MqttConfiguration mqttConf(){
        Mqtt2MqttConfiguration mqtt2MqttConfiguration = new Mqtt2MqttConfiguration();
        mqtt2MqttConfiguration.setBrokerAddress(SOURCE_BROKER_ADDRESS);
        mqtt2MqttConfiguration.setBrokerPort(SOURCE_BROKER_PORT);
        mqtt2MqttConfiguration.setDestinationBrokerAddress(DESTINATION_BROKER_ADDRESS);
        mqtt2MqttConfiguration.setDestinationBrokerPort(DESTINATION_BROKER_PORT);
        mqtt2MqttConfiguration.setDeviceId(DEVICE_ID);

        mqtt2MqttConfiguration.setTopicList(
                Arrays.asList(
                        new MqttTopicDescriptor("carTopic",
                                "carResourceId",
                                "cars/{{zoneId}}/#",
                                MqttTopicDescriptor.MQTT_TOPIC_TYPE_DEVICE_OUTGOING)
                )
        );

        return mqtt2MqttConfiguration;
    }

    public static void main(String[] args) {
        try {
            logger.info("Initializing Digital Twin ...");
            WldtConfiguration wldtConfiguration = new WldtConfiguration();
            wldtConfiguration.setDeviceNameSpace("it.unimore.iot.vehicles");
            wldtConfiguration.setWldtBaseIdentifier("wldt");
            wldtConfiguration.setWldtStartupTimeSeconds(5);
            wldtConfiguration.setApplicationMetricsEnabled(false);

            WldtEngine engine = new WldtEngine();
            Mqtt2MqttWorker mqttWorker = new Mqtt2MqttWorker(engine.getWldtId(), mqttConf());

            engine.addNewWorker(mqttWorker);
            engine.startWorkers();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
