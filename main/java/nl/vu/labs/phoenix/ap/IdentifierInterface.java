package nl.vu.labs.phoenix.ap;
/*
	@elements
		Characters of type char
	@structure
		linear
	@domain
		row of alphanumeric characters, of at least size one, first a letter
*/

/*
	Constructor;
	@precondition
	@postcondition
		A new Identifier object has been created and is initialized with at least a single character

    Copy constructor;
    @precondition
    @postcondition
        A duplicate of the Identifier object src has been returned as a new Identifier object
 */
public interface IdentifierInterface {
    String value();
	/*
	 @precondition
	    The identifier is not empty
	 @postcondition
	    The value of the Identifier has been returned md
	 */

    // your code here

    Identifier init(char c);
    /*
    @precondition
        the value of character c is a letter
    @postcondition
        The Identifier object contains only the character c

     */
    int getSize();

    /*
    @precondition
    @postcondition
        The current length of the identifier has been returned
     */
    Identifier addChar(char c);
	/*
	@precondition
		the character c is an alphanumeric character
	@postcondition
		The identifier contains the character c at the last position
	*/

    char getChar(int index);
	/*
	@precondition
	@postcondition
		The character at the 'index' position within the Identifier object has been returned
	*/

    boolean isEqual(Identifier id);
	/*
	@precondition
	@postcondition
		True; The Identifier object given in the parameter contains the same sequence
		of alphanumeric characters as the current Identifier object
		False; The Identifier object given in the parameter does not contain the same sequence as the current Identifier object
	 */

    //TODO add Hashcode


}
