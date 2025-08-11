// Elliot Maringer

public class TransitionResult {
    private final String nextState;
    private final String writeSymbol;
    private final char direction;

    public TransitionResult(String nextState, String writeSymbol, char direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }

    public String getNextState() {
        return nextState;
    }

    public String getWriteSymbol() {
        return writeSymbol;
    }

    public char getDirection() {
        return direction;
    }

    
}
