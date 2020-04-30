#ifndef _Included_Arduino
#define _Included_Arduino

#include <stdbool.h>

#define LOW  0
#define HIGH 1
#define INPUT 0
#define INPUT_PULLUP 1
#define OUTPUT 2

void setup(void);
void loop(void);

void pinMode(int, int);
void digitalWrite(int, int);
int digitalRead(int);
void exitArduino();

void delay(int);
void delayMicroseconds(int);

#endif
