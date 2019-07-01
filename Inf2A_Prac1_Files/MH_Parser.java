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
