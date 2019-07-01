/**
 * File:   GenLexer.java
 * Date:   October 2014
 *
 * Java source file provided for Informatics 2A Assignment 1 (2014).
 * Contains general infrastructure relating to DFAs and longest-match lexing,
 * along with some trivial examples.
 */

import java.io.* ;
import java.util.*;


/**
 * This class is providing usefull static methods for checking the type of
 * a character.
 */
class Character {

    /**
     * Testing a character whether it is a letter or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a letter, otherwise false
     */
    public static boolean letter(char c) {
        return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
    }

    /**
     * Testing a character whether it is a down-case or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a lower-case, otherwise false
     */
    public static boolean lowerCase(char c) {
        return (('a' <= c && c <= 'z') || c == '_');
    }

    /**
     * Testing a character whether it is an upper-case or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is an upper-case, otherwise false
     */
    public static boolean upperCase(char c) {
        return ('A' <= c && c <= 'Z');
    }

    /**
     * Testing a character whether it is a digit or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a digit, otherwise false
     */
    public static boolean digit(char c) {
        return ('0' <= c && c <= '9');
    }

    /**
     * Testing a character whether it is a symbol or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a symbol, otherwise false
     */
    public static boolean symbol(char c) {
        return (c == '!' || c == '#' || c == '$' || c == '%' || c == '&' || 
            c == '*' || c == '+' || c == '.' || c == '/' || c == '<' || 
            c == '=' || c == '>' || c == '?' || c == '@' || c == '\\' ||
            c == '^' || c == '|' || c == '-' || c == '~' || c == ':');
    }

    /**
     * Testing a character whether it is a whitespace or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a whitespace, otherwise false
     */
    public static boolean whitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
    }

    /**
     * Testing a character whether it is a new-line or not.
     *
     * @param  c the character to be tested
     * @return   true if the character is a new-line, otherwise false
     */
    public static boolean newline(char c) {
        return (c == '\r' || c == '\n' || c == '\f' );
    }

}

/**
 * This interface provides generic implementation of DFAs with
 * explicit "dead" states.
 */
interface DFA {

    /**
     * This method returns the name of the lexical class they are
     * associated with.
     *
     * @return the name of the lexical class
     */
    String lexClass();

    /**
     * This method returns the number of states of DFA.
     *
     * @return the number of states
     */
    int totalStates();

    /**
     * This method resets the DFA by setting the current state to the
     * initial state.
     */
    void reset();

    /**
     * This method processes a character by making transition from one
     * to another state.
     *
     * @param c the character to be processed
     * @throws IllegalState
     */
    void process(char c) throws IllegalState;

    /**
     * This method tests whether the DFA is at the goal state.
     *
     * @return true if the DFA is at the goal state, otherwise false.
     */
    boolean atGoalState();

    /**
     * This method tests whether the DFA is at a dead state.
     *
     * @return true if the DFA is at the dead state, otherwise false.
     */
    boolean atDeadState();

}

/**
 * This is an abstract class that implements DFA, which provides common methods
 * for DFA operations.
 */
abstract class GenAcceptor implements DFA {

    //--- Stubs for methods specific to a particular DFA ---//

    /**
     * This method returns the next state of DFA based on an input character
     * and a given state.
     *
     * @param input the input character
     * @param state the current state
     * @return      the next state
     */
    abstract int nextState(char input, int state);

    /**
     * This method tests whether given state is a goal state or not.
     *
     * @param state the state to be tested
     * @return      true if the state is a goal, otherwise false.
     */
    abstract boolean isGoalState(int state);

    /**
     * This method tests whether given state is a dead state or not.
     *
     * @param state the state to be tested
     * @return      true if the state is a dead-end, otherwise false.
     */
    abstract boolean isDeadState(int state);

    //--- General DFA machinery ---//

    /**
     * The current state of DFA (the initial state is always 0).
     */
    int currentState = 0;
 
    /**
     * This method resets the DFA by setting the current state equals to
     * the initial state.
     */
    public void reset() {
        currentState = 0;
    }  

    /**
     * This method processes a character input by performing a state transition.
     *
     * @param c the character input
     * @throws IllegalState
     */
    public void process(char c) throws IllegalState {
        // performs the state transition determined by c
        currentState = nextState (c, currentState);

        // throw a IllegalState when the DFA reaches an illegal state.
        if (currentState >= totalStates() || currentState < 0) {
            throw new IllegalState (currentState, lexClass());
        }
    }

    /**
     * @return true if the DFA is at the goal state, otherwise false.
     */
    public boolean atGoalState() {
        return isGoalState(currentState);
    }

    /**
     * @return true if the DFA is at the dead state, otherwise false.
     */
    public boolean atDeadState() {
        return isDeadState(currentState);
    }
}

/**
 * This class is an exception that will be thrown when the DFA reaches
 * an illegal state.
 */
class IllegalState extends Exception {
    /**
     * Constructor
     *
     * @param state        the illegal state
     * @param lexClassName the name of lexClass
     */
    IllegalState (int state, String lexClassName) {
        super("Illegal state " + Integer.toString(state) + 
              " in acceptor for " + lexClassName);
    }
}


