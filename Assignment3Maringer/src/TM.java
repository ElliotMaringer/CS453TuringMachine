// Elliot Maringer

import java.util.*;
import java.lang.*;

public class TM {
    private Set<String> states = new HashSet<>();
    private List<String> inputAlphabet = new ArrayList<>();
    private List<String> tapeAlphabet = new ArrayList<>();
    private Map<TransitionKey, TransitionResult> transitionFunction = new HashMap<>();
    private String initialState;
    private String acceptState;
    private String rejectState;

    public void setStates(Set<String> states) {
        this.states = states;
    }
    public void setInputAlphabet(List<String> inputAlphabet) {
        this.inputAlphabet = new ArrayList<>(inputAlphabet);
    }
    public void setTapeAlphabet(List<String> tapeAlphabet) {
        this.tapeAlphabet = new ArrayList<>(tapeAlphabet);
    }
    public void setInitialState(String initialState) {
        this.initialState = initialState;
        states.add(initialState);
    }
    public void setAcceptState(String acceptState) {
        this.acceptState = acceptState;
        states.add(acceptState);
    }
    public void setRejectState(String rejectState) {
        this.rejectState = rejectState;
        states.add(rejectState);
    }

    public void addTransition(TransitionKey key, TransitionResult result) {
        transitionFunction.put(key,result);
        states.add(key.getState());
        states.add(result.getNextState());
    }

    public Set<String> getStates() {
        return new HashSet<>(states);
    }

    public List<String> getInputAlphabet() {
        return new ArrayList<>(inputAlphabet);
    }

    public List<String> getTapeAlphabet() {
        return new ArrayList<>(tapeAlphabet);
    }

    public String getInitialState() {
        return initialState;
    }

    public String getAcceptState() {
        return acceptState;
    }

    public String getRejectState() {
        return rejectState;
    }

    public Map<TransitionKey, TransitionResult> getTransitionFunction() {
        return new HashMap<>(transitionFunction);
    }


    public void printInfo() {
        List<String> sortedStates = new ArrayList<>(states);
        Collections.sort(sortedStates);

        List<String> inputAlpha = new ArrayList<>(inputAlphabet);
        List<String> tapeAlpha = new ArrayList<>(tapeAlphabet);

        System.out.println("States: " + formatSet(states));
        System.out.println("Input alphabet: " + formatSet(inputAlphabet));
        System.out.println("Tape alphabet: " + formatSet(tapeAlphabet));
        System.out.println("Transition Table:\n");

        final int CELL_WIDTH = 18;

        // Compute max state label width (state + markers)
        int maxLabelWidth = 0;
        for (String state : sortedStates) {
            String label = "";
            if (state.equals(initialState)) label += "->";
            if (state.equals(acceptState)) label += "*";
            if (state.equals(rejectState)) label += "!";
            label += state;
            maxLabelWidth = Math.max(maxLabelWidth, label.length());
        }

        final int LABEL_WIDTH = maxLabelWidth + 2; // add spacing buffer

        // Header row: k | symbol1 symbol2 ...
        System.out.print(String.format("%" + LABEL_WIDTH + "s", inputAlphabet.size() + " |"));
        for (String symbol : tapeAlpha) {
            System.out.printf(" %" + CELL_WIDTH + "s", symbol);
        }
        System.out.println();

        // Print transition rows
        for (String state : sortedStates) {
            String prefix = "";
            if (state.equals(initialState)) prefix += "->";
            if (state.equals(acceptState)) prefix += "*";
            if (state.equals(rejectState)) prefix += "!";
            String label = prefix + state;

            System.out.print(String.format("%" + LABEL_WIDTH + "s", label + " |"));

            for (String symbol : tapeAlpha) {
                TransitionResult result = transitionFunction.get(new TransitionKey(state, symbol));
                if (result == null) {
                    System.out.printf(" %" + CELL_WIDTH + "s", "-");
                } else {
                    String entry = String.format("(%s,%s,%s)", result.getNextState(), result.getWriteSymbol(), result.getDirection());
                    System.out.printf(" %" + CELL_WIDTH + "s", entry);
                }
            }
            System.out.println();
        }

        System.out.println("\nInitial State: " + initialState);
        System.out.println("Accept State: " + acceptState);
        System.out.println("Reject State: " + rejectState);
    }



