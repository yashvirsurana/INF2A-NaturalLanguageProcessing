/**
 * File:   MH_Typechecker.java
 * Date:   October 2014
 *
 * Java template file for typechecker component of Informatics 2A Assignment 1.
 * Provides infrastructure for Micro-Haskell typechecking:
 * the core typechecking operation is to be implemented by students.
 */


import java.util.*;
import java.io.*;

/**
 * Typechecker for Micro-Haskell.
 *
 * The core of the typechecker:
 * Computing the MH_TYPE of a given MH_EXP in a given TYPE_ENV.
 * Should raise TypeError if MH_EXP isn't well-typed.
 *
 */
class MH_Typechecker {
    public final static MH_Parser MH_Parser = MH_Type_Impl.MH_Parser;

    // Integer type
    public final static MH_TYPE IntegerType = MH_Type_Impl.IntegerType;

    // Bool type
    public final static MH_TYPE BoolType = MH_Type_Impl.BoolType;

    /**
     * This method computes the type of an expression within a type-environment.
     *
     * You should implement this method!
     *
     * @return the type of expression.
     */
    public final static MH_TYPE computeType(MH_EXP exp, TYPE_ENV env) 
    throws TypeError, UnknownVariable {

        // ADD YOUR CODE HERE
    	if (exp.isVAR()) {
    		return env.typeOf(exp.value());
    	}
    	if (exp.isBOOLEAN()) {
    		return BoolType;
    	}
    	
    	if (exp.isNUM()) {
    		return IntegerType;
    	}
    	
    	if (exp.isPREFIX()) {
    		if (computeType(exp.first(), env).equals(IntegerType)){
    			return IntegerType;
    		}
    		else {
    			throw new TypeError ("Incorrect Data Type - Should be an integer!") ;
    		}
    	}
    	
    	
    	if (exp.isINFIX()) {
    		if (exp.infixOp() == "==") {
    			if (computeType(exp.first(), env).equals(IntegerType) && computeType(exp.second(), env).equals(IntegerType)) {
    				return BoolType;
    			}
    			else {
        			throw new TypeError ("Incorrect Data Type - Should be Boolean!") ;
        		}
    		}
    		if (exp.infixOp() == "<") {
    			if (computeType(exp.first(), env).equals(IntegerType) && computeType(exp.second(), env).equals(IntegerType)) {
    				return BoolType;
    			}
    			else {
        			throw new TypeError ("Incorrect Data Type - Should be Boolean!") ;
        		}
    		}
    		if (exp.infixOp() == "+") {
    			if (computeType(exp.first(), env).equals(IntegerType) && computeType(exp.second(), env).equals(IntegerType)) {
    				return IntegerType;
    			}
    			else {
        			throw new TypeError ("Incorrect Data Type - Should be Integer!") ;
        		}
    		}
    		
    	}
    	if (exp.isIF()) {
    		if (computeType(exp.first(), env).equals(BoolType)) {
    			if (computeType(exp.second(), env).equals(computeType(exp.third(), env))) {
    				return computeType(exp.second(), env);
    			}
    		}
    		else {
    			throw new TypeError ("Incorrect Data Type - condition must be Bool and types after then and else must match");
    		}
    	}
    	
    	if (exp.isAPP()) {
    		 MH_TYPE TEMP1 = computeType(exp.first(), env);
    		 MH_TYPE TEMP2 = computeType(exp.second(), env);
    		 if (TEMP1.left().equals(TEMP2)) {
    			 return (TEMP1.right());
    		 }
    		 else {
    			 throw new TypeError ("Incorrect Data Types!");
    		 }
    	}
    	else return null;
    	
// a :: Integer -> Bool T1 = Integer -> Bool T1.left() = Integer 
// n :: Integer         T2 = Integer
// a n = n > 5
// a 5
// a 5 True 3    	
// (Integer->Bool->Integer)->(Integer->Integer)
    }

    /**
     * An interface of the type environments.
     */
    interface TYPE_ENV {
        MH_TYPE typeOf (String var) throws UnknownVariable;
    }

    static class MH_Type_Env implements TYPE_ENV {
        // Holds the type of variables
        TreeMap<String, MH_TYPE> env;

        /**
	     * Constructor for building a type env from the type decls 
	     * appearing in a program.
         */
	    MH_Type_Env(TREE prog) throws DuplicatedVariable {
	        TREE prog1 = prog;
            TREE typeDecl;
            String var;
            MH_TYPE theType;

	        this.env = new TreeMap<String, MH_TYPE>();
	        while (prog1.getRhs() != MH_Parser.epsilon) {
	            typeDecl = prog1.getChildren()[0].getChildren()[0];
	            var = typeDecl.getChildren()[0].getValue();
	            theType = MH_Type_Impl.convertType(typeDecl.getChildren()[2]);
	            if (env.containsKey(var)) {
                    throw new DuplicatedVariable(var);
	            } else {
                    env.put(var,theType);
                }
	            prog1 = prog1.getChildren()[1];
	        }
	        System.out.println("Type conversions successful.");
	    }