//--- Examples of DFAs for some example weird lexical classes. ---//
//--- These show how particular DFAs are to be constructed.    ---//

/**
 * Example 1: Tokens consisting of an even number of letters (and nothing else)
 *
 * To create an instance of this class, use `new EvenLetterAcceptor()'
 */
class EvenLetterAcceptor extends GenAcceptor {

    /**
     * The name of lexClass of this DFA is "EVEN".
     */
    public String lexClass() {
        return "EVEN";
    }

    /**
     * The number of states of this DFA is 3.
     */
    public int totalStates() {
        return 3;
    }

    /**
     * Returning the next state of DFA based on given character and state.
     */
    public int nextState (char c, int state) {
        if (!Character.letter(c)) { // illegal input, declared "dead" below
            return 2;
        }

        switch (state) {
            case 0:  return 1;
            case 1:  return 0;
            default: return 2; // garbage state, declared "dead" below
        }
    }

    /**
     * Returning true if the DFA is at the goal state, otherwise false.
     */
    public boolean isGoalState(int state) {
        return (state == 0);
    }

    /**
     * Returning true if the DFA is at the dead state, otherwise false.
     */
    public boolean isDeadState(int state) {
        return (state == 2);
    }

}


/**
 * Example 2: Acceptor for just the token "&&"
 *
 * To create an instance of this class, use `new AndAcceptor()'
 */
class AndAcceptor extends GenAcceptor {

    /**
     * The name of lexClass of this DFA is "&&"
     */
    public String lexClass() {
        return "&&";
    }

    /**
     * The number of states of this DFA is 4.
     */
    public int totalStates() {
        return 4;
    }

    /**
     * Returning the next state of DFA based on given character and state.
     */
    public int nextState (char c, int state) {
        if (c != '&') { // illegal input, declared "dead" below
            return 3;
        }

        switch (state) {
            case 0:  return 1;
            case 1:  return 2;
            case 2:  return 3;
            default: return 3; // garbase state, declared "dead" below
        }
    }

    /**
     * Returning true if the DFA is at the goal state, otherwise false.
     */
    public boolean isGoalState(int state) {
        return (state == 2);
    }

    /**
     * Returning true if the DFA is at the dead state, otherwise false.
     */
    public boolean isDeadState(int state) {
        return (state == 3);
    }
}

/**
 * Example 3: Acceptor for just a space or linebreak character.
 * Setting the lexical class as "" means these tokens will be discarded
 * by the lexer.
 */
class SpaceAcceptor extends GenAcceptor {

    /**
     * The name of lexClass of this DFA is "".
     */
    public String lexClass() {
        return "";
    }

    /**
     * The number of states of this DFA is 3.
     */
    public int totalStates() {
        return 3;
    }

    /**
     * Returning the next state of DFA based on given character and state.
     */
    public int nextState (char c, int state) {
        switch (state) {
            case 0:
                if (c == ' ' || c == '\n' || c == '\r') {
                    return 1;
                } else {
                    return 2;
                }
            default:
                return 2;
        }
    }

    /**
     * Returning true if the DFA is at the goal state, otherwise false.
     */
    public boolean isGoalState(int state) {
        return (state == 1);
    }

    /**
     * Returning true if the DFA is at the dead state, otherwise false.
     */
    public boolean isDeadState(int state) {
        return (state == 2);
    }

}


/**
 * Generic lexical analyser. 
 * Uses principle of longest match, a.k.a. "maximal munch".
 *
 * A "lexical token" is simply a string tagged with the name of its
 * lexical class.
 *
 * A typical example: new LexToken("5", "num")
 */
class LexToken {
    /**
     * The value of the lexical token.
     */
    final String value;

    /**
     * The class of the lexical token.
     */
    final String lexClass;

    /**
     * This constructor receives a pair of lexical class and value.
     */
    public LexToken (String lexClass, String value) {
        this.lexClass = lexClass;
        this.value = value;
    }

    /**
     * Returning the value of this lexical token.
     */
    public String value() {
        return this.value;
    }

    /**
     * Returning the class of this lexical token.
     */
    public String lexClass() {
        return this.lexClass;
    }
}


/**
 * The output of the lexing phase, and the input to the parsing phase,
 * will be a LEX_TOKEN_STREAM object from which tokens may be drawn at will 
 * by calling `nextToken'.
 */
interface LEX_TOKEN_STREAM {
    /**
     * Pulls next token from stream, regardless of class.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken getToken() throws Exception;

    /**
     * Pulls next token not of class "" (e.g. skip whitespace and comments)
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken getProperToken() throws Exception;

    /**
     * Returns next token without removing it from stream.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken lookToken() throws Exception ;

    /**
     * Returns next non-"" token without removing it from stream.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken lookProperToken() throws Exception ;
}


/**
 * This class allows a LEX_TOKEN_STREAM object to be created for
 * a given input file and a language-specific repertoire of lexical classes.
 */
public class GenLexer implements LEX_TOKEN_STREAM {

    /**
     * for reading characters from input
     */
    Reader reader;       

