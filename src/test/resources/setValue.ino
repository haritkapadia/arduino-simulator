#include <stdio.h>
int line = 1;

void setup() {}

void loop() {
    switch(line) {
    case 1: pinMode(0, INPUT); break;
    case 2: digitalWrite(0, 0); break;
    case 3: digitalWrite(0, 1); break;
    case 4: pinMode(0, INPUT_PULLUP); break;
    case 5: pinMode(0, OUTPUT); break;
    case 6: digitalWrite(0, 0); break;
    case 7: digitalWrite(0, 1); break;
    case 8: digitalWrite(0, 100); break;
    }
    line += 1;
}
