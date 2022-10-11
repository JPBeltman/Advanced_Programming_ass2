package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	HashMap<IdentifierInterface,T> map = new HashMap<>();
	@Override
	public T getMemory(String v) {
		//TODO Implement me
		// Read the identifier v from memory and return
		return map.get(v);
	}

	Identifier readFirstLetter(Scanner in, Identifier id) throws APException{
		if (in.hasNext("[a-zA-Z]")){
			id.init(in.next().charAt(0));
		}else{
			throw new APException("Wrong first");
		}
		//read the rest of the identifier

		readTheRest(in,id);
		return id;
	}

	Identifier readTheRest(Scanner in, Identifier id)throws APException{
		boolean expectEquals = false;
		while(in.hasNext()){
		if(!in.hasNext("[a-zA-Z0-9]")){
			throw new APException("Wrong after 1st");
		} else if(in.hasNext("\\s*")){
			expectEquals = true;
			in.next();
		} else if(expectEquals){
			if(in.hasNext("=")){
				in.next();
				return id;
			}else{
				throw new APException("= expected");
			}
		}else{
			id.addChar(in.next().charAt(0));
		}

		}
		return id;
	}
	Identifier createIdentifier(Scanner in) throws APException{
		Identifier id = new Identifier();
		//map.put(id,);
		readFirstLetter(in, id);
		// while no '=' is encountered
		//while(!in.hasNext("=")){
			// check if next is a letter, assign to new id
			// check if a space exists
			//remove and place in readFirstLetter since error with " ="

			//System.out.println(in.next());
		//}
		return id;
	}
	@Override
	public T eval(String s) {
		Scanner in = new Scanner(s);
		in.useDelimiter("");
		//ignore spaces before any character
		in.skip("\\s*");
		// check if line is epxression
		if(in.hasNext("\\?")){
			// calculate expression and print
			System.out.println(s);
		// check if line is comment
		}else if(in.hasNext("\\/")){
			return null;
		// check if line is assignment
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
