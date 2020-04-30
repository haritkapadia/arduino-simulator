package fakeArduino;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

public class AppTest {
    @Test
    public void testCompile() throws IOException {
        try {
            int returnCode = App.compile(new File(AppTest.class.getResource("/test.ino").toURI()));
            assertEquals(returnCode, 0);
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
