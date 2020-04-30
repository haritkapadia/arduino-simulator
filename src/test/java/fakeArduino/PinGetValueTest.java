package fakeArduino;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

public class PinGetValueTest {
    @Test
    public void testGetValue() throws IOException {
        try {
            App.compile(new File(AppTest.class.getResource("/getValue.ino").toURI()));
            JNIArduino a = new JNIArduino();
            a.init();
            a.setup();
            // Hijack System.out
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();    
            System.setOut(new java.io.PrintStream(out));
            Pin testPin = a.getPin(0);
            testPin.setPinMode(Pin.OUTPUT);
            testPin.setValue(0, Pin.ANALOG_MODE);
            a.loop(); // digitalRead(0);
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
