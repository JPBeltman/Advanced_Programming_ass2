package nl.vu.labs.phoenix.ap;
public class Set<T extends Comparable<T>> implements SetInterface<T> {
	ListInterface<T> list ;

	Set(){
		list= new LinkedList<>();
	}
	@Override
	public boolean add(T t) {

		list.insert(t);

		return false;
	}

	@Override
	public T get() {
		// TODO Auto-generated method stub
		return list.retrieve();
	}

	@Override
	public boolean remove(T t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean elementExists(Identifier id) {
		return false;
	}

	@Override
	public SetInterface<T> copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SetInterface<T> union(SetInterface<T> s) {
		return null;
	}

	@Override
	public SetInterface<T> intersection(SetInterface<T> s) {
		return null;
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> s) {
		return null;
	}

	@Override
	public SetInterface<T> symmetricDifference(SetInterface<T> s) {
		return null;
	}

	@Override
	public void init() {

	}
}