package nl.vu.labs.phoenix.ap;

public class Identifier implements IdentifierInterface {
	StringBuffer id;

	Identifier(){
		this.id = new StringBuffer();
		this.id.append('c');
	}

	Identifier(Identifier src){
		this.id = new StringBuffer();
		this.id.append('c');
		init(src.getChar(0));
		for(int i =1;i<src.getSize();i++){
			addChar(src.getChar(i));
		}
	}
	@Override
	public String value() {
		return this.id.toString();
	}

	@Override
	public Identifier init(char c) {
		while(getSize() >0 ) {
			this.id.deleteCharAt(getSize()-1);
		}
		addChar(c);
		return this;
	}

	@Override
	public int getSize() {
		return this.id.length();
	}

	@Override
	public Identifier addChar(char c) {
		this.id.append(c);
		return this;
	}

	@Override
	public char getChar(int index) {
		return this.id.charAt(index);
	}

	@Override
	public int hashCode(){
		return this.value().hashCode();
	}
	@Override
	public boolean equals(Identifier sb) { //Object comparable
		return sb.value().hashCode() == this.value().hashCode();
	}

}
