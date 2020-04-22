package fakeArduino;

public class Pin {
    private int value = 0;
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }

    public String toString() {
        return "Pin[" + value + "]";
    }
}
