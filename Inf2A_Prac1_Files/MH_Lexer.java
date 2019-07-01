/**
 * File:   MH_Lexer.java
 * Date:   October 2014
 *
 * Java template file for lexer component of Informatics 2A Assignment 1 (2014).
 * Concerns lexical classes and lexer for the language MH (`Micro-Haskell').
 */

import java.io.* ;

class VarAcceptor extends GenAcceptor {
    // add code here
}

class NumAcceptor extends GenAcceptor {
    // add code here
}

class BooleanAcceptor extends GenAcceptor {
    // add code here
}

class SymAcceptor extends GenAcceptor {
    // add code here
}

class WhitespaceAcceptor extends GenAcceptor {
    // add code here
}

class CommentAcceptor extends GenAcceptor {
    // add code here
}

class TokAcceptor extends GenAcceptor {

    String tok;
    int tokLen;

    TokAcceptor (String tok) {
        this.tok = tok;
        tokLen = tok.length();
    }

    // add code here
}


public class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

    // add definition of MH_acceptors here

    MH_Lexer (Reader reader) {
        super(reader);

        // add code here

    }

}
