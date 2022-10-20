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

        //v.hashCode();
        //System.out.printf("%s %s \n", v, map.get(v));

        //make ID aan en vergelijk die
        // .get gebruikt equals al
        // gebruik readID
        Scanner idScanner = new Scanner(v);
        try {
            IdentifierInterface id = readID(idScanner);
            return map.get(id);
        }catch (APException e){
            e.printStackTrace();
        }
        /*for(IdentifierInterface id :map.keySet()){
            if(id.value().equals(v)){
                //System.out.println(map.get(id));
                //System.out.println("value returned");
                return map.get(id);
            }

        }*/
        System.out.println("no key found");

        return null;
    }

    char readLetter(Scanner in ) throws APException {
        if(nextCharIsLetter(in)){
            return nextChar(in);
        }else{
            throw new APException("Error: Identifiers should start with a letter");
        }
    }


    IdentifierInterface readID(Scanner in) throws APException {
        //System.out.println("Creating ID");
        in.useDelimiter("");
        IdentifierInterface id = new Identifier();
       isSpace(in);
        char c = readLetter(in);
        id.init(c);
        while(nextCharIsAlphaNum(in)){
            id.addChar(nextChar(in));
        }
        //System.out.println(nextChar(in));
        //System.out.println("Done creating ID");
        return id;
    }


    T complexFactor(Scanner in) throws APException {
        int open_factors = 0;
        T set ;
        checkCharacter(in,COMPLEX_FACTOR_OPENING_BRACKET);
        if(nextCharIs(in, '(')){
            open_factors +=1;
        }
        System.out.printf("Open factors: %d \n", open_factors);
        set = expression(in);
        System.out.printf("Open factors: %d \n", open_factors);

        while(open_factors >0 ){
            checkCharacter(in, ')');
            open_factors-=1;
        }


        System.out.printf("Open factors: %d \n", open_factors);

        checkCharacter(in,COMPLEX_FACTOR_CLOSING_BRACKET);
        if(open_factors ==0 && nextCharIs(in,')')){
            throw new APException("Missing opening");
        }
        return set;
    }

    T set(Scanner in) throws APException {
        T set = (T) new Set<BigInteger>();
        checkCharacter(in,SET_OPENING_BRACKET);
        isSpace(in);
        if(nextCharIs(in,'}')){
            checkCharacter(in,'}');
            return set;
        }
        set.add(naturalNumber(in));
        isSpace(in);
        while(nextCharIs(in,SET_DELIMITER)){
            checkCharacter(in,SET_DELIMITER);
            isSpace(in);
            if(nextCharIsDigit(in)) {
                BigInteger bint = naturalNumber(in);
                if(!set.elementExists(bint)){
                set.add(bint);
                }
                isSpace(in);
            } else{
                throw new APException("Wrong syntax for set");
            }
        }
        isSpace(in);
        checkCharacter(in,SET_CLOSING_BRACKET);
        return set;
    }


    BigInteger naturalNumber(Scanner in) throws APException{
        BigInteger integer ;
        if (nextDigitIsZero(in)) {
            integer =  new BigInteger(String.valueOf(zero(in)));
            if(nextCharIsDigit(in)){
                throw new APException("natural numbers cannot start with 0");
            }
            return integer;
        }else if(nextDigitIsPositive(in)){
            integer = new BigInteger(positiveNumber(in));
            return integer;
        }else{
            throw new APException("Not a natural number");
    }
    }

    String positiveNumber(Scanner in)throws APException{
        StringBuffer str = new StringBuffer();
        str.append(notZero(in));

        while (nextCharIsDigit(in)){
           str.append(number(in));
        }
        return str.toString();
    }
    char number (Scanner in) throws APException{
        if(nextDigitIsPositive(in)){
            return notZero(in);
        }else {
            return zero(in);
        }
    }

    char notZero(Scanner in) throws APException {
        if(!nextDigitIsPositive(in)){
            throw new APException("Is a zero while it's not allowed");
        }else {
            return nextChar(in);
        }
    }
    char zero(Scanner in) throws APException {
        if(!nextDigitIsZero(in)){
            throw new APException("Is not a zero while needed");
        }else {
            return nextChar(in);
        }
    }
    boolean nextDigitIsPositive(Scanner in){
        return in.hasNext("[1-9]");
    }
    boolean nextDigitIsZero(Scanner in){
        return in.hasNext("0");
    }
    /*
    factor = identifier | complex_factor | set
     */
    T factor(Scanner in) throws APException {
        T set;
        if (nextCharIsLetter(in)) {
            StringBuffer id = new StringBuffer();
            while(nextCharIsLetter(in)||nextCharIsDigit(in)){
                id.append(nextChar(in));
            }
            set = getMemory(id.toString());
        } else if (nextCharIs(in, SET_OPENING_BRACKET)) {
            set = set(in);
        } else if (nextCharIs(in, COMPLEX_FACTOR_OPENING_BRACKET)) {
            set = complexFactor(in);
        } else {
            throw new APException(FACTOR_ERROR);
        }
        isSpace(in);
        return set;
    }

    /*
    term = factor {multipl_op ' ' set}
     */
    T term(Scanner in) throws APException {
        T set;
        isSpace(in);
        set = factor(in);
        while(isMultiplOp(in)){
            nextChar(in);
            isSpace(in);
            set = (T) set.intersection(factor(in));
            isSpace(in);
        }
        return set;
    }

    boolean isMultiplOp(Scanner in) {
        return nextCharIs(in, '*');
    }

    char AdditiveOperator(Scanner in) throws APException {
        if(!isAdditiveOperator(in)){
            throw new APException("Error: Additive operator expected");
        }
        return nextChar(in);
    }

    T additiveOperation(Scanner in, T set) throws APException {

        isSpace(in);

        if (nextCharIs(in, '+')) {
            checkCharacter(in,'+');
                return (T) set.union(term(in));
        } else if(nextCharIs(in,'-')){
            checkCharacter(in,'-');
            return (T) set.difference(term(in));
        } else if(nextCharIs(in,'|')){
            checkCharacter(in,'|');
            return (T) set.symmetricDifference(term(in));
        }else{
            throw new APException("Unknown additive operator");
        }
    }
    T expression(Scanner in) throws APException {
       isSpace(in);

        T set = term(in);
        isSpace(in);
        if(in.hasNext() && !(isAdditiveOperator(in) || nextCharIs(in,')'))){
            throw new APException(EXPRESSION_ERROR);
        }

        isSpace(in);

        while(isAdditiveOperator(in)){
            set = additiveOperation(in,set);
            isSpace(in);

        }
        isSpace(in);
        return set;
    }
    boolean isAdditiveOperator(Scanner in) {
        return (nextCharIs(in, '|') || nextCharIs(in, '+') || nextCharIs(in, '-'));
    }

    void doAssignment(Scanner in) throws APException {
        isSpace(in);
        IdentifierInterface id = readID(in);
        isSpace(in);
        checkCharacter(in,'=');
        isSpace(in);
        T set = expression(in);
        isSpace(in);
       // System.out.println(set.printSet());
        //checkCharacter(in,'\n');
        //System.out.println(id.value());
        //System.out.println(set.printSet());
        map.put(id, set);

        isSpace(in);
    }


    void isSpace(Scanner in){
        while(nextCharIs(in,' ')){
            nextChar(in);
        }
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
            throw new APException("Expected : " + c);
        }
        nextChar(in);
    }

    private boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c + ""));
    }
    private T printStatement(Scanner in) throws APException {
        checkCharacter(in,'?');
        isSpace(in);

        T set = expression(in);

        isSpace(in);
       //checkCharacter(in,'\n');
        System.out.println(set.printSet());
        return set;
    }
    /*
    statement = print statement: "'?'expression" | assignment: "identifier '=' expression" | comment: "/.."
     */
    private T statement(Scanner in) throws APException {
        in.useDelimiter("");
        isSpace(in);
        if (nextCharIs(in, '?')) {
            return printStatement(in);
        } else if (nextCharIsAlphaNum(in)) {
            doAssignment(in);
        } else if (!nextCharIs(in, '/')) {
            throw new APException("Error: invalid starting entry for a statement");
        }
        return null;
    }
    private boolean isEndOfLine(Scanner in){
        isSpace(in);
        return nextCharIs(in,'\n');
    }
    private T program(Scanner in) throws APException {
        T statement = statement(in);
        isEndOfLine(in);
        return statement;
    }

    @Override
    public T eval(String s) {
        Scanner in = new Scanner(s);
        try {
            return program(in);
        } catch (APException e) {
            e.printStackTrace();
        }
        return null;
    }

}
