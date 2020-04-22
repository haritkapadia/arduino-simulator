package fakeArduino;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.net.*;

public class AppTest {
    @Test public void testCompile() throws IOException {
        try {
            App.compile(new File(AppTest.class.getResource("/test.ino").toURI()));
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
