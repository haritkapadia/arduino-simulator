package fakeArduino;

public class Pin {
    public int value = 0;
    // TODO verify these values
    public static final int INPUT = 0;
    public static final int INPUT_PULLUP = 1;
    public static final int OUTPUT = 2;
    public static final int DIGITAL_MODE = 0;
    public static final int ANALOG_MODE = 1;
    private int pinMode = INPUT;

    public int getPinMode() {
        return pinMode;
    }

    public void setPinMode(int pinMode) {
        this.pinMode = pinMode;
    }
    
    public int getValue(int readMode) {
        if(pinMode == OUTPUT) {
            if(readMode == DIGITAL_MODE)
                return value == 0 ? 0 : 1;
            else if(readMode == ANALOG_MODE)
                return value;
            else
                throw new UnsupportedOperationException("readMode was not set to DIGITAL_MODE or ANALOG_MODE");
        } else {
            // TODO find out what happens
            return value;
        }
    }

    public void setValue(int value, int writeMode) {
        switch(pinMode) {
        case INPUT:
            // TODO verify this is supported past Arduino 1.0.1
            // See https://www.arduino.cc/en/Tutorial/DigitalPins
            if(value != 0 && writeMode == DIGITAL_MODE)
                setPinMode(INPUT_PULLUP);
            break;
        case INPUT_PULLUP:
            // TODO find out what happens
            break;
        case OUTPUT:
            if(writeMode == DIGITAL_MODE)
                this.value = value == 0 ? 0 : 1;
            else if(writeMode == ANALOG_MODE)
                this.value = value;
            else
                throw new UnsupportedOperationException("writeMode was not set to DIGITAL_MODE or ANALOG_MODE");
            break;
        default:
            throw new UnsupportedOperationException("pinMode was not set to INPUT, INPUT_PULLUP, or OUTPUT");
        }
    }

    public String toString() {
        return "Pin[" + value + "]";
    }
}
