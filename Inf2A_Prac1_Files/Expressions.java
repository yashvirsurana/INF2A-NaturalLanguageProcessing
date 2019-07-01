/**
 * File:   Expressions.java
 * Date:   October 2014
 *
 * Java source file provided for Informatics 2A Assignment 1 (2014).
 * Provides abstract syntax trees for expressions in Micro-Haskell:
 * effectively, trees for the simplified grammar
 *
 *     Exp  -->   VAR | NUM | BOOLEAN | Exp Exp | Exp infix Exp
 *                | prefix Exp | if Exp then Exp else Exp
 */

import java.util.*;

/**
 * An interface for Micro-haskell expression.
 *
 * Examples illustrating how the MH_Exp operations are used:
 * If e is the MH_EXP tree for "if e_1 then e_2 else e_3", then
 *   - e.first() returns the tree for e_1
 *   - e.second() returns the tree for e_2
 *   - e.third() returns the tree for e_3
 *   - e.value(), e.infixOp() and e.prefixOp() won't return anything sensible.
 *
 * If e is the MH_EXP tree for "e_1 e_2" (function application), then
 *   - e.first() returns the tree for e_1
 *   - e.second() returns the tree for e_2
 *   - e.third(), e.value(), e.infixOp() and e.prefixOp() won't return
 *     anything sensible
 *
 * If e is the MH_EXP tree for "e_1 == e_2", then
 *   - e.first() returns the tree for e_1
 *   - e.second() returns the tree for e_2
 *   - e.infixOp() returns the string "==", identifying the infix involved
 *   - e.third(), e.value(), and e.prefixOp() won't return anything sensible
 * and similarly for the other infix operations.
 *
 * If e is the MH_EXP tree for "- e_1", then
 *   - e.first() returns the tree for e_1
 *   - e.prefixOp() returns the string "<", identifying the prefix involved
 *   - e.second(), e.third(), e.value() and e.infixOp() won't return anything
 *     sensible
 *
 * If e is the MH_EXP tree for a VAR, NUM or BOOLEAN, then
 *   - e.value() returns its string representation, e.g. "x" or "42" or "True"
 *   - e.first(), e.second(), e.third(), e.infixOp(), e.prefixOp() won't return 
 *     anything sensible.
 */
interface MH_EXP {
    /**
     * Returns true for a VAR expression, otherwise false.
     */
    boolean isVAR();

    /**
     * Returns true for a NUM expression, otherwise false.
     */
    boolean isNUM();

    /**
     * Returns true for a BOOLEAN expression, otherwise false.
     */
    boolean isBOOLEAN();

    /**
     * Returns true for an application expression, otherwise false.
     */
    boolean isAPP();

    /**
     * Returns true for an infix expression, otherwise false.
     */
    boolean isINFIX();

    /**
     * Returns true for a prefix expression, otherwise false.
     */
    boolean isPREFIX();

    /**
     * Returns true of an if-then-else expression, otherwise false.
     */
    boolean isIF();

    /**
     * Only needed for evaluator: MH itself doesn't have lambda expressions.
     */
    boolean isLAMBDA(); 

    /**
     * For VAR, NUM, BOOLEAN: returns e.g. "x", "5", "True"
     */
    String value();     

    /**
     * For infix expressions: returns "==", "+" or "-"
     */
    String infixOp();

    /**
     * For prefix expression: returns "<"
     */
    String prefixOp();

    /**
     * Returns first child (for application, infix, prefix, if-expressions)
     */
    MH_EXP first();

    /**
     * Returns second child (for application, infix, if-expressions)
     */
    MH_EXP second();

    /**
     * returns third child (for if-expressions only)
     */
    MH_EXP third();
}

/**
 * An implementation class of interface MH_EXP
 */
class MH_Exp_Impl implements MH_EXP {
    int kind;
    String value;
    String infixOp;
    String prefixOp;
    MH_EXP firstChild;
    MH_EXP secondChild;
    MH_EXP thirdChild;

    public boolean isVAR() {
        return kind == 0;
    }

    public boolean isNUM() {
        return kind == 1;
    }

    public boolean isBOOLEAN() {
        return kind == 2;
    }

    public boolean isAPP() {
        return kind == 3;
    }

    public boolean isINFIX() {
        return kind == 4;
    }

    public boolean isIF() {
        return kind == 5;
    }

    public boolean isLAMBDA() {
        return kind == 6;
    }

    public boolean isPREFIX() {
        return kind == 7;
    }

    public String value() {
        return value;
    }

    public String infixOp() {
        return infixOp;
    }

    public String prefixOp() {
        return prefixOp;
    }

    public MH_EXP first() {
        return firstChild;
    }

    public MH_EXP second() {
        return secondChild;
    }

    public MH_EXP third() {
        return thirdChild;
    }

    // Various constructors: number and type of arguments determine
    // the kind of expression.

    /**
     * Constructor for atomic expressions (VAR, NUM, BOOLEAN).
     */
    MH_Exp_Impl(String lexClass, String value) {
        this.value = value;
        if (lexClass.equals("VAR")) {
            kind = 0;
        } else if (lexClass.equals("NUM")) {
            kind = 1;
        } else if (lexClass.equals("BOOLEAN")) {
            kind = 2;
        } else {
            System.out.println ("Warning: unknown lexClass " + lexClass);
            kind = -1 ; 
        }
    }

