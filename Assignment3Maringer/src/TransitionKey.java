// Elliot Maringer

import java.util.*;

public class TransitionKey {
    private final String state;
    private final String tapeSymbol;

    public TransitionKey(String state, String tapeSymbol) {
        this.state = state;
        this.tapeSymbol = tapeSymbol;
    }

    public String getState() {
        return state;
    }

    public String getTapeSymbol() {
        return tapeSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransitionKey)) {
            return false;
        }
        TransitionKey key = (TransitionKey) o;
        if (state.equals(key.state) && (tapeSymbol.equals(key.tapeSymbol))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, tapeSymbol);
    }
    
    
}
