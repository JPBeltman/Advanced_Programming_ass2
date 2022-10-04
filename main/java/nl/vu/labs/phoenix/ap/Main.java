package nl.vu.labs.phoenix.ap;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.HashMap;

public class Main {
	
	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		Scanner in = new Scanner(System.in);

		while(in.hasNextLine()) {
			interpreter.eval(in.nextLine());
		}
		//1. Create a scanner on System.in
		//2. call interpreter.eval() on each line
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
