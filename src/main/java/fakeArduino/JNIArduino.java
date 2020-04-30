package fakeArduino;

import java.io.File;
import java.util.*;

public class JNIArduino {
    Pin[] pins = new Pin[14];

    public JNIArduino() {
        System.loadLibrary("codelib");
        for(int i = 0; i < pins.length; i++)
            pins[i] = new Pin();
    }
    
    public native void init();
    public native void close();
    public native void setup();
    public native void loop();
    public native boolean getRunning();
    
    public Pin getPin(int i) {
        return pins[i];
    }

    public Pin[] getPins() {
        return pins;
    }

    public void onUpdate(){
        //TODO: Replace to make it interact with the interface
        System.out.println(Arrays.toString(pins));
    }
}
