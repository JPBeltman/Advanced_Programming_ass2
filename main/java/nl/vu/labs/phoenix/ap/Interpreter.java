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

    @Override
    public T getMemory(String v) {
        //TODO Implement me
        // Read the identifier v from memory and return
        //v.hashCode();
        System.out.printf("%s %s \n", v, map.get(v));
        return map.get(v);
    }

    //  T readExpression() {
    //   return t;
    //}

    IdentifierInterface readTheRest(Scanner in, IdentifierInterface id) throws APException {
        in.useDelimiter("");
        while (in.hasNext()) {
            if (in.hasNext("[a-zA-Z0-9]")) {
                // System.out.println(in.next());
                id.addChar(in.next().charAt(0));
            } else if (in.hasNext("=") || in.hasNext(" ")) {
                //System.out.println(in.next());
                return id;
            } else {
                throw new APException("Wrong after 1st");
            }

        }
        return id;
    }

    IdentifierInterface readFirstLetter(Scanner in, IdentifierInterface id) throws APException {
       in.useDelimiter("");
       //System.out.println(in.next());
        if (in.hasNext("[a-zA-Z]")) {
            id.init(in.next().charAt(0));
        } else {
            throw new APException("Wrong first letter");
        }
        readTheRest(in, id);
        return id;
    }

    //map.put(id,);
    IdentifierInterface readID(Scanner in) throws APException {
        in.useDelimiter("");
        IdentifierInterface id = new Identifier();

        while (isWhiteSpace(in)) {
            in.next();
        }
        readFirstLetter(in, id);
        if (isWhiteSpace(in)) {
            in.next();
        }
        if (isEqualsSign(in)) {
            //System.out.println(id.value());
            in.next();
            return id;
        } else {
            throw new APException("Identifier is wrong");
        }


    }

    SetInterface<BigInteger> doComplexFactor(Scanner in) throws APException {
        SetInterface<BigInteger> set ;

        set = doExpression(in);
        if (!nextCharIs(in, ')')) {
            throw new APException("Error: ')'expected");
        }
        return set;
    }


    SetInterface<BigInteger> doSet(Scanner in) throws APException {
        SetInterface<BigInteger> set = new Set<>();
        //System.out.println(in.next());

        if(nextCharIs(in,'}')){
            nextChar(in);
            return set;
        }
        if(in.hasNext("[0-9]+}")){
            BigInteger b = new BigInteger(String.valueOf(in.next().charAt(0)));
            set.add(b);
            System.out.println("in last el");
            return set;
        }
        in.skip("\\s*");
        in.useDelimiter(",");
        while (in.hasNext() && !nextCharIs(in,'}')) {
            System.out.println("in loop");

            if(in.hasNext("[0-9]+}")){
                BigInteger b = new BigInteger(String.valueOf(in.next().charAt(0)));
                set.add(b);
                System.out.println("in last el");
                return set;
            }
           // System.out.println(in.next());
            if(!nextCharIsDigit(in)){
                throw new APException("Error: Only natural numbers allowed");
            }
            set.add(in.nextBigInteger());

        }
        if (!in.hasNext() && !nextCharIs(in, '}')) {
            throw new APException("Error: no closing bracket for the set");

        }//


        return set;
    }

    /*
    factor = identifier | complex_factor | set
     */
    SetInterface<BigInteger> doFactor(Scanner in) throws APException {
        SetInterface<BigInteger> set = new Set<>();
        if (nextCharIsLetter(in)) {
            map.get(in.next());
            readID(in);
        } else if (nextCharIs(in, '{')) {
            nextChar(in);
            StringBuffer setContents = new StringBuffer();

            set = doSet(in);

        } else if (nextCharIs(in, '(')) {
            nextChar(in);
            doComplexFactor(in);
        } else {
            throw new APException("Error: Factor does not start with identifier or is missing an opening bracket for complex factor or set");
        }
        return set;
    }

    /*
    term = factor {multipl_op ' ' set}
     */
    SetInterface<BigInteger> doTerm(Scanner in) throws APException {
        SetInterface<BigInteger> set;

        set = doFactor(in);
        while(isMultiplOp(in)){
            set.intersection(doFactor(in));
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
            throw new APException("Error: no additive operator while more terms exist");
        }

        while(isAdditiveOperator(in)){
            SetInterface<BigInteger> set2 = null;
            char operator = nextChar(in);
            set2 = doTerm(in);
            switch (operator){
                case '+':  set.union(set2);
                case '-':  set.difference(set2);
                case '|': set.symmetricDifference(set2);
            }

        }
        return set;
    }
    boolean isAdditiveOperator(Scanner in) {
        return (nextCharIs(in, '|') || nextCharIs(in, '+') || nextCharIs(in, '-'));
    }

    void doAssignment(Scanner in) throws APException {
        IdentifierInterface id = readID(in);
        T set = (T) doExpression(in);


        //var set = readExpression();
        //T set = (T) new Set<BigInteger>();
        //set.add(6);

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
    boolean isAlphaNum(Scanner in) {
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
            throw new APException("Expected %s" + c);
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
