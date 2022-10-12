package nl.vu.labs.phoenix.ap;
public class Set<T extends Comparable<T>> implements SetInterface<T> {
	ListInterface<T> list ;

	Set(){
		list= new LinkedList<T>();
	}

	@Override
	public void init() {

	}
	@Override
	public boolean add(T t) {
		if(!elementExists(t)){
			list.insert(t);
			return true;
		}else{
		return false;
		}
	}

	@Override
	public T get() {
		return list.retrieve();
	}

	@Override
	public boolean remove(T t) {
		if(list.find(t)){
			list.remove();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean elementExists(T t) {
		return list.find(t);
	}

	@Override
	public SetInterface<T> copy() {
		// kijk naar ass1
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


}