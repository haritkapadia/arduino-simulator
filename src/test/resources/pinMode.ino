#include <stdio.h>
int line = 1;

void setup() {}

void loop() {
    switch(line) {
    case 1: pinMode(0, INPUT); break;
    case 2: pinMode(0, INPUT_PULLUP); break;
    case 3: pinMode(0, OUTPUT); break;
    }
    line += 1;
}
