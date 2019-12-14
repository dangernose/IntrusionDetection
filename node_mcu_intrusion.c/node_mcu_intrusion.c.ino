#include <SPI.h>

#include <PubSubClient.h>

#include <ESP8266WiFi.h>

#ifndef STASSID
#define STASSID "beezoykc_wlink"
#define STAPSK  "CLFA650HEM"
#endif

const char* ssid     = STASSID;
const char* password = STAPSK;
char server[] = {"test.mosquitto.org"};
int port = 1883;
char topic[] = {"codifythings/lightcontrol"};
    

WiFiClient client; 

int ledPin =13;

void callback(char* topic, byte* payload, unsigned int length)
{
 // Print payload
 String payloadContent = String((char *)payload);
 Serial.println("[INFO] Payload: " + payloadContent);
 // Turn lights on/off
 turnLightsOnOff();
} 

PubSubClient pubSubClient(server, port, 0, client); 




void turnLightsOnOff()
{
 // Check if lights are currently on or off
 if(digitalRead(ledPin) == LOW)
 {
 //Turn lights on
 Serial.println("[INFO] Turning lights on");
 digitalWrite(ledPin, HIGH);
 }
 else
 {
 // Turn lights off
 Serial.println("[INFO] Turning lights off");
 digitalWrite(ledPin, LOW);
 }
} 
void setup()
{
 // Initialize serial port
 Serial.begin(115200);
 // Connect Arduino to internet
 //connectToInternet();
 connectto();
 // Set LED pin mode
 pinMode(ledPin, OUTPUT);
 //Connect MQTT Broker
// Serial.println("[INFO] Connecting to MQTT Broker");
// if (pubSubClient.connect("androidClient1"))
// {
// Serial.println("[INFO] Connection to MQTT Broker Successful");
// pubSubClient.subscribe(topic);
// }
// else
// {
// Serial.println("[INFO] Connection to MQTT Broker Failed");
// }
}


 
 
 void publishSensorData() 
 { 
   // Connect MQTT Broker 
   Serial.println("[INFO] Connecting to MQTT Broker"); 
 
 
 
  if (pubSubClient.connect( "androidClient1" )) 
   { 
 
 
 
     Serial.println("[INFO] Connection to MQTT Broker Successful"); 
   } 
   else 
   { 
     Serial.println("[INFO] Connection to MQTT Broker Failed"); 
   }   
 
 
 
  // Publish to MQTT Topic 
   if (pubSubClient.connected()) 
   {     
     Serial.println("[INFO] Publishing to MQTT Broker"); 
     pubSubClient.publish(topic, "hey its done"); 
     Serial.println("[INFO] Publish to MQTT Broker Complete"); 
   } 
   else 
   { 
     Serial.println("[ERROR] Publish to MQTT Broker Failed"); 
   } 
 
 
 
  pubSubClient.disconnect(); 
 } 
void loop()
{
 publishSensorData();
     
}


void connectto(){
  
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  }


 
