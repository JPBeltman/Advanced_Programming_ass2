package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
/**
 * A set interpreter for sets of elements of type T
 */


public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
    HashMap<IdentifierInterface, T> map = new HashMap<>();

    final static char SET_OPENING_BRACKET = '{';
    final static char SET_CLOSING_BRACKET = '}';
    final static char SET_DELIMITER = ',';
    final static char COMPLEX_FACTOR_OPENING_BRACKET = '(';
    final static char COMPLEX_FACTOR_CLOSING_BRACKET = ')';

    final static String SET_STARTING_ERROR = "ERROR: a set should start with a natural number";
    final static String SET_WRONG_DIGIT = "Error: Only natural numbers allowed";
    final static String SET_WRONG_SEPARATOR = "Error: values of the set should be separated by a comma";
    final static String SET_WRONG_SYNTAX = "Error: Wrong syntax. Should be natNumber {',' natNumber}";

    final static String NATURAL_NUMBERS_POS_CANNOT_START_WITH_0 = "Error: Positive natural numbers cannot start with a 0";
    final static String NATURAL_NUMBERS_INVALID = "Error: Input is not a valid natural number";
    final static String NATURAL_NUMBERS_INVALID_ZERO = "Is a zero while it's not allowed";
    final static String NATURAL_NUMBERS_NOT_ZERO = "ERROR: Expected a zero";


    final static String FACTOR_ERROR = "Error: Factor does not start with identifier or is missing an opening bracket for complex factor or set";
    final static String EXPRESSION_ERROR ="Error: no additive operator while more terms exist/ missing or unknown operator";
