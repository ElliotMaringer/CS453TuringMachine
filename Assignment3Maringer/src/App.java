// Elliot Maringer

import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java App <action> <arguments>");
        }

        String action = args[0];
        String filePath = args[1];
        switch (action) {
            case "--info":
                if (args.length != 2) {
                    System.out.println("Usage: java App --info <FILENAME>");
                    return;
                }

                try {
                    TM tm = TMFileParser.parseFile(filePath);
                    tm.printInfo();
                } catch (IOException e) {
                    System.out.println("An issue occured reading the file");
                }
                break;

            case "--run":
                if (args.length != 4) {
                    System.out.println("Usage: java App --run <FILENAME> <INPUT> <MOVELIMIT>");
                    return;
                }
                try {
                    TM tm = TMFileParser.parseFile(filePath);
                    String input = args[2];
                    int moveLimit = Integer.parseInt(args[3]);
                    tm.runTM(input, moveLimit);
                } catch (IOException e) {
                    System.out.println("An issue occured reading the file");
                }
                break;

            case "--language":
                if (args.length != 4) {
                    System.out.println("Usage: java App --lanaguage <FILENAME> <LENGTHLIMIT> <MOVELIMIT>");
                    return;
                }
                try {
                    TM tm = TMFileParser.parseFile(filePath);
                    int lengthLimit = Integer.parseInt(args[2]);
                    int moveLimit = Integer.parseInt(args[3]);
                    tm.runLanguage(lengthLimit, moveLimit);
                } catch (IOException e) {
                    System.out.println("An issue occured reading the file");
                }
                break;


            default: 
                System.out.println("Unrecognized action");
        }
    }
}
