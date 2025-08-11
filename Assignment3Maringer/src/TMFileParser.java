// Elliot Maringer

import java.io.*;
import java.util.*;

public class TMFileParser {
    // Parse the given file into a TM instance
    public static TM parseFile(String filePath) throws IOException {
        // Print file we're reading from
        System.out.println("Attempting to read file from: " + filePath);
        // Open file for reading
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        TM tm = new TM();
        
        // Create containers for TM componenets
        String line;
        List<String> tapeAlphabet = new ArrayList<>();
        int inputAlphabetLength;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            //Parse alphabet header
            if (tapeAlphabet.isEmpty()) {
                String[] parts = line.split("\\|");
                inputAlphabetLength = Integer.parseInt(parts[0].trim());

                tapeAlphabet.addAll(Arrays.asList(parts[1].trim().split("\\s+")));

                tm.setInputAlphabet(tapeAlphabet.subList(0, inputAlphabetLength));
                tm.setTapeAlphabet(tapeAlphabet);
                continue;
            }

            if (line.contains("|")) {
                String[] segments = line.split("\\|");
                

                String statePart = segments[0].trim();
                

                String stateName = statePart.replace("->","").replace("*","").replace("!","").trim();
                
                if (statePart.contains("->")) {
                    tm.setInitialState(stateName);
                }
                
                if (statePart.contains("*")) {
                    tm.setAcceptState(stateName);
                }

                if (statePart.contains("!")) {
                    tm.setRejectState(stateName);
                }

                if (segments.length < 2) {
                    continue;
                }
                
                String transPart = segments[1].trim();
                // Determine whether to use deault or adjacency list
                if (transPart.contains("=")) {
                    parseAdjacencyFormat(stateName, transPart, tm);
                } else {
                    parseDefaultFormat(stateName, transPart, tapeAlphabet, tm);
                }
            }
        }

        reader.close();
        return tm;

    }

    private static void parseAdjacencyFormat(String state, String transPart, TM tm) {
        String[] entries = transPart.trim().split("\\s+");

        for (String entry : entries) {
            String[] parts = entry.split("=");

            if (parts.length != 2) {
                continue;
            }

            String readSymbols = parts[0];
            String transition = parts[1].replaceAll("[()]", "");
            String[] components = transition.split(",");

            String nextState = (components.length > 0 && !components[0].isEmpty()) ? components[0] : state;
            String writeSymbol = (components.length > 1 && !components[1].isEmpty()) ? components[1] : null;
            char direction = (components.length > 2 && !components[2].isEmpty()) ? components[2].charAt(0) : 'R';

            for (String readSymbol : extractMatchedSymbols(readSymbols, tm.getTapeAlphabet())) {

                String readString = String.valueOf(readSymbol);
                String finalWrite = (writeSymbol != null) ? writeSymbol : readString;

                TransitionKey key = new TransitionKey(state, readString);
                TransitionResult result = new TransitionResult(nextState, finalWrite, direction);
                tm.addTransition(key, result);
            }
        }
    }


    private static void parseDefaultFormat(String state, String transPart, List<String> tapeAlphabet, TM tm) {
        String[] entries = transPart.trim().split("\\s+");

        for (int i = 0; i < entries.length && i < tapeAlphabet.size(); i++) {
            String entry = entries[i];
            if (entry.equals("-")) {
                continue;
            }

            String transition = entry.replaceAll("[()]", "");
            String[] components = transition.split(",");

            String readSymbol = tapeAlphabet.get(i);
            String nextState = (components.length > 0 && !components[0].isEmpty()) ? components[0] : state;
            String finalWrite = (components.length > 1 && !components[1].isEmpty()) ? components[1] : readSymbol;
            char direction = (components.length > 2 && !components[2].isEmpty()) ? components[2].charAt(0) : 'R';

            TransitionKey key = new TransitionKey(state, readSymbol);
            TransitionResult result = new TransitionResult(nextState, finalWrite, direction);
            tm.addTransition(key, result);
        }
    }

    private static List<String> extractMatchedSymbols(String blob, List<String> tapeAlphabet) {
        List<String> matched = new ArrayList<>();
        int i = 0;
        while (i < blob.length()) {
            boolean matchedSymbol = false;
            for (String symbol : tapeAlphabet) {
                if (blob.startsWith(symbol, i)) {
                    matched.add(symbol);
                    i += symbol.length();
                    matchedSymbol = true;
                    break;
                }
            }
            if (!matchedSymbol) {
                throw new IllegalArgumentException("Invalid symbol group: " + blob);
            }
        }
        return matched;
    }

}
