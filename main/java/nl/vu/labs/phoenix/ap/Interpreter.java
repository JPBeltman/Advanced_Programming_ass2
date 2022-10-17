package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
/**
 * A set interpreter for sets of elements of type T
 */

/**
 *
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
    HashMap<IdentifierInterface, T> map = new HashMap<>();

    final static char SET_OPENING_BRACKET = '{';
    final static char SET_CLOSING_BRACKET = '}';
    final static char SET_DELIMITER = ',';
    final static String SET_STARTING_ERROR = "ERROR: a set should start with a natural number";
    final static String SET_WRONG_DIGIT = "Error: Only natural numbers allowed";
    final static String SET_WRONG_SEPARATOR = "Error: values of the set should be separated by a comma";
    final static char COMPLEX_FACTOR_OPENING_BRACKET = '(';
    final static char COMPLEX_FACTOR_CLOSING_BRACKET = ')';
    final static String FACTOR_ERROR = "Error: Factor does not start with identifier or is missing an opening bracket for complex factor or set";
    final static String EXPRESSION_ERROR ="Error: no additive operator while more terms exist/ missing or unknown operator";
    @Override
    public T getMemory(String v) {
        //TODO Implement me
        // Read the identifier v from memory and return
        //v.hashCode();
        System.out.printf("%s %s \n", v, map.get(v));
        return map.get(v);
    }

    IdentifierInterface readTheRest(Scanner in, IdentifierInterface id) throws APException {
        in.useDelimiter("");
        while (in.hasNext()) {
            if (in.hasNext("[a-zA-Z0-9]")) {
                id.addChar(in.next().charAt(0));
            } else {
                return id;
            }
            throw new APException("Wrong after 1st");
        }
        return id;
    }

    IdentifierInterface readFirstLetter(Scanner in, IdentifierInterface id) throws APException {
       in.useDelimiter("");
        if (in.hasNext("[a-zA-Z]")) {
            id.init(in.next().charAt(0));
        } else {
            throw new APException("Wrong first letter");
        }
        readTheRest(in, id);
        return id;
    }

    char readLetter(Scanner in ) throws APException {
        if(nextCharIsLetter(in)){
            return nextChar(in);
        }else{
            throw new APException("Wrong character");
        }
    }


    IdentifierInterface readID(Scanner in) throws APException {
        //System.out.println("Creating ID");
        in.useDelimiter("");
        IdentifierInterface id = new Identifier();

        while (isWhiteSpace(in)) {
            nextChar(in);
        }

        char c = readLetter(in);
        id.init(c);

        while(nextCharIsAlphaNum(in)){
            id.addChar(nextChar(in));
        }
        //System.out.println(nextChar(in));
        //System.out.println("Done creating ID");
        return id;
    }

    SetInterface<BigInteger> createComplexFactor(Scanner in) throws APException {
        SetInterface<BigInteger> set ;

        set = doExpression(in);

        return set;
    }
    SetInterface<BigInteger> doComplexFactor(Scanner in) throws APException {
        SetInterface<BigInteger> set ;
        StringBuffer complexFactorContents = new StringBuffer();
        in.useDelimiter("");
        while(in.hasNext() && !nextCharIs(in,COMPLEX_FACTOR_OPENING_BRACKET)){
          if(nextCharIs(in,' ')){
              nextChar(in);
          }
            complexFactorContents.append(in.next());
        }
        //System.out.println(complexFactorContents);
        set = createComplexFactor(new Scanner(complexFactorContents.toString()));
        //set = doExpression(in);
        return set;
    }

    SetInterface<BigInteger> createSet(Scanner in) throws APException {
        SetInterface<BigInteger> set = new Set<>();
        in.skip("\\s*");
        if(!nextCharIsDigit(in)) {
            if(nextCharIs(in,SET_CLOSING_BRACKET)){
                return set;
            }else{
                throw new APException(SET_STARTING_ERROR);
            }
        }else{
            set.add(in.nextBigInteger());
        }

        while(nextCharIs(in,SET_DELIMITER)){
            checkCharacter(in,SET_DELIMITER);
            if (nextCharIsDigit(in)) {
                set.add(in.nextBigInteger());
            }

        }
        //System.out.println(set.printSet().toString());
        return set;
    }
    SetInterface<BigInteger> doSet(Scanner in) throws APException {
        SetInterface<BigInteger> set;
        /*StringBuffer setContents = new StringBuffer();

        in.useDelimiter("");
        while(in.hasNext() && !nextCharIs(in,'}')){
            if(nextCharIs(in, ' ')){
                nextChar(in);
            }else{
            setContents.append(in.next());
            }
        }*/

        set = createSet(in);
        return set;
    }

    /*
    factor = identifier | complex_factor | set
     */
    SetInterface<BigInteger> doFactor(Scanner in) throws APException {
        SetInterface<BigInteger> set;
        if (nextCharIsLetter(in)) {
            set =map.get(in.next());
        } else if (nextCharIs(in, SET_OPENING_BRACKET)) {
            nextChar(in);
            set = createSet(in);
            checkCharacter(in,SET_CLOSING_BRACKET);
        } else if (nextCharIs(in, COMPLEX_FACTOR_OPENING_BRACKET)) {
            nextChar(in);
            set = doComplexFactor(in);
            checkCharacter(in,COMPLEX_FACTOR_CLOSING_BRACKET);
        } else {
            throw new APException(FACTOR_ERROR);
        }
        return set;
    }

    /*
    term = factor {multipl_op ' ' set}
     */
    SetInterface<BigInteger> doTerm(Scanner in) throws APException {
        SetInterface<BigInteger> set;
        in.skip("\\s*");
        set = doFactor(in);
        while(isMultiplOp(in)){
           set = set.intersection(doFactor(in));
        }
        return set;
    }

    boolean isMultiplOp(Scanner in) {
        return nextCharIs(in, '*');
    }

    SetInterface<BigInteger> doExpression(Scanner in) throws APException {
        SetInterface<BigInteger> set;
        in.skip("\\s*");
        set = doTerm(in);

        if(in.hasNext() && !isAdditiveOperator(in)){
            throw new APException(EXPRESSION_ERROR);
        }

        while(isAdditiveOperator(in)){
            SetInterface<BigInteger> set2;
            char operator = nextChar(in);
            set2 = doTerm(in);
            switch (operator){
                case '+': return set.union(set2);
                case '-':  return set.difference(set2);
                case '|': return set.symmetricDifference(set2);
            }

        }
        return set;
    }
    boolean isAdditiveOperator(Scanner in) {
        return (nextCharIs(in, '|') || nextCharIs(in, '+') || nextCharIs(in, '-'));
    }

    void doAssignment(Scanner in) throws APException {
        in.skip("\\s*");

        IdentifierInterface id = readID(in);
        checkCharacter(in,'=');
        T set = (T) doExpression(in);
        //checkCharacter(in,'\n');

        map.put(id, set);
    }

    /*
    statement = print statement: "'?'expression" | assignment: "identifier '=' expression" | comment: "/.."
     */
    private void readStatement(Scanner in) throws APException {
        in.useDelimiter("");
        in.skip("\\s*");
        if (nextCharIs(in, '?')) {
            nextChar(in);
            doExpression(in);
            System.out.println("is express");
        } else if (nextCharIsLetter(in)) {
            System.out.println("assignment");
            doAssignment(in);

        } else if (!nextCharIs(in, '/')) {
            throw new APException("Error: invalid starting entry for the statement");
        }


    }


    boolean isEqualsSign(Scanner in) {
        return in.hasNext("=");
    }


    boolean isWhiteSpace(Scanner in) {
        return in.hasNext("\\s*");
    }

    char nextChar(Scanner in) {
        return in.next().charAt(0);

    }
    boolean nextCharIsAlphaNum(Scanner in) {
        return in.hasNext("[a-zA-Z0-9]");
    }

    private boolean nextCharIsLetter(Scanner in) {
        return in.hasNext("[a-zA-Z]");
    }

    private boolean nextCharIsDigit(Scanner in) {
        return in.hasNext("[0-9]");
    }

    void checkCharacter(Scanner in, char c) throws APException {
        if (!nextCharIs(in, c)) {
            String s = "";
            throw new APException("Expected : " + c);
        }
        nextChar(in);
    }

    private boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c + ""));
    }

    @Override
    public T eval(String s) {
        Scanner in = new Scanner(s);
        try {
            readStatement(in);
        } catch (APException e) {
            e.printStackTrace();
        }
        return null;
    }

}
