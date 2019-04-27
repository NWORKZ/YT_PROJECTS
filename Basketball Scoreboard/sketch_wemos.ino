#include <WiFiServerSecure.h>
#include <WiFiClientSecure.h>
#include <WiFiClientSecureBearSSL.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <WiFiUdp.h>
#include <ESP8266WiFiType.h>
#include <CertStoreBearSSL.h>
#include <ESP8266WiFiAP.h>
#include <WiFiClient.h>
#include <BearSSLHelpers.h>
#include <WiFiServer.h>
#include <ESP8266WiFiScan.h>
#include <WiFiServerSecureBearSSL.h>
#include <ESP8266WiFiGeneric.h>
#include <ESP8266WiFiSTA.h>
#include <WiFiClientSecureAxTLS.h>
#include <WiFiServerSecureAxTLS.h>

#include <ESP8266HTTPClient.h>


const char* ssid = "netbridge";
const char* password = "12345678";

const int trigPin = D2;
const int echoPin = D8;

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(BUILTIN_LED, OUTPUT);
  while(WiFi.status() != WL_CONNECTED){
    delay(1000);
    Serial.print("Connecting.....");
  }


  //digitalWrite(trigPin,HIGH);
  Serial.print("Connected");
}


// the loop function runs over and over again forever
void loop() {
  HTTPClient http;


  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);  
  int duration = pulseIn(echoPin, HIGH);
  int distance = duration / 29 /2;
  
  if(distance >= 16 && distance <= 32){
      http.begin("http://192.168.0.123:5000/scored?willAdd=scored");
      http.GET();
  }
  
  delay(10);
  //http.end();
}
