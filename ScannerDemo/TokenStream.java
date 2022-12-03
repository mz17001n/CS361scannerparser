// TokenStream.java

// Implementation of the Scanner for KAY

// This code DOES NOT implement a scanner for KAY yet. You have to complete
// the code and also make sure it implements a scanner for KAY - not something
// else, not more and not less

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    // READ THE COMPLETE FILE FIRST
    // You will need to adapt it to KAY, NOT JAY

    // Instance variables
    private boolean isEo = false; // is end of file
    private char nextChar = ' '; // next character in input stream
    private BufferedReader input;

    // This function was added to make the demo file work
    public boolean isEoFile() {
        return isEo;
    }

    // Constructor
    // Pass a filename for the program text as a source for the TokenStream.
    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            // System.exit(1); // Removed to allow ScannerDemo to continue
            // running after the input file is not found.
            isEo = true;
        }
    }

    public Token nextToken() { // Main function of the scanner
        // Return next token type and value.
        Token to = new Token();
        to.setType("Other"); // For now it is Other
        to.setValue("");

        // First check for whitespaces and bypass them
        skipWhiteSpace();

        // Then check for a comment, and bypass it
        // but remember that / may also be a division operator.
        while (nextChar == '/') {
            // Changed if to while to avoid the 2nd line being printed when
            // there are two comment lines in a row.
            nextChar = readChar();
            if (nextChar == '/') { // If / is followed by another /
                // skip rest of line - it's a comment.
                // TODO TO BE COMPLETED
                // look for <cr>, <lf>, <ff>
                while(!isEndOfLine(nextChar)) {
                    nextChar = readChar();
                }
                to.setValue("//");
                to.setType("Comment");
                return to;

            } else {
                // A slash followed by anything else must be an operator.
                to.setValue("/");
                to.setType("Operator");
                return to;
            }
        }

        // Then check for an operator; this part of the code should recover 2-character
        // operators as well as 1-character ones.
        if (isOperator(nextChar)) {
            to.setType("Operator");
            to.setValue(to.getValue() + nextChar);
            switch (nextChar) {
                // TODO TO BE COMPLETED WHERE NEEDED
                case '<':
                    // <=
                    nextChar = readChar();
                    if (nextChar == '=') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();

                    }
                    return to;

                case '>':
                    // >=
                    nextChar = readChar();
                    if (nextChar == '=') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();

                    }
                    return to;
                case '=':
                    // ==
                    nextChar = readChar();
                    if (nextChar == '=') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();
                    }
                    return to;

                case '!':
                    // !=
                    nextChar = readChar();
                    if (nextChar == '=') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();
                    }
                    return to;

                case '|':
                    // Look for ||
                    nextChar = readChar();
                    if (nextChar == '|') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();

                    }
                    return to;


                case '&':
                    // Look or &&
                    nextChar = readChar();
                    if (nextChar == '&') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();

                    }
                    return to;

                case ':':
                    // look for :=
                    nextChar = readChar();
                    if (nextChar == '=') {
                        to.setValue(to.getValue() + nextChar);
                        nextChar = readChar();
                        return to;
                    } else {
                        to.setValue(to.getValue());
                        nextChar = readChar();

                    }
                    return to;



                default: // all other operators
                    nextChar = readChar();
                    return to;
            }
        }

        // Then check for a separator
        if (isSeparator(nextChar)) {
            to.setType("Separator");
            // TODO TO BE COMPLETED
            while(isSeparator(nextChar)) {
                to.setValue("" + nextChar);
                nextChar = readChar();
            }
            return to;
        }

        // Then check for an identifier, keyword, or literal.
        if (isLetter(nextChar)) {
            // Set to an identifier
            to.setType("Identifier");
            while ((isLetter(nextChar) || isDigit(nextChar))) {
                to.setValue(to.getValue() + nextChar);
                nextChar = readChar();
            }
            // now see if this is a keyword
            if (isKeyword(to.getValue())) {
                to.setType("Keyword");

            } else if (to.getValue().equals("True") || to.getValue().equals("False")) {
                to.setType("Literal");
            }
            if (isEndOfToken(nextChar)) { // If token is valid, returns.
                return to;
            }
        }

        if (isDigit(nextChar)) { // check for integer literals
            to.setType("Literal");
            while (isDigit(nextChar)) {
                to.setValue(to.getValue() + nextChar);
                nextChar = readChar();
            }
            // An Integer-Literal is to be only followed by a space,
            // an operator, or a separator.
            if (isEndOfToken(nextChar)) {// If token is valid, returns.
                return to;
            }
        }

        to.setType("Other");

        if (isEo) {
            return to;
        }

        // Makes sure that the whole unknown token (Type: Other) is printed.
        while (!isEndOfToken(nextChar)) {
            to.setValue(to.getValue() + nextChar);
            nextChar = readChar();
        }

        // Finally check for whitespaces and bypass them
        skipWhiteSpace();

        return to;
    }

    private char readChar() {
        int a = 0;
        if (isEo)
            return (char) 0;
        System.out.flush();
        try {
            a = input.read();
        } catch (IOException e) {
            System.exit(-1);
        }
        if (a == -1) {
            isEo = true;
            return (char) 0;
        }
        return (char) a;
    }

    private boolean isKeyword(String s) {
        // TODO TO BE COMPLETED
        return (s.equals("bool") || s.equals("else") || s.equals("if") || s.equals ("integer") || s.equals("main") || s.equals("while"));
    }

    private boolean isWhiteSpace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '\f');
    }

    private boolean isEndOfLine(char ch) {
        return (ch == '\r' || ch == '\n' || ch == '\f');
    }

    private boolean isEndOfToken(char ch) { // Is the value a seperate token?
        return (isWhiteSpace(nextChar) || isOperator(nextChar) || isSeparator(nextChar) || isEo);
    }

    private void skipWhiteSpace() {
        // check for whitespaces, and bypass them
        while (!isEo && isWhiteSpace(nextChar)) {
            nextChar = readChar();
        }
    }

    private boolean isSeparator(char ch) {
        // TODO TO BE COMPLETED
        return (ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == ';' || ch == ',');
    }

    private boolean isOperator(char ch) {
        // Checks for characters that start operators
        // TODO TO BE COMPLETED
        return (ch == '<' || ch == '>' || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '!' || ch == ':' || ch == '&' || ch == '=');
    }

    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z');
    }

    private boolean isDigit(char ch) {
        // TODO TO BE COMPLETED
        return (ch >= '0' && ch <= '9');
    }

    public boolean isEndofFile() {
        return isEo;
    }

}

