package com.suresh.intrusiondetection;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DateFormat;
import java.util.Date;

public class MQTTClient {

    private static final String TAG = "MQTTClient";
    private String mqttBroker = "tcp://test.mosquitto.org:1883";
    private String mqttTopic = "codifythings/lightcontrol";
    private String deviceId = "androidClient3";

    private MainActivity activity = null;

    public MQTTClient(MainActivity activity) {
        this.activity = activity;
    }

    public void connectToMQTT() throws MqttException{
        Log.i(TAG,"Setting Connection Options");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        Log.i(TAG, "Creating New Client");
        MqttClient client = new MqttClient(mqttBroker,deviceId,new MemoryPersistence());
        client.connect(options);

        Log.i(TAG,"Subscribing To Topic");
        client.setCallback(new MqttEventCallback());
        client.subscribe(mqttTopic,0);
    }

    private class MqttEventCallback implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            // Doing Nothing
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG,"New Message Arrived from Topic - " + topic);

            try{
                DateFormat df = DateFormat.getDateTimeInstance();
                String sensorMessage = new String(message.getPayload()) + " @ "
                + df.format(new Date());

                Log.d("MQTT Client Message",new String(message.getPayload()));

                activity.createNotification("Intrusion Detection System",sensorMessage);
                activity.updateView(sensorMessage);
            }catch (Exception ex){
                Log.e(TAG,ex.getMessage());
            }


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }
}
