#include <stdio.h>

int line = 1;
void setup() {}

void loop() {
    switch(line) {
    case 1: printf("%d\n", digitalRead(0)); break;
    }
    line += 1;
}
