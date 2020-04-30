package fakeArduino;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

public class PinPinModeTest {
    @Test
    public void testPinMode() throws IOException {
        try {
            int returnCode = App.compile(new File(AppTest.class.getResource("/pinMode.ino").toURI()));
            JNIArduino a = new JNIArduino();
            a.init();
            a.setup();
            Pin testPin = a.getPin(0);
            a.loop(); // pinMode(0, INPUT);
            assertEquals(Pin.INPUT, testPin.getPinMode());
            a.loop(); // pinMode(0, INPUT_PULLUP);
            assertEquals(Pin.INPUT_PULLUP, testPin.getPinMode());
            a.loop(); // pinMode(0, OUTPUT);
            assertEquals(Pin.OUTPUT, testPin.getPinMode());
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }        
    }
}