    /**
     * Array of acceptors for the lexical classes, in order of priority
     */
	private DFA[] acceptors = null;

    /**
     * Constructor.
     *
     * @param reader    an instance of Reader that reads input characters
     * @param acceptors an array of acceptors for the lexical classes
     */
    GenLexer(Reader reader, DFA[] acceptors) {
        this.reader = reader;
        this.acceptors = acceptors;
    }

    /**
     * buffer to allow 1-token lookahead
     */
    LexToken bufferToken;

    boolean bufferInUse = false ;

    /**
     * character of EOF (end-of-file)
     */
    static final char EOF = (char)65535 ;

    /**
     * Implementation of longest-match lexer as described in lectures.
     * We go for simplicity and clarity rather than maximum efficiency.
     */
    public LexToken nextToken() throws LexError, IllegalState, IOException {
        // current input character
        char c;
        
        // characters up to last acceptance point
        String definite = "";   
   
        // characters since last acceptance point
        String maybe = "";

        // flags for use in iteration over acceptors
        boolean liveFound = false;
        boolean acceptorFound = false;

        // selected acceptor with the highest priority
        DFA selectedAcceptor = null;

        // reset all acceptors
        for (DFA acceptor : acceptors) {
            acceptor.reset();
        }

        // read a series of characters until a token is found by an acceptor
        do {
            // read a character
            c = (char) reader.read();

            acceptorFound = false;
            liveFound = false;
            if (c != EOF) {
                maybe += c;
                for (DFA acceptor : acceptors) {
                    acceptor.process(c);
                    if (!acceptor.atDeadState()) {
                        liveFound = true;
                    }
                    if (!acceptorFound && acceptor.atGoalState()) {
                        acceptorFound = true;
                        selectedAcceptor = acceptor;
                        definite += maybe;
                        maybe = "";
                        reader.mark(10); // register backup point in input
                    }
                }
            }
        } while (liveFound && c != EOF);

        if (selectedAcceptor != null) {
            // backup to last acceptance point and output token
            reader.reset();
            String theLexClass = selectedAcceptor.lexClass();
            return new LexToken(theLexClass, definite);
        } else if (c == EOF && maybe.equals("")) {
            // end of input already reached before nextToken was called
            reader.close();
            return null;    // by convention, signifies end of input
        } else {
            reader.close();
            throw new LexError(maybe);
        }
    }

    /**
     * Returns next token without removing it from stream.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken lookToken() throws LexError, IllegalState, IOException {
        if (bufferInUse) {
            return bufferToken;
        } else {
            bufferToken = nextToken();
            bufferInUse = true;
            return bufferToken;
        }
    }

    /**
     * Pulls next token from stream, regardless of class.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken getToken() throws LexError, IllegalState, IOException {
        lookToken();
        bufferInUse = false;
        return bufferToken;
    }

    /**
     * Returns next non-"" token without removing it from stream.
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken lookProperToken() throws LexError, IllegalState, IOException {
        LexToken tok = lookToken();
        while (tok != null && tok.lexClass().equals("")) {
            getToken() ;
            tok = lookToken() ;
        }
        bufferToken = tok ;
        bufferInUse = true ;
        return tok ;
    }

    /**
     * Pulls next token not of class "" (e.g. skip whitespace and comments)
     *
     * @return the next token, or null once end of input is reached.
     */
    public LexToken getProperToken() throws LexError, IllegalState, IOException {
        lookProperToken() ;
        bufferInUse = false ;
        return bufferToken ;
    }

}

/**
 * A weird example of a lexer using the DFAs constructed earlier
 */
class DemoLexer extends GenLexer {
    static DFA evenLetterAcc = new EvenLetterAcceptor() ;
    static DFA andAcc = new AndAcceptor() ;
    static DFA spaceAcc = new SpaceAcceptor() ;
    static DFA[] acceptors = new DFA[] {evenLetterAcc, andAcc, spaceAcc};

    DemoLexer(Reader reader) {
        super(reader, acceptors);
    }
}

/**
 * The main class of the weird example.
 */
class LexerDemo {
    public static void main (String[] args)
    throws LexError, IllegalState, IOException {
        // print a prompt
        System.out.print("Lexer> ");

        // create a Reader instance to read user input
        Reader reader = new BufferedReader(new InputStreamReader(System.in));

        // Instantiate the 
        GenLexer demoLexer = new DemoLexer(reader);

        // pull the first token
        LexToken currTok = demoLexer.getProperToken() ;

        // while end of stream has not been reached
        while (currTok != null) {
            // print the token's value and class
            System.out.println(currTok.value() + " \t" + currTok.lexClass());

            // pull another token
            currTok = demoLexer.getProperToken();
        }
    }
}


class LexError extends Exception {
    LexError(String nonToken) {
        super("Can't make lexical token from input \"" + nonToken + "\"") ;
    }
}


//--- Notes ---//
// To try out the lexer on the dummy examples, compile this file, type 
//    java LexerDemo
// and then type a line of input such as
//    abcd &&&&
// You can also experiment with erroneous inputs.
