package com.suresh.intrusiondetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateView("");

        try{
            MQTTClient client = new MQTTClient(this);
            client.connectToMQTT();
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void updateView(String sensorMessage){
        try{
            SharedPreferences sharedPref = getSharedPreferences(
                    "PREFERENCE_FILE_KEY", Context.MODE_PRIVATE
            );
            if(sensorMessage == null || sensorMessage == ""){
                sensorMessage = sharedPref.getString(
                        "lastSensorMessage",
                        "No Activity Detected"
                );
            }
            final String tempSensorMessage = sensorMessage;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView updateField = (TextView) findViewById(R.id.updated_field);
                    updateField.setText(tempSensorMessage);
                }
            });
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("lastSensorMessage",sensorMessage);
            editor.commit();
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
        }

    }

    public void createNotification(String notificationTitle, String notificationMessage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_rodentia_icons_walking)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage);

        Intent resultIntent = new Intent(getApplicationContext(),MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100,mBuilder.build());
    }
}