        /**
         * Constructor for cloning a type env
         */
        @SuppressWarnings("unchecked")
        MH_Type_Env(MH_Type_Env given) {
            this.env = (TreeMap<String, MH_TYPE>) given.env.clone();
        }

        /**
         * Returns the type of given variable.
         */
        public MH_TYPE typeOf(String var) throws UnknownVariable {
            MH_TYPE t = (MH_TYPE)(env.get(var));
            if (t == null) throw new UnknownVariable(var);
            else return t;
        }

        /**
         * Augmenting a type env with a list of function arguments.
         * Takes the type of the function, returns the result type.
         */
        MH_TYPE addArgBindings(TREE args, MH_TYPE theType) 
        throws DuplicatedVariable, TypeError {
            TREE args1 = args;
            MH_TYPE theType1 = theType;
            String var;

            while (args1.getRhs() != MH_Parser.epsilon) {
                if (theType1.isArrow()) {
                    var = args1.getChildren()[0].getValue();
                    if (env.containsKey(var)) {
                        throw new DuplicatedVariable(var);
                    } else {
                        this.env.put(var, theType1.left());
                        theType1 = theType1.right();
                        args1 = args1.getChildren()[1];
                    }
                } else {
                    throw new TypeError ("Too many function arguments");
                }
            }

            return theType1;
        }
    }

    public static MH_Type_Env compileTypeEnv(TREE prog)
    throws DuplicatedVariable {
        return new MH_Type_Env(prog);
    }

    /**
     * Building a closure (using lambda) from argument list and body
     */
    public static MH_EXP buildClosure(TREE args, MH_EXP exp) {
        if (args.getRhs() == MH_Parser.epsilon) {
            return exp;
        } else {
            MH_EXP exp1 = buildClosure (args.getChildren()[1], exp);
            String var = args.getChildren()[0].getValue();
            return new MH_Exp_Impl (var, exp1);
        }
    }

    /**
     * Name-closure pairs (result of processing a TermDecl).
     */
    static class Named_MH_EXP {
        String name;
        MH_EXP exp;

        Named_MH_EXP (String name, MH_EXP exp) {
            this.name = name;
            this.exp = exp;
        }
    }

    public static Named_MH_EXP typecheckDecl(TREE decl, MH_Type_Env env) 
    throws TypeError, UnknownVariable, DuplicatedVariable,
           NameMismatchError {
        // typechecks the given decl against the env, 
        // and returns a name-closure pair for the entity declared.
        String theVar = decl.getChildren()[0].getChildren()[0].getValue();
        String theVar1 = decl.getChildren()[1].getChildren()[0].getValue();

        if (!theVar.equals(theVar1)) {
            throw new NameMismatchError(theVar,theVar1);
        }

        MH_TYPE theType = MH_Type_Impl.convertType(
                              decl.getChildren()[0].getChildren()[2]);
        MH_EXP theExp = MH_Exp_Impl.convertExp(
                            decl.getChildren()[1].getChildren()[3]);
        TREE theArgs = decl.getChildren()[1].getChildren()[1];
        MH_Type_Env theEnv = new MH_Type_Env(env);
        MH_TYPE resultType = theEnv.addArgBindings(theArgs, theType);
        MH_TYPE expType = computeType(theExp, theEnv);
        if (expType.equals(resultType)) {
            return new Named_MH_EXP (theVar,buildClosure(theArgs,theExp));
        } else {
            throw new TypeError ("RHS of declaration of " + theVar +
                                 " has wrong type");
        }
    }

    public static MH_Exp_Env typecheckProg (TREE prog, MH_Type_Env env)
    throws TypeError, UnknownVariable, DuplicatedVariable,
           NameMismatchError {

        TREE prog1 = prog;
        TREE theDecl;
        Named_MH_EXP binding;
        TreeMap<String, MH_EXP> treeMap = new TreeMap<String, MH_EXP>();

        while (prog1.getRhs() != MH_Parser.epsilon) {
            theDecl = prog1.getChildren()[0];
            binding = typecheckDecl (theDecl, env);
            treeMap.put(binding.name, binding.exp);
            prog1 = prog1.getChildren()[1];
        }

        System.out.println("Typecheck successful.");
        return new MH_Exp_Env(treeMap);
    }


    /**
     * For testing:
     */
    public static void main (String[] args) throws Exception {
        Reader reader = new BufferedReader (new FileReader (args[0]));
        // try {
        LEX_TOKEN_STREAM MH_Lexer = new CheckedSymbolLexer(new MH_Lexer (reader));
        TREE prog = MH_Parser.parse(MH_Lexer);
        MH_Type_Env typeEnv = compileTypeEnv(prog);
        MH_Exp_Env runEnv = typecheckProg(prog, typeEnv);
        // } catch (Exception x) {
            // System.out.println ("MH Error: " + x.getMessage());
        // }
    }
}
