/**
 * File:   MH_Lexer.java
 * Date:   October 2014
 *
 * Java template file for lexer component of Informatics 2A Assignment 1 (2014).
 * Concerns lexical classes and lexer for the language MH (`Micro-Haskell').
 */

import java.io.* ;

class VarAcceptor extends GenAcceptor {
	  
	public String lexClass() {
	        return "VAR";
	    }
	
	public int totalStates() {
        return 4;
    }
	
	public int nextState (char c, int state) {
      

        switch (state) {
            case 0:  
            	if (Character.lowerCase(c))
            		return 1;
            	else return 3;
            case 1: 
            	if (Character.lowerCase(c))
            		return 1;
            	else if (Character.upperCase(c))
            		return 1;
            	else if (Character.digit(c))
            		return 1;
            	else if (c == '\'')
            		return 2;
            	else 
            		return 3;
            case 2:
            	if(c == '\'')
            		return 2;
            	else
            		return 3;
            default: return 3;
        }
    }
	
	public boolean isGoalState(int state) {
        return (state == 1 || state == 2);
    }
	
	public boolean isDeadState(int state) {
	    return (state == 3);
	}

}

class NumAcceptor extends GenAcceptor {
    
	public String lexClass() {
        return "NUM";
    }

	public int totalStates() {
		return 4;
	}

	public int nextState (char c, int state) {  

		switch (state) {
       		case 0:  
       			if (c == 0)
       				return 1;
       			else if (Character.digit(c) && (c != 0))
       				return 2;
       		case 2:
       			if (Character.digit(c))
       				return 2;
       		default: return 3;
		}
	}

	public boolean isGoalState(int state) {
		return (state == 1 || state == 2);
	}

	public boolean isDeadState(int state) {
		return (state == 3);
	}
	
}

class BooleanAcceptor extends GenAcceptor {
	
	public String lexClass() {
        return "BOOLEAN";
    }

	public int totalStates() {
		return 11;
	}

	public int nextState (char c, int state) {  

		switch (state) {
       		case 0:  
       			if (c == 'T')
       				return 1;
       			else if (c == 'F')
       				return 2;
       		case 1: 
       			if (c == 'r')
       				return 3;
       		case 2:
       			if (c == 'a')
       				return 4;
       		case 3:
       			if (c == 'u') 
       				return 5;
       		case 4:
       			if (c == 'l')
       				return 6;
       		case 5:
       			if (c == 'e')
       				return 7;
       		case 6:
       			if (c == 's')
       				return 8;
       		case 8:
       			if (c == 'e')
       				return 9;
       		default: return 10;
		}
	}

	public boolean isGoalState(int state) {
		return (state == 7 || state == 9);
	}

	public boolean isDeadState(int state) {
		return (state == 10);
	}
}

class SymAcceptor extends GenAcceptor {
    
	public String lexClass() {
        return "SYM";
    }

	public int totalStates() {
		return 3;
	}

	public int nextState (char c, int state) {  

		switch (state) {
       		case 0:  
       			if (Character.symbol(c))
       				return 1;
       		case 1:
       			if (Character.symbol(c))
       				return 1;
       		default: return 2;
		}
	}

	public boolean isGoalState(int state) {
		return (state == 1);
	}

	public boolean isDeadState(int state) {
		return (state == 2);
	}
	
}

class WhitespaceAcceptor extends GenAcceptor {
    
	public String lexClass() {
        return "";
    }

	public int totalStates() {
		return 3;
	}

	public int nextState (char c, int state) {  

		switch (state) {
       		case 0:  
       			if (Character.whitespace(c))
       				return 1;
       		case 1:
       			if (Character.whitespace(c))
       				return 1;
       		default: return 2;
		}
	}

	public boolean isGoalState(int state) {
		return (state == 1);
	}

	public boolean isDeadState(int state) {
		return (state == 2);
	}
	
}

class CommentAcceptor extends GenAcceptor {
    
	public String lexClass() {
        return "";
    }

	public int totalStates() {
		return 6;
	}

	public int nextState (char c, int state) {  

		switch (state) {
       		case 0:  
       			if (c == '-')
       				return 1;
       			else return 5;
       		case 1:
       			if (c == '-')
       				return 2;
       			else return 5;
       		case 2:
       			if (!Character.symbol(c) && Character.newline(c))
       				return 3;
       			else if (c == '-')
       				return 2;
       			else if (!Character.newline(c))
       				return 4;
       			else return 5;
       		case 4:
       			if (!Character.newline(c))
       				return 4;
       			else return 5;
       		default: return 5;
		}
		
	}

	public boolean isGoalState(int state) {
		return (state == 2 || state == 3 || state == 4);
	}

	public boolean isDeadState(int state) {
		return (state == 5);
	}
	
}

class TokAcceptor extends GenAcceptor {

    String tok;
    int tokLen;

    TokAcceptor (String tok) {
        this.tok = tok;
        tokLen = tok.length();
    }

    public String lexClass() {
        return tok;
    }

	public int totalStates() {
		return (tokLen+2); 
	}

	public int nextState (char c, int state) {  
		
       			if (state < tokLen && c == tok.charAt(state))
       				return (state+1);
       			else
       				return (tokLen+1);
	}
	

	public boolean isGoalState(int state) {
		return (state == tokLen);
	}

	public boolean isDeadState(int state) {
		return (state == (tokLen+1));
	}
}


public class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {
	
	static DFA CommentAcceptor = new CommentAcceptor() ;
	static DFA openBrackAcceptor = new TokAcceptor("(") ;
	static DFA closeBrackAcceptor = new TokAcceptor(")") ;
	static DFA semiColAcceptor = new TokAcceptor(";") ;
	static DFA ifAcceptor = new TokAcceptor("if") ;
	static DFA thenAcceptor = new TokAcceptor("then") ;
	static DFA elseAcceptor = new TokAcceptor("else") ;
	static DFA WhitespaceAcceptor = new WhitespaceAcceptor() ;
	static DFA SymAcceptor = new SymAcceptor() ;
	static DFA VarAcceptor = new VarAcceptor() ; 
	static DFA BooleanAcceptor = new BooleanAcceptor() ;
	static DFA NumAcceptor = new NumAcceptor() ; 
	static DFA integerAcceptor = new TokAcceptor("Integer") ;
	static DFA BoolAcceptor = new TokAcceptor("Bool") ;
	
    static DFA[] MH_acceptors = new DFA[] { CommentAcceptor, WhitespaceAcceptor,
    	openBrackAcceptor, closeBrackAcceptor, semiColAcceptor, 
    	ifAcceptor, thenAcceptor, elseAcceptor, 
    	SymAcceptor, VarAcceptor, BooleanAcceptor, NumAcceptor, 
    	integerAcceptor, BoolAcceptor};
    
    static DFA[] MH_acceptors1 = new DFA[] { CommentAcceptor };
    
    MH_Lexer (Reader reader) {
        super(reader, MH_acceptors);

        // add code here

    }

}

