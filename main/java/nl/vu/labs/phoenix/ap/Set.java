package nl.vu.labs.phoenix.ap;
public class Set<T extends Comparable<T>> implements SetInterface<T> {
	private ListInterface<T> list ;

	Set(){
		list= new LinkedList<T>();
	}

	@Override
	public void init() {

	}
	@Override
	public boolean add(T t) {
		if(!this.elementExists(t)){

			this.list.insert(t);
			return true;
		}else{
			//System.out.println("Element exists");
			return false;
		}
	}

	public String printSet(){
		StringBuffer result =new StringBuffer();
		if(size()>0){
		this.list.goToFirst();
		result.append(this.get());

		while(this.list.goToNext()){
			result.append(',');
			result.append(this.get());

		}
		}
		return result.toString();
	}
	@Override
	public T get() {
		return this.list.retrieve();
	}

	@Override
	public boolean remove(T t) {
		if(this.list.find(t)){
			this.list.remove();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public boolean elementExists(T t) {
		return this.list.find(t);
	}

	@Override
	public SetInterface<T> copy() {
		//System.out.println("Starting copy");
		SetInterface<T> newSet = new Set<T>();
		if(size()>0){

			this.list.goToFirst();
			newSet.add(this.get());

			while(this.list.goToNext()){

				newSet.add(this.get());
			}
		}
		//System.out.println("Returning copy");
		return newSet;
	}

	/*@Override
	public SetInterface<T> union(SetInterface<T> s) {
		System.out.println("Start union");
		if(s.size() ==0){
			return this.copy();
		}else if(this.size() == 0){
			return s.copy();
		}

		SetInterface<T> newSet = new Set<T>();

		System.out.println(this.printSet());
		System.out.println("New set");
		System.out.println(newSet.printSet());


		if(this.size()>0) {
			System.out.printf("num elements: %d \n",this.size());
			this.list.goToFirst();
			if(this.list.find(this.get())){
				System.out.println("EXISTS");
			}
			newSet.add(this.get());

			//System.out.println(newSet.printSet());
			//System.out.println("new set");
			while (this.list.goToNext()) {
				//System.out.println(newSet.printSet());
				//System.out.println(elementExists(this.get()));
				newSet.add(this.get());
			}
			System.out.println("done UNION");
			//System.out.println(newSet.printSet());
		}
		//System.out.println(newSet.printSet());
		return newSet;
	}*/
	@Override
	public SetInterface<T> union(SetInterface<T> s) {
		//System.out.println("Start union");
		//System.out.printf("Current set: %s \n",this.printSet());

		if(s.size() ==0){
			return this.copy();
		}else if(this.size() == 0){
			return s.copy();
		}

		SetInterface<T> newSet = s.copy();
		//System.out.printf("copy of addSet: %s \n",newSet.printSet());

		this.list.goToFirst();
		if (!newSet.elementExists(this.get())){
			newSet.add(this.get());
		}

		while(this.list.goToNext()){
			if (!newSet.elementExists(this.get())){
				newSet.add(this.get());
			}
		}
		//System.out.printf("return of addSet: %s \n",newSet.printSet());

		return newSet;
	}
	@Override
	public SetInterface<T> intersection(SetInterface<T> s) {
		SetInterface<T> diffSet = this.difference(s);

		if(s.equals(this)){
			return this.copy();
		}

		return this.difference(diffSet);
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> s) {
		//System.out.println("In difference");

		//System.out.println(newSet.printSet());
		if(this.size() ==0 || s.size() ==0){
			return this.copy();
		}

		SetInterface<T> newSet = this.copy();
			this.list.goToFirst();
			if(s.elementExists(this.get())) {
				newSet.remove(this.get());
			}
			while(this.list.goToNext()){
				if(s.elementExists(this.get())) {
					newSet.remove(this.get());
				}
			}
		//System.out.printf("copy of addSet: %s \n",newSet.printSet());

		return newSet;
	}

	@Override
	public SetInterface<T> symmetricDifference(SetInterface<T> s) {
		System.out.println("In sym. diff");
		if(s.size() ==0){
			return this.copy();
		}else if(this.size() == 0){
			System.out.println("Is empty");
			return s.copy();
		}

		return union(s).difference(intersection(s));
	}


}