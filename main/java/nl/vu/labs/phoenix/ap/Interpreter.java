package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	HashMap<Identifier,Set> map = new HashMap<>();
	@Override
	public T getMemory(String v) {
		//TODO Implement me
		// Read the identifier v from memory and return

		return null;
	}

	@Override
	public T eval(String s) {
		//TODO Implement me
		// evaluate the expression and return:
		// if print statement (?): the calculation or value of identifier
		// if comment (/): null
		// if assignment (=): null

			if (s.equals("/")){
				return null;
			}else if(s.startsWith("?")){
				//calculation
			}


		return null;
	}

}