    /**
     * Constructor for applications.
     */
    MH_Exp_Impl(MH_EXP left, MH_EXP right) {
        this.kind = 3;
        this.firstChild = left;
        this.secondChild = right;
    }

    /**
     * Constructor for infix expressions
     */
    MH_Exp_Impl(MH_EXP left, String infixOp, MH_EXP right) {
        this.kind = 4;
        this.firstChild = left;
        this.secondChild = right;
        this.infixOp = infixOp;
    }

    /**
     * Constructor for prefix expressions
     */
    MH_Exp_Impl(MH_EXP exp) {
        this.kind = 7;
        this.firstChild = exp;
        this.prefixOp = "-";
    }

    /**
     * Constructor for if-expressions
     */
    MH_Exp_Impl(MH_EXP condition, MH_EXP branch1, MH_EXP branch2) {
        this.kind = 5;
        this.firstChild = condition;
        this.secondChild = branch1;
        this.thirdChild = branch2;
    }

    /**
     * Constructor for lambda-expressions
     */
    MH_Exp_Impl(String var, MH_EXP body) {
        this.kind = 6;
        this.value = var;
        this.firstChild = body;
    }


    //--- Converting parse trees to ASTs for expressions ---//

    public final static MH_Parser MH_Parser = MH_Type_Impl.MH_Parser;

    static class TaggedExp {
        MH_EXP exp;
        String tag;
        TaggedExp (MH_EXP exp, String tag) {
            this.exp = exp;
            this.tag = tag;
        }
    }

    public static MH_EXP convertExp(TREE exp) {
        if (exp.getLabel().equals("#Exp3")) {
            if (exp.getRhs() == MH_Parser.lbr_Exp_rbr) {
                return convertExp(exp.getChildren()[1]);
            } else {
                TREE terminal = exp.getChildren()[0];
                // build atomic expression
                return new MH_Exp_Impl(terminal.getLabel(), terminal.getValue());
            }
        } else if (exp.getLabel().equals("#Exp2")) {
            if (exp.getRhs() == MH_Parser.minus_Exp2) {
                // build "-" prefix expression
                return new MH_Exp_Impl(convertExp(exp.getChildren()[1]));
            } else {
                MH_EXP head = convertExp(exp.getChildren()[0]);
                Stack rest = convertOps2(exp.getChildren()[1]);
                while (! rest.isEmpty()) {
                    // build application expression
                    head = new MH_Exp_Impl(head, (MH_EXP)(rest.pop()));
                }
                return head;
            }
        } else if (exp.getLabel().equals("#Exp1")) {
            MH_EXP head = convertExp(exp.getChildren()[0]);
            Stack rest = convertOps1(exp.getChildren()[1]);
            while (! rest.isEmpty()) {
                // build "+" infix expression
                TaggedExp tt = (TaggedExp) rest.pop();
                head = new MH_Exp_Impl(head, tt.tag, tt.exp);
            }
            return head;
        } else if (exp.getLabel().equals("#Exp0")) {
            MH_EXP head = convertExp(exp.getChildren()[0]);
            TREE op1 = exp.getChildren()[1];
            if (op1.getRhs() == MH_Parser.epsilon) {
                return head;
            } else {
                MH_EXP other = convertExp(op1.getChildren()[1]);
                String op = op1.getChildren()[0].getLabel();
                // build "==" or "<" infix expression
                return new MH_Exp_Impl(head, op, other);
            }
        } else if (exp.getLabel().equals("#Exp")) {
            if (exp.getRhs() == MH_Parser.Exp0) {
                return convertExp(exp.getChildren()[0]);
            } else {
                // construct if-expression
                return new MH_Exp_Impl(convertExp(exp.getChildren()[1]),
                    convertExp(exp.getChildren()[3]),
                    convertExp(exp.getChildren()[5]));  
            }                     
        } else {
            System.out.println("Unexpected label " + exp.getLabel());
            return null;
        }
    }

    public static Stack<MH_EXP> convertOps2(TREE ops2) {
        if (ops2.getRhs() == MH_Parser.epsilon) {
            return new Stack<MH_EXP>();
        } else {
            MH_EXP exp = convertExp(ops2.getChildren()[0]);
            Stack<MH_EXP> stack = convertOps2(ops2.getChildren()[1]);
            stack.push(exp);
            return stack;
        }
    }

    public static Stack<TaggedExp> convertOps1 (TREE ops1) {
        if (ops1.getRhs() == MH_Parser.epsilon) {
            return new Stack<TaggedExp>();
        } else {
            MH_EXP exp = convertExp(ops1.getChildren()[1]);
            String tag = ops1.getChildren()[0].getLabel() ;
            Stack<TaggedExp> stack = convertOps1(ops1.getChildren()[2]);
            stack.push(new TaggedExp(exp,tag));
            return stack;
        }
    }
}



/**
 * Expression environments, associating names with closures.
 * For use by runtime system.
 */
class MH_Exp_Env {
    // A map of variable-value
    java.util.TreeMap<String, MH_EXP> env;

    MH_Exp_Env(java.util.TreeMap<String, MH_EXP> env) {
        this.env = env;
    }

    MH_EXP valueOf(String var) throws UnknownVariable {
        if (!env.containsKey(var)) {
            throw new UnknownVariable(var);
        } else {
            return env.get(var);
        }
    }
}
