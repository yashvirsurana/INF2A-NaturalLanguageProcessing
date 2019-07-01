
// File:   CheckedSymbolLexer.java
// Date:   October 2014

// Java source file provided for Informatics 2A Assignment 1 (2014).
// Thin wrapper for token streams: checks if a symbol token is 
// among those of MH, and renames its lexical class to be the symbol itself.
// (Helpful for the parser, which only looks at lexical classes.)

import java.io.* ;

class CheckedSymbolLexer implements LEX_TOKEN_STREAM {

    GenLexer tokStream;

    CheckedSymbolLexer(GenLexer tokStream) {
        this.tokStream = tokStream;
    }

    public static String[] validTokens =
        new String[] {"::", "->", "=", "==", "<", "+", "-"};

    public static String check(String s) throws UnknownSymbol {
        for (int i=0; i<validTokens.length; i++) {
            if (s.equals(validTokens[i])) return s;
        }
        throw new UnknownSymbol(s);
    }

    public static LexToken checkToken(LexToken token) throws UnknownSymbol {
        if (token != null && token.lexClass().equals("SYM")) {
            return new LexToken(token.value(), check(token.value()));
        } else {
            return token;
        }
    }

    public LexToken getToken()
    throws LexError, IllegalState, IOException, UnknownSymbol {
        return checkToken(tokStream.getToken());
    }

    public LexToken getProperToken() 
    throws LexError, IllegalState, IOException, UnknownSymbol {
        return checkToken(tokStream.getProperToken());
    }

    public LexToken lookToken() 
	throws LexError, IllegalState, IOException, UnknownSymbol {
        return checkToken(tokStream.lookToken());
    }

    public LexToken lookProperToken() 
	throws LexError, IllegalState, IOException, UnknownSymbol {
        return checkToken(tokStream.lookProperToken());
    }

}

class UnknownSymbol extends Exception {
    UnknownSymbol (String s) {
        super("Unknown symbolic token " + s);
    }
}


