/**
 * File:   MH_Parser.java
 * Date:   October 2014
 *
 * Java template file for parser component of Informatics 2A Assignment 2 (2014).
 * Students should add a method body for the LL(1) parse table for Micro-Haskell.
 */

import java.io.*;

class MH_Parser extends GenParser implements Parser {

    public String startSymbol() {
        return "#Prog";
    }

    // Right hand sides of all productions in grammar:

    String[] epsilon              = new String[] { };
    String[] Decl_Prog            = new String[] {"#Decl", "#Prog"};
    String[] TypeDecl_TermDecl    = new String[] {"#TypeDecl", "#TermDecl"};
    String[] TypeDecl_rule        = new String[] {"VAR", "::", "#Type", ";"};
    String[] Type1_TypeOps        = new String[] {"#Type1", "#TypeOps"};
    String[] arrow_Type           = new String[] {"->", "#Type"};
    String[] Integer              = new String[] {"Integer"};
    String[] Bool                 = new String[] {"Bool"};
    String[] lbr_Type_rbr         = new String[] {"(", "#Type", ")"};

    String[] TermDecl_rule        = new String[] {"VAR", "#Args", "=", 
                                                  "#Exp", ";"};
    String[] VAR_Args             = new String[] {"VAR", "#Args"};
    String[] Exp0                 = new String[] {"#Exp0"};
    String[] if_then_else         = new String[] {"if", "#Exp", "then", 
                                                  "#Exp", "else", "#Exp"};
    String[] Exp1_Op0             = new String[] {"#Exp1", "#Op0"};
    String[] eq_Exp1              = new String[] {"==", "#Exp1"};
    String[] lt_Exp1              = new String[] {"<", "#Exp1"};
    String[] Exp2_Ops1            = new String[] {"#Exp2", "#Ops1"};
    String[] plus_Exp2_Ops1       = new String[] {"+", "#Exp2", "#Ops1"};
    String[] minus_Exp2           = new String[] {"-", "#Exp2"};
    String[] Exp3_Ops2            = new String[] {"#Exp3", "#Ops2"};
    String[] VAR                  = new String[] {"VAR"};
    String[] NUM                  = new String[] {"NUM"};
    String[] BOOLEAN              = new String[] {"BOOLEAN"};
    String[] lbr_Exp_rbr          = new String[] {"(", "#Exp", ")"};

    public String[] tableEntry (String tokenClass, String nonterm) {

        // ADD YOUR CODE HERE
	if (nonterm.equals("#Prog")) {
    		if (tokenClass == null) {
    			return epsilon;
    		} else  if (tokenClass.equals("VAR")) {
    			return Decl_Prog;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Decl")) {
    		if (tokenClass.equals("VAR")) {
    			return TypeDecl_TermDecl;
    			}
    		else return null;
    	}
    	
    	if (nonterm.equals("#TypeDecl")) {
    		if (tokenClass.equals("VAR")) {
    			return TypeDecl_rule;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Type")) {
    		if (tokenClass.equals("Integer")) {
    			return Type1_TypeOps;
    		}
    		else if (tokenClass.equals("Bool")) {
    			return Type1_TypeOps;
    		}
    		else if (tokenClass.equals("(")) {
    			return Type1_TypeOps;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#TypeOps")) {
    		if (tokenClass.equals(";")) {
    			return epsilon;
    		}
    		else if(tokenClass == ")") 
    			return epsilon;
    		else if (tokenClass.equals("->")) {
    			return arrow_Type;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Type1")) {
    		if (tokenClass.equals("Integer")) {
    			return Integer;
    		}
    		else if (tokenClass.equals("Bool")) {
    			return Bool;
    		}
    		else if (tokenClass.equals("(")) {
    			return lbr_Type_rbr;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#TermDecl")) {
    		if (tokenClass.equals("VAR")) {
    			return TermDecl_rule;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Args")) {
    		if (tokenClass.equals("VAR")) {
    			return VAR_Args;
    		}
    		else if (tokenClass.equals("=")) {
    			return epsilon;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Exp")) {
    		if (tokenClass.equals("VAR")) {
    			return Exp0; 
    		}
    		else if (tokenClass.equals("NUM")) {
    			return Exp0;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return Exp0;
    		}
    		else if (tokenClass.equals("if")) {
    			return if_then_else;
    		}
    		else if (tokenClass.equals("(")) {
    			return Exp0;
    		}
    		else if (tokenClass.equals("-")) {
    			return Exp0;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Exp0")) {
    		if (tokenClass.equals("VAR")) {
    			return Exp1_Op0;
    		}
    		else if (tokenClass.equals("NUM")) {
    			return Exp1_Op0;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return Exp1_Op0;
    		}
    		else if (tokenClass.equals("(")) {
    			return Exp1_Op0;
    		}
    		else if (tokenClass.equals("-")) {
    			return Exp1_Op0;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Op0")) {
    		if (tokenClass.equals("then")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("else")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals(")")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals(";")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("==")) {
    			return eq_Exp1;
    		}
    		else if (tokenClass.equals("<")) {
    			return lt_Exp1;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Exp1")) {
    		if (tokenClass.equals("VAR")) {
    			return Exp2_Ops1;
    		}
    		else if (tokenClass.equals("NUM")) {
    			return Exp2_Ops1;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return Exp2_Ops1;
    		}
    		else if (tokenClass.equals("(")) {
    			return Exp2_Ops1;
    		}
    		else if (tokenClass.equals("-")) {
    			return Exp2_Ops1;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Ops1")) {
    		if (tokenClass.equals("then")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("else")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals(")")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals(";")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("==")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("<")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("+")) {
    			return plus_Exp2_Ops1;
    		}
    		else return null;
     	}
    	
    	if (nonterm.equals("#Exp2")) {
    		if (tokenClass.equals("VAR")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("NUM")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("(")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("-")) {
    			return minus_Exp2;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Ops2")) {
    		if (tokenClass.equals("VAR")) {
    			return Exp3_Ops2; 
    		}
    		else if (tokenClass.equals("NUM")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals("then")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("else")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("(")) {
    			return Exp3_Ops2;
    		}
    		else if (tokenClass.equals(")")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals(";")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("==")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("<")) {
    			return epsilon;
    		}
    		else if (tokenClass.equals("+")) {
    			return epsilon;
    		}
    		else return null;
    	}
    	
    	if (nonterm.equals("#Exp3")) {
    		if (tokenClass.equals("VAR")) {
    			return VAR;
    		}
    		else if (tokenClass.equals("NUM")) {
    			return NUM;
    		}
    		else if (tokenClass.equals("BOOLEAN")) {
    			return BOOLEAN;
    		}
    		else if (tokenClass.equals("(")) {
    			return lbr_Exp_rbr;
    		}
    		else return null;
     	}
    	
    	else return null;
    
    }
    
}


/**
 * For testing
 */
class MH_ParserDemo {

    static Parser MH_Parser = new MH_Parser();

    public static void main (String[] args) throws Exception {
        Reader reader = new BufferedReader(new FileReader (args[0]));
    
        LEX_TOKEN_STREAM MH_Lexer = new CheckedSymbolLexer(new MH_Lexer(reader));

        TREE theTree = MH_Parser.parse(MH_Lexer);
    }
}
