/**
 * File:   GenParser.java
 * Date:   October 2014
 *
 * Java source file provided for Informatics 2A Assignment 1 (2014).
 * Contains general infrastructure relating to syntax trees and LL(1) parsing,
 * along with some trivial examples.
 */

import java.io.*;
import java.util.*;

/**
 * An recursive interface (class) for syntax tree nodes (any grammar).
 * This servers for both terminal and non-terminal nodes.
 */
interface TREE {
    /**
     * Returns a nonterminal symbol or lexical class of terminal.
     */
    String getLabel();

    /**
     * Returns true if this node is a terminal, otherwise false.
     */
    boolean terminal();

    /**
     * Return the value of this node. This is only relevant for terminal nodes.
     */
    String getValue();

    /**
     * Set the value of this node.
     */
    void setValue(String value);

    /**
     * Only relevant for non-terminal nodes.
     */
    String[] getRhs();

    /**
     * Returns the children nodes. Only relevant for non-terminal nodes.
     */
    TREE[] getChildren();

    /**
     * Only relevant for non-terminal nodes
     */
    void setChildren(TREE[] children, String[] rhs); 
}

/**
 * A concret class of syntax tree nodes.
 */
class STree implements TREE {
    /**
     * TREE's label. Convention: non-terminals begin with "#".
     */
    String label;

    /**
     * TREE's value.
     */
    String value;

    String[] rhs;

    /**
     * Children nodes.
     */
    TREE[] children;


    // Constructors
    STree (String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Returns true if this is a terminal node, otherwise false.
     */
    public boolean terminal() {
        return (label.charAt(0) != '#'); // see above convention
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getRhs() {
        return rhs;
    }

    public TREE[] getChildren() {
        return children;
    }

    public void setChildren(TREE[] children, String[] rhs) {
        this.rhs = rhs;
        this.children = children;
    }
}

/**
 * An interface for parser.
 */
interface Parser {
    /**
     * Parses given token stream, and then return syntax tree nodes.
     */
    TREE parse(LEX_TOKEN_STREAM tokStream) throws Exception;

    /**
     * Parses a token stream with particular starting non-terminal symbol.
     */
    TREE parseAs(String nonterm, LEX_TOKEN_STREAM tokStream)
        throws Exception;
}

/**
 * An abstract class of parser.
 */
abstract class GenParser implements Parser {

    //--- Stubs for methods specific to a particular grammar ---//

    /**
     * Returns a starting symbol.
     */
    public abstract String startSymbol();

    /**
     * LL(1) parse table - should return null for blank entries.
     * In the second argument, null serves as the end-of-input marker '$'.
     */
    public abstract String[] tableEntry(String tokenClass, String symbol);


    //--- The LL(1) parsing algorithm, as in lectures ---//

    /**
     * Parses given token stream, and then returns syntax tree nodes.
     */
    public TREE parse(LEX_TOKEN_STREAM tokStream) throws Exception {
        return parseAs(startSymbol(), tokStream);
    }

    /**
     * Parses a token stream with particular starting non-terminal symbol.
     */
    public TREE parseAs(String nonterm, LEX_TOKEN_STREAM tokStream)
    throws Exception {
        Stack<TREE> theStack = new Stack<TREE>();
        TREE rootTREE = new STree(nonterm);
        TREE currTREE;
        String currLabel;
        LexToken currToken;
        String currLexClass;

        theStack.push(rootTREE);

        do {
            currTREE = theStack.pop();
            currLabel = currTREE.getLabel();
            currToken = tokStream.lookProperToken();
            if (currToken == null) {
                currLexClass = null;
            } else {
                currLexClass = currToken.lexClass();
            }

            if (currTREE.terminal()) {
                // match expected terminal against input token
                if (currLexClass != null && currLexClass.equals(currLabel)) {
                    // all OK
                    currTREE.setValue(currToken.value());
                    tokStream.getToken();
                } else { // report error: expected terminal not found
                    if (currToken == null) {
                        throw new IllegalInput(currLabel, "end of input");
                    } else {
                        throw new IllegalInput(currLabel, currLexClass);
                    }
                } 
            } else { 
                // lookup expected nonterminal vs input token in table
                // OK if currLexClass is null (end-of-input marker)
                String[] rhs = tableEntry(currLexClass, currLabel);
                if (rhs != null) {
                    STree[] children = new STree[rhs.length];
                    for (int i = 0; i < rhs.length; i++) {
                        children[i] = new STree(rhs[i]);
                    }
                    currTREE.setChildren(children, rhs);
                    for (int i = rhs.length-1; i >= 0; i--) {
                        theStack.push(children[i]);
                    }
                } else {
                    // report error: blank entry in table
                    throw new IllegalInput (currLabel, currLexClass);
                }
            }
        } while (!theStack.empty());

        LexToken next = tokStream.getProperToken();
        if (next != null) {
            // non-fatal warning: parse completed before end of input
            System.out.println("Warning: " + next.value() +
                " found after parse completed.");
        } else {
            System.out.println ("Parse successful.");
        }

        return rootTREE;
    }

    // Perhaps add method for parsing as a specified nonterminal
}

class IllegalInput extends Exception {
    IllegalInput(String found, String expected) {
        super ("Parse error: " + found + " encountered where " + 
           expected + " expected.");
    }
}


/**
 * Tiny example: Parser for grammar
 * #S -> epsilon | EVEN #S && #S
 * Hint: read EVEN as (, && as ). 
 */
class EvenAndParser extends GenParser {

    public String startSymbol() {
        return "#S";
    }

    static String[] epsilon      = new String[] { };
    static String[] EVEN_S_AND_S = new String[] {"EVEN", "#S", "&&", "#S"};

    /**
     * A parser table entry for the "tiny" grammar.
     *
     * N.B. All this use of strings isn't great for efficiency,
     * but at least it makes for relatively readable code.
     */
    public String[] tableEntry(String tokenClass, String nonterm) {
        if (nonterm.equals("#S")) {
            if (tokenClass == null) {
                return epsilon;
            } else if (tokenClass.equals("&&")) {
                return epsilon;
            } else if (tokenClass.equals("EVEN")) {
                return EVEN_S_AND_S;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}


/**
 * For testing
 */
class ParserDemo {
    static Parser evenAndParser = new EvenAndParser();

    public static void main(String[] args) throws Exception {
        // create a reader for the source file
        Reader reader = new BufferedReader(new FileReader (args[0]));

        // create a lexer for the input characters
        GenLexer demoLexer = new DemoLexer(reader);

        // parses the lexer stream to generate syntax tree nodes
        TREE theTree = evenAndParser.parse(demoLexer);
    }
}
