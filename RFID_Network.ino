#include <WiFi.h>
#include <HTTPClient.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SH1106.h>
#include <SPI.h>
#include <MFRC522.h>
#include "RTClib.h"
#include <Servo.h>



#define RST_PIN         14          
#define SS_PIN          12         



#define OLED_SDA 21
#define OLED_SCL 22
Servo myservo;
RTC_DS1307 rtc;
Adafruit_SH1106 display(21, 22);
MFRC522 mfrc522(SS_PIN, RST_PIN);  
int LED = 13; 
int Pin = 4;  
int hasObstacle = HIGH; 
int pos = 90;
long ctimer=0;
long cdelay=1000;
String dt,tim;
  int i=1;
char daysOfTheWeek[7][12] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

long tdelay=1000;
long ttime=0;

const char* ssid = "5T";
const char* password = "fcb12345";
 int c=0;
 String he="";
 String rf="";
 byte readCard[10];
void setup() {
  
  Serial.begin(115200);                                          
                                                
  mfrc522.PCD_Init();
   SPI.begin();                                             
   pinMode(LED, OUTPUT);
  pinMode(Pin, INPUT);
    myservo.attach(2);
   display.begin(SH1106_SWITCHCAPVCC, 0x3C);  

  display.display();
 delay(2000);

  // Clear the buffer.
  display.clearDisplay();
  //////////////////////
  delay(4000);  
 
  WiFi.begin(ssid, password); 
 
  while (WiFi.status() != WL_CONNECTED) { //Check for the connection
    delay(1000);
    //display.clearDisplay();
    Serial.println("Connecting to WiFi..");
    display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0,0);
  display.println("Connecting to WiFi..");
  display.display();
  display.clearDisplay();
   
  }
  Serial.println("Connected to the WiFi network");
   display.clearDisplay();
 display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0,0);
  display.print("Connected to the WiFi network:");
   display.println(ssid);
  display.display();
  delay(2000);
  display.clearDisplay();
  
   if (! rtc.begin()) {
    Serial.println("Couldn't find RTC");
    while (1);
  }
  Serial.print(F(__DATE__));
  Serial.print(" ");
  Serial.println(F(__TIME__));
  rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));

  if (! rtc.isrunning()) {
    Serial.println("RTC is NOT running!");
    // following line sets the RTC to the date & time this sketch was compiled
    rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));
  }
  delay(3000);
  }
void RunClock(){
  DateTime now = rtc.now();
  String yer=String(now.year(), DEC);
  String mon=String(now.month(), DEC);
  if(mon.length()!=2){mon="0"+mon;}
  String da=String(now.day(), DEC);
   if(da.length()!=2){da="0"+da;}
  String hr=String(now.hour(), DEC);
   if(hr.length()!=2){hr="0"+hr;}
  String mint=String(now.minute(), DEC);
   if(mint.length()!=2){mint="0"+mint;}
  String sec=String(now.second(), DEC);
   if(sec.length()!=2){sec="0"+sec;}
  dt=da+"-"+mon+"-"+yer ; tim=hr+":"+mint+":"+sec;
   //display.clearDisplay();
 
   // delay(2000);
  //display.clearDisplay();
    
  Serial.println(dt);
  Serial.println(tim);

}
void getDelay(){
if(millis()-ttime>1000){
 RunClock();
 ttime=millis();
}
}


int getID() {
  // Getting ready for Reading PICCs
  if ( ! mfrc522.PICC_IsNewCardPresent()) { //If a new PICC placed to RFID reader continue
    return 0;
  }
  if ( ! mfrc522.PICC_ReadCardSerial()) { //Since a PICC placed get Serial and continue
    return 0;
  }
  // There are Mifare PICCs which have 4 byte or 7 byte UID care if you use 7 byte PICC
  Serial.println("Scanned PICC's UID:");
  for (int i = 0; i < mfrc522.uid.size; i++) {  // 
    readCard[i] = mfrc522.uid.uidByte[i];
   he=String(readCard[i], HEX);
   he.toUpperCase();
    Serial.print(he);
  }
  Serial.println("");
  mfrc522.PICC_HaltA(); // Stop reading
  return 1;
}

 
void loop() {
  
 getDelay();
  hasObstacle = digitalRead(Pin);  
  if (hasObstacle == HIGH)
  {
    Serial.println("Stop something is ahead!!");
    digitalWrite(LED, HIGH);
    delay(1000);
    myservo.write(90); 
    
  }
  
 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status

   HTTPClient http;   
// if(mfrc522.PICC_IsNewCardPresent()) 
   http.begin("http://192.168.43.216:8080/ArdServer?");  //Specify destination for HTTP request
   http.addHeader("Content-Type", "text/plain");             //Specify content-type header
   int httpResponseCode=0;
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Serial.println(httpResponseCode);
    
      // Serial.print(he);
    getID();
    Serial.print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\/////////////////////\\\\\\\\\\\\\\\\\\\\\\///////////==");
    Serial.println(he);
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
if(he.length() !=0){
    httpResponseCode = http.POST(he);  
}
/*else{

  String st = String(i);
  httpResponseCode = http.POST(st); 
  i++;
  }*/
   if(httpResponseCode>0){
 
    String response = http.getString();                       //Get the response to the request
 
    Serial.println(httpResponseCode);   //Print return code
    Serial.println(response);           //Print request answer
 display.clearDisplay();
 display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0,0);
 // display.println(httpResponseCode);
   display.println(response);
   // display.println(c);
  display.display();
  if(httpResponseCode==20 && hasObstacle == HIGH){
    digitalWrite(LED, LOW);
    myservo.write(180); 
    delay(2000);
    }
  c++;
  he="";
   delay(2500); 
   display.clearDisplay();
   }else{
 
    Serial.print("Error on sending POST: ");
    Serial.println(httpResponseCode);
    if(httpResponseCode==0){
    
 display.setTextSize(1);
  display.setTextColor(WHITE);
    display.setCursor(80,0);
   display.print(tim);
   display.setCursor(0,0);
   display.print(dt);
   //display.display();
  display.setCursor(0,30);
  display.println("Place RFID card on\nSensor!");
   //display.println(response);
    //display.println(c);
  display.display();
  display.clearDisplay();
  //readrf();
  }
   if(httpResponseCode== -1){
    display.clearDisplay();
 display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(80,0);
   display.print(tim);
   display.setCursor(0,0);
   display.print(dt);
   //display.display();
  display.setCursor(0,30);
  display.setCursor(0,30);
  display.println("Server Offline!");
   //display.println(response);
    //display.println(c);
  display.display();
  display.clearDisplay();}
 
   }
 
    // http.end();//Free resources
 
 }else{
 
    Serial.println("Error in WiFi connection");   
  display.clearDisplay();
 display.setTextSize(1);
 display.setCursor(80,0);
   display.print(tim);
   display.setCursor(0,0);
   display.print(dt);
   //display.display();
  display.setCursor(0,30);
  display.setTextColor(WHITE);
  display.setCursor(0,20);
  display.println("Error in WiFi connection\n\nPress Reset Button");
   //display.println(response);
    //display.println(c);
  display.display();
   digitalWrite(LED, LOW);
    myservo.write(180); 
  
 }
 
 // delay(1000);  
 
}

