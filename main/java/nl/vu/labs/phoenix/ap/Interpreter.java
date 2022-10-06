package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	HashMap<Identifier,T> map = new HashMap<>();
	@Override
	public T getMemory(String v) {
		//TODO Implement me
		// Read the identifier v from memory and return
		return map.get(v);
	}

	Identifier createIdentifier(Scanner in) throws APException{
		Identifier id = new Identifier();
		//map.put(id,);
		// while no '=' is encountered
		while(!in.hasNext("=")){
			// check validity of identifier
			if(in.hasNext( "\\s*")){

			}
			System.out.println(in.next());
		}
		return id;
	}
	@Override
	public T eval(String s) {
		Scanner in = new Scanner(s);
		in.useDelimiter("");
		in.skip("\\s*"); //ignore spaces before any character
		if(in.hasNext("\\?")){
			// calculate expression and print
			System.out.println(s);
		}else if(in.hasNext("\\/")){
			return null;
		}else if(in.hasNext("[a-zA-z]")){
			try {
				createIdentifier(in);
				return null; //return the object
			}catch (APException e){
				e.printStackTrace();
			}
		}
		return null;
	}

}