    public void runTM(String input, int moveLimit) {
        System.out.println("Running on input [" + input + "] with limit " + moveLimit + ":");
        
        TMConfig config = new TMConfig();
        config.currentState = initialState;
        config.headPosition = 0;
        config.tape = input.length() == 0 ? "_" : input;

        int moves = 0;

        while (moves <= moveLimit) {
            printConfig(config);
            
            if (config.currentState.equals(acceptState)) {
                System.out.println("ACCEPT");
                return;
            } else if (config.currentState.equals(rejectState)) {
                System.out.println("REJECT");
                return;
            }

            if (moves == moveLimit) {
                System.out.println("LIMIT");
                return;
            }

            String readSymbol = matchTapeSymbol(config.tape, config.headPosition);
            TransitionKey key = new TransitionKey(config.currentState, readSymbol);
            TransitionResult result = transitionFunction.get(key);

            if (result == null) {
                config.currentState = rejectState;
                continue;
            }

            // Modify tape
            StringBuilder tapeBuilder = new StringBuilder(config.tape);
            int symbolLength = readSymbol.length();

            // Extend tape if needed
            while (config.headPosition + symbolLength > tapeBuilder.length()) {
                tapeBuilder.append('_');
            }

            // Replace full matched symbol with the full write symbol
            tapeBuilder.delete(config.headPosition, config.headPosition + symbolLength);
            String writeSymbol = result.getWriteSymbol();
            tapeBuilder.insert(config.headPosition, writeSymbol);
            config.tape = tapeBuilder.toString();

            int newHead;
            if (result.getDirection() == 'R') {
                newHead = config.headPosition + writeSymbol.length();
            } else {
                newHead = moveLeftToPreviousSymbol(config.tape, config.headPosition);
            }

            config.headPosition = newHead;
            config.currentState = result.getNextState();
            moves++;
        }
    }

    private void printConfig(TMConfig config) {
        StringBuilder tape = new StringBuilder(config.tape);
        while (config.headPosition > tape.length()) {
            tape.append('_');
        }

        // ⬇Add one trailing blank if head is at the end 
    boolean willInsertAtEnd = (config.headPosition == config.tape.length());

        // Clean insertion of state before full symbol under head
        String stateMarker = "<" + config.currentState + ">";
        tape.insert(config.headPosition, stateMarker);

    if (willInsertAtEnd) {
        tape.append('_');
    }

        System.out.println("Config: " + tape.toString());
    }

    public String simulate(String input, int moveLimit) {
        TMConfig config = new TMConfig();
        config.currentState = initialState;
        config.headPosition = 0;
        config.tape = input.length() == 0 ? "_" : input;

        int moves = 0;

        while (moves <= moveLimit) {
            if (config.currentState.equals(acceptState)) {
                return "ACCEPT";
            } else if (config.currentState.equals(rejectState)) {
                return "REJECT";
            }

            if (moves == moveLimit) {
                return "LIMIT";
            }

            String readSymbol = matchTapeSymbol(config.tape, config.headPosition);
            TransitionKey key = new TransitionKey(config.currentState, readSymbol);
            TransitionResult result = transitionFunction.get(key);

            if (result == null) {
                return "REJECT";
            }

            // Modify tape
            StringBuilder tapeBuilder = new StringBuilder(config.tape);
            int symbolLength = readSymbol.length();

            // Extend tape if needed
            while (config.headPosition + symbolLength > tapeBuilder.length()) {
                tapeBuilder.append('_');
            }

            // Replace full matched symbol with the full write symbol
            tapeBuilder.delete(config.headPosition, config.headPosition + symbolLength);
            String writeSymbol = result.getWriteSymbol();
            tapeBuilder.insert(config.headPosition, writeSymbol);
            config.tape = tapeBuilder.toString();

            // Move head with full symbol-awareness
            int newHead;
            if (result.getDirection() == 'R') {
                newHead = config.headPosition + writeSymbol.length();
            } else {
                // Move to the start of the previous symbol
                newHead = moveLeftToPreviousSymbol(config.tape, config.headPosition);
            }

            config.headPosition = newHead;
            config.currentState = result.getNextState();
            moves++;
        }

        return "LIMIT";
    }



    public void runLanguage(int lengthLimit, int moveLimit) {
        List<String> accepted = new ArrayList<>();
        List<String> undertermined = new ArrayList<>();

        Set<String> alphabet = new HashSet<>(inputAlphabet);
        List<String> strings = GenerateString.generateAllStrings(alphabet, lengthLimit);

        for (String input : strings) {
            String result = simulate(input, moveLimit);
            if (result == "ACCEPT") {
                accepted.add(input);
            } else if (result == "LIMIT") {
                undertermined.add(input);
            }
        }

        System.out.println("L(M) = {");
        for (String s : accepted) {
            System.out.println("\t" + (s.isEmpty() ? "\"\"" : s + ","));
        }
        System.out.println("\t...");
        System.out.println("}");

        if (!undertermined.isEmpty()) {
            System.out.println("Undetermined string (due to limit): ");
            for (String s : undertermined) {
                System.out.println("\t" + (s.isEmpty() ? "\"\"" : s));
            }
        }
    }

    
    private String formatSet(Collection<String> items) {
        List<String> sorted = new ArrayList<>(items);
        Collections.sort(sorted);
        return "{" + String.join(",", sorted) + "}";
    }
    
    private String matchTapeSymbol(String tape, int pos) {
        for (String symbol : tapeAlphabet) {
            if (pos + symbol.length() <= tape.length() &&
                tape.substring(pos, pos + symbol.length()).equals(symbol)) {
                return symbol;
            }
        }
        return "_";  // fallback if nothing matches
    }

    private int moveLeftToPreviousSymbol(String tape, int currentPos) {
    for (int i = currentPos - 1; i >= 0; i--) {
        for (String symbol : tapeAlphabet) {
            if (i + symbol.length() == currentPos &&
                currentPos <= tape.length() &&
                tape.startsWith(symbol, i)) {
                return i;
            }
        }
    }
    return 0;  // fallback
}


}
