// Elliot Maringer

import java.util.*;
import java.io.*;

public class TMConfig {
    String currentState;
    String tape;
    int headPosition;

    public TMConfig() {
        this.currentState = "";
        this.tape = "";
        this.headPosition = 0;
    }

    public TMConfig(String currentState, String tape, int headPositon) {
        this.currentState = currentState;
        this.tape = tape;
        this.headPosition = headPositon;
    }

    
}
