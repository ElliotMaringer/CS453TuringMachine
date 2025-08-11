# Turing Machine Simulator

Author: Elliot Maringer  
Date: 05/12/2025

This Java-based command-line application simulates single-tape Turing machines. It supports multiple input formats, simulates Turing machines with step limits, and can enumerate the language accepted by a Turing machine up to a certain string length.

## Features

- Load Turing machine (TM) descriptions from `.txt` files
- Support for both default and adjacency-list transition formats
- Print TM information including states, alphabets, and transition tables
- Simulate TM execution on an input string with a move limit
- Generate the language of a TM up to a specified string length and report inconclusive cases

## File Format

Turing machines must be specified in a valid `.txt` format that:
- Begins with an alphabet header: `<inputAlphabetSize> | <tape symbols>`
- Lists states and transitions in either the default or adjacency-list format
- Uses the following markers:
  - `->` for the initial state
  - `*` for the accept state
  - `!` for the reject state


## Compilation

To compile the program, navigate to the directory containing the `.java` files and run:

javac *.java

## Running

The program is designed to be run from the command line with one of several commands. Filenames should be given as complete file paths. Here is a list of the supported command-line options:

1) Display TM information:
    java App --info <FILENAME>

2) Simulate a TM on an input string for a given number of moves:
    java App --run <FILENAME> <INPUT> <MOVELIMIT>
    (Use "" to denote the empty string.)

3) Generate all strings up to a given length in a TM’s language with a given number of moves:
    java App --language <FILENAME> <LENGTHLIMIT> <MOVELIMIT>


