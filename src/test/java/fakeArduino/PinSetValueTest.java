package fakeArduino;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

public class PinSetValueTest {
    @Test
    public void testSetValue() throws IOException {
        try {
            App.compile(new File(AppTest.class.getResource("/setValue.ino").toURI()));
            JNIArduino a = new JNIArduino();
            a.init();
            a.setup();
            Pin testPin = a.getPin(0);
            testPin.value = -1;
            a.loop(); // setPinMode(INPUT);
            a.loop(); // digitalWrite(0, 0);
            assertEquals(Pin.INPUT, testPin.getPinMode());
            a.loop(); // digitalWrite(0, 1);
            // testPin.setValue(1, 0);
            assertEquals(Pin.INPUT_PULLUP, testPin.getPinMode());
            a.loop(); // setPinMode(INPUT_PULLUP);
            a.loop(); // setPinMode(OUTPUT);
            a.loop(); // digitalWrite(0, 0);
            assertEquals(0, testPin.value);
            a.loop(); // digitalWrite(0, 1);
            assertEquals(1, testPin.value);
            a.loop(); // digitalWrite(0, 100);
            assertEquals(1, testPin.value);
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
