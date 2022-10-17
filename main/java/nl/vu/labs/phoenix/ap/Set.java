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

	public StringBuffer printSet(){
		StringBuffer result =new StringBuffer();
		this.list.goToFirst();
		result.append(this.get());
		while(this.list.goToNext()){
			result.append(this.get());
		}
		return result;
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
		SetInterface<T> newSet = new Set<>();
		this.list.goToFirst();
		for (int i =0; i< this.size();i++){
			newSet.add(this.list.retrieve());
			this.list.goToNext();
		}

		return this;
	}

	@Override
	public SetInterface<T> union(SetInterface<T> s) {
		SetInterface<T> newSet = s.copy();

		this.list.goToFirst();
		while(this.list.goToNext()){
			if(!newSet.elementExists(this.get())){
				newSet.add(this.get());
			}
		}
		return newSet;
	}

	@Override
	public SetInterface<T> intersection(SetInterface<T> s) {
		SetInterface<T> newSet = this.copy();
		SetInterface<T> diffSet = difference(s);

		/*diffSet.goToFirst();

		while(diffSet.goToNext()){
			newSet.remove(diffSet.get());
		}*/

		return newSet;
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> s) {
		SetInterface<T> newSet = new Set();

		this.list.goToFirst();
		if(!s.elementExists(this.get())){
			newSet.add(this.get());
		}
		while(this.list.goToNext()){
			if(!s.elementExists(this.get())){
				newSet.add(this.get());
			}
		}
		return newSet;
	}

	@Override
	public SetInterface<T> symmetricDifference(SetInterface<T> s) {
		return union(s).difference(intersection(s));
	}


}