final static String ADDITIVE_OPERATOR_UNKNOWN_ERROR = "Unknown additive operator";
final static String ENDOFLINE_INPUT_ERROR = "Error: input detected between end of statement and end of line";
final static String STATEMENT_INVALID_STARTING_ENTRY="Error: invalid starting entry for a statement";
final static String PRINT_STATEMENT_ERROR_ENDOFLINE = "Error: input detected between end of statement and end of line";
    @Override
    public T getMemory(String v) {
        Scanner idScanner;
        IdentifierInterface id;

        try {
            idScanner = new Scanner(v);
            id = identifier(idScanner);

            idScanner.close();

            return map.get(id);
        }catch (APException e){
            e.printStackTrace();
        }

        System.out.println("no key found");
        return null;
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
            throw new APException(NATURAL_NUMBERS_INVALID_ZERO);
        }else {
            return nextChar(in);
        }
    }

    char zero(Scanner in) throws APException {
        if(!nextDigitIsZero(in)){
            throw new APException(NATURAL_NUMBERS_NOT_ZERO);
        }else {
            return nextChar(in);
        }
    }

    boolean nextDigitIsPositive(Scanner in){
        return in.hasNext("[1-9]");
    }

    boolean nextDigitIsZero(Scanner in){
        return nextCharIs(in,'0');
    }

    BigInteger positiveNum(Scanner in){
        //!nextDigitIsZero(in);

        return null;
    }
    String positiveNumber(Scanner in)throws APException{
        StringBuffer str = new StringBuffer();
        str.append(notZero(in));

        while (nextCharIsDigit(in)){
            str.append(number(in));
        }
        return str.toString();
    }
    BigInteger naturalNumber(Scanner in) throws APException{
        BigInteger integer ;

        if (nextDigitIsZero(in)) {
            integer =  new BigInteger(String.valueOf(zero(in)));
            if(nextCharIsDigit(in)){
                throw new APException(NATURAL_NUMBERS_POS_CANNOT_START_WITH_0);
            }

            return integer;
        }else if(nextDigitIsPositive(in)){
            integer = new BigInteger(positiveNumber(in));

            return integer;
        }else{
            throw new APException(NATURAL_NUMBERS_INVALID);
        }
    }

    T set(Scanner in) throws APException {
        T set = (T) new Set<BigInteger>();

        checkCharacter(in,SET_OPENING_BRACKET);
        isSpace(in);
        if(nextCharIs(in,SET_CLOSING_BRACKET)){
            nextChar(in);
            //checkCharacter(in,SET_CLOSING_BRACKET);
            return set;
        }

        set.add(naturalNumber(in));
        isSpace(in);
        while(nextCharIs(in,SET_DELIMITER)){
            nextChar(in);//checkCharacter(in,SET_DELIMITER);
            isSpace(in);
            if(nextCharIsDigit(in)) {
                BigInteger bint = naturalNumber(in);
                set.add(bint);
                isSpace(in);
            } else{
                throw new APException(SET_WRONG_SYNTAX); // Error: natural number expected after set delimiter
            }
        }
        checkCharacter(in,SET_CLOSING_BRACKET);

        return set;
    }
    char readLetter(Scanner in ) throws APException {
        if(nextCharIsLetter(in)){
            return nextChar(in);
        }else{
            throw new APException("Error: Identifiers should start with a letter");
        }
    }
    IdentifierInterface identifier(Scanner in) throws APException {
        in.useDelimiter("");
        IdentifierInterface identifier = new Identifier();

        identifier.init(readLetter(in));
        while(nextCharIsAlphanumeric(in)){
            identifier.addChar(nextChar(in));
        }

        return identifier;
    }
    T complexFactor(Scanner in) throws APException {
        T expression ;

        checkCharacter(in,COMPLEX_FACTOR_OPENING_BRACKET);
        expression = expression(in);
        checkCharacter(in,COMPLEX_FACTOR_CLOSING_BRACKET);

        return expression;
    }

    T factor(Scanner in) throws APException {
        T factor;

        isSpace(in);
        if (nextCharIsLetter(in)) {
            IdentifierInterface id = identifier(in);
            factor = map.get(id);
        } else if (nextCharIs(in, SET_OPENING_BRACKET)) {
            factor = set(in);
        } else if (nextCharIs(in, COMPLEX_FACTOR_OPENING_BRACKET)) {
            factor = complexFactor(in);
        } else {
            throw new APException(FACTOR_ERROR);
        }
        isSpace(in);

        return factor;
    }

    T multiplicativeOperation(Scanner in, T factor) throws APException {
        nextChar(in);
        return (T) factor.intersection(factor(in));

    }

    T term(Scanner in) throws APException {
        T term;

        term = factor(in);
        while(isMultiplOp(in)){
            term = multiplicativeOperation(in,term);//(T) term.intersection(factor(in));
        }

        return term;
    }

    T additiveOperation(Scanner in, T term) throws APException {
        switch(nextChar(in)){
            case '+':  return (T) term.union(term(in));
            case '-': return (T) term.difference(term(in));
            case '|': return (T) term.symmetricDifference(term(in));
            default:  throw new APException(ADDITIVE_OPERATOR_UNKNOWN_ERROR);
        }
        /*if (nextCharIs(in, '+')) {
            checkCharacter(in,'+');
                return (T) set.union(term(in));
        } else if(nextCharIs(in,'-')){
            checkCharacter(in,'-');
            return (T) set.difference(term(in));
        } else if(nextCharIs(in,'|')){
            checkCharacter(in,'|');
            return (T) set.symmetricDifference(term(in));
        }else{
            throw new APException(ADDITIVE_OPERATOR_UNKNOWN_ERROR);
        }*/
    }
    T expression(Scanner in) throws APException {
        T expression;

        expression = term(in);

        if(in.hasNext() ){
            if(nextCharIs(in,COMPLEX_FACTOR_CLOSING_BRACKET)){
                return expression;
            }else if(!isAdditiveOperator(in) ){
                throw new APException(EXPRESSION_ERROR);
            }

        }
        while(isAdditiveOperator(in)){
            expression = additiveOperation(in,expression);
        }

        return expression;
    }
    void assignment(Scanner in) throws APException {
        IdentifierInterface id;
        T expression;

        id = identifier(in);
        isSpace(in);
        checkCharacter(in,'=');
        expression = expression(in);
        endOfLine(in);

        map.put(id, expression);
    }

    private T printStatement(Scanner in) throws APException {

        checkCharacter(in,'?');
        T print_statement = expression(in);
        endOfLine(in);

        System.out.println(print_statement.printSet());
        return print_statement;
    }
    private T statement(Scanner in) throws APException {
        in.useDelimiter("");
        isSpace(in);
        if (nextCharIs(in, '?')) {
            return printStatement(in);
        } else if (nextCharIsAlphanumeric(in)) {
            assignment(in);
        } else if (!nextCharIs(in, '/')) {
            throw new APException(STATEMENT_INVALID_STARTING_ENTRY);
        }
        return null;
    }

    private T program(Scanner in) throws APException {
        T statement = statement(in);

        in.close();
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
    boolean isMultiplOp(Scanner in) {
        return nextCharIs(in, '*');
    }
    private boolean isEndOfLine(Scanner in){
        isSpace(in);
        return nextCharIs(in,'\n');
    }
    boolean isAdditiveOperator(Scanner in) {
        return (nextCharIs(in, '|') || nextCharIs(in, '+') || nextCharIs(in, '-'));
    }

    void endOfLine(Scanner in) throws APException {
        if(in.hasNext() && !isEndOfLine(in)){
            throw new APException(ENDOFLINE_INPUT_ERROR);
        }
    }
    void isSpace(Scanner in){
        while(nextCharIs(in,' ')){
            nextChar(in);
        }
    }

    char nextChar(Scanner in) {
        return in.next().charAt(0);

    }
    boolean nextCharIsAlphanumeric(Scanner in) {
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
}
