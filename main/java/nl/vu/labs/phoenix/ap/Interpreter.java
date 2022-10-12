package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;
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
        System.out.printf("%s %s \n",v,map.get(v));
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
            } else if (in.hasNext("=" )|| in.hasNext(" ")) {
                //System.out.println(in.next());
                return id;
            }else{
                throw new APException("Wrong after 1st");
            }

        }
        return id;
    }
    IdentifierInterface readFirstLetter(Scanner in, IdentifierInterface id) throws APException {
        if (in.hasNext("[a-zA-Z]")) {
            id.init(in.next().charAt(0));
        } else {
            throw new APException("Wrong first letter");
        }
        readTheRest(in,id);
        return id;
    }

    //map.put(id,);
    IdentifierInterface readID(Scanner in) throws APException {
        in.useDelimiter("");
        IdentifierInterface id = new Identifier();

        while (isWhiteSpace(in)){
            in.next();
        }
        readFirstLetter(in,id);
        if(isWhiteSpace(in)){
            in.next();
        }
        if(isEqualsSign(in)){
            //System.out.println(id.value());
            in.next();
            return id;
        }else{
            throw new APException("Identifier is wrong");
        }


    }
    boolean isEqualsSign(Scanner in) {
        return in.hasNext("=");
    }

    void doExpress(){
        //
        //T set = (T) new Set<BigInteger>();
    }
    void doAssignment(Scanner in) throws APException {
        IdentifierInterface id = readID(in);

        //var set = readExpression();

        //set.add(6);
        //map.put(id, set.get());
    }


    private void readStatement(Scanner in) throws APException {
        in.useDelimiter("");
        in.skip("\\s*");
        if (isPrintStatement(in)) {
            in.next();
            // calculate expression
            // print expression
            System.out.println("is express");
        } else if (isComment(in)) {
            System.out.println("comment");
            return;
        } else if (isAssignment(in)) {
            System.out.println("assignment");
            doAssignment(in);
        }else{
            throw new APException("Wrong statement entry");
        }
    }

    boolean isComment(Scanner in) {
        return in.hasNext("\\/");
    }

    boolean isPrintStatement(Scanner in) {
        return in.hasNext("\\?");
    }

    boolean isAssignment(Scanner in) {
        return in.hasNext("[a-zA-Z0-9]");
    }

    boolean isWhiteSpace(Scanner in){
        return in.hasNext("\\s*");
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
