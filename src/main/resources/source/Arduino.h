#ifndef _Included_Arduino
#define _Included_Arduino

#define LOW  0
#define HIGH 1

void setup(void);
void loop(void);

int digitalRead(int);
void digitalWrite(int, int);
void exitArduino();

void delay(int);
void delayMicroseconds(int);

#endif
