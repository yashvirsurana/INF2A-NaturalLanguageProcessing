/**
 * File:   Types.java
 * Date:   October 2014
 *
 * Java source file provided for Informatics 2A Assignment 1 (2014).
 * Provides abstract syntax trees for types in Micro-Haskell:
 * effectively, trees for the simplified grammar
 *     Type  -->   Integer | Bool | Type->Type
 */


/**
 * An interface for all Micro-Haskell types.
 */
interface MH_TYPE {
    /**
     * Returns true if the type is integer, otherwise false.
     */
    boolean isInteger();

    /**
     * Returns true if the type is boolean, otherwise false.
     */
    boolean isBool();

    /**
     * Returns true if the type is arrow, otherwise false.
     */
    boolean isArrow();

    /**
     * Returns left constituent of arrow type.
     */
    MH_TYPE left();

    /**
     * Returns right constituent of arrow type.
     */
    MH_TYPE right();

    /**
     * Returns true if given type is equal to this, otherwise false.
     */
    boolean equals(MH_TYPE other);

    /**
     * For testing/debugging.
     */
    String toString();
}

/**
 * This class implements MH_Type.
 */
class MH_Type_Impl implements MH_TYPE {
    // Constants for MH type Integer
    public static MH_TYPE IntegerType = new MH_Type_Impl (0,null,null);

    // Constants for MH type Bool
    public static MH_TYPE BoolType = new MH_Type_Impl (1,null,null);

    int kind;
    MH_TYPE leftChild;
    MH_TYPE rightChild;

    // Constructor that should be called internally.
    MH_Type_Impl(int kind, MH_TYPE leftChild, MH_TYPE rightChild) {
        this.kind = kind;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    // Constructor for arrow types.
    MH_Type_Impl (MH_TYPE leftChild, MH_TYPE rightChild) {
        this(2, leftChild, rightChild);
    }

    public boolean isInteger() {
        return (kind == 0);
    }

    public boolean isBool() {
        return (kind == 1);
    }

    public boolean isArrow() {
        return (kind == 2);
    }

    public MH_TYPE left() {
        return leftChild;
    }

    public MH_TYPE right() {
        return rightChild;
    }

    public boolean equals(MH_TYPE other) {
        return (
            (this.isInteger() && other.isInteger()) ||
            (this.isBool() && other.isBool()) ||
            (this.isArrow() && other.isArrow() && 
             this.left().equals(other.left()) &&
             this.right().equals(other.right()))
        );
    }

    public String toString() {
        if (this.isInteger()) {
            return "Integer";
        } else if (this.isBool()) {
            return "Bool";
        } else {
            return ("(" + this.left().toString() + " -> "
                + this.right().toString() + ")");
        }
    }

    //--- Conversion from parse trees to ASTs for MH types ---//

    public final static MH_Parser MH_Parser = new MH_Parser();

    /**
     * Method 'convertType' accepts any well-formed tree whose root node has
     * label #Type, and returns the corresponding abstract syntax tree 
     * (see Types.java for the definition of ASTs for types).
     *
     * The relevant LL(1) grammar rules are:
     *
     * #Type    -> #Type1 #TypeOps
     * #Type1   -> Integer | Bool | ( #Type )
     * #TypeOps -> epsilon | -> #Type
     *
     * Since trees with label #Type can have subtrees of label #Type1
     * and vice versa, these two methods are mutually recursive.
     */
    public static MH_TYPE convertType(TREE tree) {
        if (tree.getChildren()[1].getRhs() == MH_Parser.epsilon) {
            // Case if TypeOps is empty
            return convertType1(tree.getChildren()[0]);
        } else {
            // Case if TypeOps is -> Type
            MH_TYPE left = convertType1(tree.getChildren()[0]);
            MH_TYPE right = convertType(tree.getChildren()[1].getChildren()[1]);
            return new MH_Type_Impl(left,right);
        } 
    }

    /**
     * Method 'convertType1' does the same but for #Type1.
     */
    public static MH_TYPE convertType1(TREE tree1) {
        if (tree1.getRhs() == MH_Parser.Integer) { 
            return MH_Type_Impl.IntegerType;
        } else if (tree1.getRhs() == MH_Parser.Bool) {
            return MH_Type_Impl.BoolType;
        } else {
            // This covers case in which tree1 matches ( Type )
            return convertType (tree1.getChildren()[1]);
        }
    }
}

//--- Errors that may arise during typechecking ---//

class TypeError extends Exception {
    TypeError(String s) {
        super ("Type error: " + s);
    }
}

class UnknownVariable extends Exception {
    UnknownVariable (String var) {
        super("Variable " + var + " not in scope.");
    }
}

class DuplicatedVariable extends Exception {
    DuplicatedVariable (String var) {
        super("Duplicated variable " + var);
    }
}

class NameMismatchError extends Exception {
    NameMismatchError (String var1, String var2) {
        super("Name mismatch between " + var1 + " and " + var2);
    }
}

