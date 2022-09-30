package nl.vu.labs.phoenix.ap;

import java.util.Set;

/*
 * Elements : Elements of type T
 * Structure: None
 * Domain   : Collection of elements of same types
 *
 * Constructors:
 * Set():
 * PRE  -
 * POST - A new Set object has been created an initialized with the empty Set
 *
 * Set(Set src): //TODO; copy method
 * A new Set object has been created from the provided Set an initialized with the empty Set
 *
 */
public interface SetInterface<T extends Comparable<T>> {

	boolean add(T t);
	/*
	PRE  -
	POST - True; The element has been inserted
		    False; The element has was already present
	 */

	T get();
	/*
	PRE  - The Set may not be empty \\TODO verbeteren
	POST - An element of the Set has been returned
	 */
	
	boolean remove(T t);
	/*
	PRE  -
	POST - True; The element t has been removed from the Set
		   False; The element t does not exist in the Set
	 */
	int getSize();
	/*
	PRE  -
	POST - The number of elements in the Set has been returned
	 */

	boolean elementExists(Identifier id);
	/*
	@precondition
	@postcondition
		True; The identifier object given in the parameter is equal to an Identifier object in the set
		False; the Identifier object given in the parameter is not equal to any Identifier object in the set
	 */

	Identifier getElement();
	/*
	@precondition
		The set contains at least one element
	@postcondition
		?
	*/
	void removeElement(Identifier id);
	/*
	@precondition
	@postcondition
		The parameter Identifier object has been removed from the set

	 */

	int size();

	SetInterface<T> copy();
	/*
	PRE  -
	POST - A copy of the Set object has been returned
	 */

	SetInterface<T>	union(SetInterface<T> s);
	/*
	PRE -
	POST-   Sucess;A new set has been returned containing all elements of both sets
			Failure; The new set containing all elements of both sets
	*/

	SetInterface<T> intersection(SetInterface<T> s);
	/*
	PRE  -
	POST - A new set has been returned containing all the values of the current set which also exist in s
	 */

	SetInterface<T> difference(SetInterface<T> s);
	/*
	PRE  -
	POST - A new set has been returned containing all elements of the current set which do not exist in s
	 */

	SetInterface<T> symmetricDifference(SetInterface<T> s);
	/*
	PRE  -
	POST - A new set has been returned containing all unique elements existing in both sets
	 */

	// your code here
	void init();
	/*
		PRE  -
		POST - The set is empty
	 */

	boolean ElementExists(T t);
	/*
	PRE -
	POST -
	 */

}
